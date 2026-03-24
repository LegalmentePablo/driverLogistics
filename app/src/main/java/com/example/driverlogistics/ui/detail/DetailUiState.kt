package com.example.driverlogistics.ui.detail

import com.example.driverlogistics.domain.model.Delivery

data class DetailUiState(
    val isLoading: Boolean = false,
    val delivery: Delivery? = null,
    val errorMessage: String? = null
)
