package com.example.driverlogistics.testutil

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.domain.model.MarkDeliveryCompletionResult
import com.example.driverlogistics.domain.repository.DeliveryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeDeliveryRepository(
    initialDeliveries: List<Delivery> = emptyList(),
    private var isOnline: Boolean = true
) : DeliveryRepository {

    private val deliveriesState = MutableStateFlow(initialDeliveries)
    private val pendingSyncIds = MutableStateFlow(setOf<String>())

    override fun observeDeliveries(): Flow<List<Delivery>> = deliveriesState

    override fun observeDeliveryById(deliveryId: String): Flow<Delivery?> =
        deliveriesState.map { deliveries -> deliveries.firstOrNull { it.id == deliveryId } }

    override fun observePendingSyncForDelivery(deliveryId: String): Flow<Boolean> =
        pendingSyncIds.map { pending -> pending.contains(deliveryId) }

    override suspend fun refreshDeliveries() = Unit

    override suspend fun resetDeliveriesToInitialState() {
        deliveriesState.value = deliveriesState.value.map {
            it.copy(status = DeliveryStatus.Pending)
        }
        pendingSyncIds.value = emptySet()
    }

    override suspend fun markDeliveryAsCompleted(deliveryId: String): MarkDeliveryCompletionResult {
        deliveriesState.value = deliveriesState.value.map {
            if (it.id == deliveryId) it.copy(status = DeliveryStatus.Delivered) else it
        }

        return if (isOnline) {
            MarkDeliveryCompletionResult.UpdatedLocally
        } else {
            pendingSyncIds.value = pendingSyncIds.value + deliveryId
            MarkDeliveryCompletionResult.QueuedForSync
        }
    }

    fun setOnline(value: Boolean) {
        isOnline = value
    }
}
