package com.example.driverlogistics.di

import android.content.Context
import com.example.driverlogistics.data.remote.FakeDeliveryRemoteDataSource
import com.example.driverlogistics.data.repository.OfflineFirstDeliveryRepository
import com.example.driverlogistics.domain.repository.DeliveryRepository
import com.example.driverlogistics.domain.usecase.InitializeDeliveriesUseCase
import com.example.driverlogistics.domain.usecase.MarkDeliveryAsCompletedUseCase
import com.example.driverlogistics.domain.usecase.ObserveDeliveriesUseCase

class AppContainer(context: Context) {
    private val database = LocalDataProvider.provideDatabase(context)
    private val deliveryDao = LocalDataProvider.provideDeliveryDao(database)
    private val remoteDataSource = FakeDeliveryRemoteDataSource()

    val deliveryRepository: DeliveryRepository = OfflineFirstDeliveryRepository(
        deliveryDao = deliveryDao,
        remoteDataSource = remoteDataSource
    )

    val observeDeliveriesUseCase = ObserveDeliveriesUseCase(deliveryRepository)
    val markDeliveryAsCompletedUseCase = MarkDeliveryAsCompletedUseCase(deliveryRepository)
    val initializeDeliveriesUseCase = InitializeDeliveriesUseCase(deliveryRepository)
}
