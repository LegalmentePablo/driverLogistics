package com.example.driverlogistics.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.driverlogistics.domain.usecase.ObserveDeliveriesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val observeDeliveriesUseCase: ObserveDeliveriesUseCase
) : ViewModel() {

    private val _uiState = kotlinx.coroutines.flow.MutableStateFlow(HomeUiState(isLoading = true))
    val uiState = _uiState

    init {
        viewModelScope.launch {
            observeDeliveriesUseCase()
                .catch {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudieron cargar las entregas"
                        )
                    }
                }
                .collect { deliveries ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            deliveries = deliveries,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}

class HomeViewModelFactory(
    private val observeDeliveriesUseCase: ObserveDeliveriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(observeDeliveriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
