package com.example.driverlogistics.di

import android.content.Context
import com.example.driverlogistics.core.network.ConnectivityChecker
import com.example.driverlogistics.data.remote.FakeDeliveryRemoteDataSource
import com.example.driverlogistics.data.repository.OfflineFirstDeliveryRepository
import com.example.driverlogistics.data.sync.PendingActionsSyncProcessor
import com.example.driverlogistics.domain.repository.DeliveryRepository
import com.example.driverlogistics.domain.usecase.InitializeDeliveriesUseCase
import com.example.driverlogistics.domain.usecase.MarkDeliveryAsCompletedUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveryByIdUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveriesUseCase
import com.example.driverlogistics.domain.usecase.ObservePendingSyncForDeliveryUseCase
import com.example.driverlogistics.domain.usecase.ResetDeliveriesUseCase

class AppContainer(context: Context) {
    private val database = LocalDataProvider.provideDatabase(context)
    private val deliveryDao = LocalDataProvider.provideDeliveryDao(database)
    private val pendingSyncActionDao = LocalDataProvider.providePendingSyncActionDao(database)
    private val remoteDataSource = FakeDeliveryRemoteDataSource()
    val connectivityChecker = ConnectivityChecker(context)

    val pendingActionsSyncProcessor = PendingActionsSyncProcessor(
        pendingSyncActionDao = pendingSyncActionDao,
        remoteDataSource = remoteDataSource
    )

    val deliveryRepository: DeliveryRepository = OfflineFirstDeliveryRepository(
        deliveryDao = deliveryDao,
        pendingSyncActionDao = pendingSyncActionDao,
        remoteDataSource = remoteDataSource,
        connectivityChecker = connectivityChecker
    )

    val observeDeliveriesUseCase = ObserveDeliveriesUseCase(deliveryRepository)
    val observeDeliveryByIdUseCase = ObserveDeliveryByIdUseCase(deliveryRepository)
    val observePendingSyncForDeliveryUseCase = ObservePendingSyncForDeliveryUseCase(deliveryRepository)
    val markDeliveryAsCompletedUseCase = MarkDeliveryAsCompletedUseCase(deliveryRepository)
    val resetDeliveriesUseCase = ResetDeliveriesUseCase(deliveryRepository)
    val initializeDeliveriesUseCase = InitializeDeliveriesUseCase(deliveryRepository)
}
