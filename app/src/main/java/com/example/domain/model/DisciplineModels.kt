package com.example.domain.model

enum class DisciplineId(
    val title: String,
    val shortTitle: String,
    val signatureLabel: String,
    val transferableLabel: String
) {
    YOGA(
        title = "योग (Yoga)",
        shortTitle = "योग",
        signatureLabel = "मुख्य / एडवांस आसन",
        transferableLabel = "रोज़मर्रा के उपयोगी अभ्यास"
    ),
    CALISTHENICS(
        title = "कैलिस्थेनिक्स (Calisthenics)",
        shortTitle = "कैलिस्थेनिक्स",
        signatureLabel = "मुख्य / एडवांस स्किल्स",
        transferableLabel = "ताकत व कंट्रोल अभ्यास"
    ),
    STRENGTH(
        title = "स्ट्रेंथ ट्रेनिंग (Strength Training)",
        shortTitle = "स्ट्रेंथ",
        signatureLabel = "मुख्य ताकत अभ्यास",
        transferableLabel = "सपोर्ट और मसल्स अभ्यास"
    ),
    CARDIO(
        title = "कार्डियो व स्टैमिना (Cardio)",
        shortTitle = "कार्डियो",
        signatureLabel = "मुख्य दौड़ व एंड्योरेंस लक्ष्य",
        transferableLabel = "आसान कार्डियो तरीके"
    ),
    SPRINT_POWER(
        title = "स्प्रिंट व तेज़ी (Sprint & Power)",
        shortTitle = "स्प्रिंट व पावर",
        signatureLabel = "मुख्य स्पीड व जंप स्किल्स",
        transferableLabel = "शुरुआती ड्रिल्स व तैयारी"
    ),
    MOBILITY(
        title = "मोबिलिटी व लचीलापन (Mobility)",
        shortTitle = "मोबिलिटी",
        signatureLabel = "गहरे लचीलेपन के अभ्यास",
        transferableLabel = "रोज़ाना के जॉइंट मोबिलिटी अभ्यास"
    ),
    CORE(
        title = "कोर ट्रेनिंग (Core Training)",
        shortTitle = "कोर",
        signatureLabel = "एडवांस कोर स्किल्स",
        transferableLabel = "मजबूत कोर के बेसिक अभ्यास"
    ),
    BALANCE_AGILITY(
        title = "बैलेंस व फुर्ती (Balance & Agility)",
        shortTitle = "बैलेंस व फुर्ती",
        signatureLabel = "एडवांस संतुलन स्किल्स",
        transferableLabel = "कंट्रोल व फुटवर्क अभ्यास"
    ),
    PREHAB(
        title = "जॉइंट सुरक्षा व प्रीहैब (Prehab)",
        shortTitle = "जॉइंट सुरक्षा",
        signatureLabel = "विशेष टिश्यू मजबूती अभ्यास",
        transferableLabel = "रोज़ाना के जॉइंट सपोर्ट अभ्यास"
    ),
    OLYMPIC_LIFTING(
        title = "ओलंपिक लिफ्टिंग (Olympic Lifting)",
        shortTitle = "ओलंपिक लिफ्ट",
        signatureLabel = "मुख्य टेक्निकल लिफ्ट्स",
        transferableLabel = "आसान विस्फोटक पावर अभ्यास"
    ),
    MARTIAL_ARTS(
        title = "मार्शल आर्ट्स व मूवमेंट",
        shortTitle = "मार्शल आर्ट्स",
        signatureLabel = "विशेष किक व कॉम्बो स्किल्स",
        transferableLabel = "कंडीशनिंग व फुटवर्क ड्रिल्स"
    ),
    BREATHING(
        title = "साँस व आराम (Breathing)",
        shortTitle = "साँस व आराम",
        signatureLabel = "विशेष प्राणायाम व ब्रीदिंग",
        transferableLabel = "सरल रिलैक्सेशन तकनीक"
    )
}

enum class MovementCategory(val badge: String) {
    SIGNATURE_ADVANCED("एडवांस / विशेष स्किल"),
    TRANSFERABLE_FUNCTIONAL("उपयोगी / बेसिक अभ्यास")
}

enum class DifficultyLevel(val label: String, val rank: Int) {
    BEGINNER("आसान (शुरुआती)", 1),
    INTERMEDIATE("मध्यम", 2),
    ADVANCED("कठिन", 3),
    EXPERT("विशेषज्ञ", 4)
}

enum class ProgrammingModel(val label: String) {
    STRENGTH_HYPERTROPHY("मसल्स और ताकत"),
    STATIC_SKILL("स्टैटिक बैलेंस व होल्ड"),
    MOBILITY("लचीलापन और जॉइंट कंट्रोल"),
    CARDIO("कार्डियो व स्टैमिना"),
    SPRINT_POWER("स्पीड और विस्फोटक पावर"),
    BALANCE_COORDINATION("बैलेंस और तालमेल"),
    BREATHING("साँस और मानसिक शांति")
}

enum class EvidenceLevel(val label: String) {
    STRONG("पक्का वैज्ञानिक आधार"),
    MODERATE("मध्यम वैज्ञानिक आधार"),
    LIMITED("सीमित प्रमाण"),
    EMERGING("नया शोध"),
    EXPERT_PRACTICE("प्रैक्टिकल कोचिंग अनुभव")
}

enum class BodyRegion(val label: String) {
    NECK("गर्दन"),
    SHOULDERS("कंधे"),
    ROTATOR_CUFF("रोटेटर कफ"),
    SCAPULA("स्कैपुला (पीठ ऊपरी हिस्सा)"),
    THORACIC_SPINE("ऊपरी रीढ़"),
    ELBOWS("कोहनी"),
    WRISTS("कलाई"),
    CORE("पेट और कमर (कोर)"),
    HIPS("कूल्हे (हिप्स)"),
    GLUTES("ग्लूट्स"),
    HAMSTRINGS("हैमस्ट्रिंग्स"),
    ADDUCTORS("अंदरूनी जांघ"),
    KNEES("घुटने"),
    CALVES("पिंडली (काव्स)"),
    ANKLES("टखने (एंकल)"),
    FEET("पैर और तलवे"),
    CARDIORESPIRATORY("दिल और फेफड़े"),
    FULL_BODY("पूरा शरीर")
}

enum class MovementPattern(val label: String) {
    HORIZONTAL_PUSH("सामने धक्का (Horizontal Push)"),
    VERTICAL_PUSH("ऊपर धक्का (Vertical Push)"),
    HORIZONTAL_PULL("सामने से खींचना (Horizontal Pull)"),
    VERTICAL_PULL("ऊपर से खींचना (Vertical Pull)"),
    HIP_HINGE("हिप हिंज (कमर मोड़ना)"),
    SQUAT("स्क्वैट (बैठना-उठना)"),
    SINGLE_LEG("एक पैर का अभ्यास"),
    CARRY_ANTI_ROTATION("कोर स्थिरता व वज़न उठाकर चलना"),
    LOCOMOTION_SPRINT("तेज़ दौड़ व कदम"),
    PLYOMETRIC_EXPLOSIVE("जंप व विस्फोटक शक्ति"),
    INVERSION_BALANCE("उल्टा बैलेंस व कंट्रोल"),
    END_RANGE_MOBILITY("जॉइंट लचीलापन"),
    ENDURANCE_CYCLICAL("लगातार कार्डियो"),
    STRIKE_EVASION("किक, पंच व फुर्ती"),
    RESPIRATORY("साँस नियंत्रण")
}

data class DisciplineInfo(
    val id: DisciplineId,
    val name: String,
    val tagline: String,
    val overview: String,
    val signatureTitle: String,
    val signatureDescription: String,
    val transferableTitle: String,
    val transferableDescription: String,
    val skillBenefits: String,
    val limitations: String,
    val hypertrophyIntegrationNote: String,
    val safetyWarning: String? = null
)

data class ReadinessTest(
    val testName: String,
    val benchmark: String,
    val rationale: String
)

data class CommonMistake(
    val title: String,
    val looksLike: String,
    val whyItHappens: String,
    val whyItMatters: String,
    val howToCorrect: String
)

data class ProgressionLevelSpec(
    val levelNumber: Int,
    val title: String,
    val objective: String,
    val exercises: String,
    val prescription: String,
    val rest: String,
    val weeklyFrequency: String,
    val techniqueCues: String,
    val advancementCriteria: String,
    val regressionOption: String,
    val commonFailurePoints: String
)

data class PracticePrescription(
    val frequencyPerWeek: String,
    val volumeAndSets: String,
    val targetEffortOrZone: String,
    val restPeriods: String,
    val weeklyProgressionRule: String,
    val deloadGuidance: String
)

enum class DiagramArchetype {
    INVERSION_STACK,
    HINGE_LINE,
    SQUAT_DEPTH,
    SPRINT_DRIVE,
    HORIZONTAL_LEVER,
    MOBILITY_ARC,
    BREATH_CADENCE,
    OLYMPIC_TRIPLE_EXT
}

data class VisualAlignmentSpec(
    val archetype: DiagramArchetype,
    val startPositionCue: String,
    val executionAlignmentCue: String,
    val finishPositionCue: String,
    val keyJointAngles: List<String>
)

data class MovementDetail(
    val id: String,
    val name: String,
    val alternativeNames: List<String>,
    val discipline: DisciplineId,
    val category: MovementCategory,
    val difficulty: DifficultyLevel,
    val programmingModel: ProgrammingModel,
    val primaryAbilities: List<String>,
    val secondaryAbilities: List<String>,
    val targetMuscles: List<String>,
    val bodyRegions: List<BodyRegion>,
    val movementPattern: MovementPattern,
    val trainingPurpose: String,
    val equipment: List<String>,
    val estimatedMinutes: Int,
    val experienceLevel: String,
    val whatIsIt: String,
    val bodyMechanics: String,
    val whyItMatters: String,
    val musclesAndJoints: String,
    val developsWhat: String,
    val doesNotImprove: String,
    val primaryBenefits: List<String>,
    val secondaryBenefits: List<String>,
    val performanceApplications: List<String>,
    val crossTrainingApplications: List<String>,
    val limitations: List<String>,
    val commonMisconceptions: List<String>,
    val baselineStrength: String,
    val jointMobility: String,
    val stabilityBalance: String,
    val techniquePrereqs: String,
    val previousMovements: List<String>,
    val readinessTests: List<ReadinessTest>,
    val steps: List<String>,
    val commonMistakes: List<CommonMistake>,
    val injuryRisks: String,
    val jointLoading: String,
    val painWarningSigns: String,
    val fatigueConsiderations: String,
    val whenToStop: String,
    val professionalSupervision: String,
    val criticalSafetyNotice: String? = null,
    val progressionRoadmap: List<ProgressionLevelSpec>,
    val prescription: PracticePrescription,
    val visualGuide: VisualAlignmentSpec,
    val evidenceLevel: EvidenceLevel = EvidenceLevel.MODERATE
)
