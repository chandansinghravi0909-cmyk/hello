package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MovementCatalog
import com.example.domain.model.DisciplineId
import com.example.ui.HpsUiState
import com.example.ui.components.*
import java.util.Calendar

@Composable
fun HomeScreen(
    state: HpsUiState,
    onOpenDiscipline: (DisciplineId) -> Unit,
    onOpenMovement: (String) -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToScience: () -> Unit,
    onNavigateToReadiness: () -> Unit,
    onLoadStarterPlan: () -> Unit,
    onDismissOnboarding: () -> Unit
) {
    val todayDayOfWeek = rememberTodayDayOfWeek()
    val todayPlan = state.plannedDays.firstOrNull { it.dayOfWeek == todayDayOfWeek }
    val latestReadiness = state.readinessLogs.firstOrNull()
    val recentMovements = state.workoutLogs
        .distinctBy { it.movementId }
        .take(5)
        .mapNotNull { MovementCatalog.getById(it.movementId) }
    val continueMovement = recentMovements.firstOrNull()
        ?: MovementCatalog.getById(
            state.movementProgress.entries.maxByOrNull { it.value.lastPracticedAt ?: 0L }?.key
                ?: todayPlan?.items?.firstOrNull()?.movementId
                ?: "str_back_squat"
        )

    val sevenDaysAgo = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
    val weeklyLogs = state.workoutLogs.filter { it.loggedAt >= sevenDaysAgo }
    val weeklySets = weeklyLogs.sumOf { it.sets }
    val weeklyCardioMin = weeklyLogs.sumOf { it.durationMinutes }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Status Bar: Priority & Readiness Pill
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (state.userProfile.name.isNotBlank()) "नमस्ते, ${state.userProfile.name}" else "आज का अभ्यास",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "फोकस: ${state.trainingPriority.label}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Surface(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clickable(onClick = onNavigateToReadiness)
                        .testTag("home_readiness_pill"),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Bedtime,
                            contentDescription = "आज की तैयारी",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = latestReadiness?.let { "तैयारी ${it.readinessScore}/10" } ?: "तैयारी जांचें",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        if (!state.userProfile.onboardingDismissed && state.workoutLogs.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("शुरुआत करें", style = MaterialTheme.typography.titleMedium)
                            TextButton(onClick = onDismissOnboarding) { Text("छोड़ें") }
                        }
                        Text(
                            "हर अभ्यास के 7 विज़ुअल फ़्रेम, सही तकनीक, सेट्स-रैप्स और आराम टाइमर के साथ स्वतंत्र रूप से अभ्यास करें।",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = onLoadStarterPlan, modifier = Modifier.minimumInteractiveComponentSize()) {
                                Text("7-दिन का प्लान लोड करें")
                            }
                            OutlinedButton(onClick = onNavigateToLibrary, modifier = Modifier.minimumInteractiveComponentSize()) {
                                Text("अभ्यास खोजें")
                            }
                        }
                    }
                }
            }
        }

        // 1. आज की ट्रेनिंग + जारी रखें
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = todayPlan?.let { "${it.dayName} • ${it.title}" } ?: "आज की ट्रेनिंग",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        if (todayPlan != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (todayPlan.isRestDay) "आराम" else "${todayPlan.items.size} अभ्यास",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (todayPlan != null) {
                        if (todayPlan.isRestDay) {
                            Text(
                                text = "आराम व रिकवरी का दिन: पूरी नींद, हल्की स्ट्रेचिंग और नाक से साँस लें।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else if (todayPlan.items.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                todayPlan.items.take(3).forEach { item ->
                                    val movement = MovementCatalog.getById(item.movementId)
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onOpenMovement(item.movementId) },
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            if (movement != null) {
                                                ExercisePoseThumbnail(
                                                    movement = movement,
                                                    modifier = Modifier.size(44.dp)
                                                )
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    item.movementName,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    "${item.sets} सेट • ${item.repsOrDuration}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowForward,
                                                contentDescription = "${item.movementName} खोलें",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "शुरू करने के लिए 7-दिन का संतुलित प्लान लोड करें या कोई भी अभ्यास चुनकर विज़ुअल गाइड देखें।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (continueMovement != null) onOpenMovement(continueMovement.id)
                                else onNavigateToLibrary()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("continue_learning_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = continueMovement?.let { "जारी रखें: ${it.name.substringBefore("(").trim()}" } ?: "सीखना जारी रखें",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        OutlinedButton(
                            onClick = onNavigateToPlanner,
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Text("प्लानर")
                        }
                    }
                }
            }
        }

        // 2. 7-दिन के रियल आंकड़े
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeStatCard(
                    title = "7-दिन के सेट",
                    value = if (weeklyLogs.isEmpty()) "0" else "$weeklySets",
                    subtitle = if (weeklyLogs.isEmpty()) "कोई रिकॉर्ड नहीं" else "${weeklyLogs.size} एंट्री सेव",
                    onClick = onNavigateToProgress,
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    title = "कार्डियो (मिनट)",
                    value = "$weeklyCardioMin",
                    subtitle = "पिछले 7 दिन",
                    onClick = onNavigateToProgress,
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    title = "तैयारी स्कोर",
                    value = latestReadiness?.let { "${it.readinessScore}/10" } ?: "--",
                    subtitle = latestReadiness?.let { "${it.sleepHours}h नींद" } ?: "चेक-इन करें",
                    onClick = onNavigateToReadiness,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. साप्ताहिक प्लान पट्टी
        item {
            SectionHeaderRow(title = "साप्ताहिक प्लान", actionLabel = "प्लान बदलें", onAction = onNavigateToPlanner)
            Spacer(Modifier.height(8.dp))
            if (state.plannedDays.isEmpty()) {
                EmptyStatePanel(
                    title = "कोई साप्ताहिक प्लान सेव नहीं है",
                    description = "7 दिन का शुरुआती प्लान लोड करें या प्लानर में हर दिन का रूटीन बनाएं।",
                    actionLabel = "7-दिन का प्लान लोड करें",
                    onAction = onLoadStarterPlan,
                    icon = Icons.Outlined.CalendarMonth
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.plannedDays.sortedBy { it.dayOfWeek }) { day ->
                        val isToday = day.dayOfWeek == todayDayOfWeek
                        Surface(
                            modifier = Modifier
                                .width(136.dp)
                                .clickable(onClick = onNavigateToPlanner),
                            shape = MaterialTheme.shapes.medium,
                            color = if (isToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                            else MaterialTheme.colorScheme.surfaceContainer,
                            border = BorderStroke(
                                1.dp,
                                if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = if (isToday) "${day.dayName} • आज" else day.dayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(day.title, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(
                                    text = if (day.isRestDay) "आराम / रिकवरी" else "${day.items.size} अभ्यास",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. हाल के अभ्यास और लेवल
        item {
            SectionHeaderRow(title = "हाल के अभ्यास", actionLabel = "मेरी प्रगति", onAction = onNavigateToProgress)
            Spacer(Modifier.height(8.dp))
            if (recentMovements.isEmpty() && state.movementProgress.isEmpty()) {
                EmptyStatePanel(
                    title = "अभी कोई सेशन सेव नहीं है",
                    description = "लाइब्रेरी से किसी भी अभ्यास को खोलें, विज़ुअल गाइड देखें और अपना पहला सेट सेव करें।",
                    actionLabel = "अभ्यास लाइब्रेरी खोलें",
                    onAction = onNavigateToLibrary,
                    icon = Icons.Outlined.Insights
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val activeIds = (recentMovements.map { it.id } + state.movementProgress.keys).distinct().take(4)
                    activeIds.forEach { mId ->
                        val m = MovementCatalog.getById(mId)
                        if (m != null) {
                            val prog = state.movementProgress[mId]
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenMovement(m.id) },
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    ExercisePoseThumbnail(movement = m, modifier = Modifier.size(48.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(m.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(
                                            text = buildString {
                                                append("लेवल ${prog?.currentLevel ?: 0}")
                                                if ((prog?.workingLoadKg ?: 0.0) > 0) append(" • ${formatWeight(prog!!.workingLoadKg, state.unitSystem)}")
                                                if ((prog?.bestHoldSeconds ?: 0) > 0) append(" • बेस्ट ${prog?.bestHoldSeconds}s")
                                                if ((prog?.bestReps ?: 0) > 0) append(" • ${prog?.bestReps} रैप्स")
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "${m.name} खोलें", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. सभी 12 ट्रेनिंग सिस्टम
        item {
            SectionHeaderRow(title = "ट्रेनिंग श्रेणियाँ (12)", actionLabel = "विज्ञान गाइड", onAction = onNavigateToScience)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MovementCatalog.disciplines.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pair.forEach { disc ->
                            val accent = disciplineColor(disc.id)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .minimumInteractiveComponentSize()
                                    .clickable { onOpenDiscipline(disc.id) }
                                    .testTag("home_system_${disc.id.name}"),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = accent.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = disciplineIcon(disc.id),
                                            contentDescription = disc.name,
                                            tint = accent,
                                            modifier = Modifier.padding(6.dp).size(18.dp)
                                        )
                                    }
                                    Text(
                                        text = disc.id.shortTitle,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeStatCard(
    title: String,
    value: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun SectionHeaderRow(
    title: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = onAction, modifier = Modifier.minimumInteractiveComponentSize()) {
            Text(actionLabel)
        }
    }
}

private fun rememberTodayDayOfWeek(): Int {
    return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        Calendar.SATURDAY -> 6
        else -> 7
    }
}
