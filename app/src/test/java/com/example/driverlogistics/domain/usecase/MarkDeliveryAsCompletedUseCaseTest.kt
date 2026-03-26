package com.example.driverlogistics.domain.usecase

import com.example.driverlogistics.domain.model.Delivery
import com.example.driverlogistics.domain.model.DeliveryStatus
import com.example.driverlogistics.domain.model.MarkDeliveryCompletionResult
import com.example.driverlogistics.testutil.FakeDeliveryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarkDeliveryAsCompletedUseCaseTest {

    @Test
    fun markDeliveryAsCompleted_whenOffline_returnsQueuedForSync() = runTest {
        val repository = FakeDeliveryRepository(
            initialDeliveries = listOf(
                Delivery("1", "PKG-1001", "Calle 1", DeliveryStatus.Pending)
            ),
            isOnline = false
        )
        val useCase = MarkDeliveryAsCompletedUseCase(repository)

        val result = useCase("1")

        assertEquals(MarkDeliveryCompletionResult.QueuedForSync, result)
    }
}
