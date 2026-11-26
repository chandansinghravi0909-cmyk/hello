package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.local.ReadinessLogEntity
import com.example.ui.HpsUiState
import com.example.ui.components.EmptyStatePanel
import com.example.ui.components.SafetyCalloutCard
import com.example.ui.components.SimpleTrendLineChart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecoveryReadinessScreen(
    state: HpsUiState,
    onLogReadiness: (ReadinessLogEntity) -> Unit,
    onDeleteReadiness: (Long) -> Unit,
    onOpenMovement: (String) -> Unit
) {
    var sleepHours by remember { mutableFloatStateOf(7.5f) }
    var fatigueScore by remember { mutableIntStateOf(2) }
    var sorenessScore by remember { mutableIntStateOf(2) }
    var readinessScore by remember { mutableIntStateOf(7) }
    var restingHrText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val dateFmt = remember { SimpleDateFormat("d MMM, HH:mm", Locale.getDefault()) }
    val latest = state.readinessLogs.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("रिकवरी और आज की तैयारी", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "नींद, थकान और मसल्स की जकड़न बताती है कि आज कितना भारी वजन उठाना या स्प्रिंट करना सुरक्षित है।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (latest != null) {
            item {
                val advice = when {
                    latest.readinessScore <= 4 -> "कम तैयारी (${latest.readinessScore}/10): आज तेज स्प्रिंट और ऊंची कूद न करें। लिफ्टिंग में 2-3 RIR रखें, 1 सेट कम करें, या हल्की वॉक व ब्रीदिंग से रिकवर करें।"
                    latest.readinessScore <= 6 -> "मध्यम तैयारी (${latest.readinessScore}/10): साफ तकनीक के साथ तय मसल्स वॉल्यूम पूरा करें; भारी पीक टेस्ट आज न करें।"
                    else -> "अच्छी तैयारी (${latest.readinessScore}/10): आज वजन बढ़ाने, मुख्य ताकत और ताज़ा स्प्रिंट अभ्यास के लिए सही दिन है।"
                }
                SafetyCalloutCard(
                    title = "आज की ट्रेनिंग सलाह",
                    message = advice,
                    isCritical = latest.readinessScore <= 4
                )
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("आज की तैयारी दर्ज करें", style = MaterialTheme.typography.titleMedium)

                    Text("नींद का समय: ${"%.1f".format(sleepHours)} घंटे", style = MaterialTheme.typography.labelLarge)
                    Slider(value = sleepHours, onValueChange = { sleepHours = it }, valueRange = 3f..11f, steps = 15)

                    Text("थकान (1 तरोताज़ा -> 5 बहुत थका हुआ): $fatigueScore / 5", style = MaterialTheme.typography.labelLarge)
                    Slider(value = fatigueScore.toFloat(), onValueChange = { fatigueScore = it.toInt() }, valueRange = 1f..5f, steps = 3)

                    Text("मांसपेशियों में दर्द/जकड़न (1 बिल्कुल नहीं -> 5 बहुत ज्यादा): $sorenessScore / 5", style = MaterialTheme.typography.labelLarge)
                    Slider(value = sorenessScore.toFloat(), onValueChange = { sorenessScore = it.toInt() }, valueRange = 1f..5f, steps = 3)

                    Text("आज की कुल तैयारी: $readinessScore / 10", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Slider(value = readinessScore.toFloat(), onValueChange = { readinessScore = it.toInt() }, valueRange = 1f..10f, steps = 8)

                    OutlinedTextField(
                        value = restingHrText,
                        onValueChange = { restingHrText = it },
                        label = { Text("सुबह की रेस्टिंग हार्ट रेट bpm (वैकल्पिक)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("रिकवरी या तनाव के नोट्स") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            onLogReadiness(
                                ReadinessLogEntity(
                                    sleepHours = sleepHours.toDouble(),
                                    fatigueScore = fatigueScore,
                                    sorenessScore = sorenessScore,
                                    readinessScore = readinessScore,
                                    restingHr = restingHrText.toIntOrNull() ?: 0,
                                    notes = notes
                                )
                            )
                            notes = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("save_readiness_btn")
                    ) {
                        Text("तैयारी सेव करें")
                    }
                }
            }
        }

        item {
            SafetyCalloutCard(
                title = "सांस अभ्यास का सुरक्षा नियम",
                message = "सांस रोकने से पहले कभी भी तेज-तेज सांस (हाइपरवेंटिलेशन) न लें और पानी के अंदर या किनारे सांस रोकने का अभ्यास कभी न करें।",
                isCritical = false
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { onOpenMovement("bre_extended_exhale") },
                    modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                ) { Text("लंबा सांस छोड़ना") }
                OutlinedButton(
                    onClick = { onOpenMovement("bre_diaphragmatic") },
                    modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                ) { Text("गहरी पेट की सांस") }
            }
        }

        item {
            Text("तैयारी का इतिहास (${state.readinessLogs.size})", style = MaterialTheme.typography.titleLarge)
        }

        if (state.readinessLogs.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = "अभी कोई रिकॉर्ड नहीं है",
                    description = "अपनी नींद, थकान और तैयारी का ट्रेंड देखने के लिए ऊपर पहला चेक-इन सेव करें।",
                    icon = Icons.Outlined.Bedtime
                )
            }
        } else {
            item {
                val chronological = state.readinessLogs.take(14).reversed()
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("तैयारी स्कोर ट्रेंड (1-10)", style = MaterialTheme.typography.titleSmall)
                        SimpleTrendLineChart(
                            values = chronological.map { it.readinessScore.toFloat() },
                            labels = chronological.map { dateFmt.format(Date(it.loggedAt)).substringBefore(",") }
                        )
                    }
                }
            }

            items(state.readinessLogs.take(20), key = { it.id }) { log ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("तैयारी ${log.readinessScore}/10 • ${"%.1f".format(log.sleepHours)}h नींद", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                            Text("थकान ${log.fatigueScore}/5 • जकड़न ${log.sorenessScore}/5" + if (log.restingHr > 0) " • ${log.restingHr} bpm" else "", style = MaterialTheme.typography.bodySmall)
                            if (log.notes.isNotBlank()) Text(log.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(dateFmt.format(Date(log.loggedAt)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { onDeleteReadiness(log.id) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "रिकॉर्ड हटाएं")
                        }
                    }
                }
            }
        }
    }
}
