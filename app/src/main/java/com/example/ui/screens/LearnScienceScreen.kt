package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MovementCatalog
import com.example.data.catalog.ScienceCatalog
import com.example.data.local.UnitSystem
import com.example.data.local.WorkoutLogEntity
import com.example.domain.model.*
import com.example.ui.HpsUiState
import com.example.ui.components.*

enum class IntelligenceHub(val tag: String, val label: String) {
    HYPERTROPHY("hypertrophy", "मसल्स और ताकत"),
    CARDIO("cardio", "कार्डियो गाइड"),
    SPRINT("sprint", "स्प्रिंट और पावर"),
    MOBILITY("mobility", "मोबिलिटी और लचीलापन"),
    PREHAB("prehab", "चोट से बचाव")
}

@Composable
fun LearnScienceScreen(
    state: HpsUiState,
    initialHub: String? = null,
    onLogWorkout: (WorkoutLogEntity) -> Unit,
    onOpenMovement: (String) -> Unit
) {
    var topMode by remember(initialHub) { mutableIntStateOf(if (initialHub != null) 1 else 0) }
    var selectedHub by remember(initialHub) {
        mutableStateOf(IntelligenceHub.entries.firstOrNull { it.tag == initialHub } ?: IntelligenceHub.HYPERTROPHY)
    }
    var selectedScienceCat by remember { mutableStateOf<ScienceCategory?>(null) }
    var selectedBodyRegion by remember { mutableStateOf<BodyRegion?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("सीखें और विज्ञान गाइड", style = MaterialTheme.typography.headlineMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = topMode == 0,
                        onClick = { topMode = 0 },
                        label = { Text("विज्ञान केंद्र (A-G)") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = topMode == 1,
                        onClick = { topMode = 1 },
                        label = { Text("ट्रेनिंग नियम व बिल्डर") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (topMode == 0) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedScienceCat == null,
                            onClick = { selectedScienceCat = null },
                            label = { Text("सभी विषय") }
                        )
                    }
                    items(ScienceCategory.entries) { cat ->
                        FilterChip(
                            selected = selectedScienceCat == cat,
                            onClick = { selectedScienceCat = if (selectedScienceCat == cat) null else cat },
                            label = { Text(cat.title.substringAfter(". ")) }
                        )
                    }
                }
            }

            val filteredTopics = ScienceCatalog.topics.filter { selectedScienceCat == null || it.category == selectedScienceCat }
            items(filteredTopics, key = { it.id }) { topic ->
                ExpandableSectionCard(
                    title = topic.title,
                    subtitle = topic.category.title,
                    icon = Icons.Outlined.Science,
                    initiallyExpanded = filteredTopics.size <= 2
                ) {
                    EvidenceBadge(topic.evidenceLevel)
                    TopicField("1. आसान परिभाषा", topic.simpleDefinition)
                    TopicField("2. यह कैसे काम करता है", topic.howItWorks)
                    TopicField("3. वैज्ञानिक कारण", topic.scientificMechanism)
                    TopicField("4. यह क्यों ज़रूरी है", topic.whyItMatters)
                    TopicField("5. ट्रेनिंग में कैसे उपयोग करें", topic.practicalApplication)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("6. आम गलतफहमियां", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
                        topic.commonMisconceptions.forEach { m -> Text("• $m", style = MaterialTheme.typography.bodySmall) }
                    }
                    TopicField("7. मुख्य सीमाएं", topic.limitations)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("8. प्रमाणिक स्रोत", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        topic.verifiedReferences.forEach { ref -> Text("• $ref", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                }
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(IntelligenceHub.entries) { hub ->
                        FilterChip(
                            selected = selectedHub == hub,
                            onClick = { selectedHub = hub },
                            label = { Text(hub.label) }
                        )
                    }
                }
            }

            when (selectedHub) {
                IntelligenceHub.HYPERTROPHY -> {
                    item {
                        HypertrophyHubContent(
                            state = state,
                            onLogWorkout = onLogWorkout,
                            onOpenMovement = onOpenMovement
                        )
                    }
                }
                IntelligenceHub.CARDIO -> {
                    item {
                        CardioHubContent(unitSystem = state.unitSystem, onLogWorkout = onLogWorkout)
                    }
                }
                IntelligenceHub.SPRINT -> {
                    item {
                        SprintPowerHubContent(
                            latestReadiness = state.readinessLogs.firstOrNull()?.readinessScore,
                            onOpenMovement = onOpenMovement
                        )
                    }
                }
                IntelligenceHub.MOBILITY -> {
                    item {
                        MobilityHubContent(
                            selectedRegion = selectedBodyRegion,
                            onSelectRegion = { selectedBodyRegion = it },
                            onOpenMovement = onOpenMovement
                        )
                    }
                }
                IntelligenceHub.PREHAB -> {
                    item {
                        PrehabHubContent(onOpenMovement = onOpenMovement)
                    }
                }
            }
        }
    }
}

@Composable
private fun HypertrophyHubContent(
    state: HpsUiState,
    onLogWorkout: (WorkoutLogEntity) -> Unit,
    onOpenMovement: (String) -> Unit
) {
    val strengthMovements = remember {
        MovementCatalog.allMovements.filter { it.discipline == DisciplineId.STRENGTH }
    }
    var selectedMove by remember { mutableStateOf(strengthMovements.first()) }
    var setsText by remember { mutableStateOf("3") }
    var repsText by remember { mutableStateOf("8") }
    var loadText by remember { mutableStateOf(if (state.unitSystem == UnitSystem.IMPERIAL) "135" else "60") }
    var rirText by remember { mutableStateOf("2") }
    var notes by remember { mutableStateOf("") }

    val previousBest = remember(state.workoutLogs, selectedMove.id) {
        state.workoutLogs.firstOrNull { it.movementId == selectedMove.id }
    }
    val wUnit = weightUnitLabel(state.unitSystem)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("मसल्स और ताकत बढ़ाने के मुख्य नियम", style = MaterialTheme.typography.titleMedium)
                Text("• पूरा रेंज और कंट्रोल: वजन नीचे लाते समय 2-3 सेकंड कंट्रोल रखें और मसल को पूरा खिंचाव दें।", style = MaterialTheme.typography.bodySmall)
                Text("• सही मात्रा: शुरुआत प्रति सप्ताह ~8-12 सेट प्रति मसल से करें; बिना रिकवरी ज्यादा सेट नुकसान कर सकते हैं।", style = MaterialTheme.typography.bodySmall)
                Text("• सही मेहनत (RIR): अधिकतर सेट में 1-3 रैप बाकी (RIR) रखें, हर सेट को फेलियर तक न खींचें।", style = MaterialTheme.typography.bodySmall)
                Text("• प्रगति का क्रम: पहले रैप्स बढ़ाएं -> फिर वजन बढ़ाएं -> तकनीक सुधारें -> तभी सेट बढ़ाएं।", style = MaterialTheme.typography.bodySmall)
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("तुरंत सेट रिकॉर्ड करें", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(strengthMovements) { m ->
                        FilterChip(
                            selected = selectedMove.id == m.id,
                            onClick = { selectedMove = m },
                            label = { Text(m.name) }
                        )
                    }
                }
                Text("लक्ष्य मसल्स: ${selectedMove.targetMuscles.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                if (previousBest != null) {
                    Text("पिछला रिकॉर्ड: ${previousBest.sets} x ${previousBest.reps} @ ${formatWeight(previousBest.loadKg, state.unitSystem)} (${previousBest.rir} RIR)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = setsText, onValueChange = { setsText = it }, label = { Text("सेट") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = repsText, onValueChange = { repsText = it }, label = { Text("रैप्स") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = loadText, onValueChange = { loadText = it }, label = { Text(wUnit) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = rirText, onValueChange = { rirText = it }, label = { Text("RIR") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("तकनीक / रेंज नोट") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val rawLoad = loadText.toDoubleOrNull() ?: 0.0
                            onLogWorkout(
                                WorkoutLogEntity(
                                    movementId = selectedMove.id,
                                    movementName = selectedMove.name,
                                    disciplineId = selectedMove.discipline.name,
                                    programmingModel = selectedMove.programmingModel.name,
                                    targetMuscle = selectedMove.targetMuscles.firstOrNull() ?: "",
                                    sets = setsText.toIntOrNull() ?: 3,
                                    reps = repsText.toIntOrNull() ?: 8,
                                    loadKg = parseWeightToKg(rawLoad, state.unitSystem),
                                    rir = rirText.toIntOrNull() ?: 2,
                                    notes = notes
                                )
                            )
                        },
                        modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                    ) { Text("सेट सेव करें") }
                    OutlinedButton(onClick = { onOpenMovement(selectedMove.id) }, modifier = Modifier.minimumInteractiveComponentSize()) {
                        Text("पूरी गाइड")
                    }
                }
            }
        }
    }
}

@Composable
private fun CardioHubContent(
    unitSystem: UnitSystem,
    onLogWorkout: (WorkoutLogEntity) -> Unit
) {
    val cardioTypes = listOf(
        "car_zone2_run" to "ज़ोन 2 दौड़",
        "car_incline_walking" to "ऊंचाई पर चलना",
        "car_cycling" to "साइकिलिंग",
        "car_swimming" to "तैराकी",
        "car_rowing" to "रोइंग",
        "car_interval_training" to "इंटरवल ट्रेनिंग"
    )
    var selectedType by remember { mutableStateOf(cardioTypes.first()) }
    var duration by remember { mutableStateOf("35") }
    var distance by remember { mutableStateOf(if (unitSystem == UnitSystem.IMPERIAL) "3.0" else "5.0") }
    var hr by remember { mutableStateOf("") }
    var rpe by remember { mutableStateOf("4") }
    val dUnit = distanceUnitLabel(unitSystem)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("कार्डियो के सही नियम", style = MaterialTheme.typography.titleMedium)
                Text("• बात करने की जांच (Talk Test): उम्र से हार्ट रेट निकालने (220 - उम्र) के बजाय अपनी सांस पर ध्यान दें। ज़ोन 2 में आप 6-8 शब्द आसानी से बोल पाते हैं।", style = MaterialTheme.typography.bodySmall)
                Text("• सेहत बनाम रेस तैयारी: सामान्य सेहत के लिए हफ्ते में ~150 मिनट मध्यम कार्डियो काफी है; 5K/10K के लिए चरणबद्ध प्लान चाहिए।", style = MaterialTheme.typography.bodySmall)
                Text("• मसल्स की सुरक्षा: 80% कार्डियो हल्का रखें (साइकिल या तेज वॉक) ताकि पैरों की ताकत और रिकवरी बची रहे।", style = MaterialTheme.typography.bodySmall)
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("कार्डियो सेशन सेव करें", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(cardioTypes) { pair ->
                        FilterChip(
                            selected = selectedType.first == pair.first,
                            onClick = { selectedType = pair },
                            label = { Text(pair.second) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("मिनट") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = distance, onValueChange = { distance = it }, label = { Text(dUnit) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = hr, onValueChange = { hr = it }, label = { Text("HR (वैकल्पिक)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = rpe, onValueChange = { rpe = it }, label = { Text("RPE") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), singleLine = true)
                }
                Button(
                    onClick = {
                        val rawDist = distance.toDoubleOrNull() ?: 0.0
                        onLogWorkout(
                            WorkoutLogEntity(
                                movementId = selectedType.first,
                                movementName = selectedType.second,
                                disciplineId = DisciplineId.CARDIO.name,
                                programmingModel = ProgrammingModel.CARDIO.name,
                                targetMuscle = "दिल और फेफड़े",
                                sets = 1,
                                durationMinutes = duration.toIntOrNull() ?: 30,
                                distanceKm = parseDistanceToKm(rawDist, unitSystem),
                                heartRateBpm = hr.toIntOrNull() ?: 0,
                                rpe = rpe.toIntOrNull() ?: 4,
                                notes = "कार्डियो बिल्डर सेशन"
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
                ) { Text("कार्डियो सेव करें") }
            }
        }

        Text("5 व्यावहारिक कार्डियो प्लान", style = MaterialTheme.typography.titleMedium)
        ScienceCatalog.cardioPlans.forEach { plan ->
            ExpandableSectionCard(
                title = plan.title,
                subtitle = plan.targetAudience,
                icon = Icons.AutoMirrored.Outlined.DirectionsRun
            ) {
                plan.weeklyStructure.forEach { line -> Text("• $line", style = MaterialTheme.typography.bodySmall) }
                TopicField("तीव्रता का बंटवारा", plan.intensityDistribution)
                TopicField("आगे बढ़ने का नियम", plan.progressionRule)
                TopicField("सुरक्षा और रिकवरी", plan.safetyNote)
            }
        }
    }
}

@Composable
private fun SprintPowerHubContent(
    latestReadiness: Int?,
    onOpenMovement: (String) -> Unit
) {
    val sprintDrills = remember {
        MovementCatalog.allMovements.filter { it.discipline == DisciplineId.SPRINT_POWER }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (latestReadiness != null && latestReadiness <= 5) {
            SafetyCalloutCard(
                title = "तैयारी कम है ($latestReadiness/10)",
                message = "आज थकान ज्यादा है। पूरी रफ्तार से स्प्रिंट या डेप्थ जंप के बजाय हल्की A-स्किप, एंकलिंग या वॉल ड्रिल ही करें।",
                isCritical = true
            )
        } else {
            SafetyCalloutCard(
                title = "स्प्रिंट और पावर सुरक्षा नियम",
                message = "बिना तैयारी के शुरुआती लोगों को 100% स्प्रिंट या डेप्थ जंप न दें। हर प्रयास के बीच 2-4 मिनट का पूरा आराम लें—थककर स्प्रिंट न करें।",
                isCritical = false
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("रफ्तार और पावर के 5 मुख्य प्रकार", style = MaterialTheme.typography.titleMedium)
                TopicField("1. शुरुआती रफ्तार (0-30m)", "शरीर को आगे झुकाकर जमीन को पीछे धकेलना और तेज शुरुआत करना।")
                TopicField("2. अधिकतम गति (30-60m)", "सीधी कमर, बहुत कम जमीन संपर्क समय और मजबूत हैमस्ट्रिंग।")
                TopicField("3. स्पीड सहनशक्ति", "हल्की थकान में भी रफ्तार बनाए रखना (यह मुख्य ताकत के बाद आती है)।")
                TopicField("4. विस्फोटक पावर", "तुरंत जोर लगाना जैसे ब्रॉड जंप, जंप स्क्वाट और ट्रैप-बार जंप।")
                TopicField("5. प्लायोमेट्रिक उछाल", "पोगो जंप और डेप्थ जंप जैसी तेज रिबाउंड क्षमता।")
            }
        }

        Text("स्प्रिंट और पावर अभ्यास (${sprintDrills.size})", style = MaterialTheme.typography.titleMedium)
        sprintDrills.forEach { m ->
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { onOpenMovement(m.id) },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(m.name, style = MaterialTheme.typography.titleSmall)
                        Text("${m.difficulty.label} • ${m.category.badge}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("देखें ->", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun MobilityHubContent(
    selectedRegion: BodyRegion?,
    onSelectRegion: (BodyRegion?) -> Unit,
    onOpenMovement: (String) -> Unit
) {
    val regions = listOf(
        BodyRegion.NECK, BodyRegion.SHOULDERS, BodyRegion.THORACIC_SPINE, BodyRegion.WRISTS,
        BodyRegion.ELBOWS, BodyRegion.HIPS, BodyRegion.HAMSTRINGS, BodyRegion.ADDUCTORS,
        BodyRegion.KNEES, BodyRegion.ANKLES, BodyRegion.FEET
    )
    val mobMoves = remember(selectedRegion) {
        MovementCatalog.allMovements.filter { m ->
            (m.discipline == DisciplineId.MOBILITY || m.programmingModel == ProgrammingModel.MOBILITY) &&
                (selectedRegion == null || selectedRegion in m.bodyRegions)
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("मोबिलिटी और लचीलेपन में अंतर", style = MaterialTheme.typography.titleMedium)
                Text("• लचीलापन बनाम मोबिलिटी: लचीलापन सिर्फ खिंचाव है; मोबिलिटी उस पूरे रेंज में जोड़ की अपनी ताकत और नियंत्रण है।", style = MaterialTheme.typography.bodySmall)
                Text("• कब क्या करें: वर्कआउट से पहले डायनामिक और CARs करें; स्टैटिक और लोडेड स्ट्रेच वर्कआउट के बाद या आराम वाले दिन करें।", style = MaterialTheme.typography.bodySmall)
                Text("• सुरक्षा नियम: किसी जोड़ में चुभने वाला दर्द होने पर कभी जोर न लगाएं।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
            }
        }

        Text("जोड़ के अनुसार चुनें (11 हिस्से)", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                FilterChip(selected = selectedRegion == null, onClick = { onSelectRegion(null) }, label = { Text("सभी") })
            }
            items(regions) { r ->
                FilterChip(selected = selectedRegion == r, onClick = { onSelectRegion(if (selectedRegion == r) null else r) }, label = { Text(r.label) })
            }
        }

        mobMoves.forEach { m ->
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { onOpenMovement(m.id) },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(m.name, style = MaterialTheme.typography.titleSmall)
                        Text("${m.prescription.volumeAndSets} • ${m.prescription.frequencyPerWeek}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("खोलें ->", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun PrehabHubContent(onOpenMovement: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SafetyCalloutCard(
            title = "चोट से बचाव बनाम इलाज (फर्क समझें)",
            message = "प्रीहैब अभ्यास धीरे-धीरे टिशू और जोड़ों की सहनशक्ति बढ़ाते हैं—यह किसी चोट की गारंटी या मेडिकल इलाज नहीं है। लगातार दर्द में डॉक्टर या फिजियोथेरेपिस्ट से मिलें।",
            isCritical = true
        )
        ScienceCatalog.bodyRegionGuides.forEach { guide ->
            ExpandableSectionCard(
                title = "${guide.priorityOrder}. ${guide.region.label}",
                subtitle = guide.roleInPerformance,
                icon = Icons.Outlined.Healing
            ) {
                TopicField("सुझाए गए अभ्यास", (guide.prehabExercises + guide.mobilityExercises).joinToString(", "))
                TopicField("सही मात्रा", guide.recommendedDosage)
                TopicField("आगे कैसे बढ़ें", guide.progressionRule)
                TopicField("सप्ताह में कब करें", guide.weeklyIntegration)
                TopicField("डॉक्टर को कब दिखाएं", guide.medicalRedFlags)
            }
        }
        val prehabMoves = MovementCatalog.allMovements.filter { it.discipline == DisciplineId.PREHAB }
        Text("प्रीहैब अभ्यास गाइड (${prehabMoves.size})", style = MaterialTheme.typography.titleMedium)
        prehabMoves.forEach { m ->
            OutlinedButton(
                onClick = { onOpenMovement(m.id) },
                modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
            ) {
                Text("${m.name} (${m.difficulty.label})")
            }
        }
    }
}

@Composable
private fun TopicField(label: String, text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
