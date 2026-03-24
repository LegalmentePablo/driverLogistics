package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.repository.DeliveryRepository
import kotlinx.coroutines.flow.Flow

class ObserveDeliveriesUseCase(
    private val deliveryRepository: DeliveryRepository
) {
    operator fun invoke(): Flow<List<Delivery>> = deliveryRepository.observeDeliveries()
}
