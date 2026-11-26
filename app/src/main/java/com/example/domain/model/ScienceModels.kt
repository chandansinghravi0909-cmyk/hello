package com.example.domain.model

enum class ScienceCategory(val title: String, val subtitle: String) {
    HYPERTROPHY("A. मसल्स बनाने का विज्ञान", "सही खिंचाव, सेट्स की संख्या और ग्रोथ"),
    STRENGTH("B. ताकत बढ़ाने का विज्ञान", "दिमाग और मांसपेशियों का तालमेल"),
    CARDIO("C. कार्डियो और स्टैमिना", "दिल की क्षमता, ज़ोन 2 और सही रफ़्तार"),
    SPRINT_POWER("D. स्प्रिंट और तेज़ी", "विस्फोटक ताकत और जमीन से उछाल"),
    MOBILITY("E. मोबिलिटी व लचीलापन", "दर्द-मुक्त रेंज और सक्रिय कंट्रोल"),
    RECOVERY("F. रिकवरी और आराम", "नींद, थकान प्रबंधन और डीलोज़"),
    NERVOUS_SYSTEM("G. नर्वस सिस्टम व स्किल्स", "सही तकनीक और संतुलन सीखना")
}

data class ScienceTopic(
    val id: String,
    val category: ScienceCategory,
    val title: String,
    val simpleDefinition: String,
    val howItWorks: String,
    val scientificMechanism: String,
    val whyItMatters: String,
    val practicalApplication: String,
    val commonMisconceptions: List<String>,
    val limitations: String,
    val evidenceLevel: EvidenceLevel,
    val verifiedReferences: List<String>
)

data class CardioPlanTemplate(
    val id: String,
    val title: String,
    val targetAudience: String,
    val weeklyStructure: List<String>,
    val intensityDistribution: String,
    val progressionRule: String,
    val safetyNote: String
)

data class BodyRegionCareGuide(
    val region: BodyRegion,
    val priorityOrder: Int,
    val roleInPerformance: String,
    val mobilityExercises: List<String>,
    val prehabExercises: List<String>,
    val recommendedDosage: String,
    val progressionRule: String,
    val weeklyIntegration: String,
    val medicalRedFlags: String
)
