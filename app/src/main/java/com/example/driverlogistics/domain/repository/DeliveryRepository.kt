package com.example.driverlogistics.domain.repository

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.MarkDeliveryCompletionResult
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository {
    fun observeDeliveries(): Flow<List<Delivery>>

    fun observeDeliveryById(deliveryId: String): Flow<Delivery?>

    suspend fun refreshDeliveries()

    suspend fun markDeliveryAsCompleted(deliveryId: String): MarkDeliveryCompletionResult
}
