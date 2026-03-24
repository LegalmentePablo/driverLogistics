package com.example.driverlogistics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.driverlogistics.data.local.entity.PendingSyncActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingSyncActionDao {
    @Query("SELECT * FROM pending_sync_actions ORDER BY createdAtEpochMillis ASC")
    fun observePendingActions(): Flow<List<PendingSyncActionEntity>>

    @Query("SELECT * FROM pending_sync_actions ORDER BY createdAtEpochMillis ASC")
    suspend fun getPendingActions(): List<PendingSyncActionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingAction(action: PendingSyncActionEntity)

    @Query("DELETE FROM pending_sync_actions WHERE id = :actionId")
    suspend fun deletePendingActionById(actionId: Long)
}
