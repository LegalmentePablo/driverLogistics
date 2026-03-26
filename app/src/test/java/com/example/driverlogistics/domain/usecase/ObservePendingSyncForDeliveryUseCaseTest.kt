package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.testutil.FakeDeliveryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObservePendingSyncForDeliveryUseCaseTest {

    @Test
    fun observePendingSyncForDelivery_whenActionQueued_emitsTrue() = runTest {
        val repository = FakeDeliveryRepository(
            initialDeliveries = listOf(
                Delivery("1", "PKG-1001", "Calle 1", DeliveryStatus.Pending)
            ),
            isOnline = false
        )
        val markUseCase = MarkDeliveryAsCompletedUseCase(repository)
        val observeUseCase = ObservePendingSyncForDeliveryUseCase(repository)

        markUseCase("1")

        val hasPending = observeUseCase("1").first()
        assertTrue(hasPending)
    }
}
