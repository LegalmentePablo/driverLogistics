package com.example.driverlogistics.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.driverlogistics.data.local.dao.DeliveryDao
import com.example.driverlogistics.data.local.dao.PendingSyncActionDao
import com.example.driverlogistics.data.local.entity.DeliveryEntity
import com.example.driverlogistics.data.local.entity.PendingSyncActionEntity

@Database(
    entities = [DeliveryEntity::class, PendingSyncActionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DriverLogisticsDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao
    abstract fun pendingSyncActionDao(): PendingSyncActionDao
}
