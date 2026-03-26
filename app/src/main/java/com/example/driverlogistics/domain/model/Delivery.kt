package com.example.driverlogistics.domain.model

data class Delivery(
    val id: String,
    val packageId: String,
    val address: String,
    val status: DeliveryStatus
)
