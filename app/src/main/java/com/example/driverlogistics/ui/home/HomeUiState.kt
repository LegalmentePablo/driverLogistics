package com.example.driverlogistics.ui.home

import com.example.driverlogistics.domain.model.Delivery

data class HomeUiState(
    val isLoading: Boolean = false,
    val deliveries: List<Delivery> = emptyList(),
    val errorMessage: String? = null
)
