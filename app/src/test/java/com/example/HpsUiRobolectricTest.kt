package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.data.catalog.MovementCatalog
import com.example.domain.model.ProgressionEngine
import com.example.ui.HpsUiState
import com.example.ui.screens.MovementDetailScreen
import com.example.ui.screens.MovementLibraryScreen
import com.example.ui.theme.HumanPerformanceTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xxhdpi")
class HpsUiRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun movementDetailShowsSevenFrameGalleryRestTimerAndFullscreenModal() {
        val squat = MovementCatalog.getById("str_back_squat")!!
        val rec = ProgressionEngine.evaluate(squat, null, emptyList(), 8)

        composeTestRule.setContent {
            HumanPerformanceTheme {
                MovementDetailScreen(
                    movementId = squat.id,
                    state = HpsUiState(),
                    recommendation = rec,
                    onBack = {},
                    onToggleFavorite = {},
                    onSaveProgress = {},
                    onLogWorkout = {},
                    onDeleteWorkoutLog = {},
                    onAddPhoto = { _, _ -> },
                    onRemovePhoto = { _, _ -> },
                    onAddToPlannerDay = { _, _ -> }
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("exercise_visual_gallery").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("gallery_thumbnail_strip").performScrollTo().assertIsDisplayed()

        // Tap 4th thumbnail (End / Peak Position)
        composeTestRule.onNodeWithTag("gallery_thumb_3").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("4. अंतिम / पीक स्थिति").assertIsDisplayed()

        // Open fullscreen lightbox modal and close it
        composeTestRule.onNodeWithTag("gallery_expand_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("fullscreen_gallery_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("close_fullscreen_gallery").performClick()
        composeTestRule.waitForIdle()

        // Toggle Rest Timer
        composeTestRule.onNodeWithTag("toggle_rest_timer_btn").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("rest_timer_card").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun librarySearchFiltersMovementsCleanly() {
        composeTestRule.setContent {
            HumanPerformanceTheme {
                MovementLibraryScreen(
                    state = HpsUiState(),
                    onOpenMovement = {},
                    onToggleFavorite = {}
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("library_search_input").performTextInput("Handstand")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("movement_card_yog_handstand").assertIsDisplayed()
    }
}
