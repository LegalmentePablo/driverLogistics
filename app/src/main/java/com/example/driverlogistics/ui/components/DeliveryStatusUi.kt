package com.example.driverlogistics.ui.components

import androidx.compose.ui.graphics.Color
import com.example.driverlogistics.domain.model.DeliveryStatus

fun DeliveryStatus.toSpanishLabel(): String = when (this) {
    DeliveryStatus.Pending -> "Pendiente"
    DeliveryStatus.Delivered -> "Entregado"
}

fun DeliveryStatus.containerColor(): Color = when (this) {
    DeliveryStatus.Pending -> Color(0xFFFFF3E0)
    DeliveryStatus.Delivered -> Color(0xFFE8F5E9)
}

fun DeliveryStatus.contentColor(): Color = when (this) {
    DeliveryStatus.Pending -> Color(0xFF9A3412)
    DeliveryStatus.Delivered -> Color(0xFF1B5E20)
}
