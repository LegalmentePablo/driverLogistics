package com.example.driverlogistics.data.repository

import com.example.driverlogistics.data.local.dao.DeliveryDao
import com.example.driverlogistics.data.local.mapper.toDeliveryEntity
import com.example.driverlogistics.data.local.mapper.toDomain
import com.example.driverlogistics.data.remote.FakeDeliveryRemoteDataSource
import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.domain.repository.DeliveryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstDeliveryRepository(
    private val deliveryDao: DeliveryDao,
    private val remoteDataSource: FakeDeliveryRemoteDataSource
) : DeliveryRepository {

    override fun observeDeliveries(): Flow<List<Delivery>> =
        deliveryDao.observeDeliveries().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun refreshDeliveries() {
        try {
            val remoteDeliveries = remoteDataSource.fetchDeliveries()
            deliveryDao.upsertDeliveries(remoteDeliveries.map { it.toDeliveryEntity() })
        } catch (_: Exception) {
            // Keep existing local cache when remote refresh fails.
        }
    }

    override suspend fun markDeliveryAsCompleted(deliveryId: String) {
        deliveryDao.updateDeliveryStatus(
            deliveryId = deliveryId,
            newStatus = DeliveryStatus.Delivered.name
        )
    }
}
