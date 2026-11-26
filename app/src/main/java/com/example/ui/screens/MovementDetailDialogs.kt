package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.local.MovementProgressEntity
import com.example.data.local.UnitSystem
import com.example.data.local.WorkoutLogEntity
import com.example.domain.model.*
import com.example.ui.components.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RestTimerCard(
    defaultSeconds: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    var selectedPreset by remember(defaultSeconds) { mutableIntStateOf(defaultSeconds) }
    var remainingSeconds by remember(defaultSeconds) { mutableIntStateOf(defaultSeconds) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
            if (remainingSeconds == 0) {
                isRunning = false
            }
        }
    }

    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val formatted = String.format(Locale.US, "%d:%02d", mins, secs)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("rest_timer_card"),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(
            1.dp,
            if (isRunning) accentColor.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.Timer, contentDescription = "आराम टाइमर", tint = accentColor)
                    Column {
                        Text("आराम टाइमर (Rest Timer)", style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = if (remainingSeconds == 0) "आराम पूरा — अगला सेट शुरू करें" else formatted,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isRunning) accentColor else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilledTonalIconButton(
                        onClick = {
                            if (remainingSeconds == 0) remainingSeconds = selectedPreset
                            isRunning = !isRunning
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("rest_timer_toggle")
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isRunning) "टाइमर रोकें" else "टाइमर चलाएं"
                        )
                    }
                    IconButton(
                        onClick = {
                            isRunning = false
                            remainingSeconds = selectedPreset
                        },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "रीसेट")
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(60, 90, 120, 180).forEach { sec ->
                    FilterChip(
                        selected = selectedPreset == sec,
                        onClick = {
                            isRunning = false
                            selectedPreset = sec
                            remainingSeconds = sec
                        },
                        label = { Text("${sec}s") }
                    )
                }
            }
        }
    }
}

@Composable
fun MovementSecondaryTabsSection(
    movement: MovementDetail,
    progress: MovementProgressEntity,
    recommendation: ProgressionRecommendation?,
    movementLogs: List<WorkoutLogEntity>,
    photoUris: List<String>,
    unitSystem: UnitSystem,
    accentColor: Color,
    onSaveProgress: (MovementProgressEntity) -> Unit,
    onDeleteWorkoutLog: (Long) -> Unit,
    onLaunchPhotoPicker: () -> Unit,
    onRemovePhoto: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var notesText by remember(progress.personalNotes) { mutableStateOf(progress.personalNotes) }
    var painNotesText by remember(progress.painNotes) { mutableStateOf(progress.painNotes) }
    val dateFmt = remember { SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("अधिक जानकारी, प्रगति और नोट्स", style = MaterialTheme.typography.titleMedium)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("प्रगति लेवल") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("नोट्स (${movementLogs.size})") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("तैयारी और नियम") },
                    modifier = Modifier.weight(1f)
                )
            }

            when (selectedTab) {
                0 -> {
                    if (recommendation != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("प्रगति गाइड", style = MaterialTheme.typography.titleSmall)
                                    Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            recommendation.statusBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                if (recommendation.safetyGateMessage != null) {
                                    SafetyCalloutCard("सुरक्षा अलर्ट", recommendation.safetyGateMessage, isCritical = true)
                                }
                                Text(recommendation.currentLevelTitle, style = MaterialTheme.typography.labelMedium)
                                Text(recommendation.nextSessionAction, style = MaterialTheme.typography.bodySmall)
                                Text(
                                    recommendation.whenToIncreaseDifficulty,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    movement.progressionRoadmap.forEach { lvl ->
                        val isCurrent = progress.currentLevel == lvl.levelNumber
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                            else MaterialTheme.colorScheme.surfaceContainerHigh,
                            border = BorderStroke(
                                1.dp,
                                if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(lvl.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                                    OutlinedButton(
                                        onClick = { onSaveProgress(progress.copy(currentLevel = lvl.levelNumber)) },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    ) {
                                        Text(if (isCurrent) "सक्रिय" else "चुनें")
                                    }
                                }
                                Text("${lvl.exercises} • ${lvl.prescription}", style = MaterialTheme.typography.bodySmall, color = accentColor)
                                Text(lvl.advancementCriteria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                1 -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DetailStatChip("लेवल ${progress.currentLevel}")
                        if (progress.workingLoadKg > 0) DetailStatChip("बेस्ट ${formatWeight(progress.workingLoadKg, unitSystem)}")
                        if (progress.bestHoldSeconds > 0) DetailStatChip("बेस्ट ${progress.bestHoldSeconds}s")
                        if (progress.bestReps > 0) DetailStatChip("${progress.bestReps} रैप्स")
                    }

                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("अपने नोट्स और संकेत") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    OutlinedTextField(
                        value = painNotesText,
                        onValueChange = { painNotesText = it },
                        label = { Text("दर्द या असहजता के नोट्स (यदि हों)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onSaveProgress(progress.copy(personalNotes = notesText, painNotes = painNotesText)) },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Text("नोट्स सेव करें")
                        }
                        OutlinedButton(
                            onClick = onLaunchPhotoPicker,
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Outlined.AddAPhoto, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("फोटो जोड़ें")
                        }
                    }

                    if (photoUris.isNotEmpty()) {
                        Text("प्रोग्रेस फोटो", style = MaterialTheme.typography.labelLarge)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(photoUris) { uri ->
                                Box(modifier = Modifier.size(92.dp)) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "प्रोग्रेस फोटो",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))
                                    )
                                    IconButton(
                                        onClick = { onRemovePhoto(uri) },
                                        modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "फोटो हटाएं", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text("पिछले सेशन (${movementLogs.size})", style = MaterialTheme.typography.titleSmall)
                    if (movementLogs.isEmpty()) {
                        Text(
                            "अभी कोई सेशन रिकॉर्ड नहीं है। ऊपर 'वर्कआउट सेव करें' बटन दबाकर पहला सेशन जोड़ें।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        movementLogs.take(10).forEach { log ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(dateFmt.format(Date(log.loggedAt)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                        Text(formatLogSummary(log, unitSystem), style = MaterialTheme.typography.bodySmall)
                                        if (log.notes.isNotBlank()) {
                                            Text(log.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        if (log.painFlag) {
                                            Text("दर्द दर्ज किया गया", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                    IconButton(onClick = { onDeleteWorkoutLog(log.id) }) {
                                        Icon(Icons.Outlined.Delete, contentDescription = "रिकॉर्ड हटाएं")
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    EvidenceBadge(movement.evidenceLevel)
                    DetailBulletBlock("यह अभ्यास क्यों करें", movement.whyItMatters)
                    DetailBulletBlock("जरूरी बुनियादी क्षमता", movement.baselineStrength)
                    DetailBulletBlock("साप्ताहिक प्रगति नियम", movement.prescription.weeklyProgressionRule)
                    DetailBulletBlock("डिलोड / हल्का सप्ताह", movement.prescription.deloadGuidance)
                    movement.readinessTests.forEach { rt ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("तैयारी जांच: ${rt.testName}", style = MaterialTheme.typography.titleSmall)
                                Text(rt.benchmark, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogPracticeDialog(
    movement: MovementDetail,
    unitSystem: UnitSystem,
    onDismiss: () -> Unit,
    onSubmit: (WorkoutLogEntity) -> Unit
) {
    var setsText by remember { mutableStateOf("3") }
    var repsText by remember { mutableStateOf("8") }
    var loadText by remember { mutableStateOf("0") }
    var rirText by remember { mutableStateOf("2") }
    var holdText by remember { mutableStateOf("15") }
    var durationText by remember { mutableStateOf("30") }
    var distanceText by remember { mutableStateOf(if (unitSystem == UnitSystem.IMPERIAL) "2.5" else "4.0") }
    var paceText by remember { mutableStateOf("") }
    var hrText by remember { mutableStateOf("") }
    var rpeText by remember { mutableStateOf("5") }
    var sprintSecText by remember { mutableStateOf("3.2") }
    var jumpText by remember { mutableStateOf(if (unitSystem == UnitSystem.IMPERIAL) "18" else "45") }
    var romScore by remember { mutableIntStateOf(3) }
    var notes by remember { mutableStateOf("") }
    var painFlag by remember { mutableStateOf(false) }

    val wUnit = weightUnitLabel(unitSystem)
    val dUnit = distanceUnitLabel(unitSystem)
    val lUnit = lengthUnitLabel(unitSystem)

    CopyableAlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${movement.name} का रिकॉर्ड जोड़ें") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when (movement.programmingModel) {
                    ProgrammingModel.STRENGTH_HYPERTROPHY -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("सेट", setsText, { setsText = it }, Modifier.weight(1f))
                            DetailNumField("रैप्स", repsText, { repsText = it }, Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("वजन ($wUnit)", loadText, { loadText = it }, Modifier.weight(1f), decimal = true)
                            DetailNumField("RIR (0-5)", rirText, { rirText = it }, Modifier.weight(1f))
                        }
                    }
                    ProgrammingModel.STATIC_SKILL -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("सेट", setsText, { setsText = it }, Modifier.weight(1f))
                            DetailNumField("बेस्ट होल्ड (s)", holdText, { holdText = it }, Modifier.weight(1f))
                        }
                        DetailNumField("प्रयास RPE (1-10)", rpeText, { rpeText = it }, Modifier.fillMaxWidth())
                    }
                    ProgrammingModel.MOBILITY -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("सेट", setsText, { setsText = it }, Modifier.weight(1f))
                            DetailNumField("समय (मिनट)", durationText, { durationText = it }, Modifier.weight(1f))
                        }
                        Text("खिंचाव में आराम: $romScore / 5", style = MaterialTheme.typography.labelMedium)
                        Slider(value = romScore.toFloat(), onValueChange = { romScore = it.toInt() }, valueRange = 1f..5f, steps = 3)
                    }
                    ProgrammingModel.CARDIO -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("समय (मिनट)", durationText, { durationText = it }, Modifier.weight(1f))
                            DetailNumField("दूरी ($dUnit)", distanceText, { distanceText = it }, Modifier.weight(1f), decimal = true)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("औसत HR (वैकल्पिक)", hrText, { hrText = it }, Modifier.weight(1f))
                            DetailNumField("RPE (1-10)", rpeText, { rpeText = it }, Modifier.weight(1f))
                        }
                        OutlinedTextField(value = paceText, onValueChange = { paceText = it }, label = { Text("पेस / टॉक टेस्ट नोट") }, singleLine = true)
                    }
                    ProgrammingModel.SPRINT_POWER -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("प्रयास (सेट)", setsText, { setsText = it }, Modifier.weight(1f))
                            DetailNumField("समय (सेकंड)", sprintSecText, { sprintSecText = it }, Modifier.weight(1f), decimal = true)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("कूद ($lUnit)", jumpText, { jumpText = it }, Modifier.weight(1f), decimal = true)
                            DetailNumField("गुणवत्ता RPE", rpeText, { rpeText = it }, Modifier.weight(1f))
                        }
                    }
                    ProgrammingModel.BALANCE_COORDINATION -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailNumField("सेट", setsText, { setsText = it }, Modifier.weight(1f))
                            DetailNumField("होल्ड (s) / रैप्स", holdText, { holdText = it }, Modifier.weight(1f))
                        }
                    }
                    ProgrammingModel.BREATHING -> {
                        DetailNumField("समय (मिनट)", durationText, { durationText = it }, Modifier.fillMaxWidth())
                    }
                }
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("सेशन के नोट्स") }, singleLine = true)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = painFlag, onCheckedChange = { painFlag = it })
                    Text("जोड़ या मांसपेशी में असामान्य दर्द हुआ", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val rawLoad = loadText.toDoubleOrNull() ?: 0.0
                val rawDistance = distanceText.toDoubleOrNull() ?: 0.0
                val rawJump = jumpText.toDoubleOrNull() ?: 0.0
                onSubmit(
                    WorkoutLogEntity(
                        movementId = movement.id,
                        movementName = movement.name,
                        disciplineId = movement.discipline.name,
                        programmingModel = movement.programmingModel.name,
                        targetMuscle = movement.targetMuscles.firstOrNull() ?: "",
                        sets = setsText.toIntOrNull() ?: 1,
                        reps = repsText.toIntOrNull() ?: 0,
                        loadKg = parseWeightToKg(rawLoad, unitSystem),
                        rir = rirText.toIntOrNull() ?: 2,
                        holdSeconds = holdText.toIntOrNull() ?: 0,
                        durationMinutes = durationText.toIntOrNull() ?: 0,
                        distanceKm = parseDistanceToKm(rawDistance, unitSystem),
                        paceText = paceText,
                        heartRateBpm = hrText.toIntOrNull() ?: 0,
                        rpe = rpeText.toIntOrNull() ?: 5,
                        sprintSeconds = sprintSecText.toDoubleOrNull() ?: 0.0,
                        jumpCm = parseLengthToCm(rawJump, unitSystem),
                        romScore = romScore,
                        notes = notes,
                        painFlag = painFlag
                    )
                )
            }) { Text("सेव करें") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("रद्द करें") } }
    )
}

@Composable
private fun DetailNumField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    decimal: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
private fun DetailBulletBlock(label: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(body, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun DetailStatChip(label: String) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh, shape = RoundedCornerShape(6.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

fun formatLogSummary(log: WorkoutLogEntity, unitSystem: UnitSystem = UnitSystem.METRIC): String {
    val parts = mutableListOf<String>()
    if (log.sets > 0 && log.reps > 0) parts += "${log.sets} x ${log.reps} रैप्स"
    if (log.loadKg > 0.0) parts += formatWeight(log.loadKg, unitSystem)
    if (log.holdSeconds > 0) parts += "${log.holdSeconds}s होल्ड"
    if (log.durationMinutes > 0) parts += "${log.durationMinutes} मिनट"
    if (log.distanceKm > 0.0) parts += formatDistance(log.distanceKm, unitSystem)
    if (log.heartRateBpm > 0) parts += "${log.heartRateBpm} bpm"
    if (log.sprintSeconds > 0.0) parts += "${log.sprintSeconds}s स्प्रिंट"
    if (log.jumpCm > 0.0) parts += "${formatLength(log.jumpCm, unitSystem)} कूद"
    parts += "RPE ${log.rpe}"
    return parts.joinToString(" • ")
}
