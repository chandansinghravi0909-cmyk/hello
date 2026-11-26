package com.example.data.local

import com.example.domain.model.PlannedDayModel
import com.example.domain.model.PlannedItem
import com.example.domain.model.PlannerConflictDetector
import com.example.domain.model.TrainingPriority
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class DataBackupPayload(
    val schemaVersion: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val userProfile: UserProfileEntity? = null,
    val favorites: List<FavoriteEntity> = emptyList(),
    val movementProgress: List<MovementProgressEntity> = emptyList(),
    val workoutLogs: List<WorkoutLogEntity> = emptyList(),
    val plannedDays: List<PlannedDayEntity> = emptyList(),
    val readinessLogs: List<ReadinessLogEntity> = emptyList(),
    val bodyMeasurements: List<BodyMeasurementEntity> = emptyList(),
    val themeMode: String = ThemeMode.DARK.name,
    val unitSystem: String = UnitSystem.METRIC.name,
    val trainingPriority: String = TrainingPriority.HYPERTROPHY.name
)

class HpsRepository(
    private val db: HpsDatabase,
    private val prefs: PreferencesManager
) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }

    val themeMode: Flow<ThemeMode> = prefs.themeMode
    val unitSystem: Flow<UnitSystem> = prefs.unitSystem
    val trainingPriority: Flow<TrainingPriority> = prefs.trainingPriority

    val favorites: Flow<Set<String>> = db.favoriteDao().observeAll().map { list ->
        list.map { it.movementId }.toSet()
    }

    val allMovementProgress: Flow<Map<String, MovementProgressEntity>> =
        db.movementProgressDao().observeAll().map { list ->
            list.associateBy { it.movementId }
        }

    fun observeMovementProgress(movementId: String): Flow<MovementProgressEntity?> =
        db.movementProgressDao().observeById(movementId)

    val allWorkoutLogs: Flow<List<WorkoutLogEntity>> = db.workoutLogDao().observeAll()

    fun observeMovementLogs(movementId: String): Flow<List<WorkoutLogEntity>> =
        db.workoutLogDao().observeForMovement(movementId)

    val readinessLogs: Flow<List<ReadinessLogEntity>> = db.readinessLogDao().observeAll()
    val bodyMeasurements: Flow<List<BodyMeasurementEntity>> = db.bodyMeasurementDao().observeAll()
    val userProfile: Flow<UserProfileEntity> = db.userProfileDao().observeProfile().map {
        it ?: UserProfileEntity()
    }

    val plannedDays: Flow<List<PlannedDayModel>> = db.plannedDayDao().observeAll().map { entities ->
        if (entities.isEmpty()) {
            emptyList()
        } else {
            entities.map { entity ->
                val items = runCatching {
                    json.decodeFromString<List<PlannedItem>>(entity.itemsJson)
                }.getOrDefault(emptyList())
                PlannedDayModel(
                    dayOfWeek = entity.dayOfWeek,
                    dayName = entity.dayName,
                    title = entity.title,
                    focusTag = entity.focusTag,
                    isRestDay = entity.isRestDay,
                    items = items,
                    notes = entity.notes
                )
            }
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) = prefs.setThemeMode(mode)
    suspend fun setUnitSystem(system: UnitSystem) = prefs.setUnitSystem(system)
    suspend fun setTrainingPriority(priority: TrainingPriority) = prefs.setTrainingPriority(priority)

    suspend fun toggleFavorite(movementId: String, currentlyFavorite: Boolean) {
        if (currentlyFavorite) {
            db.favoriteDao().deleteById(movementId)
        } else {
            db.favoriteDao().insert(FavoriteEntity(movementId = movementId))
        }
    }

    suspend fun saveMovementProgress(progress: MovementProgressEntity) {
        db.movementProgressDao().upsert(progress)
    }

    suspend fun addMovementPhoto(movementId: String, uriString: String) {
        val existing = db.movementProgressDao().getById(movementId)
            ?: MovementProgressEntity(movementId = movementId)
        val currentList = runCatching {
            json.decodeFromString<List<String>>(existing.photoUrisJson)
        }.getOrDefault(emptyList())
        if (uriString !in currentList) {
            val updated = json.encodeToString(currentList + uriString)
            db.movementProgressDao().upsert(existing.copy(photoUrisJson = updated))
        }
    }

    suspend fun removeMovementPhoto(movementId: String, uriString: String) {
        val existing = db.movementProgressDao().getById(movementId) ?: return
        val currentList = runCatching {
            json.decodeFromString<List<String>>(existing.photoUrisJson)
        }.getOrDefault(emptyList())
        val updated = json.encodeToString(currentList.filterNot { it == uriString })
        db.movementProgressDao().upsert(existing.copy(photoUrisJson = updated))
    }

    suspend fun logWorkout(log: WorkoutLogEntity) {
        db.workoutLogDao().insert(log)
        val existing = db.movementProgressDao().getById(log.movementId)
            ?: MovementProgressEntity(movementId = log.movementId)
        val updated = existing.copy(
            bestHoldSeconds = maxOf(existing.bestHoldSeconds, log.holdSeconds),
            bestReps = maxOf(existing.bestReps, log.reps),
            workingLoadKg = maxOf(existing.workingLoadKg, log.loadKg),
            lastPracticedAt = log.loggedAt
        )
        db.movementProgressDao().upsert(updated)
    }

    suspend fun deleteWorkoutLog(id: Long) {
        db.workoutLogDao().deleteById(id)
    }

    suspend fun savePlannedDay(day: PlannedDayModel) {
        db.plannedDayDao().upsert(
            PlannedDayEntity(
                dayOfWeek = day.dayOfWeek,
                dayName = day.dayName,
                title = day.title,
                focusTag = day.focusTag,
                isRestDay = day.isRestDay,
                itemsJson = json.encodeToString(day.items),
                notes = day.notes
            )
        )
    }

    suspend fun loadStarterWeeklyTemplate() {
        val template = PlannerConflictDetector.defaultTemplateDays()
        db.plannedDayDao().upsertAll(
            template.map { day ->
                PlannedDayEntity(
                    dayOfWeek = day.dayOfWeek,
                    dayName = day.dayName,
                    title = day.title,
                    focusTag = day.focusTag,
                    isRestDay = day.isRestDay,
                    itemsJson = json.encodeToString(day.items),
                    notes = day.notes
                )
            }
        )
    }

    suspend fun clearWeeklyPlan() {
        db.plannedDayDao().clearAll()
    }

    suspend fun logReadiness(log: ReadinessLogEntity) {
        db.readinessLogDao().insert(log)
    }

    suspend fun deleteReadinessLog(id: Long) {
        db.readinessLogDao().deleteById(id)
    }

    suspend fun logBodyMeasurement(item: BodyMeasurementEntity) {
        db.bodyMeasurementDao().insert(item)
    }

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        db.userProfileDao().upsert(profile.copy(id = 1))
    }

    suspend fun dismissOnboarding() {
        val current = db.userProfileDao().getProfileOnce() ?: UserProfileEntity()
        db.userProfileDao().upsert(current.copy(id = 1, onboardingDismissed = true))
    }

    suspend fun exportBackupJson(
        currentTheme: ThemeMode,
        currentUnit: UnitSystem,
        currentPriority: TrainingPriority
    ): String {
        val payload = DataBackupPayload(
            schemaVersion = 1,
            exportedAt = System.currentTimeMillis(),
            userProfile = db.userProfileDao().getProfileOnce(),
            favorites = db.favoriteDao().getAllOnce(),
            movementProgress = db.movementProgressDao().getAllOnce(),
            workoutLogs = db.workoutLogDao().getAllOnce(),
            plannedDays = db.plannedDayDao().getAllOnce(),
            readinessLogs = db.readinessLogDao().getAllOnce(),
            bodyMeasurements = db.bodyMeasurementDao().getAllOnce(),
            themeMode = currentTheme.name,
            unitSystem = currentUnit.name,
            trainingPriority = currentPriority.name
        )
        return json.encodeToString(payload)
    }

    suspend fun importBackupJson(rawJson: String): Result<String> {
        return runCatching {
            val payload = json.decodeFromString<DataBackupPayload>(rawJson)
            require(payload.schemaVersion in 1..1) {
                "यह बैकअप वर्ज़न समर्थित नहीं है: ${payload.schemaVersion}"
            }
            db.favoriteDao().clearAll()
            db.movementProgressDao().clearAll()
            db.workoutLogDao().clearAll()
            db.plannedDayDao().clearAll()
            db.readinessLogDao().clearAll()
            db.bodyMeasurementDao().clearAll()

            payload.userProfile?.let { db.userProfileDao().upsert(it.copy(id = 1)) }
            payload.favorites.forEach { db.favoriteDao().insert(it) }
            payload.movementProgress.forEach { db.movementProgressDao().upsert(it) }
            if (payload.workoutLogs.isNotEmpty()) {
                db.workoutLogDao().insertAll(payload.workoutLogs)
            }
            if (payload.plannedDays.isNotEmpty()) {
                db.plannedDayDao().upsertAll(payload.plannedDays)
            }
            payload.readinessLogs.forEach { db.readinessLogDao().insert(it) }
            payload.bodyMeasurements.forEach { db.bodyMeasurementDao().insert(it) }

            ThemeMode.entries.firstOrNull { it.name == payload.themeMode }?.let { prefs.setThemeMode(it) }
            UnitSystem.entries.firstOrNull { it.name == payload.unitSystem }?.let { prefs.setUnitSystem(it) }
            TrainingPriority.entries.firstOrNull { it.name == payload.trainingPriority }?.let { prefs.setTrainingPriority(it) }

            "${payload.workoutLogs.size} वर्कआउट, ${payload.favorites.size} पसंदीदा और ${payload.plannedDays.size} प्लान किए गए दिन वापस लोड हो गए।"
        }
    }

    suspend fun clearAllUserData() {
        db.favoriteDao().clearAll()
        db.movementProgressDao().clearAll()
        db.workoutLogDao().clearAll()
        db.plannedDayDao().clearAll()
        db.readinessLogDao().clearAll()
        db.bodyMeasurementDao().clearAll()
        db.userProfileDao().clearAll()
    }
}
