package com.example.driverlogistics.data.remote.model

data class RemoteDeliveryDto(
    val id: String,
    val packageId: String,
    val address: String,
    val status: String
)
