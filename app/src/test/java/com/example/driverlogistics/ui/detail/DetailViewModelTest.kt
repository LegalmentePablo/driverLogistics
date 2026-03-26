package com.example.driverlogistics.ui.detail

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.domain.usecase.MarkDeliveryAsCompletedUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveryByIdUseCase
import com.example.driverlogistics.domain.usecase.ObservePendingSyncForDeliveryUseCase
import com.example.driverlogistics.testutil.FakeDeliveryRepository
import com.example.driverlogistics.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun markAsDelivered_offline_updatesUiOptimistically_andShowsQueuedState() = runTest {
        val repository = FakeDeliveryRepository(
            initialDeliveries = listOf(
                Delivery("1", "PKG-1001", "Calle 1", DeliveryStatus.Pending)
            ),
            isOnline = false
        )

        val viewModel = DetailViewModel(
            deliveryId = "1",
            observeDeliveryByIdUseCase = ObserveDeliveryByIdUseCase(repository),
            observePendingSyncForDeliveryUseCase = ObservePendingSyncForDeliveryUseCase(repository),
            markDeliveryAsCompletedUseCase = MarkDeliveryAsCompletedUseCase(repository)
        )

        advanceUntilIdle()
        viewModel.markAsDelivered()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(DeliveryStatus.Delivered, state.delivery?.status)
        assertTrue(state.wasQueuedForSync)
        assertEquals(false, state.isMarkingDelivered)
    }
}
