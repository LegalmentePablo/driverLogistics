package com.example.driverlogistics.ui.detail

import com.example.driverlogistics.domain.model.Delivery

data class DetailUiState(
    val isLoading: Boolean = false,
    val delivery: Delivery? = null,
    val isMarkingDelivered: Boolean = false,
    val wasQueuedForSync: Boolean = false,
    val errorMessage: String? = null
)
