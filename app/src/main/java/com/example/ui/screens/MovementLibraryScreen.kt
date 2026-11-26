package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MovementCatalog
import com.example.domain.model.*
import com.example.ui.HpsUiState
import com.example.ui.components.EmptyStatePanel
import com.example.ui.components.MovementCard
import com.example.ui.components.disciplineIcon

@Composable
fun MovementLibraryScreen(
    state: HpsUiState,
    onOpenMovement: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDiscipline by remember { mutableStateOf<DisciplineId?>(null) }
    var selectedDifficulty by remember { mutableStateOf<DifficultyLevel?>(null) }
    var selectedRegion by remember { mutableStateOf<BodyRegion?>(null) }
    var selectedCategory by remember { mutableStateOf<MovementCategory?>(null) }
    var noEquipmentOnly by remember { mutableStateOf(false) }
    var favoritesOnly by remember { mutableStateOf(false) }
    var showMoreFilters by remember { mutableStateOf(false) }

    val results = remember(
        searchQuery, selectedDiscipline, selectedDifficulty, selectedRegion,
        selectedCategory, noEquipmentOnly, favoritesOnly, state.favorites
    ) {
        MovementCatalog.searchAndFilter(
            query = searchQuery,
            discipline = selectedDiscipline,
            category = selectedCategory,
            difficulty = selectedDifficulty,
            bodyRegion = selectedRegion,
            noEquipmentOnly = noEquipmentOnly,
            favoritesOnly = favoritesOnly,
            favoriteIds = state.favorites
        )
    }

    val favoriteMovements = remember(state.favorites) {
        state.favorites.mapNotNull { MovementCatalog.getById(it) }
    }

    val hasActiveFilters = searchQuery.isNotBlank() || selectedDiscipline != null ||
        selectedDifficulty != null || selectedRegion != null || selectedCategory != null ||
        noEquipmentOnly || favoritesOnly

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("अभ्यास खोजें", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("library_search_input"),
                    placeholder = { Text("नाम, मसल, उपकरण या हिंदी/अंग्रेजी से खोजें...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "खोजें") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "साफ़ करें")
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
            }
        }

        // Quick 1-tap Discipline Selector Strip
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    FilterChip(
                        selected = selectedDiscipline == null,
                        onClick = { selectedDiscipline = null },
                        label = { Text("सभी (${MovementCatalog.allMovements.size})") }
                    )
                }
                items(DisciplineId.entries) { d ->
                    FilterChip(
                        selected = selectedDiscipline == d,
                        onClick = { selectedDiscipline = if (selectedDiscipline == d) null else d },
                        leadingIcon = {
                            Icon(disciplineIcon(d), contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = { Text(d.shortTitle) }
                    )
                }
            }
        }

        if (favoriteMovements.isNotEmpty() && !favoritesOnly) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("पसंदीदा अभ्यास (${favoriteMovements.size})", style = MaterialTheme.typography.titleSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(favoriteMovements, key = { "fav_${it.id}" }) { fav ->
                            Surface(
                                modifier = Modifier.clickable { onOpenMovement(fav.id) },
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(14.dp))
                                    Text(fav.name, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = favoritesOnly,
                        onClick = { favoritesOnly = !favoritesOnly },
                        label = { Text("पसंदीदा (${state.favorites.size})") },
                        modifier = Modifier.testTag("filter_favorites")
                    )
                }
                item {
                    FilterChip(
                        selected = noEquipmentOnly,
                        onClick = { noEquipmentOnly = !noEquipmentOnly },
                        label = { Text("बिना उपकरण") },
                        modifier = Modifier.testTag("filter_no_equipment")
                    )
                }
                item {
                    FilterChip(
                        selected = showMoreFilters,
                        onClick = { showMoreFilters = !showMoreFilters },
                        leadingIcon = { Icon(Icons.Outlined.FilterList, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        label = { Text(if (showMoreFilters) "फ़िल्टर छुपाएं" else "मसल / श्रेणी") }
                    )
                }
                items(DifficultyLevel.entries) { diff ->
                    FilterChip(
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = if (selectedDifficulty == diff) null else diff },
                        label = { Text(diff.label) }
                    )
                }
            }
        }

        if (showMoreFilters) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("श्रेणी (Category)", style = MaterialTheme.typography.labelLarge)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MovementCategory.entries.forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                                    label = { Text(cat.badge) }
                                )
                            }
                        }

                        Text("शरीर का हिस्सा", style = MaterialTheme.typography.labelLarge)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(BodyRegion.entries) { r ->
                                FilterChip(
                                    selected = selectedRegion == r,
                                    onClick = { selectedRegion = if (selectedRegion == r) null else r },
                                    label = { Text(r.label) }
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${results.size} अभ्यास • विज़ुअल गाइड के साथ",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (hasActiveFilters) {
                    TextButton(onClick = {
                        searchQuery = ""
                        selectedDiscipline = null
                        selectedDifficulty = null
                        selectedRegion = null
                        selectedCategory = null
                        noEquipmentOnly = false
                        favoritesOnly = false
                    }) {
                        Text("फ़िल्टर हटाएं")
                    }
                }
            }
        }

        if (results.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = "कोई अभ्यास नहीं मिला",
                    description = "एक या अधिक फ़िल्टर हटाकर फिर से खोजें।",
                    actionLabel = "सभी फ़िल्टर हटाएं",
                    onAction = {
                        searchQuery = ""
                        selectedDiscipline = null
                        selectedDifficulty = null
                        selectedRegion = null
                        selectedCategory = null
                        noEquipmentOnly = false
                        favoritesOnly = false
                    }
                )
            }
        } else {
            items(results, key = { it.id }) { movement ->
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
}
