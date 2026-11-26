package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MovementCatalog
import com.example.domain.model.DisciplineId
import com.example.ui.components.disciplineColor
import com.example.ui.components.disciplineIcon

@Composable
fun TrainingSystemsScreen(
    onOpenDiscipline: (DisciplineId) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("12 ट्रेनिंग सिस्टम", style = MaterialTheme.typography.headlineMedium)
                Text(
                    "हर सिस्टम में एडवांस स्किल (A) और रोज़ाना के काम आने वाले अभ्यास (B) अलग रखे गए हैं ताकि मसल्स और रिकवरी सुरक्षित रहें।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(MovementCatalog.disciplines, key = { it.id.name }) { disc ->
            val (sig, trans) = MovementCatalog.forDiscipline(disc.id)
            val accent = disciplineColor(disc.id)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("discipline_card_${disc.id.name}")
                    .clickable { onOpenDiscipline(disc.id) },
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                color = accent.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = disciplineIcon(disc.id),
                                    contentDescription = disc.name,
                                    tint = accent,
                                    modifier = Modifier.padding(10.dp).size(24.dp)
                                )
                            }
                            Column {
                                Text(disc.name, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    disc.tagline,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "${disc.name} खोलें",
                            tint = accent
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "A: ${sig.size} खास स्किल",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "B: ${trans.size} उपयोगी अभ्यास",
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
