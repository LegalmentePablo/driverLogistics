package com.example.driverlogistics.data.sync

import com.example.driverlogistics.core.sync.SyncActionType
import com.example.driverlogistics.data.local.dao.PendingSyncActionDao
import com.example.driverlogistics.data.remote.FakeDeliveryRemoteDataSource

class PendingActionsSyncProcessor(
    private val pendingSyncActionDao: PendingSyncActionDao,
    private val remoteDataSource: FakeDeliveryRemoteDataSource
) {
    suspend fun syncPendingActions() {
        val pendingActions = pendingSyncActionDao.getPendingActions()

        for (action in pendingActions) {
            when (action.actionType) {
                SyncActionType.MARK_DELIVERED -> {
                    remoteDataSource.syncMarkDelivered(action.deliveryId)
                }
            }

            pendingSyncActionDao.deletePendingActionById(action.id)
        }
    }
}
