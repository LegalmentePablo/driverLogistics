package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.repository.DeliveryRepository
import kotlinx.coroutines.flow.Flow

class ObserveDeliveryByIdUseCase(
    private val deliveryRepository: DeliveryRepository
) {
    operator fun invoke(deliveryId: String): Flow<Delivery?> =
        deliveryRepository.observeDeliveryById(deliveryId)
}
