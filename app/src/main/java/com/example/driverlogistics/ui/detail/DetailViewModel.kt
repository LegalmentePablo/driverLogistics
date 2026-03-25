package com.example.driverlogistics.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.driverlogistics.domain.model.MarkDeliveryCompletionResult
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.domain.usecase.MarkDeliveryAsCompletedUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveryByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val deliveryId: String,
    private val observeDeliveryByIdUseCase: ObserveDeliveryByIdUseCase,
    private val markDeliveryAsCompletedUseCase: MarkDeliveryAsCompletedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState = _uiState

    init {
        observeDelivery()
    }

    private fun observeDelivery() {
        viewModelScope.launch {
            observeDeliveryByIdUseCase(deliveryId).collect { delivery ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        delivery = delivery,
                        errorMessage = if (delivery == null) "Entrega no encontrada" else null
                    )
                }
            }
        }
    }

    fun markAsDelivered() {
        val currentDelivery = _uiState.value.delivery ?: return
        if (currentDelivery.status == DeliveryStatus.Delivered) return

        // Optimistic UI: reflect delivered status immediately.
        _uiState.update {
            it.copy(
                delivery = currentDelivery.copy(status = DeliveryStatus.Delivered),
                isMarkingDelivered = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                markDeliveryAsCompletedUseCase(deliveryId)
            }.onSuccess { result ->
                _uiState.update {
                    it.copy(
                        isMarkingDelivered = false,
                        wasQueuedForSync = result == MarkDeliveryCompletionResult.QueuedForSync
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isMarkingDelivered = false,
                        errorMessage = "No se pudo actualizar la entrega"
                    )
                }
            }
        }
    }
}

class DetailViewModelFactory(
    private val deliveryId: String,
    private val observeDeliveryByIdUseCase: ObserveDeliveryByIdUseCase,
    private val markDeliveryAsCompletedUseCase: MarkDeliveryAsCompletedUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(
                deliveryId = deliveryId,
                observeDeliveryByIdUseCase = observeDeliveryByIdUseCase,
                markDeliveryAsCompletedUseCase = markDeliveryAsCompletedUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
