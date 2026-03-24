package com.example.driverlogistics.domain.repository

import com.example.driverlogistics.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository {
    fun observeDeliveries(): Flow<List<Delivery>>

    suspend fun refreshDeliveries()

    suspend fun markDeliveryAsCompleted(deliveryId: String)
}
