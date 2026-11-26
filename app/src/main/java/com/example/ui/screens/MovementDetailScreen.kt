package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ExerciseGalleryCatalog
import com.example.data.catalog.MovementCatalog
import com.example.data.local.MovementProgressEntity
import com.example.data.local.WorkoutLogEntity
import com.example.domain.model.*
import com.example.ui.HpsUiState
import com.example.ui.components.*
import kotlinx.serialization.json.Json

@Composable
fun MovementDetailScreen(
    movementId: String,
    state: HpsUiState,
    recommendation: ProgressionRecommendation?,
    onBack: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onSaveProgress: (MovementProgressEntity) -> Unit,
    onLogWorkout: (WorkoutLogEntity) -> Unit,
    onDeleteWorkoutLog: (Long) -> Unit,
    onAddPhoto: (String, String) -> Unit,
    onRemovePhoto: (String, String) -> Unit,
    onAddToPlannerDay: (Int, MovementDetail) -> Unit
) {
    val movement = MovementCatalog.getById(movementId)
    if (movement == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("अभ्यास नहीं मिला", style = MaterialTheme.typography.titleLarge)
                Button(onClick = onBack) { Text("वापस जाएं") }
            }
        }
        return
    }

    val isFav = movement.id in state.favorites
    val progress = state.movementProgress[movement.id] ?: MovementProgressEntity(movementId = movement.id)
    val movementLogs = remember(state.workoutLogs, movement.id) {
        state.workoutLogs.filter { it.movementId == movement.id }
    }
    val photoUris = remember(progress.photoUrisJson) {
        runCatching { Json.decodeFromString<List<String>>(progress.photoUrisJson) }.getOrDefault(emptyList())
    }
    val trainingSummary = remember(movement.id) {
        ExerciseGalleryCatalog.trainingSummaryFor(movement)
    }
    val defaultRestSeconds = remember(movement.id) {
        when (movement.programmingModel) {
            ProgrammingModel.SPRINT_POWER -> 180
            ProgrammingModel.STRENGTH_HYPERTROPHY -> 120
            ProgrammingModel.STATIC_SKILL -> 90
            else -> 60
        }
    }

    var showPlannerDialog by remember { mutableStateOf(false) }
    var showLogDialog by remember { mutableStateOf(false) }
    var showRestTimer by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onAddPhoto(movement.id, uri.toString())
    }

    val accent = disciplineColor(movement.discipline)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showRestTimer = !showRestTimer },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("toggle_rest_timer_btn")
                    ) {
                        Icon(Icons.Outlined.Timer, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (showRestTimer) "छुपाएं" else "टाइमर")
                    }
                    Button(
                        onClick = { showLogDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("log_practice_button")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("अभ्यास पूरा करें / सेव करें")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top bar + A. Identity
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "वापस जाएं")
                        }
                        Row {
                            IconButton(
                                onClick = { showPlannerDialog = true },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("add_to_planner_button")
                            ) {
                                Icon(Icons.Outlined.CalendarToday, contentDescription = "साप्ताहिक प्लानर में जोड़ें")
                            }
                            IconButton(
                                onClick = { onToggleFavorite(movement.id) },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("detail_favorite_button")
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (isFav) "पसंदीदा से हटाएं" else "पसंदीदा में जोड़ें",
                                    tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Text(movement.name, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = movement.trainingPurpose,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = accent.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = movement.discipline.shortTitle,
                                style = MaterialTheme.typography.labelMedium,
                                color = accent,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        DifficultyPill(movement.difficulty)
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "~${movement.estimatedMinutes} मिनट",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            if (movement.criticalSafetyNotice != null) {
                item {
                    SafetyCalloutCard(
                        title = "जरूरी सुरक्षा सूचना",
                        message = movement.criticalSafetyNotice,
                        isCritical = true
                    )
                }
            }

            // B. Visual Learning (7-Frame Gallery)
            item {
                ExerciseVisualGallery(
                    movement = movement,
                    accentColor = accent
                )
            }

            // Optional Rest Timer when toggled
            if (showRestTimer) {
                item {
                    RestTimerCard(defaultSeconds = defaultRestSeconds, accentColor = accent)
                }
            }

            // C. Exercise Info (Target Muscles, Secondary Regions, Equipment)
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("मांसपेशियाँ और ज़रूरी सामान", style = MaterialTheme.typography.titleMedium)

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("मुख्य मांसपेशियाँ", style = MaterialTheme.typography.labelMedium, color = accent)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(movement.targetMuscles) { muscle ->
                                    Surface(
                                        color = accent.copy(alpha = 0.14f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = muscle,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = accent,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("सहायक जोड़ व हिस्से", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(movement.bodyRegions) { region ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = region.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.FitnessCenter, contentDescription = "उपकरण", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            Text(
                                text = "उपकरण: ${movement.equipment.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // D. Training Parameters (Sets, Reps/Duration, Rest)
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ट्रेनिंग नियम (Sets • Reps • Rest)", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = trainingSummary.frequencyLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = accent
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ParameterTile(
                                label = "सेट",
                                value = trainingSummary.setsLabel,
                                accentColor = accent,
                                modifier = Modifier.weight(1f)
                            )
                            ParameterTile(
                                label = "रैप्स / समय",
                                value = trainingSummary.repsOrTimeLabel,
                                accentColor = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.weight(1f)
                            )
                            ParameterTile(
                                label = "आराम",
                                value = trainingSummary.restLabel,
                                accentColor = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Text(
                            text = "प्रयास स्तर: ${movement.prescription.targetEffortOrZone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // E. Execution: Steps + Key Cues + Common Mistakes + Essential Safety
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("कैसे करें (Step-by-Step)", style = MaterialTheme.typography.titleMedium)

                        movement.steps.forEachIndexed { idx, rawStep ->
                            val cleanText = rawStep.replace(Regex("^\\d+\\.\\s*"), "").trim()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    modifier = Modifier.size(24.dp),
                                    shape = CircleShape,
                                    color = accent.copy(alpha = 0.16f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${idx + 1}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = accent
                                        )
                                    }
                                }
                                Text(
                                    text = cleanText,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        if (movement.visualGuide.keyJointAngles.isNotEmpty()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Text("मुख्य तकनीक संकेत", style = MaterialTheme.typography.labelLarge, color = accent)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(movement.visualGuide.keyJointAngles) { cue ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = cue,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Common Mistakes & Corrections
            if (movement.commonMistakes.isNotEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("आम गलतियाँ और सही तरीका", style = MaterialTheme.typography.titleMedium)
                            movement.commonMistakes.forEach { mk ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(mk.title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error)
                                        Text("गलती: ${mk.looksLike}", style = MaterialTheme.typography.bodySmall)
                                        Text("सही तरीका: ${mk.howToCorrect}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Essential Safety
            item {
                SafetyCalloutCard(
                    title = "सुरक्षा और सावधानी",
                    message = "${movement.injuryRisks} • ${movement.whenToStop}",
                    isCritical = false
                )
            }

            // Progressive disclosure: Progression levels, personal notes, photos, logs & science
            item {
                MovementSecondaryTabsSection(
                    movement = movement,
                    progress = progress,
                    recommendation = recommendation,
                    movementLogs = movementLogs,
                    photoUris = photoUris,
                    unitSystem = state.unitSystem,
                    accentColor = accent,
                    onSaveProgress = onSaveProgress,
                    onDeleteWorkoutLog = onDeleteWorkoutLog,
                    onLaunchPhotoPicker = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onRemovePhoto = { uri -> onRemovePhoto(movement.id, uri) }
                )
            }
        }
    }

    if (showLogDialog) {
        LogPracticeDialog(
            movement = movement,
            unitSystem = state.unitSystem,
            onDismiss = { showLogDialog = false },
            onSubmit = { log ->
                onLogWorkout(log)
                showLogDialog = false
            }
        )
    }

    if (showPlannerDialog) {
        val dayNums = (1..7).toList()
        CopyableAlertDialog(
            onDismissRequest = { showPlannerDialog = false },
            title = { Text("${movement.name} को प्लानर में जोड़ें") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    dayNums.forEach { num ->
                        OutlinedButton(
                            onClick = {
                                onAddToPlannerDay(num, movement)
                                showPlannerDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(PlannerConflictDetector.dayNameFor(num))
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showPlannerDialog = false }) { Text("रद्द करें") } }
        )
    }
}

@Composable
private fun ParameterTile(
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = accentColor.copy(alpha = 0.11f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = accentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
