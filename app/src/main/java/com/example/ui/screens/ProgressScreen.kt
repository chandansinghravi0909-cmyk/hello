package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.local.BodyMeasurementEntity
import com.example.domain.model.DisciplineId
import com.example.domain.model.ProgrammingModel
import com.example.ui.HpsUiState
import com.example.ui.components.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TimeRangeFilter(val label: String, val days: Long) {
    WEEK("साप्ताहिक", 7L),
    MONTH("मासिक", 30L),
    YEAR("वार्षिक", 365L)
}

enum class ProgressDomainTab(val label: String) {
    ALL("सब कुछ"),
    HYPERTROPHY("मसल्स"),
    STRENGTH("ताकत"),
    CARDIO("कार्डियो"),
    SPRINT_POWER("स्प्रिंट और पावर"),
    MOBILITY("मोबिलिटी"),
    CALISTHENICS("कैलिसथेनिक्स"),
    READINESS("रिकवरी")
}

@Composable
fun ProgressScreen(
    state: HpsUiState,
    weeklyMuscleSets: Map<String, Int>,
    onOpenMovement: (String) -> Unit,
    onDeleteWorkoutLog: (Long) -> Unit,
    onLogBodyMeasurement: (BodyMeasurementEntity) -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToReadiness: () -> Unit
) {
    var timeRange by remember { mutableStateOf(TimeRangeFilter.WEEK) }
    var selectedDomain by remember { mutableStateOf(ProgressDomainTab.ALL) }
    var showMeasurementDialog by remember { mutableStateOf(false) }

    val now = System.currentTimeMillis()
    val cutoff = now - timeRange.days * 24L * 60L * 60L * 1000L
    val prevCutoff = cutoff - timeRange.days * 24L * 60L * 60L * 1000L

    val periodLogs = remember(state.workoutLogs, cutoff) {
        state.workoutLogs.filter { it.loggedAt >= cutoff }
    }
    val previousLogs = remember(state.workoutLogs, cutoff, prevCutoff) {
        state.workoutLogs.filter { it.loggedAt in prevCutoff until cutoff }
    }

    val dateFmt = remember { SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("मेरी प्रगति", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "यहाँ केवल आपके द्वारा सेव किए गए असली सेशन, रिकॉर्ड, माप और तैयारी का डेटा दिखता है।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TimeRangeFilter.entries.forEach { tr ->
                        FilterChip(
                            selected = timeRange == tr,
                            onClick = { timeRange = tr },
                            label = { Text(tr.label) }
                        )
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ProgressDomainTab.entries) { tab ->
                        FilterChip(
                            selected = selectedDomain == tab,
                            onClick = { selectedDomain = tab },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }

        item {
            val delta = periodLogs.size - previousLogs.size
            val deltaText = if (previousLogs.isEmpty()) "शुरुआती अवधि" else if (delta >= 0) "+$delta पिछली अवधि से" else "$delta पिछली अवधि से"
            val distinctDays = periodLogs.map { it.loggedAt / (24L * 60 * 60 * 1000) }.distinct().size

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricBox("कुल सेशन", "${periodLogs.size}", deltaText, Modifier.weight(1f))
                MetricBox("सक्रिय दिन", "$distinctDays", timeRange.label, Modifier.weight(1f))
                MetricBox("कुल सेट", "${periodLogs.sumOf { it.sets }}", "${state.movementProgress.size} अभ्यास", Modifier.weight(1f))
            }
        }

        if (state.workoutLogs.isEmpty() && state.readinessLogs.isEmpty() && state.bodyMeasurements.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = "अभी कोई प्रगति डेटा नहीं है",
                    description = "लाइब्रेरी से किसी अभ्यास को खोलकर अपना सेट, वजन, होल्ड या दौड़ सेव करें या आज की तैयारी दर्ज करें।",
                    actionLabel = "अभ्यास लाइब्रेरी खोलें",
                    onAction = onNavigateToLibrary,
                    icon = Icons.Outlined.BarChart
                )
            }
        } else {
            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.HYPERTROPHY)) {
                item {
                    DashboardSectionCard(title = "मसल्स (Hypertrophy): साप्ताहिक सेट और शरीर का माप") {
                        if (weeklyMuscleSets.isEmpty()) {
                            Text("मसल्स के अनुसार सेट चार्ट देखने के लिए पिछले 7 दिनों के वर्कआउट सेव करें।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            val bars = weeklyMuscleSets.entries.take(6).map { ChartBarPoint(it.key.take(8), it.value.toFloat()) }
                            SimpleBarChart(points = bars, barColor = MaterialTheme.colorScheme.primary, unitSuffix = " सेट")
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("शरीर का माप (${state.bodyMeasurements.size})", style = MaterialTheme.typography.titleSmall)
                            OutlinedButton(onClick = { showMeasurementDialog = true }, modifier = Modifier.minimumInteractiveComponentSize()) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("माप जोड़ें")
                            }
                        }
                        state.bodyMeasurements.firstOrNull()?.let { m ->
                            Text(
                                "नवीनतम: ${formatWeight(m.weightKg, state.unitSystem)} • छाती ${formatLength(m.chestCm, state.unitSystem)} • कमर ${formatLength(m.waistCm, state.unitSystem)} • बांह ${formatLength(m.armCm, state.unitSystem)} • जांघ ${formatLength(m.thighCm, state.unitSystem)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.STRENGTH)) {
                item {
                    val strengthLogs = periodLogs.filter { it.programmingModel == ProgrammingModel.STRENGTH_HYPERTROPHY.name && it.loadKg > 0 }
                    DashboardSectionCard(title = "ताकत (Strength) और अनुमानित 1RM") {
                        if (strengthLogs.isEmpty()) {
                            Text("इस अवधि में वजन के साथ कोई सेट सेव नहीं किया गया है।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            val bestByLift = strengthLogs.groupBy { it.movementName }.mapValues { (_, list) ->
                                list.maxByOrNull { it.loadKg * (1.0 + it.reps / 30.0) }!!
                            }
                            bestByLift.forEach { (name, log) ->
                                val e1rm = log.loadKg * (1.0 + log.reps / 30.0)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                    Text("${formatWeight(log.loadKg, state.unitSystem)} x ${log.reps} (1RM ~${formatWeight(e1rm, state.unitSystem)})", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.CARDIO)) {
                item {
                    val cardioLogs = periodLogs.filter { it.disciplineId == DisciplineId.CARDIO.name }
                    DashboardSectionCard(title = "कार्डियो और सहनशक्ति") {
                        if (cardioLogs.isEmpty()) {
                            Text("इस अवधि में कोई कार्डियो सेशन सेव नहीं है।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            val totalMin = cardioLogs.sumOf { it.durationMinutes }
                            val totalKm = cardioLogs.sumOf { it.distanceKm }
                            val avgHr = cardioLogs.filter { it.heartRateBpm > 0 }.map { it.heartRateBpm }.average().takeIf { !it.isNaN() }
                            Text("कुल समय: $totalMin मिनट  •  दूरी: ${formatDistance(totalKm, state.unitSystem)}" + (avgHr?.let { "  •  औसत HR: ${it.toInt()} bpm" } ?: ""), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.SPRINT_POWER)) {
                item {
                    val sprintLogs = periodLogs.filter { it.programmingModel == ProgrammingModel.SPRINT_POWER.name }
                    DashboardSectionCard(title = "स्प्रिंट और विस्फोटक पावर") {
                        if (sprintLogs.isEmpty()) {
                            Text("इस अवधि में कोई स्प्रिंट या कूद रिकॉर्ड सेव नहीं है।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            val bestSprint = sprintLogs.filter { it.sprintSeconds > 0 }.minByOrNull { it.sprintSeconds }
                            val bestJump = sprintLogs.filter { it.jumpCm > 0 }.maxByOrNull { it.jumpCm }
                            Text("सेशन: ${sprintLogs.size} • कुल प्रयास: ${sprintLogs.sumOf { it.sets }}", style = MaterialTheme.typography.bodyMedium)
                            bestSprint?.let { Text("सबसे तेज स्प्रिंट: ${it.movementName} — ${it.sprintSeconds}s", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary) }
                            bestJump?.let { Text("सबसे ऊंची/लंबी कूद: ${it.movementName} — ${formatLength(it.jumpCm, state.unitSystem)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary) }
                        }
                    }
                }
            }

            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.MOBILITY)) {
                item {
                    val mobLogs = periodLogs.filter { it.programmingModel == ProgrammingModel.MOBILITY.name }
                    DashboardSectionCard(title = "मोबिलिटी और लचीलापन") {
                        if (mobLogs.isEmpty()) {
                            Text("इस अवधि में कोई मोबिलिटी सेशन सेव नहीं है।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            val avgComfort = mobLogs.map { it.romScore }.average()
                            Text("सेशन: ${mobLogs.size}  •  औसत आराम स्कोर: ${"%.1f".format(avgComfort)}/5", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.CALISTHENICS)) {
                item {
                    val calLogs = periodLogs.filter { it.disciplineId == DisciplineId.CALISTHENICS.name || it.programmingModel == ProgrammingModel.STATIC_SKILL.name }
                    DashboardSectionCard(title = "कैलिसथेनिक्स और स्टैटिक स्किल") {
                        if (calLogs.isEmpty() && state.movementProgress.isEmpty()) {
                            Text("अभी कोई होल्ड समय या स्किल लेवल सेव नहीं है।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            val bestHold = state.movementProgress.values.maxOfOrNull { it.bestHoldSeconds } ?: 0
                            val bestReps = state.movementProgress.values.maxOfOrNull { it.bestReps } ?: 0
                            Text("स्किल सेशन: ${calLogs.size} • बेस्ट होल्ड: ${bestHold}s • बेस्ट रैप्स: $bestReps", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            if (selectedDomain in listOf(ProgressDomainTab.ALL, ProgressDomainTab.READINESS)) {
                item {
                    DashboardSectionCard(title = "रिकवरी और तैयारी ट्रेंड") {
                        if (state.readinessLogs.isEmpty()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("अभी कोई तैयारी जांच सेव नहीं है।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                TextButton(onClick = onNavigateToReadiness) { Text("चेक-इन करें") }
                            }
                        } else {
                            val chronological = state.readinessLogs.take(10).reversed()
                            SimpleTrendLineChart(
                                values = chronological.map { it.readinessScore.toFloat() },
                                labels = chronological.map { dateFmt.format(Date(it.loggedAt)).substringBefore(",") }
                            )
                            val avgSleep = state.readinessLogs.take(7).map { it.sleepHours }.average()
                            Text("हाल की औसत नींद: ${"%.1f".format(avgSleep)} घंटे  •  ताज़ा तैयारी स्कोर: ${state.readinessLogs.first().readinessScore}/10", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                Text("हाल का ट्रेनिंग इतिहास (${periodLogs.size})", style = MaterialTheme.typography.titleLarge)
            }
            items(periodLogs.take(25), key = { it.id }) { log ->
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { onOpenMovement(log.movementId) },
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(log.movementName, style = MaterialTheme.typography.titleSmall)
                            Text(formatLogSummary(log, state.unitSystem), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            Text(dateFmt.format(Date(log.loggedAt)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { onDeleteWorkoutLog(log.id) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "रिकॉर्ड हटाएं")
                        }
                    }
                }
            }
        }
    }

    if (showMeasurementDialog) {
        var weight by remember { mutableStateOf("") }
        var chest by remember { mutableStateOf("") }
        var waist by remember { mutableStateOf("") }
        var arm by remember { mutableStateOf("") }
        var thigh by remember { mutableStateOf("") }
        val wUnit = weightUnitLabel(state.unitSystem)
        val lUnit = lengthUnitLabel(state.unitSystem)

        CopyableAlertDialog(
            onDismissRequest = { showMeasurementDialog = false },
            title = { Text("शरीर का माप सेव करें") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("वजन ($wUnit)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(value = chest, onValueChange = { chest = it }, label = { Text("छाती ($lUnit)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(value = waist, onValueChange = { waist = it }, label = { Text("कमर ($lUnit)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(value = arm, onValueChange = { arm = it }, label = { Text("बांह ($lUnit)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(value = thigh, onValueChange = { thigh = it }, label = { Text("जांघ ($lUnit)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    onLogBodyMeasurement(
                        BodyMeasurementEntity(
                            weightKg = weight.toDoubleOrNull()?.let { parseWeightToKg(it, state.unitSystem) } ?: 0.0,
                            chestCm = chest.toDoubleOrNull()?.let { parseLengthToCm(it, state.unitSystem) } ?: 0.0,
                            waistCm = waist.toDoubleOrNull()?.let { parseLengthToCm(it, state.unitSystem) } ?: 0.0,
                            armCm = arm.toDoubleOrNull()?.let { parseLengthToCm(it, state.unitSystem) } ?: 0.0,
                            thighCm = thigh.toDoubleOrNull()?.let { parseLengthToCm(it, state.unitSystem) } ?: 0.0
                        )
                    )
                    showMeasurementDialog = false
                }) { Text("सेव करें") }
            },
            dismissButton = { TextButton(onClick = { showMeasurementDialog = false }) { Text("रद्द करें") } }
        )
    }
}

@Composable
private fun MetricBox(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DashboardSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}
