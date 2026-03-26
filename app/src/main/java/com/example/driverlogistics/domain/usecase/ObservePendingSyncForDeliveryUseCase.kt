package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.repository.DeliveryRepository
import kotlinx.coroutines.flow.Flow

class ObservePendingSyncForDeliveryUseCase(
    private val deliveryRepository: DeliveryRepository
) {
    operator fun invoke(deliveryId: String): Flow<Boolean> =
        deliveryRepository.observePendingSyncForDelivery(deliveryId)
}
