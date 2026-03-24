package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.repository.DeliveryRepository

class InitializeDeliveriesUseCase(
    private val deliveryRepository: DeliveryRepository
) {
    suspend operator fun invoke() {
        deliveryRepository.refreshDeliveries()
    }
}
