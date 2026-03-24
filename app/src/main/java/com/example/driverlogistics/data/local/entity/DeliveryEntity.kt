package com.example.driverlogistics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deliveries")
data class DeliveryEntity(
    @PrimaryKey val id: String,
    val packageId: String,
    val address: String,
    val status: String
)
