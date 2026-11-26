package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MovementProgressEntity::class,
        WorkoutLogEntity::class,
        FavoriteEntity::class,
        PlannedDayEntity::class,
        ReadinessLogEntity::class,
        BodyMeasurementEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HpsDatabase : RoomDatabase() {
    abstract fun movementProgressDao(): MovementProgressDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun plannedDayDao(): PlannedDayDao
    abstract fun readinessLogDao(): ReadinessLogDao
    abstract fun bodyMeasurementDao(): BodyMeasurementDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: HpsDatabase? = null

        fun getInstance(context: Context): HpsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    HpsDatabase::class.java,
                    "hps_mastery.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
