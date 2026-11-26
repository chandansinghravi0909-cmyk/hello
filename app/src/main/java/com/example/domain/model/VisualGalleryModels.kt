package com.example.domain.model

enum class GalleryFrameCategory(
    val stepNumber: Int,
    val title: String,
    val defaultAngle: String
) {
    START(1, "शुरुआती स्थिति", "सेटअप व्यू"),
    INITIATION(2, "गति की शुरुआत", "साइड व्यू"),
    MID_MOVEMENT(3, "मध्य स्थिति", "सक्रिय चरण"),
    END_POSITION(4, "अंतिम / पीक स्थिति", "पीक पोज़िशन"),
    RETURN_MOVEMENT(5, "वापसी", "नियंत्रित वापसी"),
    ALT_ANGLE(6, "साइड / वैकल्पिक कोण", "सीध और एलाइनमेंट"),
    TECHNIQUE_DETAIL(7, "मुख्य तकनीक विवरण", "क्लोज़-अप फोकस")
}

enum class PoseFamily {
    SQUAT,
    SPLIT_SQUAT_LUNGE,
    HIP_HINGE_DEADLIFT,
    HORIZONTAL_PUSH_BENCH,
    PUSHUP_FLOOR,
    DIP_SUPPORT,
    VERTICAL_PUSH_OHP,
    HANDSTAND_INVERSION,
    PULL_UP_HANG,
    ROW_PULL,
    PLANK_ANTI_EXT,
    HOLLOW_DRAGON_LEVER,
    LEG_RAISE_LSIT,
    ROTATION_CARRY,
    SPRINT_DRIVE,
    JUMP_PLYO,
    LATERAL_SKATER_AGILITY,
    MARTIAL_STRIKE_KICK,
    YOGA_DOWNDOG_FOLD,
    YOGA_BACKBEND_WHEEL,
    YOGA_BALANCE_CROW,
    YOGA_TWIST_WARRIOR,
    MOBILITY_SPLIT_STRETCH,
    MOBILITY_HIP_90_90,
    OLYMPIC_LIFT,
    CARDIO_RUN_WALK,
    CARDIO_CYCLE_ROW,
    BREATHING_POSE
}

enum class EquipmentProp {
    NONE,
    BARBELL,
    DUMBBELL,
    KETTLEBELL,
    BENCH,
    PULLUP_BAR,
    RINGS,
    WALL,
    BLOCKS_MAT,
    AB_WHEEL,
    CYCLE_ROWER,
    CONES_BOX
}

enum class MuscleHighlight {
    QUADS,
    GLUTES,
    HAMSTRINGS,
    CHEST,
    LATS_BACK,
    SHOULDERS,
    ARMS,
    CORE,
    CALVES_ANKLES,
    CARDIO_LUNGS,
    FULL_BODY
}

data class ExerciseVisualFrame(
    val category: GalleryFrameCategory,
    val title: String,
    val angleLabel: String,
    val instructionCue: String,
    val focusHighlight: String,
    val progressT: Float,
    val isAltAngle: Boolean = false,
    val isDetailZoom: Boolean = false
)

data class ExerciseVisualGallerySpec(
    val poseFamily: PoseFamily,
    val prop: EquipmentProp,
    val highlights: List<MuscleHighlight>,
    val frames: List<ExerciseVisualFrame>
)

data class ExerciseTrainingSummary(
    val setsLabel: String,
    val repsOrTimeLabel: String,
    val restLabel: String,
    val frequencyLabel: String
)
