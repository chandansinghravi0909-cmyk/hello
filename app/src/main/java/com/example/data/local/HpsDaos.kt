package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementProgressDao {
    @Query("SELECT * FROM movement_progress ORDER BY lastPracticedAt DESC")
    fun observeAll(): Flow<List<MovementProgressEntity>>

    @Query("SELECT * FROM movement_progress WHERE movementId = :movementId")
    fun observeById(movementId: String): Flow<MovementProgressEntity?>

    @Query("SELECT * FROM movement_progress WHERE movementId = :movementId")
    suspend fun getById(movementId: String): MovementProgressEntity?

    @Query("SELECT * FROM movement_progress")
    suspend fun getAllOnce(): List<MovementProgressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MovementProgressEntity)

    @Query("DELETE FROM movement_progress")
    suspend fun clearAll()
}

@Dao
interface WorkoutLogDao {
    @Query("SELECT * FROM workout_logs ORDER BY loggedAt DESC")
    fun observeAll(): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE movementId = :movementId ORDER BY loggedAt DESC")
    fun observeForMovement(movementId: String): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs ORDER BY loggedAt DESC")
    suspend fun getAllOnce(): List<WorkoutLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WorkoutLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<WorkoutLogEntity>)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM workout_logs")
    suspend fun clearAll()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites")
    suspend fun getAllOnce(): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE movementId = :movementId")
    suspend fun deleteById(movementId: String)

    @Query("DELETE FROM favorites")
    suspend fun clearAll()
}

@Dao
interface PlannedDayDao {
    @Query("SELECT * FROM planned_days ORDER BY dayOfWeek ASC")
    fun observeAll(): Flow<List<PlannedDayEntity>>

    @Query("SELECT * FROM planned_days ORDER BY dayOfWeek ASC")
    suspend fun getAllOnce(): List<PlannedDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(day: PlannedDayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(days: List<PlannedDayEntity>)

    @Query("DELETE FROM planned_days")
    suspend fun clearAll()
}

@Dao
interface ReadinessLogDao {
    @Query("SELECT * FROM readiness_logs ORDER BY loggedAt DESC")
    fun observeAll(): Flow<List<ReadinessLogEntity>>

    @Query("SELECT * FROM readiness_logs ORDER BY loggedAt DESC")
    suspend fun getAllOnce(): List<ReadinessLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: ReadinessLogEntity): Long

    @Query("DELETE FROM readiness_logs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM readiness_logs")
    suspend fun clearAll()
}

@Dao
interface BodyMeasurementDao {
    @Query("SELECT * FROM body_measurements ORDER BY loggedAt DESC")
    fun observeAll(): Flow<List<BodyMeasurementEntity>>

    @Query("SELECT * FROM body_measurements ORDER BY loggedAt DESC")
    suspend fun getAllOnce(): List<BodyMeasurementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BodyMeasurementEntity): Long

    @Query("DELETE FROM body_measurements WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM body_measurements")
    suspend fun clearAll()
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun clearAll()
}
