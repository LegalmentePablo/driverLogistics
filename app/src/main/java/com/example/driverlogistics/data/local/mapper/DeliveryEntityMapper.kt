package com.example.driverlogistics.data.local.mapper

import com.example.driverlogistics.data.local.entity.DeliveryEntity
import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus

fun DeliveryEntity.toDomain(): Delivery {
    val mappedStatus = when (status) {
        DeliveryStatus.Delivered.name -> DeliveryStatus.Delivered
        else -> DeliveryStatus.Pending
    }

    return Delivery(
        id = id,
        packageId = packageId,
        address = address,
        status = mappedStatus
    )
}

fun Delivery.toEntity(): DeliveryEntity = DeliveryEntity(
    id = id,
    packageId = packageId,
    address = address,
    status = status.name
)
