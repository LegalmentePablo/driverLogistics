package com.example.driverlogistics.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.driverlogistics.DriverLogisticsApp

class PendingSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private companion object {
        const val TAG = "PendingSyncWorker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Pending sync work started")
        val appContainer = (applicationContext as DriverLogisticsApp).appContainer

        return runCatching {
            appContainer.pendingActionsSyncProcessor.syncPendingActions()
        }.fold(
            onSuccess = {
                Log.d(TAG, "Pending sync work finished successfully")
                Result.success()
            },
            onFailure = {
                Log.w(TAG, "Pending sync work failed; scheduling retry", it)
                Result.retry()
            }
        )
    }
}
