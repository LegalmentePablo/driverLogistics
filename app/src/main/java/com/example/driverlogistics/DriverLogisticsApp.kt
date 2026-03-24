package com.example.driverlogistics

import android.app.Application
import com.example.driverlogistics.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DriverLogisticsApp : Application() {
	lateinit var appContainer: AppContainer
		private set

	private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

	override fun onCreate() {
		super.onCreate()
		appContainer = AppContainer(applicationContext)

		applicationScope.launch {
			appContainer.initializeDeliveriesUseCase()
		}
	}
}
