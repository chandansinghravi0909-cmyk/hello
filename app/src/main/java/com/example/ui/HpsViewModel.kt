package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.catalog.MovementCatalog
import com.example.data.local.*
import com.example.domain.model.*
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HpsUiState(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val trainingPriority: TrainingPriority = TrainingPriority.HYPERTROPHY,
    val userProfile: UserProfileEntity = UserProfileEntity(),
    val favorites: Set<String> = emptySet(),
    val movementProgress: Map<String, MovementProgressEntity> = emptyMap(),
    val workoutLogs: List<WorkoutLogEntity> = emptyList(),
    val plannedDays: List<PlannedDayModel> = emptyList(),
    val conflictWarnings: List<PlannerConflictWarning> = emptyList(),
    val readinessLogs: List<ReadinessLogEntity> = emptyList(),
    val bodyMeasurements: List<BodyMeasurementEntity> = emptyList(),
    val feedbackMessage: String? = null
)

class HpsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HpsDatabase.getInstance(application)
    private val prefs = PreferencesManager(application)
    private val repo = HpsRepository(db, prefs)

    private val _feedback = MutableStateFlow<String?>(null)

    private val settingsFlow = combine(
        repo.themeMode,
        repo.unitSystem,
        repo.trainingPriority,
        repo.userProfile
    ) { theme, unit, priority, profile ->
        Quad(theme, unit, priority, profile)
    }

    private val userDataFlow = combine(
        repo.favorites,
        repo.allMovementProgress,
        repo.allWorkoutLogs,
        repo.plannedDays,
        repo.readinessLogs
    ) { favs, prog, logs, plan, readiness ->
        Penta(favs, prog, logs, plan, readiness)
    }

    val uiState: StateFlow<HpsUiState> = combine(
        settingsFlow,
        userDataFlow,
        repo.bodyMeasurements,
        _feedback
    ) { settings, data, measurements, feedback ->
        val warnings = if (data.plan.isEmpty()) emptyList()
        else PlannerConflictDetector.analyzeWeek(data.plan, settings.priority)

        HpsUiState(
            themeMode = settings.theme,
            unitSystem = settings.unit,
            trainingPriority = settings.priority,
            userProfile = settings.profile,
            favorites = data.favs,
            movementProgress = data.prog,
            workoutLogs = data.logs,
            plannedDays = data.plan,
            conflictWarnings = warnings,
            readinessLogs = data.readiness,
            bodyMeasurements = measurements,
            feedbackMessage = feedback
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HpsUiState()
    )

    fun clearFeedback() {
        _feedback.value = null
    }

    fun showFeedback(msg: String) {
        _feedback.value = msg
    }

    fun setThemeMode(mode: ThemeMode) = viewModelScope.launch { repo.setThemeMode(mode) }
    fun setUnitSystem(system: UnitSystem) = viewModelScope.launch { repo.setUnitSystem(system) }
    fun setTrainingPriority(priority: TrainingPriority) = viewModelScope.launch { repo.setTrainingPriority(priority) }

    fun toggleFavorite(movementId: String) = viewModelScope.launch {
        val current = uiState.value.favorites.contains(movementId)
        repo.toggleFavorite(movementId, current)
    }

    fun dismissOnboarding() = viewModelScope.launch {
        repo.dismissOnboarding()
    }

    fun saveUserProfile(profile: UserProfileEntity) = viewModelScope.launch {
        repo.saveUserProfile(profile)
        _feedback.value = "प्रोफ़ाइल सेव हो गई"
    }

    fun saveMovementProgress(progress: MovementProgressEntity) = viewModelScope.launch {
        repo.saveMovementProgress(progress)
        _feedback.value = "लेवल और नोट्स सेव हो गए"
    }

    fun addMovementPhoto(movementId: String, uri: String) = viewModelScope.launch {
        repo.addMovementPhoto(movementId, uri)
        _feedback.value = "प्रोग्रेस फोटो जुड़ गई"
    }

    fun removeMovementPhoto(movementId: String, uri: String) = viewModelScope.launch {
        repo.removeMovementPhoto(movementId, uri)
    }

    fun logWorkout(log: WorkoutLogEntity) = viewModelScope.launch {
        repo.logWorkout(log)
        _feedback.value = "${log.movementName} का रिकॉर्ड सेव हो गया"
    }

    fun deleteWorkoutLog(id: Long) = viewModelScope.launch {
        repo.deleteWorkoutLog(id)
        _feedback.value = "रिकॉर्ड हटाया गया"
    }

    fun loadStarterWeeklyTemplate() = viewModelScope.launch {
        repo.loadStarterWeeklyTemplate()
        _feedback.value = "7 दिन का शुरुआती प्लान लोड हो गया"
    }

    fun clearWeeklyPlan() = viewModelScope.launch {
        repo.clearWeeklyPlan()
        _feedback.value = "साप्ताहिक प्लान साफ़ किया गया"
    }

    fun savePlannedDay(day: PlannedDayModel) = viewModelScope.launch {
        repo.savePlannedDay(day)
        _feedback.value = "${day.dayName} का प्लान अपडेट हुआ"
    }

    fun addMovementToPlannedDay(dayOfWeek: Int, movement: MovementDetail) = viewModelScope.launch {
        val currentDays = uiState.value.plannedDays.ifEmpty {
            (1..7).map { d ->
                val name = PlannerConflictDetector.dayNameFor(d)
                PlannedDayModel(
                    dayOfWeek = d,
                    dayName = name,
                    title = if (d == 7) "रिकवरी" else "ट्रेनिंग सेशन",
                    focusTag = movement.discipline.shortTitle,
                    isRestDay = d == 7,
                    items = emptyList()
                )
            }
        }
        val target = currentDays.firstOrNull { it.dayOfWeek == dayOfWeek } ?: return@launch
        val defaultReps = when (movement.programmingModel) {
            ProgrammingModel.STATIC_SKILL -> "15s होल्ड"
            ProgrammingModel.CARDIO -> "30 मिनट ज़ोन 2"
            ProgrammingModel.MOBILITY -> "10 रैप्स / 45s"
            ProgrammingModel.BREATHING -> "8 मिनट नाक से"
            ProgrammingModel.SPRINT_POWER -> "4 x 20m (पूरा आराम)"
            else -> "8-12 रैप्स"
        }
        val newItem = PlannedItem(
            uid = "${movement.id}_${System.currentTimeMillis()}",
            movementId = movement.id,
            movementName = movement.name,
            disciplineId = movement.discipline.name,
            targetMuscles = movement.targetMuscles,
            sets = if (movement.programmingModel in listOf(ProgrammingModel.CARDIO, ProgrammingModel.BREATHING)) 1 else 3,
            repsOrDuration = defaultReps,
            loadKg = 0.0,
            rir = 2,
            restSeconds = if (movement.programmingModel == ProgrammingModel.SPRINT_POWER) 180 else 120
        )
        if (uiState.value.plannedDays.isEmpty()) {
            currentDays.forEach { d ->
                if (d.dayOfWeek == dayOfWeek) {
                    repo.savePlannedDay(d.copy(isRestDay = false, items = d.items + newItem))
                } else {
                    repo.savePlannedDay(d)
                }
            }
        } else {
            repo.savePlannedDay(target.copy(isRestDay = false, items = target.items + newItem))
        }
        _feedback.value = "${movement.name} को ${target.dayName} में जोड़ा गया"
    }

    fun logReadiness(log: ReadinessLogEntity) = viewModelScope.launch {
        repo.logReadiness(log)
        _feedback.value = "आज की तैयारी सेव हो गई"
    }

    fun deleteReadinessLog(id: Long) = viewModelScope.launch {
        repo.deleteReadinessLog(id)
    }

    fun logBodyMeasurement(item: BodyMeasurementEntity) = viewModelScope.launch {
        repo.logBodyMeasurement(item)
        _feedback.value = "शरीर का माप सेव हो गया"
    }

    suspend fun exportBackupJson(): String {
        val state = uiState.value
        return repo.exportBackupJson(state.themeMode, state.unitSystem, state.trainingPriority)
    }

    fun importBackupJson(raw: String) = viewModelScope.launch {
        val result = repo.importBackupJson(raw)
        _feedback.value = result.fold(
            onSuccess = { it },
            onFailure = { "बैकअप लोड नहीं हुआ: ${it.message ?: "JSON फ़ाइल गलत है"}" }
        )
    }

    fun clearAllUserData() = viewModelScope.launch {
        repo.clearAllUserData()
        _feedback.value = "आपका सारा डेटा साफ़ कर दिया गया"
    }

    fun evaluateMovementProgression(movement: MovementDetail): ProgressionRecommendation {
        val state = uiState.value
        val prog = state.movementProgress[movement.id]?.let {
            MovementProgressSnapshot(
                movementId = it.movementId,
                currentLevel = it.currentLevel,
                bestHoldSeconds = it.bestHoldSeconds,
                bestReps = it.bestReps,
                workingLoadKg = it.workingLoadKg,
                lastPracticedAt = it.lastPracticedAt
            )
        }
        val logs = state.workoutLogs
            .filter { it.movementId == movement.id }
            .map {
                LoggedSessionSummary(
                    sets = it.sets,
                    reps = it.reps,
                    loadKg = it.loadKg,
                    rir = it.rir,
                    holdSeconds = it.holdSeconds,
                    durationMinutes = it.durationMinutes,
                    distanceKm = it.distanceKm,
                    rpe = it.rpe,
                    sprintSeconds = it.sprintSeconds,
                    jumpCm = it.jumpCm,
                    romScore = it.romScore,
                    painFlag = it.painFlag,
                    loggedAt = it.loggedAt
                )
            }
        val latestReadiness = state.readinessLogs.firstOrNull()?.readinessScore
        return ProgressionEngine.evaluate(movement, prog, logs, latestReadiness)
    }

    fun getWeeklyMuscleSets(logs: List<WorkoutLogEntity>): Map<String, Int> {
        val sevenDaysAgo = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
        val recent = logs.filter { it.loggedAt >= sevenDaysAgo }
        val counts = mutableMapOf<String, Int>()
        recent.forEach { log ->
            val movement = MovementCatalog.getById(log.movementId)
            val muscles = movement?.targetMuscles?.take(2) ?: listOfNotNull(log.targetMuscle.takeIf { it.isNotBlank() })
            muscles.forEach { muscle ->
                counts[muscle] = (counts[muscle] ?: 0) + log.sets.coerceAtLeast(1)
            }
        }
        return counts.toList().sortedByDescending { it.second }.toMap()
    }

    private data class Quad<A, B, C, D>(val theme: A, val unit: B, val priority: C, val profile: D)
    private data class Penta<A, B, C, D, E>(val favs: A, val prog: B, val logs: C, val plan: D, val readiness: E)
}
