package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.TrainingPriority
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.hpsDataStore: DataStore<Preferences> by preferencesDataStore(name = "hps_preferences")

enum class UnitSystem(val label: String) {
    METRIC("मेट्रिक (kg / km / cm)"),
    IMPERIAL("इम्पीरियल (lb / mi / in)")
}

class PreferencesManager(private val context: Context) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val UNIT_SYSTEM = stringPreferencesKey("unit_system")
        val TRAINING_PRIORITY = stringPreferencesKey("training_priority")
    }

    val themeMode: Flow<ThemeMode> = context.hpsDataStore.data.map { prefs ->
        val raw = prefs[Keys.THEME_MODE]
        ThemeMode.entries.firstOrNull { it.name == raw } ?: ThemeMode.DARK
    }

    val unitSystem: Flow<UnitSystem> = context.hpsDataStore.data.map { prefs ->
        val raw = prefs[Keys.UNIT_SYSTEM]
        UnitSystem.entries.firstOrNull { it.name == raw } ?: UnitSystem.METRIC
    }

    val trainingPriority: Flow<TrainingPriority> = context.hpsDataStore.data.map { prefs ->
        val raw = prefs[Keys.TRAINING_PRIORITY]
        TrainingPriority.entries.firstOrNull { it.name == raw } ?: TrainingPriority.HYPERTROPHY
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.hpsDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setUnitSystem(system: UnitSystem) {
        context.hpsDataStore.edit { it[Keys.UNIT_SYSTEM] = system.name }
    }

    suspend fun setTrainingPriority(priority: TrainingPriority) {
        context.hpsDataStore.edit { it[Keys.TRAINING_PRIORITY] = priority.name }
    }
}
