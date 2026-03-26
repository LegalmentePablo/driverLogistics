package com.example.driverlogistics.data.local.mapper

import com.example.driverlogistics.data.local.entity.DeliveryEntity
import com.example.driverlogistics.data.remote.model.RemoteDeliveryDto
import com.example.driverlogistics.domain.model.DeliveryStatus

fun RemoteDeliveryDto.toDeliveryEntity(): DeliveryEntity {
    val mappedStatus = when (status) {
        DeliveryStatus.Delivered.name -> DeliveryStatus.Delivered
        else -> DeliveryStatus.Pending
    }

    return DeliveryEntity(
        id = id,
        packageId = packageId,
        address = address,
        status = mappedStatus.name
    )
}
