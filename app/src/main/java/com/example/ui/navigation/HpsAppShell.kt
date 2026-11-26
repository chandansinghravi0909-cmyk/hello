package com.example.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.domain.model.DisciplineId
import com.example.ui.HpsViewModel
import com.example.ui.screens.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable object HomeRoute
@Serializable object SystemsRoute
@Serializable data class DisciplineDetailRoute(val disciplineName: String)
@Serializable object LibraryRoute
@Serializable data class MovementDetailRoute(val movementId: String)
@Serializable object PlannerRoute
@Serializable object ProgressRoute
@Serializable data class ScienceRoute(val initialHub: String? = null)
@Serializable object ReadinessRoute
@Serializable object SettingsRoute

private data class PrimaryNavItem(
    val label: String,
    val shortLabel: String,
    val route: Any,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String,
    val matchClass: kotlin.reflect.KClass<*>
)

private val allPrimaryItems = listOf(
    PrimaryNavItem("होम", "होम", HomeRoute, Icons.Filled.Home, Icons.Outlined.Home, "nav_home", HomeRoute::class),
    PrimaryNavItem("ट्रेनिंग सिस्टम", "सिस्टम", SystemsRoute, Icons.Filled.GridView, Icons.Outlined.GridView, "nav_systems", SystemsRoute::class),
    PrimaryNavItem("अभ्यास लाइब्रेरी", "लाइब्रेरी", LibraryRoute, Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook, "nav_library", LibraryRoute::class),
    PrimaryNavItem("ट्रेनिंग प्लानर", "प्लानर", PlannerRoute, Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "nav_planner", PlannerRoute::class),
    PrimaryNavItem("मेरी प्रगति", "प्रगति", ProgressRoute, Icons.Filled.Insights, Icons.Outlined.Insights, "nav_progress", ProgressRoute::class),
    PrimaryNavItem("सीखें और विज्ञान", "साइंस", ScienceRoute(), Icons.Filled.Science, Icons.Outlined.Science, "nav_science", ScienceRoute::class),
    PrimaryNavItem("रिकवरी और तैयारी", "रिकवरी", ReadinessRoute, Icons.Filled.Bedtime, Icons.Outlined.Bedtime, "nav_readiness", ReadinessRoute::class),
    PrimaryNavItem("सेटिंग्स", "सेटिंग्स", SettingsRoute, Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings", SettingsRoute::class)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HpsAppShell(viewModel: HpsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.feedbackMessage) {
        state.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    fun navigateTopLevel(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val isDetailRoute = currentDest?.hasRoute(MovementDetailRoute::class) == true ||
        currentDest?.hasRoute(DisciplineDetailRoute::class) == true

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 700.dp

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !isWideScreen,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(290.dp)) {
                    SelectionContainer {
                        Column {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "HUMAN PERFORMANCE",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                            Text(
                                text = "सभी 8 मुख्य भाग",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            allPrimaryItems.forEach { item ->
                                val selected = currentDest?.hasRoute(item.matchClass) == true
                                NavigationDrawerItem(
                                    icon = { Icon(if (selected) item.selectedIcon else item.unselectedIcon, contentDescription = item.label) },
                                    label = { Text(item.label) },
                                    selected = selected,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        navigateTopLevel(item.route)
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    }
                }
            }
        ) {
            SelectionContainer {
                Row(modifier = Modifier.fillMaxSize()) {
                    if (isWideScreen) {
                        NavigationRail(
                            windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start)
                        ) {
                            Spacer(Modifier.height(8.dp))
                            allPrimaryItems.forEach { item ->
                                val selected = currentDest?.hasRoute(item.matchClass) == true
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(item.route) },
                                    icon = { Icon(if (selected) item.selectedIcon else item.unselectedIcon, contentDescription = item.label) },
                                    label = { Text(item.shortLabel, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                    modifier = Modifier.testTag(item.testTag)
                                )
                            }
                        }
                    }

                    Scaffold(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                        topBar = {
                            if (!isDetailRoute) {
                                TopAppBar(
                                    title = {
                                        Text(
                                            text = when {
                                                currentDest?.hasRoute(SystemsRoute::class) == true -> "ट्रेनिंग सिस्टम"
                                                currentDest?.hasRoute(LibraryRoute::class) == true -> "अभ्यास लाइब्रेरी"
                                                currentDest?.hasRoute(PlannerRoute::class) == true -> "ट्रेनिंग प्लानर"
                                                currentDest?.hasRoute(ProgressRoute::class) == true -> "मेरी प्रगति"
                                                currentDest?.hasRoute(ScienceRoute::class) == true -> "सीखें और विज्ञान"
                                                currentDest?.hasRoute(ReadinessRoute::class) == true -> "रिकवरी और तैयारी"
                                                currentDest?.hasRoute(SettingsRoute::class) == true -> "सेटिंग्स"
                                                else -> "Human Performance"
                                            },
                                            style = MaterialTheme.typography.titleLarge,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    navigationIcon = {
                                        if (!isWideScreen) {
                                            IconButton(
                                                onClick = { scope.launch { drawerState.open() } },
                                                modifier = Modifier.minimumInteractiveComponentSize().testTag("open_nav_drawer")
                                            ) {
                                                Icon(Icons.Default.Menu, contentDescription = "मेनू खोलें")
                                            }
                                        }
                                    },
                                    actions = {
                                        if (!isWideScreen) {
                                            IconButton(
                                                onClick = { navigateTopLevel(ReadinessRoute) },
                                                modifier = Modifier.minimumInteractiveComponentSize()
                                            ) {
                                                Icon(Icons.Outlined.Bedtime, contentDescription = "रिकवरी और तैयारी")
                                            }
                                            IconButton(
                                                onClick = { navigateTopLevel(SettingsRoute) },
                                                modifier = Modifier.minimumInteractiveComponentSize()
                                            ) {
                                                Icon(Icons.Outlined.Settings, contentDescription = "सेटिंग्स")
                                            }
                                        }
                                    }
                                )
                            }
                        },
                    bottomBar = {
                        if (!isWideScreen && !isDetailRoute) {
                            NavigationBar(windowInsets = WindowInsets.navigationBars) {
                                allPrimaryItems.take(5).forEach { item ->
                                    val selected = currentDest?.hasRoute(item.matchClass) == true
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(item.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.label
                                            )
                                        },
                                        label = { Text(item.shortLabel, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                        modifier = Modifier.testTag(item.testTag)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .let { mod -> if (isWideScreen) mod.navigationBarsPadding() else mod },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(modifier = Modifier.fillMaxSize().widthIn(max = 920.dp)) {
                            NavHost(navController = navController, startDestination = HomeRoute) {
                                composable<HomeRoute> {
                                    HomeScreen(
                                        state = state,
                                        onOpenDiscipline = { d -> navController.navigate(DisciplineDetailRoute(d.name)) },
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) },
                                        onNavigateToLibrary = { navigateTopLevel(LibraryRoute) },
                                        onNavigateToPlanner = { navigateTopLevel(PlannerRoute) },
                                        onNavigateToProgress = { navigateTopLevel(ProgressRoute) },
                                        onNavigateToScience = { navigateTopLevel(ScienceRoute()) },
                                        onNavigateToReadiness = { navigateTopLevel(ReadinessRoute) },
                                        onLoadStarterPlan = { viewModel.loadStarterWeeklyTemplate() },
                                        onDismissOnboarding = { viewModel.dismissOnboarding() }
                                    )
                                }
                                composable<SystemsRoute> {
                                    TrainingSystemsScreen(
                                        onOpenDiscipline = { d -> navController.navigate(DisciplineDetailRoute(d.name)) }
                                    )
                                }
                                composable<DisciplineDetailRoute> { entry ->
                                    val args = entry.toRoute<DisciplineDetailRoute>()
                                    val discId = DisciplineId.entries.firstOrNull { it.name == args.disciplineName } ?: DisciplineId.STRENGTH
                                    DisciplineDetailScreen(
                                        disciplineId = discId,
                                        state = state,
                                        onBack = { navController.popBackStack() },
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) },
                                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                                        onOpenIntelligenceHub = { hub -> navController.navigate(ScienceRoute(hub)) }
                                    )
                                }
                                composable<LibraryRoute> {
                                    MovementLibraryScreen(
                                        state = state,
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) },
                                        onToggleFavorite = { viewModel.toggleFavorite(it) }
                                    )
                                }
                                composable<MovementDetailRoute> { entry ->
                                    val args = entry.toRoute<MovementDetailRoute>()
                                    val movement = com.example.data.catalog.MovementCatalog.getById(args.movementId)
                                    val rec = remember(movement, state.movementProgress, state.workoutLogs, state.readinessLogs) {
                                        movement?.let { viewModel.evaluateMovementProgression(it) }
                                    }
                                    MovementDetailScreen(
                                        movementId = args.movementId,
                                        state = state,
                                        recommendation = rec,
                                        onBack = { navController.popBackStack() },
                                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                                        onSaveProgress = { viewModel.saveMovementProgress(it) },
                                        onLogWorkout = { viewModel.logWorkout(it) },
                                        onDeleteWorkoutLog = { viewModel.deleteWorkoutLog(it) },
                                        onAddPhoto = { mId, uri -> viewModel.addMovementPhoto(mId, uri) },
                                        onRemovePhoto = { mId, uri -> viewModel.removeMovementPhoto(mId, uri) },
                                        onAddToPlannerDay = { dayNum, m -> viewModel.addMovementToPlannedDay(dayNum, m) }
                                    )
                                }
                                composable<PlannerRoute> {
                                    TrainingPlannerScreen(
                                        state = state,
                                        onLoadStarterTemplate = { viewModel.loadStarterWeeklyTemplate() },
                                        onClearPlan = { viewModel.clearWeeklyPlan() },
                                        onSaveDay = { viewModel.savePlannedDay(it) },
                                        onAddMovementToDay = { dayNum, m -> viewModel.addMovementToPlannedDay(dayNum, m) },
                                        onLogWorkout = { viewModel.logWorkout(it) },
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) }
                                    )
                                }
                                composable<ProgressRoute> {
                                    val weeklyMuscleSets = remember(state.workoutLogs) {
                                        viewModel.getWeeklyMuscleSets(state.workoutLogs)
                                    }
                                    ProgressScreen(
                                        state = state,
                                        weeklyMuscleSets = weeklyMuscleSets,
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) },
                                        onDeleteWorkoutLog = { viewModel.deleteWorkoutLog(it) },
                                        onLogBodyMeasurement = { viewModel.logBodyMeasurement(it) },
                                        onNavigateToLibrary = { navigateTopLevel(LibraryRoute) },
                                        onNavigateToReadiness = { navigateTopLevel(ReadinessRoute) }
                                    )
                                }
                                composable<ScienceRoute> { entry ->
                                    val args = entry.toRoute<ScienceRoute>()
                                    LearnScienceScreen(
                                        state = state,
                                        initialHub = args.initialHub,
                                        onLogWorkout = { viewModel.logWorkout(it) },
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) }
                                    )
                                }
                                composable<ReadinessRoute> {
                                    RecoveryReadinessScreen(
                                        state = state,
                                        onLogReadiness = { viewModel.logReadiness(it) },
                                        onDeleteReadiness = { viewModel.deleteReadinessLog(it) },
                                        onOpenMovement = { mId -> navController.navigate(MovementDetailRoute(mId)) }
                                    )
                                }
                                composable<SettingsRoute> {
                                    SettingsScreen(
                                        state = state,
                                        onSetThemeMode = { viewModel.setThemeMode(it) },
                                        onSetUnitSystem = { viewModel.setUnitSystem(it) },
                                        onSetTrainingPriority = { viewModel.setTrainingPriority(it) },
                                        onSaveProfile = { viewModel.saveUserProfile(it) },
                                        onExportBackupJson = { viewModel.exportBackupJson() },
                                        onImportBackupJson = { viewModel.importBackupJson(it) },
                                        onClearAllData = { viewModel.clearAllUserData() },
                                        onShowMessage = { viewModel.showFeedback(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }
}
