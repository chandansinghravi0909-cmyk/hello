package com.example

import com.example.data.catalog.MovementCatalog
import com.example.data.local.DataBackupPayload
import com.example.domain.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class HpsDomainTests {

    @Test
    fun allTwelveDisciplinesHaveSignatureAndTransferableMovements() {
        assertEquals(12, MovementCatalog.disciplines.size)
        DisciplineId.entries.forEach { disciplineId ->
            val (sig, trans) = MovementCatalog.forDiscipline(disciplineId)
            assertTrue("Missing signature movements for $disciplineId", sig.isNotEmpty())
            assertTrue("Missing transferable movements for $disciplineId", trans.isNotEmpty())
        }
    }

    @Test
    fun handstandHasTenLevelProgressionRoadmap() {
        val handstand = MovementCatalog.getById("yog_handstand")
        assertNotNull(handstand)
        assertEquals(10, handstand!!.progressionRoadmap.size)
        assertEquals(8, handstand.steps.size)
    }

    @Test
    fun multiFilterSearchMatchesCriteriaSimultaneously() {
        val beginnerNoEquip = MovementCatalog.searchAndFilter(
            difficulty = DifficultyLevel.BEGINNER,
            noEquipmentOnly = true
        )
        assertTrue(beginnerNoEquip.isNotEmpty())
        assertTrue(beginnerNoEquip.all { it.difficulty == DifficultyLevel.BEGINNER })

        val hipMobility = MovementCatalog.searchAndFilter(
            discipline = DisciplineId.MOBILITY,
            bodyRegion = BodyRegion.HIPS
        )
        assertTrue(hipMobility.any { it.id == "mob_90_90_hip" })

        val partialHand = MovementCatalog.searchAndFilter(query = "hand")
        assertTrue(partialHand.any { it.id == "yog_handstand" })

        val hindiHandstand = MovementCatalog.searchAndFilter(query = "हैंडस्टैंड")
        assertTrue(hindiHandstand.any { it.id == "yog_handstand" })
    }

    @Test
    fun plannerConflictDetectorFlagsSprintAndHeavyLowerCollision() {
        val starter = PlannerConflictDetector.defaultTemplateDays()
        val modified = starter.map { day ->
            if (day.dayOfWeek == 2) {
                day.copy(
                    items = day.items + PlannedItem(
                        uid = "test_spr",
                        movementId = "spr_max_velocity",
                        movementName = "मैक्सिमम वेलोसिटी स्प्रिंट",
                        disciplineId = DisciplineId.SPRINT_POWER.name
                    )
                )
            } else day
        }
        val warnings = PlannerConflictDetector.analyzeWeek(modified, TrainingPriority.HYPERTROPHY)
        assertTrue(warnings.any { it.severity == ConflictSeverity.HIGH && it.title.contains("स्प्रिंट") })
    }

    @Test
    fun progressionEngineDoesNotAutoAdvanceWithoutEvidenceAndRespectsPainGate() {
        val squat = MovementCatalog.getById("str_back_squat")!!
        val baseline = ProgressionEngine.evaluate(squat, null, emptyList(), latestReadinessScore = 8)
        assertEquals("पहला रिकॉर्ड ज़रूरी है", baseline.statusBadge)

        val painLog = LoggedSessionSummary(
            sets = 3, reps = 8, loadKg = 80.0, rir = 2,
            holdSeconds = 0, durationMinutes = 0, distanceKm = 0.0, rpe = 7,
            sprintSeconds = 0.0, jumpCm = 0.0, romScore = 3, painFlag = true, loggedAt = 1L
        )
        val gated = ProgressionEngine.evaluate(squat, null, listOf(painLog), latestReadinessScore = 8)
        assertEquals("सुरक्षा रोक / हल्का करें", gated.statusBadge)
        assertNotNull(gated.safetyGateMessage)

        val sprint = MovementCatalog.getById("spr_max_velocity")!!
        val sprintLog = LoggedSessionSummary(
            sets = 3, reps = 1, loadKg = 0.0, rir = 2,
            holdSeconds = 0, durationMinutes = 0, distanceKm = 0.0, rpe = 8,
            sprintSeconds = 3.2, jumpCm = 0.0, romScore = 3, painFlag = false, loggedAt = 1L
        )
        val lowReadinessSprint = ProgressionEngine.evaluate(sprint, null, listOf(sprintLog), latestReadinessScore = 3)
        assertEquals("तेज़ प्रयास रोकें", lowReadinessSprint.statusBadge)
    }

    @Test
    fun backupPayloadRoundTripPreservesSchemaVersion() {
        val payload = DataBackupPayload(schemaVersion = 1, exportedAt = 123456L)
        val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }
        val encoded = json.encodeToString(payload)
        val decoded = json.decodeFromString<DataBackupPayload>(encoded)
        assertEquals(1, decoded.schemaVersion)
        assertEquals(123456L, decoded.exportedAt)
    }

    @Test
    fun everyMovementHasSevenDistinctVisualFramesAndTrainingSummary() {
        val movements = MovementCatalog.allMovements
        assertTrue(movements.size >= 150)
        movements.forEach { m ->
            val gallery = com.example.data.catalog.ExerciseGalleryCatalog.galleryFor(m)
            assertEquals("Expected 7 frames for ${m.id}", 7, gallery.frames.size)
            assertEquals(
                "Expected 7 distinct frame categories for ${m.id}",
                7,
                gallery.frames.map { it.category }.distinct().size
            )
            gallery.frames.forEach { f ->
                assertTrue("Empty instructionCue in ${m.id}", f.instructionCue.isNotBlank())
                assertTrue("progressT out of range in ${m.id}", f.progressT in 0f..1f)
            }
            val summary = com.example.data.catalog.ExerciseGalleryCatalog.trainingSummaryFor(m)
            assertTrue(summary.setsLabel.isNotBlank())
            assertTrue(summary.repsOrTimeLabel.isNotBlank())
            assertTrue(summary.restLabel.isNotBlank())
        }
    }
}
