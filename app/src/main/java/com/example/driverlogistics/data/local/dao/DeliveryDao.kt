package com.example.driverlogistics.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.driverlogistics.data.local.entity.DeliveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao {
    @Query("SELECT * FROM deliveries ORDER BY packageId")
    fun observeDeliveries(): Flow<List<DeliveryEntity>>

    @Query("SELECT * FROM deliveries")
    suspend fun getDeliveries(): List<DeliveryEntity>

    @Query("SELECT * FROM deliveries WHERE id = :deliveryId LIMIT 1")
    fun observeDeliveryById(deliveryId: String): Flow<DeliveryEntity?>

    @Upsert
    suspend fun upsertDeliveries(deliveries: List<DeliveryEntity>)

    @Query("UPDATE deliveries SET status = :newStatus WHERE id = :deliveryId")
    suspend fun updateDeliveryStatus(deliveryId: String, newStatus: String)
}
