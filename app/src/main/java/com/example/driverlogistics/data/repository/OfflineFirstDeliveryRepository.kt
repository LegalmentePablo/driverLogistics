package com.example.driverlogistics.data.repository

import com.example.driverlogistics.data.local.dao.DeliveryDao
import com.example.driverlogistics.data.local.dao.PendingSyncActionDao
import com.example.driverlogistics.data.local.entity.PendingSyncActionEntity
import com.example.driverlogistics.data.local.entity.DeliveryEntity
import com.example.driverlogistics.core.sync.SyncActionType
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

    override fun observePendingSyncForDelivery(deliveryId: String): Flow<Boolean> =
        pendingSyncActionDao.observeHasPendingActionForDelivery(deliveryId)

    override suspend fun refreshDeliveries() {
        try {
            val remoteDeliveries = remoteDataSource.fetchDeliveries().map { it.toDeliveryEntity() }
            val localById = deliveryDao.getDeliveries().associateBy { it.id }

            val mergedDeliveries = remoteDeliveries.map { remoteDelivery ->
                val localDelivery = localById[remoteDelivery.id]
                if (localDelivery?.status == DeliveryStatus.Delivered.name) {
                    remoteDelivery.copy(status = DeliveryStatus.Delivered.name)
                } else {
                    remoteDelivery
                }
            }

            deliveryDao.upsertDeliveries(mergedDeliveries)
        } catch (_: Exception) {
            // Keep existing local cache when remote refresh fails.
        }
    }

    override suspend fun resetDeliveriesToInitialState() {
        val initialDeliveries = remoteDataSource.fetchDeliveries().map { it.toDeliveryEntity() }
        deliveryDao.upsertDeliveries(initialDeliveries)
        pendingSyncActionDao.clearAllPendingActions()
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
                    actionType = SyncActionType.MARK_DELIVERED,
                    createdAtEpochMillis = System.currentTimeMillis()
                )
            )
            return MarkDeliveryCompletionResult.QueuedForSync
        }

        return MarkDeliveryCompletionResult.UpdatedLocally
    }
}
