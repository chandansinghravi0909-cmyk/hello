package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MovementCatalog
import com.example.domain.model.DisciplineId
import com.example.domain.model.MovementCategory
import com.example.ui.HpsUiState
import com.example.ui.components.*

@Composable
fun DisciplineDetailScreen(
    disciplineId: DisciplineId,
    state: HpsUiState,
    onBack: () -> Unit,
    onOpenMovement: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenIntelligenceHub: (String) -> Unit
) {
    val info = MovementCatalog.getDisciplineInfo(disciplineId)
    val (signatureList, transferableList) = MovementCatalog.forDiscipline(disciplineId)
    var selectedCategory by remember { mutableStateOf(MovementCategory.SIGNATURE_ADVANCED) }
    val accent = disciplineColor(disciplineId)

    val matchingHubTag = when (disciplineId) {
        DisciplineId.STRENGTH -> "hypertrophy"
        DisciplineId.CARDIO -> "cardio"
        DisciplineId.SPRINT_POWER -> "sprint"
        DisciplineId.MOBILITY -> "mobility"
        DisciplineId.PREHAB -> "prehab"
        else -> null
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "वापस जाएं")
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(info.name, style = MaterialTheme.typography.headlineMedium)
                    Text(info.tagline, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        if (info.safetyWarning != null) {
            item {
                SafetyCalloutCard(
                    title = "सुरक्षा सावधानी",
                    message = info.safetyWarning,
                    isCritical = true
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
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(info.overview, style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Text("खास फायदे", style = MaterialTheme.typography.labelLarge, color = accent)
                    Text(info.skillBenefits, style = MaterialTheme.typography.bodySmall)
                    Text("सीमाएं", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.tertiary)
                    Text(info.limitations, style = MaterialTheme.typography.bodySmall)
                    Text("मसल्स और रिकवरी पर असर", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Text(info.hypertrophyIntegrationNote, style = MaterialTheme.typography.bodySmall)

                    if (matchingHubTag != null) {
                        OutlinedButton(
                            onClick = { onOpenIntelligenceHub(matchingHubTag) },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Outlined.Science, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("${disciplineId.shortTitle} का विज्ञान नियम पढ़ें")
                        }
                    }
                }
            }
        }

        // Strict Category A vs Category B Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == MovementCategory.SIGNATURE_ADVANCED,
                    onClick = { selectedCategory = MovementCategory.SIGNATURE_ADVANCED },
                    label = { Text("A: ${info.signatureTitle} (${signatureList.size})") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cat_a_chip")
                )
                FilterChip(
                    selected = selectedCategory == MovementCategory.TRANSFERABLE_FUNCTIONAL,
                    onClick = { selectedCategory = MovementCategory.TRANSFERABLE_FUNCTIONAL },
                    label = { Text("B: ${info.transferableTitle} (${transferableList.size})") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cat_b_chip")
                )
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                    Text(
                        text = if (selectedCategory == MovementCategory.SIGNATURE_ADVANCED)
                            info.signatureDescription
                        else
                            info.transferableDescription,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        val activeList = if (selectedCategory == MovementCategory.SIGNATURE_ADVANCED) signatureList else transferableList
        items(activeList, key = { it.id }) { movement ->
            MovementCard(
                movement = movement,
                isFavorite = movement.id in state.favorites,
                currentLevel = state.movementProgress[movement.id]?.currentLevel,
                onClick = { onOpenMovement(movement.id) },
                onToggleFavorite = { onToggleFavorite(movement.id) }
            )
        }
    }
}
