package com.example.driverlogistics.data.repository

import com.example.driverlogistics.data.local.dao.DeliveryDao
import com.example.driverlogistics.data.local.dao.PendingSyncActionDao
import com.example.driverlogistics.data.local.entity.PendingSyncActionEntity
import com.example.driverlogistics.data.local.mapper.toDeliveryEntity
import com.example.driverlogistics.data.local.mapper.toDomain
import com.example.driverlogistics.data.remote.FakeDeliveryRemoteDataSource
import com.example.driverlogistics.domain.model.MarkDeliveryCompletionResult
import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.domain.repository.DeliveryRepository
import com.example.driverlogistics.core.network.ConnectivityChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstDeliveryRepository(
    private val deliveryDao: DeliveryDao,
    private val pendingSyncActionDao: PendingSyncActionDao,
    private val remoteDataSource: FakeDeliveryRemoteDataSource,
    private val connectivityChecker: ConnectivityChecker
) : DeliveryRepository {

    override fun observeDeliveries(): Flow<List<Delivery>> =
        deliveryDao.observeDeliveries().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeDeliveryById(deliveryId: String): Flow<Delivery?> =
        deliveryDao.observeDeliveryById(deliveryId).map { entity ->
            entity?.toDomain()
        }

    override suspend fun refreshDeliveries() {
        try {
            val remoteDeliveries = remoteDataSource.fetchDeliveries()
            deliveryDao.upsertDeliveries(remoteDeliveries.map { it.toDeliveryEntity() })
        } catch (_: Exception) {
            // Keep existing local cache when remote refresh fails.
        }
    }

    override suspend fun markDeliveryAsCompleted(deliveryId: String): MarkDeliveryCompletionResult {
        deliveryDao.updateDeliveryStatus(
            deliveryId = deliveryId,
            newStatus = DeliveryStatus.Delivered.name
        )

        if (!connectivityChecker.isOnline()) {
            pendingSyncActionDao.insertPendingAction(
                PendingSyncActionEntity(
                    deliveryId = deliveryId,
                    actionType = ACTION_MARK_DELIVERED,
                    createdAtEpochMillis = System.currentTimeMillis()
                )
            )
            return MarkDeliveryCompletionResult.QueuedForSync
        }

        return MarkDeliveryCompletionResult.UpdatedLocally
    }

    private companion object {
        const val ACTION_MARK_DELIVERED = "MARK_DELIVERED"
    }
}
