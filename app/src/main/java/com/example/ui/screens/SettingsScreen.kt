package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.local.UnitSystem
import com.example.data.local.UserProfileEntity
import com.example.domain.model.TrainingPriority
import com.example.ui.HpsUiState
import com.example.ui.components.*
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale

@Composable
fun SettingsScreen(
    state: HpsUiState,
    onSetThemeMode: (ThemeMode) -> Unit,
    onSetUnitSystem: (UnitSystem) -> Unit,
    onSetTrainingPriority: (TrainingPriority) -> Unit,
    onSaveProfile: (UserProfileEntity) -> Unit,
    onExportBackupJson: suspend () -> String,
    onImportBackupJson: (String) -> Unit,
    onClearAllData: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val profile = state.userProfile
    val initialHeightDisplay = profile.heightCm?.let {
        val v = if (state.unitSystem == UnitSystem.IMPERIAL) it / 2.54 else it
        String.format(Locale.US, "%.1f", v)
    } ?: ""
    val initialWeightDisplay = profile.weightKg?.let {
        val v = if (state.unitSystem == UnitSystem.IMPERIAL) it * 2.20462 else it
        String.format(Locale.US, "%.1f", v)
    } ?: ""

    var name by remember(profile.name) { mutableStateOf(profile.name) }
    var ageText by remember(profile.age) { mutableStateOf(profile.age?.toString() ?: "") }
    var heightText by remember(profile.heightCm, state.unitSystem) { mutableStateOf(initialHeightDisplay) }
    var weightText by remember(profile.weightKg, state.unitSystem) { mutableStateOf(initialWeightDisplay) }
    var experience by remember(profile.experienceLevel) { mutableStateOf(profile.experienceLevel) }
    var primaryGoal by remember(profile.primaryGoal) { mutableStateOf(profile.primaryGoal) }
    var trainingDaysText by remember(profile.trainingDaysPerWeek) { mutableStateOf(profile.trainingDaysPerWeek.toString()) }
    var sessionMinText by remember(profile.availableMinutesPerSession) { mutableStateOf(profile.availableMinutesPerSession.toString()) }
    var limitations by remember(profile.movementLimitations) { mutableStateOf(profile.movementLimitations) }

    val availableEquipmentOptions = listOf(
        "बॉडीवेट", "बारबेल", "डंबल", "पुल-अप बार", "रिंग्स",
        "केटलबेल", "रेजिस्टेंस बैंड", "केबल मशीन", "साइकिल", "रनिंग ट्रैक"
    )
    var selectedEquip by remember(profile.equipmentJson) {
        mutableStateOf(
            runCatching { Json.decodeFromString<List<String>>(profile.equipmentJson).toSet() }
                .getOrDefault(setOf("बॉडीवेट"))
        )
    }

    var showClearDialog by remember { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                runCatching {
                    val jsonString = onExportBackupJson()
                    context.contentResolver.openOutputStream(uri)?.use { out ->
                        out.write(jsonString.toByteArray(Charsets.UTF_8))
                    } ?: error("फ़ाइल खोलने में समस्या आई")
                }.fold(
                    onSuccess = { onShowMessage("JSON बैकअप सेव हो गया") },
                    onFailure = { onShowMessage("बैकअप सेव नहीं हुआ: ${it.message}") }
                )
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    input.bufferedReader().readText()
                } ?: error("फ़ाइल पढ़ी नहीं जा सकी")
            }.fold(
                onSuccess = { onImportBackupJson(it) },
                onFailure = { onShowMessage("बैकअप पढ़ा नहीं जा सका: ${it.message}") }
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("प्रोफ़ाइल और सेटिंग्स", style = MaterialTheme.typography.headlineMedium)
        }

        // थीम, यूनिट और प्राथमिकता
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("थीम, यूनिट और प्राथमिकता", style = MaterialTheme.typography.titleMedium)

                    Text("ऐप थीम", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ThemeMode.entries.forEach { mode ->
                            FilterChip(
                                selected = state.themeMode == mode,
                                onClick = { onSetThemeMode(mode) },
                                label = { Text(mode.label) }
                            )
                        }
                    }

                    Text("माप की इकाई (Units)", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        UnitSystem.entries.forEach { unit ->
                            FilterChip(
                                selected = state.unitSystem == unit,
                                onClick = { onSetUnitSystem(unit) },
                                label = { Text(unit.label) }
                            )
                        }
                    }

                    Text("मुख्य ट्रेनिंग प्राथमिकता", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(TrainingPriority.entries) { prio ->
                            FilterChip(
                                selected = state.trainingPriority == prio,
                                onClick = { onSetTrainingPriority(prio) },
                                label = { Text(prio.label) }
                            )
                        }
                    }
                }
            }
        }

        // सरल प्रोफ़ाइल
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("मेरी प्रोफ़ाइल", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("नाम (वैकल्पिक)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = ageText, onValueChange = { ageText = it }, label = { Text("उम्र") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = heightText, onValueChange = { heightText = it }, label = { Text("लंबाई (${lengthUnitLabel(state.unitSystem)})") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = weightText, onValueChange = { weightText = it }, label = { Text("वजन (${weightUnitLabel(state.unitSystem)})") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true, modifier = Modifier.weight(1f))
                    }

                    Text("अनुभव स्तर", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf("शुरुआती", "शुरुआती-मध्यम", "मध्यम", "अनुभवी")) { exp ->
                            FilterChip(selected = experience == exp, onClick = { experience = exp }, label = { Text(exp) })
                        }
                    }

                    OutlinedTextField(value = primaryGoal, onValueChange = { primaryGoal = it }, label = { Text("मुख्य लक्ष्य") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = trainingDaysText, onValueChange = { trainingDaysText = it }, label = { Text("हफ्ते में दिन") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = sessionMinText, onValueChange = { sessionMinText = it }, label = { Text("समय (मिनट)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                    }

                    Text("उपलब्ध उपकरण", style = MaterialTheme.typography.labelLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(availableEquipmentOptions) { eq ->
                            FilterChip(
                                selected = eq in selectedEquip,
                                onClick = {
                                    selectedEquip = if (eq in selectedEquip) selectedEquip - eq else selectedEquip + eq
                                },
                                label = { Text(eq) }
                            )
                        }
                    }

                    OutlinedTextField(value = limitations, onValueChange = { limitations = it }, label = { Text("कोई दर्द या सीमा (नोट्स)") }, modifier = Modifier.fillMaxWidth())

                    Button(
                        onClick = {
                            onSaveProfile(
                                profile.copy(
                                    name = name,
                                    age = ageText.toIntOrNull(),
                                    heightCm = heightText.toDoubleOrNull()?.let { parseLengthToCm(it, state.unitSystem) },
                                    weightKg = weightText.toDoubleOrNull()?.let { parseWeightToKg(it, state.unitSystem) },
                                    experienceLevel = experience,
                                    primaryGoal = primaryGoal,
                                    equipmentJson = Json.encodeToString(selectedEquip.toList()),
                                    trainingDaysPerWeek = trainingDaysText.toIntOrNull()?.coerceIn(1, 7) ?: 5,
                                    availableMinutesPerSession = sessionMinText.toIntOrNull()?.coerceIn(15, 180) ?: 60,
                                    movementLimitations = limitations
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("save_profile_btn")
                    ) {
                        Text("प्रोफ़ाइल सेव करें")
                    }
                }
            }
        }

        // बैकअप और डेटा प्रबंधन
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("डेटा बैकअप (JSON Export / Import)", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "आपका सारा वर्कआउट, प्लानर, तैयारी और प्रोफ़ाइल डेटा फोन में सुरक्षित रहता है। जरूरत पड़ने पर आप JSON बैकअप सेव या वापस लोड कर सकते हैं।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { exportLauncher.launch("hps_backup_v1.json") },
                            modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Outlined.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("बैकअप सेव करें")
                        }
                        OutlinedButton(
                            onClick = { importLauncher.launch(arrayOf("application/json", "text/plain")) },
                            modifier = Modifier.weight(1f).minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("बैकअप लोड करें")
                        }
                    }
                    OutlinedButton(
                        onClick = { showClearDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
                    ) {
                        Text("सारा डेटा साफ़ करें")
                    }
                }
            }
        }

        item {
            SafetyCalloutCard(
                title = "सूचना (Disclaimer)",
                message = "यह ऐप केवल सीखने और ट्रेनिंग प्लान करने में मदद के लिए है—यह कोई मेडिकल सलाह या इलाज नहीं है। तेज या बढ़ता हुआ दर्द होने पर अभ्यास तुरंत रोकें और डॉक्टर से सलाह लें।",
                isCritical = false
            )
        }
    }

    if (showClearDialog) {
        CopyableAlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("क्या आप सारा डेटा हटाना चाहते हैं?") },
            text = { Text("इससे फोन में सेव सभी वर्कआउट, पसंदीदा, साप्ताहिक प्लान, तैयारी रिकॉर्ड और प्रोफ़ाइल हट जाएंगे। चाहें तो पहले JSON बैकअप सेव कर लें।") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("सब हटाएं") }
            },
            dismissButton = { TextButton(onClick = { showClearDialog = false }) { Text("रद्द करें") } }
        )
    }
}
