package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlannedItem(
    val uid: String,
    val movementId: String,
    val movementName: String,
    val disciplineId: String,
    val targetMuscles: List<String> = emptyList(),
    val sets: Int = 3,
    val repsOrDuration: String = "8-12 रैप्स",
    val loadKg: Double = 0.0,
    val rir: Int = 2,
    val restSeconds: Int = 120,
    val notes: String = ""
)

@Serializable
data class PlannedDayModel(
    val dayOfWeek: Int, // 1 = Monday .. 7 = Sunday
    val dayName: String,
    val title: String,
    val focusTag: String,
    val isRestDay: Boolean,
    val items: List<PlannedItem>,
    val notes: String = ""
)

enum class ConflictSeverity {
    HIGH,
    MODERATE,
    INFO
}

data class PlannerConflictWarning(
    val title: String,
    val detail: String,
    val suggestedAdjustment: String,
    val severity: ConflictSeverity
)

enum class TrainingPriority(val label: String) {
    HYPERTROPHY("मसल्स (Hypertrophy पहले)"),
    STRENGTH("ताकत (Strength पहले)"),
    ATHLETIC_POWER("स्प्रिंट और तेज़ी पहले"),
    ENDURANCE("स्टैमिना (Cardio पहले)"),
    BALANCED("संतुलित फिटनेस")
}

object PlannerConflictDetector {

    fun dayNameFor(dayOfWeek: Int): String = when (dayOfWeek) {
        1 -> "सोमवार"
        2 -> "मंगलवार"
        3 -> "बुधवार"
        4 -> "गुरुवार"
        5 -> "शुक्रवार"
        6 -> "शनिवार"
        else -> "रविवार"
    }

    fun defaultTemplateDays(): List<PlannedDayModel> = listOf(
        PlannedDayModel(
            dayOfWeek = 1,
            dayName = "सोमवार",
            title = "ऊपरी शरीर (हैवी)",
            focusTag = "मसल्स व ताकत",
            isRestDay = false,
            items = listOf(
                PlannedItem("m1", "str_bench_press", "बेंच प्रेस (Bench Press)", DisciplineId.STRENGTH.name, listOf("चेस्ट", "ट्राइसेप्स", "कंधे"), 4, "5-8 रैप्स", 0.0, 2, 180),
                PlannedItem("m2", "str_weighted_pullup", "वेटेड पुल-अप (Weighted Pull-up)", DisciplineId.STRENGTH.name, listOf("लैट्स", "पीठ", "बाइसेप्स"), 4, "5-8 रैप्स", 0.0, 2, 180),
                PlannedItem("m3", "str_ohp", "ओवरहेड प्रेस (OHP)", DisciplineId.STRENGTH.name, listOf("कंधे", "ट्राइसेप्स"), 3, "6-10 रैप्स", 0.0, 2, 150),
                PlannedItem("m4", "str_barbell_row", "बार्बेल रो (Barbell Row)", DisciplineId.STRENGTH.name, listOf("पीठ", "लैट्स"), 3, "8-10 रैप्स", 0.0, 2, 120)
            )
        ),
        PlannedDayModel(
            dayOfWeek = 2,
            dayName = "मंगलवार",
            title = "निचला शरीर (हैवी)",
            focusTag = "पैर की ताकत व मजबूती",
            isRestDay = false,
            items = listOf(
                PlannedItem("t1", "str_back_squat", "बैक स्क्वैट (Back Squat)", DisciplineId.STRENGTH.name, listOf("क्वाड्स", "ग्लूट्स"), 4, "5-8 रैप्स", 0.0, 2, 180),
                PlannedItem("t2", "str_rdl", "रोमानियन डेडलिफ्ट (RDL)", DisciplineId.STRENGTH.name, listOf("हैमस्ट्रिंग्स", "ग्लूट्स"), 3, "6-10 रैप्स", 0.0, 2, 150),
                PlannedItem("t3", "str_standing_calf_raise", "स्टैंडिंग काफ़ रेज़ (Calf Raise)", DisciplineId.STRENGTH.name, listOf("पिंडली"), 4, "10-15 रैप्स", 0.0, 1, 90),
                PlannedItem("t4", "pre_nordic_curl", "नॉर्डिक हैमस्ट्रिंग कर्ल (Nordic)", DisciplineId.PREHAB.name, listOf("हैमस्ट्रिंग्स"), 2, "4-6 धीमे रैप्स", 0.0, 2, 120)
            )
        ),
        PlannedDayModel(
            dayOfWeek = 3,
            dayName = "बुधवार",
            title = "ऊपरी शरीर (लाइट व कंट्रोल)",
            focusTag = "मसल्स व कंधे का कंट्रोल",
            isRestDay = false,
            items = listOf(
                PlannedItem("w1", "str_weighted_dip", "वेटेड डिप्स (Weighted Dip)", DisciplineId.STRENGTH.name, listOf("चेस्ट", "ट्राइसेप्स", "कंधे"), 3, "8-12 रैप्स", 0.0, 2, 120),
                PlannedItem("w2", "cal_scapular_pullup", "स्कैपुलर पुल-अप (Scapular Pull-up)", DisciplineId.CALISTHENICS.name, listOf("पीठ ऊपरी हिस्सा", "स्कैपुला"), 3, "10-12 रैप्स", 0.0, 2, 75),
                PlannedItem("w3", "str_face_pull", "फेस पुल (Face Pull)", DisciplineId.STRENGTH.name, listOf("रियर डेल्ट्स", "रोटेटर कफ"), 3, "12-15 रैप्स", 0.0, 2, 75),
                PlannedItem("w4", "mob_shoulder_cars", "शोल्डर CARs (कंधे घुमाना)", DisciplineId.MOBILITY.name, listOf("कंधे"), 2, "5 धीमे चक्कर/तरफ", 0.0, 0, 45)
            )
        ),
        PlannedDayModel(
            dayOfWeek = 4,
            dayName = "गुरुवार",
            title = "निचला शरीर (लाइट व स्प्रिंट तैयारी)",
            focusTag = "संतुलन व पैरों की तैयारी",
            isRestDay = false,
            items = listOf(
                PlannedItem("th1", "str_bulgarian_split_squat", "बुल्गारियन स्प्लिट स्क्वैट", DisciplineId.STRENGTH.name, listOf("क्वाड्स", "ग्लूट्स"), 3, "8-12 रैप्स/पैर", 0.0, 2, 120),
                PlannedItem("th2", "spr_a_skip", "ए-स्किप (A-Skip ड्रिल)", DisciplineId.SPRINT_POWER.name, listOf("हिप फ्लेक्सर", "पिंडली"), 3, "20 मीटर", 0.0, 0, 90),
                PlannedItem("th3", "str_tibialis_raise", "टिबियालिस रेज़ (Tibialis Raise)", DisciplineId.STRENGTH.name, listOf("पैर का अगला हिस्सा"), 3, "15-20 रैप्स", 0.0, 2, 60),
                PlannedItem("th4", "mob_90_90_hip", "90/90 हिप मोबिलिटी", DisciplineId.MOBILITY.name, listOf("कूल्हे"), 2, "60 सेकंड/तरफ", 0.0, 0, 30)
            )
        ),
        PlannedDayModel(
            dayOfWeek = 5,
            dayName = "शुक्रवार",
            title = "बाजू, ग्रिप और कोर",
            focusTag = "आर्म्स व कोर मजबूती",
            isRestDay = false,
            items = listOf(
                PlannedItem("f1", "str_farmers_carry", "फार्मर्स कैरी (Farmer's Carry)", DisciplineId.STRENGTH.name, listOf("फोरआर्म्स", "ट्रैप्स", "कोर"), 3, "30-40 मीटर", 0.0, 2, 120),
                PlannedItem("f2", "cor_ab_wheel", "एब व्हील रोलआउट (Ab Wheel)", DisciplineId.CORE.name, listOf("पेट", "कोर"), 3, "8-12 रैप्स", 0.0, 2, 90),
                PlannedItem("f3", "cor_copenhagen_plank", "कोपनहेगन प्लैंक", DisciplineId.CORE.name, listOf("अंदरूनी जांघ", "कोर"), 3, "20-30 सेकंड/तरफ", 0.0, 2, 75),
                PlannedItem("f4", "cor_pallof_press", "पैलॉफ प्रेस (Pallof Press)", DisciplineId.CORE.name, listOf("ऑब्लिक्स", "कोर"), 3, "10-12 रैप्स/तरफ", 0.0, 1, 60)
            )
        ),
        PlannedDayModel(
            dayOfWeek = 6,
            dayName = "शनिवार",
            title = "रनिंग व आसान कार्डियो",
            focusTag = "ज़ोन 2 स्टैमिना",
            isRestDay = false,
            items = listOf(
                PlannedItem("s1", "car_zone2_run", "ज़ोन 2 रनिंग (आसान दौड़)", DisciplineId.CARDIO.name, listOf("दिल और फेफड़े"), 1, "35-45 मिनट (आराम से बात करते हुए)", 0.0, 0, 0),
                PlannedItem("s2", "mob_knee_to_wall", "दीवार तक घुटना (टखना मोबिलिटी)", DisciplineId.MOBILITY.name, listOf("टखने"), 2, "10 रैप्स/पैर", 0.0, 0, 30),
                PlannedItem("s3", "bre_extended_exhale", "लंबी साँस छोड़ना (Extended Exhale)", DisciplineId.BREATHING.name, listOf("रिलैक्सेशन"), 1, "दौड़ के बाद 5 मिनट", 0.0, 0, 0)
            )
        ),
        PlannedDayModel(
            dayOfWeek = 7,
            dayName = "रविवार",
            title = "रविवार रिकवरी व आराम",
            focusTag = "आराम, हल्की स्ट्रेचिंग व साँस",
            isRestDay = true,
            items = listOf(
                PlannedItem("su1", "bre_diaphragmatic", "गहरी पेट से साँस (Diaphragmatic)", DisciplineId.BREATHING.name, listOf("आराम"), 1, "8-10 मिनट", 0.0, 0, 0),
                PlannedItem("su2", "yog_childs_pose", "बालासन (Child's Pose)", DisciplineId.YOGA.name, listOf("कमर", "कूल्हे"), 1, "2-3 मिनट", 0.0, 0, 0)
            )
        )
    )

    fun analyzeWeek(
        days: List<PlannedDayModel>,
        priority: TrainingPriority = TrainingPriority.HYPERTROPHY
    ): List<PlannerConflictWarning> {
        val warnings = mutableListOf<PlannerConflictWarning>()
        val sorted = days.sortedBy { it.dayOfWeek }

        val heavyLowerIds = setOf("str_deadlift", "str_back_squat", "str_front_squat", "str_rdl", "oly_snatch", "oly_clean_and_jerk", "oly_squat_clean")
        val sprintIds = setOf("spr_acceleration", "spr_max_velocity", "spr_flying_sprint", "spr_depth_jump", "spr_bounding", "spr_hurdle_hops")
        val hamstringIds = setOf("str_deadlift", "str_rdl", "pre_nordic_curl", "str_nordic_curl", "bal_single_leg_rdl")
        val pushIds = setOf("str_bench_press", "str_ohp", "str_weighted_dip", "cal_dips", "cal_hspu", "cal_planche", "oly_split_jerk")
        val pullIds = setOf("str_weighted_pullup", "str_barbell_row", "cal_one_arm_pullup", "cal_front_lever", "cal_muscle_up")
        val highImpactIds = setOf("spr_depth_jump", "spr_bounding", "spr_hurdle_hops", "spr_max_velocity", "spr_acceleration", "bal_single_leg_box_jump", "car_5k", "car_10k", "car_half_marathon", "car_marathon")

        val activeDaysCount = sorted.count { !it.isRestDay && it.items.isNotEmpty() }
        if (activeDaysCount >= 7) {
            warnings += PlannerConflictWarning(
                title = "हफ्ते में कोई आराम का दिन नहीं है",
                detail = "सभी 7 दिनों में ट्रेनिंग रखी गई है। बिना आराम के मसल्स की ग्रोथ कम होती है और थकान जमा होती है।",
                suggestedAdjustment = "अपनी प्राथमिकता (${priority.label}) के लिए कम से कम 1 दिन (जैसे रविवार) को आराम या हल्की मोबिलिटी का रखें।",
                severity = ConflictSeverity.HIGH
            )
        }

        for (i in sorted.indices) {
            val today = sorted[i]
            val tomorrow = sorted[(i + 1) % sorted.size]
            val hasSprintToday = today.items.any { it.movementId in sprintIds }
            val hasHeavyLowerToday = today.items.any { it.movementId in heavyLowerIds }
            val hasHeavyLowerTomorrow = tomorrow.items.any { it.movementId in heavyLowerIds }
            val hasSprintTomorrow = tomorrow.items.any { it.movementId in sprintIds }

            if (hasSprintToday && hasHeavyLowerToday) {
                warnings += PlannerConflictWarning(
                    title = "${today.dayName}: तेज़ स्प्रिंट और भारी पैर एक्सरसाइज एक साथ",
                    detail = "एक ही दिन तेज़ स्प्रिंट/जंप और भारी स्क्वैट/डेडलिफ्ट करने से नर्वस सिस्टम और हैमस्ट्रिंग पर ज़्यादा दबाव पड़ता है।",
                    suggestedAdjustment = if (priority == TrainingPriority.HYPERTROPHY)
                        "भारी पैर के दिन सिर्फ मसल्स पर ध्यान दें और तेज़ स्प्रिंट के लिए कम से कम 48 घंटे का अंतर रखें।"
                    else "स्प्रिंट बिल्कुल तरोताज़ा होकर अलग दिन करें या 48 घंटे का गैप रखें।",
                    severity = ConflictSeverity.HIGH
                )
            } else if ((hasSprintToday && hasHeavyLowerTomorrow) || (hasHeavyLowerToday && hasSprintTomorrow)) {
                warnings += PlannerConflictWarning(
                    title = "${today.dayName} -> ${tomorrow.dayName}: स्प्रिंट और भारी पैर लगातार दिन",
                    detail = "भारी स्क्वैट या RDL के 24 घंटे के अंदर तेज़ स्प्रिंट करने से मांसपेशी खिंचने का खतरा बढ़ता है।",
                    suggestedAdjustment = "तेज़ स्प्रिंट और भारी लेग-डे के बीच ऊपरी शरीर या आराम का एक दिन रखें।",
                    severity = ConflictSeverity.HIGH
                )
            }
        }

        val hamstringSets = sorted.flatMap { it.items }.filter { it.movementId in hamstringIds }.sumOf { it.sets }
        if (hamstringSets > 16) {
            warnings += PlannerConflictWarning(
                title = "हैमस्ट्रिंग के बहुत ज़्यादा सेट्स ($hamstringSets साप्ताहिक सेट्स)",
                detail = "एक हफ्ते में 16 से ज़्यादा भारी हैमस्ट्रिंग सेट्स (डेडलिफ्ट/RDL/नॉर्डिक) से रिकवरी धीमी पड़ सकती है।",
                suggestedAdjustment = "हफ्ते में 10 से 14 अच्छे सेट्स तक सीमित रखें और 1-3 रैप्स बचे रहने पर सेट रोकें।",
                severity = ConflictSeverity.MODERATE
            )
        }

        val pushSets = sorted.flatMap { it.items }.filter { it.movementId in pushIds }.sumOf { it.sets }
        val pullSets = sorted.flatMap { it.items }.filter { it.movementId in pullIds }.sumOf { it.sets }
        if (pushSets > 22) {
            warnings += PlannerConflictWarning(
                title = "धक्का देने (Pressing) का लोड ज़्यादा है ($pushSets सेट्स)",
                detail = "ज़्यादा बेंच, डिप्स या हैंडस्टैंड करने से कंधे के अगले हिस्से में थकान या दर्द हो सकता है।",
                suggestedAdjustment = "हफ्ते में 12 से 20 सेट्स रखें और साथ में फेस पुल व कंधे के पीछे की एक्सरसाइज करें।",
                severity = ConflictSeverity.MODERATE
            )
        }
        if (pullSets > 22) {
            warnings += PlannerConflictWarning(
                title = "खींचने (Pulling) के सेट्स ज़्यादा हैं ($pullSets सेट्स)",
                detail = "ज़्यादा पुल-अप्स व भारी रोइंग से कोहनी और फोरआर्म पर दबाव आ सकता है।",
                suggestedAdjustment = "अतिरिक्त सेट्स कम करके 12-20 अच्छे सेट्स रखें।",
                severity = ConflictSeverity.MODERATE
            )
        }

        val highImpactDays = sorted.count { day -> day.items.any { it.movementId in highImpactIds } }
        if (highImpactDays >= 4) {
            warnings += PlannerConflictWarning(
                title = "पैरों पर हाई-इम्पैक्ट वाले दिन ज़्यादा ($highImpactDays दिन)",
                detail = "हफ्ते में 4+ दिन तेज़ दौड़ या जंप करने से पैरों की रिकवरी और मसल्स ग्रोथ पर असर पड़ सकता है।",
                suggestedAdjustment = "हल्की साइकिल या ढलान पर वॉक चुनें और हाई-इम्पैक्ट 2-3 दिन तक सीमित रखें।",
                severity = ConflictSeverity.MODERATE
            )
        }

        sorted.forEach { day ->
            val counts = day.items.groupingBy { it.movementId }.eachCount()
            val dupes = counts.filterValues { it > 1 }.keys
            if (dupes.isNotEmpty()) {
                warnings += PlannerConflictWarning(
                    title = "${day.dayName} में एक ही अभ्यास दो बार है",
                    detail = "एक ही एक्सरसाइज को दो बार जोड़ा गया है।",
                    suggestedAdjustment = "दोनों को मिलाकर एक ही एंट्री में सेट्स जोड़ दें।",
                    severity = ConflictSeverity.INFO
                )
            }
        }

        return warnings
    }
}
