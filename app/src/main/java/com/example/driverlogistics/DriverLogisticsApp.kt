package com.example.driverlogistics

import android.app.Application
import android.util.Log
import com.example.driverlogistics.di.AppContainer
import com.example.driverlogistics.worker.SyncWorkScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DriverLogisticsApp : Application() {
	private companion object {
		const val TAG = "DriverLogisticsApp"
	}

	lateinit var appContainer: AppContainer
		private set

	private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

	override fun onCreate() {
		super.onCreate()
		appContainer = AppContainer(applicationContext)
		SyncWorkScheduler.enqueuePeriodicPendingSync(applicationContext)
		if (appContainer.connectivityChecker.isOnline()) {
			Log.d(TAG, "App opened with internet, enqueueing one-time sync")
			SyncWorkScheduler.enqueueOneTimePendingSync(applicationContext)
		}

		applicationScope.launch {
			appContainer.initializeDeliveriesUseCase()
		}

		applicationScope.launch {
			appContainer.connectivityChecker.observeConnectivity().collectLatest { isOnline ->
				if (!isOnline) {
					Log.d(TAG, "Connectivity lost")
					return@collectLatest
				}

				Log.d(TAG, "Connectivity available, ensuring one-time sync work is enqueued")
				SyncWorkScheduler.enqueueOneTimePendingSync(applicationContext)
			}
		}
	}
}
