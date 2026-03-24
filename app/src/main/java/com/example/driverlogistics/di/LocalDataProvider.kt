package com.example.driverlogistics.di

import android.content.Context
import androidx.room.Room
import com.example.driverlogistics.data.local.dao.DeliveryDao
import com.example.driverlogistics.data.local.dao.PendingSyncActionDao
import com.example.driverlogistics.data.local.db.DriverLogisticsDatabase

object LocalDataProvider {
    private const val DATABASE_NAME = "driver_logistics.db"

    fun provideDatabase(context: Context): DriverLogisticsDatabase =
        Room.databaseBuilder(
            context,
            DriverLogisticsDatabase::class.java,
            DATABASE_NAME
        ).build()

    fun provideDeliveryDao(database: DriverLogisticsDatabase): DeliveryDao = database.deliveryDao()

    fun providePendingSyncActionDao(
        database: DriverLogisticsDatabase
    ): PendingSyncActionDao = database.pendingSyncActionDao()
}
