package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movement_progress")
data class MovementProgressEntity(
    @PrimaryKey val movementId: String,
    val currentLevel: Int = 0,
    val bestHoldSeconds: Int = 0,
    val bestReps: Int = 0,
    val workingLoadKg: Double = 0.0,
    val lastPracticedAt: Long? = null,
    val personalNotes: String = "",
    val painNotes: String = "",
    val photoUrisJson: String = "[]"
)

@Serializable
@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val movementId: String,
    val movementName: String,
    val disciplineId: String,
    val programmingModel: String,
    val targetMuscle: String = "",
    val sets: Int = 0,
    val reps: Int = 0,
    val loadKg: Double = 0.0,
    val rir: Int = 2,
    val holdSeconds: Int = 0,
    val durationMinutes: Int = 0,
    val distanceKm: Double = 0.0,
    val paceText: String = "",
    val heartRateBpm: Int = 0,
    val rpe: Int = 5,
    val sprintSeconds: Double = 0.0,
    val jumpCm: Double = 0.0,
    val romScore: Int = 3,
    val notes: String = "",
    val painFlag: Boolean = false,
    val loggedAt: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val movementId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "planned_days")
data class PlannedDayEntity(
    @PrimaryKey val dayOfWeek: Int, // 1..7
    val dayName: String,
    val title: String,
    val focusTag: String,
    val isRestDay: Boolean,
    val itemsJson: String,
    val notes: String = ""
)

@Serializable
@Entity(tableName = "readiness_logs")
data class ReadinessLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sleepHours: Double = 7.5,
    val fatigueScore: Int = 2, // 1 (fresh) .. 5 (exhausted)
    val sorenessScore: Int = 2, // 1 (none) .. 5 (severe)
    val readinessScore: Int = 7, // 1 .. 10
    val restingHr: Int = 0,
    val notes: String = "",
    val loggedAt: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "body_measurements")
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Double = 0.0,
    val chestCm: Double = 0.0,
    val waistCm: Double = 0.0,
    val armCm: Double = 0.0,
    val thighCm: Double = 0.0,
    val notes: String = "",
    val loggedAt: Long = System.currentTimeMillis()
)

@Serializable
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int? = null,
    val heightCm: Double? = null,
    val weightKg: Double? = null,
    val experienceLevel: String = "शुरुआती-मध्यम",
    val primaryGoal: String = "मसल्स और ताकत बढ़ाना",
    val secondaryGoals: String = "ताकत, फुर्ती, मोबिलिटी और सुरक्षा",
    val equipmentJson: String = "[\"बॉडीवेट\", \"बारबेल\", \"डंबल\", \"पुल-अप बार\"]",
    val trainingDaysPerWeek: Int = 5,
    val availableMinutesPerSession: Int = 60,
    val movementLimitations: String = "",
    val exercisePreferences: String = "",
    val personalRecordsNotes: String = "",
    val onboardingDismissed: Boolean = false
)
