package com.example.data.catalog

import com.example.domain.model.*
import java.util.concurrent.ConcurrentHashMap

object ExerciseGalleryCatalog {

    private val cache = ConcurrentHashMap<String, ExerciseVisualGallerySpec>()

    fun galleryFor(movement: MovementDetail): ExerciseVisualGallerySpec {
        return cache.getOrPut(movement.id) { buildGallerySpec(movement) }
    }

    fun trainingSummaryFor(movement: MovementDetail): ExerciseTrainingSummary {
        val vol = movement.prescription.volumeAndSets
        val rest = movement.prescription.restPeriods
        val freq = movement.prescription.frequencyPerWeek

        val setsLabel = extractSets(vol, movement.programmingModel)
        val repsOrTime = extractRepsOrDuration(vol, movement.estimatedMinutes, movement.programmingModel)
        val cleanRest = if (rest.length > 26) rest.substringBefore("(").trim() else rest
        val cleanFreq = freq.substringBefore("(").trim()

        return ExerciseTrainingSummary(
            setsLabel = setsLabel,
            repsOrTimeLabel = repsOrTime,
            restLabel = cleanRest.ifBlank { "60-90 सेकंड" },
            frequencyLabel = cleanFreq
        )
    }

    private fun extractSets(vol: String, model: ProgrammingModel): String {
        val match = Regex("(\\d+(?:-\\d+)?)\\s*(?:सेट|राउंड|प्रयास)").find(vol)
        if (match != null) {
            return match.value.trim()
        }
        return when (model) {
            ProgrammingModel.CARDIO -> "1 मुख्य सेशन"
            ProgrammingModel.BREATHING -> "1 शांत सेशन"
            ProgrammingModel.SPRINT_POWER -> "3–5 प्रयास"
            else -> "3–4 सेट"
        }
    }

    private fun extractRepsOrDuration(vol: String, minutes: Int, model: ProgrammingModel): String {
        val afterX = vol.substringAfter("x ", "").trim()
        if (afterX.isNotBlank()) {
            return afterX.substringBefore("(").trim().take(28)
        }
        val minMatch = Regex("(\\d+(?:-\\d+)?)\\s*(?:मिनट|सेकंड|s\\b|रैप्स)").find(vol)
        if (minMatch != null) return minMatch.value.trim()
        return when (model) {
            ProgrammingModel.CARDIO -> "$minutes मिनट"
            ProgrammingModel.BREATHING -> "$minutes मिनट"
            ProgrammingModel.STATIC_SKILL -> "15–30 सेकंड"
            else -> "8–12 रैप्स"
        }
    }

    private fun buildGallerySpec(movement: MovementDetail): ExerciseVisualGallerySpec {
        val family = resolvePoseFamily(movement)
        val prop = resolveProp(movement, family)
        val highlights = resolveMuscleHighlights(movement)
        val frames = buildSevenFrames(movement, family)
        return ExerciseVisualGallerySpec(
            poseFamily = family,
            prop = prop,
            highlights = highlights,
            frames = frames
        )
    }

    private fun resolvePoseFamily(m: MovementDetail): PoseFamily {
        val id = m.id
        val nameLower = (m.name + " " + m.alternativeNames.joinToString(" ")).lowercase()

        // Specific ID/name-based accurate matching
        return when {
            id.contains("handstand") || id == "yog_headstand" || id == "yog_forearm_stand" || nameLower.contains("handstand") || nameLower.contains("pincha") ->
                PoseFamily.HANDSTAND_INVERSION
            id.contains("bridge") || id.contains("wheel") || id.contains("camel") || id.contains("cobra") || id.contains("upward_dog") || nameLower.contains("wheel") || nameLower.contains("bridge") ->
                PoseFamily.YOGA_BACKBEND_WHEEL
            id.contains("crow") || id.contains("crane") || id.contains("peacock") || id.contains("firefly") || id.contains("scale") ->
                PoseFamily.YOGA_BALANCE_CROW
            id.contains("split") || id.contains("pancake") || id.contains("forward_fold") || id.contains("jefferson") || id.contains("pigeon") || id.contains("hamstring") || id.contains("adductor") ->
                PoseFamily.MOBILITY_SPLIT_STRETCH
            id.contains("90_90") || id.contains("hip_cars") || id.contains("thoracic") || id.contains("shoulder_cars") || id.contains("wall_slides") || id.contains("knee_to_wall") || id.contains("deep_squat_hold") || id.contains("cat_cow") ->
                PoseFamily.MOBILITY_HIP_90_90
            id.contains("downward_dog") || id.contains("sun_salutation") ->
                PoseFamily.YOGA_DOWNDOG_FOLD
            id.contains("warrior") || id.contains("triangle") || id.contains("twist") || id.contains("chair") || id.contains("child") ->
                PoseFamily.YOGA_TWIST_WARRIOR

            id.contains("pullup") || id.contains("pull_up") || id.contains("chinup") || id.contains("muscle_up") || id.contains("dead_hang") || id.contains("german_hang") || id.contains("front_lever") || id.contains("back_lever") || id.contains("iron_cross") || id.contains("skin_the_cat") || m.movementPattern == MovementPattern.VERTICAL_PULL ->
                PoseFamily.PULL_UP_HANG
            id.contains("row") || id.contains("face_pull") || m.movementPattern == MovementPattern.HORIZONTAL_PULL ->
                PoseFamily.ROW_PULL

            id.contains("bench") || id.contains("chest_press") || id.contains("fly") ->
                PoseFamily.HORIZONTAL_PUSH_BENCH
            id.contains("dip") ->
                PoseFamily.DIP_SUPPORT
            id.contains("pushup") || id.contains("push_up") || id.contains("planche") || id.contains("pseudo_planche") ->
                PoseFamily.PUSHUP_FLOOR
            id.contains("ohp") || id.contains("overhead_press") || id.contains("lateral_raise") || m.movementPattern == MovementPattern.VERTICAL_PUSH ->
                PoseFamily.VERTICAL_PUSH_OHP

            id.contains("snatch") || id.contains("clean") || id.contains("jerk") || m.discipline == DisciplineId.OLYMPIC_LIFTING ->
                PoseFamily.OLYMPIC_LIFT

            id.contains("split_squat") || id.contains("lunge") || id.contains("step_up") || id.contains("step_down") || id.contains("cossack") || m.movementPattern == MovementPattern.SINGLE_LEG ->
                PoseFamily.SPLIT_SQUAT_LUNGE
            id.contains("squat") || id.contains("pistol") || id.contains("calf") || id.contains("tibialis") || m.movementPattern == MovementPattern.SQUAT ->
                PoseFamily.SQUAT
            id.contains("deadlift") || id.contains("rdl") || id.contains("hip_thrust") || id.contains("good_morning") || id.contains("nordic") || id.contains("kettlebell_swing") || id.contains("swing") || m.movementPattern == MovementPattern.HIP_HINGE ->
                PoseFamily.HIP_HINGE_DEADLIFT

            id.contains("dragon_flag") || id.contains("hollow") || id.contains("dead_bug") || id.contains("v_up") ->
                PoseFamily.HOLLOW_DRAGON_LEVER
            id.contains("l_sit") || id.contains("v_sit") || id.contains("leg_raise") || id.contains("windshield") ->
                PoseFamily.LEG_RAISE_LSIT
            id.contains("plank") || id.contains("ab_wheel") || id.contains("copenhagen") ->
                PoseFamily.PLANK_ANTI_EXT
            id.contains("pallof") || id.contains("carry") || id.contains("turkish") || m.movementPattern == MovementPattern.CARRY_ANTI_ROTATION ->
                PoseFamily.ROTATION_CARRY

            id.contains("pogo") || id.contains("jump") || id.contains("bound") || id.contains("hop") || m.movementPattern == MovementPattern.PLYOMETRIC_EXPLOSIVE ->
                PoseFamily.JUMP_PLYO
            id.contains("skater") || id.contains("shuffle") || id.contains("cod") || id.contains("crossover") || id.contains("deceleration") || id.contains("footwork") || id.contains("slackline") || id.contains("balance") ->
                PoseFamily.LATERAL_SKATER_AGILITY
            id.contains("kick") || id.contains("boxing") || id.contains("sprawl") || id.contains("hip_escape") || id.contains("slipping") || id.contains("throw") || id.contains("reaction") || id.contains("takedown") || m.discipline == DisciplineId.MARTIAL_ARTS ->
                PoseFamily.MARTIAL_STRIKE_KICK
            id.contains("sprint") || id.contains("accel") || id.contains("skip") || id.contains("ankling") || id.contains("wall_sprint") || m.movementPattern == MovementPattern.LOCOMOTION_SPRINT ->
                PoseFamily.SPRINT_DRIVE

            id.contains("cycling") || id.contains("rowing") || id.contains("swimming") || id.contains("elliptical") ->
                PoseFamily.CARDIO_CYCLE_ROW
            m.discipline == DisciplineId.CARDIO || m.movementPattern == MovementPattern.ENDURANCE_CYCLICAL ->
                PoseFamily.CARDIO_RUN_WALK
            m.discipline == DisciplineId.BREATHING || m.movementPattern == MovementPattern.RESPIRATORY ->
                PoseFamily.BREATHING_POSE

            else -> when (m.visualGuide.archetype) {
                DiagramArchetype.INVERSION_STACK -> PoseFamily.HANDSTAND_INVERSION
                DiagramArchetype.HINGE_LINE -> PoseFamily.HIP_HINGE_DEADLIFT
                DiagramArchetype.SQUAT_DEPTH -> PoseFamily.SQUAT
                DiagramArchetype.SPRINT_DRIVE -> PoseFamily.SPRINT_DRIVE
                DiagramArchetype.HORIZONTAL_LEVER -> PoseFamily.PLANK_ANTI_EXT
                DiagramArchetype.MOBILITY_ARC -> PoseFamily.MOBILITY_HIP_90_90
                DiagramArchetype.BREATH_CADENCE -> PoseFamily.BREATHING_POSE
                DiagramArchetype.OLYMPIC_TRIPLE_EXT -> PoseFamily.OLYMPIC_LIFT
            }
        }
    }

    private fun resolveProp(m: MovementDetail, family: PoseFamily): EquipmentProp {
        val eq = m.equipment.joinToString(" ").lowercase()
        return when {
            eq.contains("बार्बेल") || eq.contains("बारबेल") || eq.contains("barbell") -> EquipmentProp.BARBELL
            eq.contains("रिंग्स") || eq.contains("rings") -> EquipmentProp.RINGS
            eq.contains("पुल-अप बार") || eq.contains("बार") && family == PoseFamily.PULL_UP_HANG -> EquipmentProp.PULLUP_BAR
            eq.contains("बेंच") || eq.contains("bench") -> EquipmentProp.BENCH
            eq.contains("केटलबेल") || eq.contains("kettlebell") -> EquipmentProp.KETTLEBELL
            eq.contains("डंबल") || eq.contains("डम्बल") || eq.contains("dumbbell") -> EquipmentProp.DUMBBELL
            eq.contains("एब व्हील") || eq.contains("wheel") -> EquipmentProp.AB_WHEEL
            eq.contains("साइकिल") || eq.contains("रोइंग") || eq.contains("bike") || eq.contains("rower") -> EquipmentProp.CYCLE_ROWER
            eq.contains("दीवार") || eq.contains("wall") -> EquipmentProp.WALL
            eq.contains("बॉक्स") || eq.contains("कोन") || eq.contains("cone") || eq.contains("box") -> EquipmentProp.CONES_BOX
            eq.contains("ब्लॉक") || eq.contains("मैट") || eq.contains("mat") -> EquipmentProp.BLOCKS_MAT
            else -> EquipmentProp.NONE
        }
    }

    private fun resolveMuscleHighlights(m: MovementDetail): List<MuscleHighlight> {
        val joined = (m.targetMuscles + m.bodyRegions.map { it.label }).joinToString(" ").lowercase()
        val out = mutableListOf<MuscleHighlight>()
        if (joined.contains("क्वाड") || joined.contains("जांघ") || joined.contains("घुटन")) out += MuscleHighlight.QUADS
        if (joined.contains("ग्लूट") || joined.contains("कूल्ह")) out += MuscleHighlight.GLUTES
        if (joined.contains("हैमस्ट्रिंग")) out += MuscleHighlight.HAMSTRINGS
        if (joined.contains("चेस्ट") || joined.contains("सीन")) out += MuscleHighlight.CHEST
        if (joined.contains("लैट") || joined.contains("पीठ") || joined.contains("स्कैपुला") || joined.contains("रीढ़")) out += MuscleHighlight.LATS_BACK
        if (joined.contains("कंध")) out += MuscleHighlight.SHOULDERS
        if (joined.contains("बाइसेप") || joined.contains("ट्राइसेप") || joined.contains("कोहन") || joined.contains("कला")) out += MuscleHighlight.ARMS
        if (joined.contains("कोर") || joined.contains("पेट") || joined.contains("ऑब्लिक")) out += MuscleHighlight.CORE
        if (joined.contains("पिंडल") || joined.contains("टखन") || joined.contains("तलव")) out += MuscleHighlight.CALVES_ANKLES
        if (joined.contains("दिल") || joined.contains("फेफड़")) out += MuscleHighlight.CARDIO_LUNGS
        if (out.isEmpty()) out += MuscleHighlight.FULL_BODY
        return out.distinct().take(4)
    }

    private fun cleanStepText(raw: String, fallback: String): String {
        val stripped = raw.replace(Regex("^\\d+\\.\\s*[^:]+:\\s*"), "").trim()
        return stripped.ifBlank { fallback }
    }

    private fun buildSevenFrames(m: MovementDetail, family: PoseFamily): List<ExerciseVisualFrame> {
        val vg = m.visualGuide
        val s1 = cleanStepText(m.steps.getOrElse(0) { "" }, vg.startPositionCue)
        val s2 = cleanStepText(m.steps.getOrElse(2) { "" }, vg.executionAlignmentCue)
        val s3 = cleanStepText(m.steps.getOrElse(3) { "" }, vg.executionAlignmentCue)
        val s4 = cleanStepText(m.steps.getOrElse(6) { "" }, vg.finishPositionCue)
        val s5 = cleanStepText(m.steps.getOrElse(7) { "" }, "नियंत्रण के साथ शुरुआती स्थिति में लौटें")
        val primaryAngle = vg.keyJointAngles.firstOrNull() ?: "रीढ़ और जोड़ सीधी लाइन में"
        val mistakeFix = m.commonMistakes.firstOrNull()?.howToCorrect ?: vg.executionAlignmentCue

        val altAngleTitle = when (family) {
            PoseFamily.SQUAT, PoseFamily.SPLIT_SQUAT_LUNGE, PoseFamily.HIP_HINGE_DEADLIFT -> "सामने से घुटनों व पैर की सीध"
            PoseFamily.HORIZONTAL_PUSH_BENCH, PoseFamily.PUSHUP_FLOOR, PoseFamily.VERTICAL_PUSH_OHP -> "कोहनी कोण (45°) व कंधे का सेटअप"
            PoseFamily.HANDSTAND_INVERSION -> "कलाई, कंधे और कूल्हे की सीधी लाइन"
            PoseFamily.CARDIO_RUN_WALK, PoseFamily.SPRINT_DRIVE -> "कदम, धड़ का झुकाव और हाथ स्विंग"
            PoseFamily.BREATHING_POSE -> "पसलियों का फैलाव और कंधों का ढीलापन"
            else -> "वैकल्पिक कोण: जोड़ों की सीध"
        }

        val detailTitle = when (family) {
            PoseFamily.SQUAT -> "पैरों का दबाव व घुटने की दिशा"
            PoseFamily.HIP_HINGE_DEADLIFT, PoseFamily.OLYMPIC_LIFT -> "ग्रिप और बार का शरीर के करीब पथ"
            PoseFamily.HANDSTAND_INVERSION -> "उंगलियों से बैलेंस और कंधे का पुश"
            PoseFamily.PULL_UP_HANG, PoseFamily.ROW_PULL -> "स्कैपुला (कंधे की हड्डी) का लॉक व ग्रिप"
            PoseFamily.HORIZONTAL_PUSH_BENCH, PoseFamily.PUSHUP_FLOOR -> "कलाई सीधी और कंधे कानों से दूर"
            PoseFamily.BREATHING_POSE -> "डायाफ्राम (पेट) गति व लंबी साँस छोड़ना"
            else -> "मुख्य तकनीक व सुरक्षा फोकस"
        }

        val primaryHighlightLabel = m.targetMuscles.take(2).joinToString(" · ").ifBlank { "सही मुद्रा" }

        return listOf(
            ExerciseVisualFrame(
                category = GalleryFrameCategory.START,
                title = "1. शुरुआती स्थिति",
                angleLabel = "सेटअप व्यू",
                instructionCue = s1,
                focusHighlight = vg.startPositionCue,
                progressT = 0.0f
            ),
            ExerciseVisualFrame(
                category = GalleryFrameCategory.INITIATION,
                title = "2. गति की शुरुआत",
                angleLabel = "साइड व्यू",
                instructionCue = s2,
                focusHighlight = "लक्ष्य मसल: $primaryHighlightLabel",
                progressT = 0.28f
            ),
            ExerciseVisualFrame(
                category = GalleryFrameCategory.MID_MOVEMENT,
                title = "3. मध्य स्थिति",
                angleLabel = "सक्रिय चरण",
                instructionCue = s3,
                focusHighlight = vg.executionAlignmentCue,
                progressT = 0.62f
            ),
            ExerciseVisualFrame(
                category = GalleryFrameCategory.END_POSITION,
                title = "4. अंतिम / पीक स्थिति",
                angleLabel = "पीक रेंज",
                instructionCue = s4,
                focusHighlight = vg.finishPositionCue,
                progressT = 1.0f
            ),
            ExerciseVisualFrame(
                category = GalleryFrameCategory.RETURN_MOVEMENT,
                title = "5. वापसी",
                angleLabel = "नियंत्रित वापसी",
                instructionCue = s5,
                focusHighlight = "बिना झटके आराम से लौटें",
                progressT = 0.35f
            ),
            ExerciseVisualFrame(
                category = GalleryFrameCategory.ALT_ANGLE,
                title = "6. $altAngleTitle",
                angleLabel = "वैकल्पिक कोण",
                instructionCue = vg.keyJointAngles.joinToString(" • ").ifBlank { vg.executionAlignmentCue },
                focusHighlight = primaryAngle,
                progressT = 0.85f,
                isAltAngle = true
            ),
            ExerciseVisualFrame(
                category = GalleryFrameCategory.TECHNIQUE_DETAIL,
                title = "7. $detailTitle",
                angleLabel = "क्लोज़-अप फोकस",
                instructionCue = mistakeFix,
                focusHighlight = primaryAngle,
                progressT = 0.72f,
                isDetailZoom = true
            )
        )
    }
}
