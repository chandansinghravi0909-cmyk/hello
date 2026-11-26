package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.local.UnitSystem
import com.example.domain.model.*
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.CoralDanger
import com.example.ui.theme.CyanBright
import com.example.ui.theme.EmeraldPrimary
import java.util.Locale

fun weightUnitLabel(unitSystem: UnitSystem): String =
    if (unitSystem == UnitSystem.IMPERIAL) "lb" else "kg"

fun distanceUnitLabel(unitSystem: UnitSystem): String =
    if (unitSystem == UnitSystem.IMPERIAL) "mi" else "km"

fun lengthUnitLabel(unitSystem: UnitSystem): String =
    if (unitSystem == UnitSystem.IMPERIAL) "in" else "cm"

fun formatWeight(kg: Double, unitSystem: UnitSystem): String {
    val value = if (unitSystem == UnitSystem.IMPERIAL) kg * 2.20462 else kg
    val formatted = if (value % 1.0 == 0.0) value.toInt().toString() else String.format(Locale.US, "%.1f", value)
    return "$formatted ${weightUnitLabel(unitSystem)}"
}

fun formatDistance(km: Double, unitSystem: UnitSystem): String {
    val value = if (unitSystem == UnitSystem.IMPERIAL) km * 0.621371 else km
    return "${String.format(Locale.US, "%.1f", value)} ${distanceUnitLabel(unitSystem)}"
}

fun formatLength(cm: Double, unitSystem: UnitSystem): String {
    val value = if (unitSystem == UnitSystem.IMPERIAL) cm / 2.54 else cm
    return "${String.format(Locale.US, "%.1f", value)} ${lengthUnitLabel(unitSystem)}"
}

fun parseWeightToKg(input: Double, unitSystem: UnitSystem): Double =
    if (unitSystem == UnitSystem.IMPERIAL) input / 2.20462 else input

fun parseDistanceToKm(input: Double, unitSystem: UnitSystem): Double =
    if (unitSystem == UnitSystem.IMPERIAL) input / 0.621371 else input

fun parseLengthToCm(input: Double, unitSystem: UnitSystem): Double =
    if (unitSystem == UnitSystem.IMPERIAL) input * 2.54 else input

@Composable
fun CopyableAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        title = title?.let { titleSlot -> { SelectionContainer { titleSlot() } } },
        text = text?.let { textSlot -> { SelectionContainer { textSlot() } } }
    )
}

fun disciplineColor(id: DisciplineId): Color = when (id) {
    DisciplineId.STRENGTH -> EmeraldPrimary
    DisciplineId.CALISTHENICS -> CyanBright
    DisciplineId.YOGA -> Color(0xFFA78BFA)
    DisciplineId.CARDIO -> Color(0xFF38BDF8)
    DisciplineId.SPRINT_POWER -> AmberTertiary
    DisciplineId.MOBILITY -> Color(0xFF2DD4BF)
    DisciplineId.CORE -> Color(0xFFFB7185)
    DisciplineId.BALANCE_AGILITY -> Color(0xFF60A5FA)
    DisciplineId.PREHAB -> Color(0xFF34D399)
    DisciplineId.OLYMPIC_LIFTING -> Color(0xFFFBBF24)
    DisciplineId.MARTIAL_ARTS -> Color(0xFFF87171)
    DisciplineId.BREATHING -> Color(0xFF818CF8)
}

fun disciplineIcon(id: DisciplineId): ImageVector = when (id) {
    DisciplineId.YOGA -> Icons.Outlined.SelfImprovement
    DisciplineId.CALISTHENICS -> Icons.Outlined.AccessibilityNew
    DisciplineId.STRENGTH -> Icons.Outlined.FitnessCenter
    DisciplineId.CARDIO -> Icons.AutoMirrored.Outlined.DirectionsRun
    DisciplineId.SPRINT_POWER -> Icons.Outlined.Bolt
    DisciplineId.MOBILITY -> Icons.Outlined.SyncAlt
    DisciplineId.CORE -> Icons.Outlined.Shield
    DisciplineId.BALANCE_AGILITY -> Icons.Outlined.Balance
    DisciplineId.PREHAB -> Icons.Outlined.Healing
    DisciplineId.OLYMPIC_LIFTING -> Icons.Outlined.SportsGymnastics
    DisciplineId.MARTIAL_ARTS -> Icons.Outlined.SportsMartialArts
    DisciplineId.BREATHING -> Icons.Outlined.Air
}

@Composable
fun MovementCard(
    movement: MovementDetail,
    isFavorite: Boolean,
    currentLevel: Int? = null,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = disciplineColor(movement.discipline)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("movement_card_${movement.id}")
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExercisePoseThumbnail(
                movement = movement,
                accentColor = accent,
                modifier = Modifier.size(width = 68.dp, height = 74.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movement.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${movement.discipline.shortTitle} • ${movement.equipment.firstOrNull() ?: "बॉडीवेट"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = accent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("favorite_btn_${movement.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "पसंदीदा से हटाएं" else "पसंदीदा में जोड़ें",
                            tint = if (isFavorite) CoralDanger else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    text = movement.trainingPurpose,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DifficultyPill(movement.difficulty)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    ) {
                        Text(
                            text = movement.targetMuscles.take(2).joinToString(" · "),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    if (currentLevel != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "लेवल $currentLevel",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DifficultyPill(level: DifficultyLevel) {
    val bg = when (level) {
        DifficultyLevel.BEGINNER -> EmeraldPrimary.copy(alpha = 0.16f)
        DifficultyLevel.INTERMEDIATE -> CyanBright.copy(alpha = 0.16f)
        DifficultyLevel.ADVANCED -> AmberTertiary.copy(alpha = 0.18f)
        DifficultyLevel.EXPERT -> CoralDanger.copy(alpha = 0.18f)
    }
    val fg = when (level) {
        DifficultyLevel.BEGINNER -> EmeraldPrimary
        DifficultyLevel.INTERMEDIATE -> CyanBright
        DifficultyLevel.ADVANCED -> AmberTertiary
        DifficultyLevel.EXPERT -> CoralDanger
    }
    Surface(shape = RoundedCornerShape(6.dp), color = bg) {
        Text(
            text = level.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun EvidenceBadge(level: EvidenceLevel) {
    val color = when (level) {
        EvidenceLevel.STRONG -> EmeraldPrimary
        EvidenceLevel.MODERATE -> CyanBright
        EvidenceLevel.LIMITED -> AmberTertiary
        EvidenceLevel.EMERGING -> Color(0xFFA78BFA)
        EvidenceLevel.EXPERT_PRACTICE -> Color(0xFF94A3B8)
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Text(
            text = level.label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ExpandableSectionCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    initiallyExpanded: Boolean = false,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(20.dp))
                    Column {
                        Text(title, style = MaterialTheme.typography.titleMedium)
                        if (subtitle != null) {
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "$title बंद करें" else "$title खोलें",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
fun SafetyCalloutCard(
    title: String,
    message: String,
    isCritical: Boolean = false
) {
    val bg = if (isCritical) CoralDanger.copy(alpha = 0.14f) else AmberTertiary.copy(alpha = 0.14f)
    val fg = if (isCritical) CoralDanger else AmberTertiary
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = bg,
        border = BorderStroke(1.dp, fg.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if (isCritical) Icons.Filled.Warning else Icons.Outlined.HealthAndSafety,
                contentDescription = title,
                tint = fg,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = fg)
                Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun EmptyStatePanel(
    title: String,
    description: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    icon: ImageVector = Icons.Outlined.Insights
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (actionLabel != null && onAction != null) {
                Button(onClick = onAction, modifier = Modifier.minimumInteractiveComponentSize()) {
                    Text(actionLabel)
                }
            }
        }
    }
}
