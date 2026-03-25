package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.model.MarkDeliveryCompletionResult
import com.example.driverlogistics.domain.repository.DeliveryRepository

class MarkDeliveryAsCompletedUseCase(
    private val deliveryRepository: DeliveryRepository
) {
    suspend operator fun invoke(deliveryId: String): MarkDeliveryCompletionResult =
        deliveryRepository.markDeliveryAsCompleted(deliveryId)
}
