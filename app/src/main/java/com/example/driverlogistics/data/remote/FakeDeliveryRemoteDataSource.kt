package com.example.driverlogistics.data.remote

import com.example.driverlogistics.data.remote.model.RemoteDeliveryDto
import kotlinx.coroutines.delay

class FakeDeliveryRemoteDataSource {
    suspend fun fetchDeliveries(): List<RemoteDeliveryDto> {
        delay(1200)

        return listOf(
            RemoteDeliveryDto(
                id = "1",
                packageId = "PKG-1001",
                address = "Av. Corrientes 1234, CABA",
                status = "Pending"
            ),
            RemoteDeliveryDto(
                id = "2",
                packageId = "PKG-1002",
                address = "Calle 50 742, La Plata",
                status = "Delivered"
            ),
            RemoteDeliveryDto(
                id = "3",
                packageId = "PKG-1003",
                address = "Bv. Oroño 540, Rosario",
                status = "Pending"
            )
        )
    }

    suspend fun syncMarkDelivered(deliveryId: String) {
        delay(400)
        if (deliveryId.isBlank()) {
            throw IllegalArgumentException("Invalid delivery id")
        }
    }
}
