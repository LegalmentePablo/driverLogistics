package com.example.driverlogistics.di

import android.content.Context
import com.example.driverlogistics.core.network.ConnectivityChecker
import com.example.driverlogistics.data.remote.FakeDeliveryRemoteDataSource
import com.example.driverlogistics.data.repository.OfflineFirstDeliveryRepository
import com.example.driverlogistics.domain.repository.DeliveryRepository
import com.example.driverlogistics.domain.usecase.InitializeDeliveriesUseCase
import com.example.driverlogistics.domain.usecase.MarkDeliveryAsCompletedUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveryByIdUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveriesUseCase

class AppContainer(context: Context) {
    private val database = LocalDataProvider.provideDatabase(context)
    private val deliveryDao = LocalDataProvider.provideDeliveryDao(database)
    private val pendingSyncActionDao = LocalDataProvider.providePendingSyncActionDao(database)
    private val remoteDataSource = FakeDeliveryRemoteDataSource()
    private val connectivityChecker = ConnectivityChecker(context)

    val deliveryRepository: DeliveryRepository = OfflineFirstDeliveryRepository(
        deliveryDao = deliveryDao,
        pendingSyncActionDao = pendingSyncActionDao,
        remoteDataSource = remoteDataSource,
        connectivityChecker = connectivityChecker
    )

    val observeDeliveriesUseCase = ObserveDeliveriesUseCase(deliveryRepository)
    val observeDeliveryByIdUseCase = ObserveDeliveryByIdUseCase(deliveryRepository)
    val markDeliveryAsCompletedUseCase = MarkDeliveryAsCompletedUseCase(deliveryRepository)
    val initializeDeliveriesUseCase = InitializeDeliveriesUseCase(deliveryRepository)
}
