package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MovementCatalog
import com.example.data.local.UnitSystem
import com.example.data.local.WorkoutLogEntity
import com.example.domain.model.*
import com.example.ui.HpsUiState
import com.example.ui.components.*
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.CoralDanger
import com.example.ui.theme.EmeraldPrimary
import java.util.Locale

@Composable
fun TrainingPlannerScreen(
    state: HpsUiState,
    onLoadStarterTemplate: () -> Unit,
    onClearPlan: () -> Unit,
    onSaveDay: (PlannedDayModel) -> Unit,
    onAddMovementToDay: (Int, MovementDetail) -> Unit,
    onLogWorkout: (WorkoutLogEntity) -> Unit,
    onOpenMovement: (String) -> Unit
) {
    var selectedDayNum by remember { mutableIntStateOf(1) }
    var showAddMovementPicker by remember { mutableStateOf(false) }
    var showEditDayMeta by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<PlannedItem?>(null) }
    var showWeeklyReview by remember { mutableStateOf(false) }

    val days = state.plannedDays.sortedBy { it.dayOfWeek }
    val activeDay = days.firstOrNull { it.dayOfWeek == selectedDayNum }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("7-दिन का ट्रेनिंग प्लानर", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "प्राथमिकता: ${state.trainingPriority.label}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (days.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { showWeeklyReview = true },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("सप्ताह की समीक्षा")
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLoadStarterTemplate,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("load_starter_plan_btn")
                ) {
                    Text(if (days.isEmpty()) "शुरुआती प्लान लोड करें" else "शुरुआती प्लान रीसेट करें")
                }
                if (days.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onClearPlan,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("सप्ताह साफ़ करें")
                    }
                }
            }
        }

        if (state.conflictWarnings.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("टकराव और रिकवरी चेतावनियां (${state.conflictWarnings.size})", style = MaterialTheme.typography.titleMedium)
                    state.conflictWarnings.forEach { w ->
                        val color = when (w.severity) {
                            ConflictSeverity.HIGH -> CoralDanger
                            ConflictSeverity.MODERATE -> AmberTertiary
                            ConflictSeverity.INFO -> MaterialTheme.colorScheme.secondary
                        }
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            color = color.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(w.title, style = MaterialTheme.typography.titleSmall, color = color)
                                Text(w.detail, style = MaterialTheme.typography.bodySmall)
                                Text("सुझाव: ${w.suggestedAdjustment}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        } else if (days.isNotEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = EmeraldPrimary.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = EmeraldPrimary)
                        Text("कोई शेड्यूल टकराव नहीं मिला। स्प्रिंट, भारी लोड और रिकवरी के दिन संतुलित हैं।", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        if (days.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = "कोई साप्ताहिक प्लान नहीं है",
                    description = "शुरुआती अपर/लोअर + कार्डियो + रविवार रिकवरी प्लान लोड करें और अपनी ज़रूरत से बदलें।",
                    actionLabel = "7-दिन का प्लान लोड करें",
                    onAction = onLoadStarterTemplate,
                    icon = Icons.Outlined.CalendarMonth
                )
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(days, key = { it.dayOfWeek }) { day ->
                        FilterChip(
                            selected = selectedDayNum == day.dayOfWeek,
                            onClick = { selectedDayNum = day.dayOfWeek },
                            label = { Text("${day.dayName} (${day.items.size})") },
                            modifier = Modifier.testTag("planner_day_${day.dayOfWeek}")
                        )
                    }
                }
            }

            if (activeDay != null) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${activeDay.dayName}: ${activeDay.title}", style = MaterialTheme.typography.titleLarge)
                                    Text(
                                        if (activeDay.isRestDay) "आराम / रिकवरी का दिन • ${activeDay.focusTag}"
                                        else "${activeDay.focusTag} • कुल ${activeDay.items.sumOf { it.sets }} सेट",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { showEditDayMeta = true }, modifier = Modifier.minimumInteractiveComponentSize()) {
                                    Icon(Icons.Outlined.Edit, contentDescription = "दिन की सेटिंग बदलें")
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { showAddMovementPicker = true },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("अभ्यास जोड़ें")
                                }
                                if (activeDay.items.isNotEmpty()) {
                                    OutlinedButton(
                                        onClick = {
                                            activeDay.items.forEach { item ->
                                                val m = MovementCatalog.getById(item.movementId)
                                                onLogWorkout(
                                                    WorkoutLogEntity(
                                                        movementId = item.movementId,
                                                        movementName = item.movementName,
                                                        disciplineId = item.disciplineId,
                                                        programmingModel = m?.programmingModel?.name ?: ProgrammingModel.STRENGTH_HYPERTROPHY.name,
                                                        targetMuscle = item.targetMuscles.firstOrNull() ?: "",
                                                        sets = item.sets,
                                                        reps = item.repsOrDuration.filter { it.isDigit() }.take(2).toIntOrNull() ?: 8,
                                                        loadKg = item.loadKg,
                                                        rir = item.rir,
                                                        notes = "${activeDay.dayName} प्लान से सेव"
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    ) {
                                        Text("सभी सेव करें (${activeDay.items.size})")
                                    }
                                }
                            }
                        }
                    }
                }

                if (activeDay.items.isEmpty()) {
                    item {
                        EmptyStatePanel(
                            title = if (activeDay.isRestDay) "रिकवरी का दिन" else "कोई अभ्यास तय नहीं है",
                            description = "${activeDay.dayName} के लिए कोई भी अभ्यास जोड़ने हेतु ऊपर 'अभ्यास जोड़ें' दबाएं।",
                            icon = Icons.Outlined.SelfImprovement
                        )
                    }
                } else {
                    items(activeDay.items, key = { it.uid }) { item ->
                        val movement = MovementCatalog.getById(item.movementId)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenMovement(item.movementId) },
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (movement != null) {
                                    ExercisePoseThumbnail(
                                        movement = movement,
                                        modifier = Modifier.size(width = 52.dp, height = 56.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(item.movementName, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(
                                        "${item.sets} सेट • ${item.repsOrDuration}" +
                                            (if (item.loadKg > 0) " • ${formatWeight(item.loadKg, state.unitSystem)}" else "") +
                                            (if (item.restSeconds > 0) " • ${item.restSeconds}s आराम" else ""),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (item.targetMuscles.isNotEmpty()) {
                                        Text(item.targetMuscles.take(3).joinToString(" · "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                Row {
                                    IconButton(onClick = { editingItem = item }, modifier = Modifier.minimumInteractiveComponentSize()) {
                                        Icon(Icons.Outlined.Tune, contentDescription = "${item.movementName} बदलें")
                                    }
                                    IconButton(
                                        onClick = {
                                            onSaveDay(activeDay.copy(items = activeDay.items.filterNot { it.uid == item.uid }))
                                        },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    ) {
                                        Icon(Icons.Outlined.Delete, contentDescription = "${item.movementName} हटाएं")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddMovementPicker && activeDay != null) {
        MovementPickerDialog(
            onDismiss = { showAddMovementPicker = false },
            onPick = { movement ->
                onAddMovementToDay(activeDay.dayOfWeek, movement)
                showAddMovementPicker = false
            }
        )
    }

    if (showEditDayMeta && activeDay != null) {
        var title by remember { mutableStateOf(activeDay.title) }
        var focus by remember { mutableStateOf(activeDay.focusTag) }
        var isRest by remember { mutableStateOf(activeDay.isRestDay) }
        CopyableAlertDialog(
            onDismissRequest = { showEditDayMeta = false },
            title = { Text("${activeDay.dayName} बदलें") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("सेशन का नाम") }, singleLine = true)
                    OutlinedTextField(value = focus, onValueChange = { focus = it }, label = { Text("मुख्य फोकस") }, singleLine = true)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isRest, onCheckedChange = { isRest = it })
                        Text("इसे आराम / रिकवरी का दिन बनाएं")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSaveDay(activeDay.copy(title = title.ifBlank { activeDay.dayName }, focusTag = focus, isRestDay = isRest))
                    showEditDayMeta = false
                }) { Text("सेव करें") }
            },
            dismissButton = { TextButton(onClick = { showEditDayMeta = false }) { Text("रद्द करें") } }
        )
    }

    if (editingItem != null && activeDay != null) {
        val target = editingItem!!
        val initialDisplayLoad = if (state.unitSystem == UnitSystem.IMPERIAL) target.loadKg * 2.20462 else target.loadKg
        var sets by remember { mutableStateOf(target.sets.toString()) }
        var reps by remember { mutableStateOf(target.repsOrDuration) }
        var load by remember { mutableStateOf(String.format(Locale.US, "%.1f", initialDisplayLoad)) }
        var rest by remember { mutableStateOf(target.restSeconds.toString()) }
        CopyableAlertDialog(
            onDismissRequest = { editingItem = null },
            title = { Text("${target.movementName} बदलें") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = sets, onValueChange = { sets = it }, label = { Text("सेट") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = reps, onValueChange = { reps = it }, label = { Text("रैप्स / समय") }, singleLine = true)
                    OutlinedTextField(value = load, onValueChange = { load = it }, label = { Text("वजन (${weightUnitLabel(state.unitSystem)})") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(value = rest, onValueChange = { rest = it }, label = { Text("आराम (सेकंड)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val parsedLoadKg = load.toDoubleOrNull()?.let { parseWeightToKg(it, state.unitSystem) } ?: target.loadKg
                    val updated = target.copy(
                        sets = sets.toIntOrNull()?.coerceIn(1, 20) ?: target.sets,
                        repsOrDuration = reps.ifBlank { target.repsOrDuration },
                        loadKg = parsedLoadKg,
                        restSeconds = rest.toIntOrNull() ?: target.restSeconds
                    )
                    onSaveDay(activeDay.copy(items = activeDay.items.map { if (it.uid == target.uid) updated else it }))
                    editingItem = null
                }) { Text("अपडेट करें") }
            },
            dismissButton = { TextButton(onClick = { editingItem = null }) { Text("रद्द करें") } }
        )
    }

    if (showWeeklyReview) {
        val totalSets = days.flatMap { it.items }.sumOf { it.sets }
        val restDays = days.count { it.isRestDay }
        val discCounts = days.flatMap { it.items }.groupingBy { item ->
            DisciplineId.entries.firstOrNull { it.name == item.disciplineId }?.shortTitle ?: item.disciplineId
        }.eachCount()
        CopyableAlertDialog(
            onDismissRequest = { showWeeklyReview = false },
            title = { Text("साप्ताहिक प्लान समीक्षा") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("कुल साप्ताहिक सेट: $totalSets", style = MaterialTheme.typography.titleSmall)
                    Text("आराम / रिकवरी के दिन: $restDays / 7", style = MaterialTheme.typography.bodySmall)
                    Text("सक्रिय टकराव चेतावनियां: ${state.conflictWarnings.size}", style = MaterialTheme.typography.bodySmall)
                    HorizontalDivider()
                    discCounts.forEach { (disc, count) ->
                        Text("• $disc: $count अभ्यास", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(
                        "सुझाव: मुख्य मसल्स के लिए 10-20 क्वालिटी सेट प्रति सप्ताह रखें और भारी स्क्वाट/RDL से 24 घंटे पहले या बाद में तेज स्प्रिंट न रखें।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = { Button(onClick = { showWeeklyReview = false }) { Text("ठीक है") } }
        )
    }
}

@Composable
private fun MovementPickerDialog(
    onDismiss: () -> Unit,
    onPick: (MovementDetail) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query) {
        MovementCatalog.searchAndFilter(query = query).take(25)
    }
    CopyableAlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("इस दिन में अभ्यास जोड़ें") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 380.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("अभ्यास या सिस्टम खोजें...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(filtered, key = { it.id }) { m ->
                        Surface(
                            modifier = Modifier.fillMaxWidth().clickable { onPick(m) },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(m.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("${m.discipline.shortTitle} • ${m.difficulty.label}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("बंद करें") } }
    )
}
