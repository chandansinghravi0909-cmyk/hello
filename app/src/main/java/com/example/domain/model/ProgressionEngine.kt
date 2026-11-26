package com.example.domain.model

data class MovementProgressSnapshot(
    val movementId: String,
    val currentLevel: Int,
    val bestHoldSeconds: Int,
    val bestReps: Int,
    val workingLoadKg: Double,
    val lastPracticedAt: Long?
)

data class LoggedSessionSummary(
    val sets: Int,
    val reps: Int,
    val loadKg: Double,
    val rir: Int,
    val holdSeconds: Int,
    val durationMinutes: Int,
    val distanceKm: Double,
    val rpe: Int,
    val sprintSeconds: Double,
    val jumpCm: Double,
    val romScore: Int,
    val painFlag: Boolean,
    val loggedAt: Long
)

data class ProgressionRecommendation(
    val currentLevelIndex: Int,
    val currentLevelTitle: String,
    val nextLevelTitle: String?,
    val statusBadge: String,
    val nextSessionAction: String,
    val howToKnowImproving: String,
    val whenToIncreaseDifficulty: String,
    val whenToStaySame: String,
    val whenToRegress: String,
    val whenToDeload: String,
    val rationaleWhy: String,
    val unlockRequirements: String,
    val safetyGateMessage: String?
)

object ProgressionEngine {

    fun evaluate(
        movement: MovementDetail,
        progress: MovementProgressSnapshot?,
        recentLogs: List<LoggedSessionSummary>,
        latestReadinessScore: Int?
    ): ProgressionRecommendation {
        val levelIdx = (progress?.currentLevel ?: 0).coerceIn(0, (movement.progressionRoadmap.size - 1).coerceAtLeast(0))
        val currentSpec = movement.progressionRoadmap.getOrNull(levelIdx)
        val nextSpec = movement.progressionRoadmap.getOrNull(levelIdx + 1)

        val latest = recentLogs.firstOrNull()
        val hasPainFlag = recentLogs.take(2).any { it.painFlag }
        val lowReadiness = latestReadinessScore != null && latestReadinessScore <= 4

        if (hasPainFlag) {
            return ProgressionRecommendation(
                currentLevelIndex = levelIdx,
                currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
                nextLevelTitle = nextSpec?.title,
                statusBadge = "सुरक्षा रोक / हल्का करें",
                nextSessionAction = "अभी कठिन स्तर पर न जाएँ। वज़न या गहराई 30-50% कम करें, या आसान विकल्प चुनें: ${currentSpec?.regressionOption ?: "बिना दर्द वाला अभ्यास"}।",
                howToKnowImproving = "दर्द पूरी तरह खत्म हो जाए और लगातार दो दिन बिना तकलीफ अभ्यास हो।",
                whenToIncreaseDifficulty = "कम से कम 2 पूरे सेशन बिना किसी दर्द के होने के बाद ही।",
                whenToStaySame = "अगर हल्की जकड़न 24 घंटे में ठीक हो जाए।",
                whenToRegress = "जब भी जोड़ में तेज, चुभने वाला या बढ़ता हुआ दर्द महसूस हो।",
                whenToDeload = "3-5 दिन आराम या हल्का अभ्यास लें; दर्द बना रहे तो डॉक्टर या फिजियो से मिलें।",
                rationaleWhy = "पिछले लॉग में दर्द या तकलीफ दर्ज हुई थी, इसलिए सुरक्षा के लिए आगे बढ़ना रोका गया है।",
                unlockRequirements = currentSpec?.advancementCriteria ?: "दर्द-मुक्त सही तकनीक।",
                safetyGateMessage = "पिछले लॉग में दर्द दर्ज है — बिना दर्द के सेशन दर्ज होने तक अगला लेवल लॉक है।"
            )
        }

        if (recentLogs.isEmpty()) {
            return ProgressionRecommendation(
                currentLevelIndex = levelIdx,
                currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
                nextLevelTitle = nextSpec?.title,
                statusBadge = "पहला रिकॉर्ड ज़रूरी है",
                nextSessionAction = "लेवल $levelIdx के अनुसार 1-2 शुरुआती सेशन करें (${currentSpec?.prescription ?: movement.prescription.volumeAndSets}) और अपना डेटा लॉग करें।",
                howToKnowImproving = improvementMetricFor(movement.programmingModel),
                whenToIncreaseDifficulty = currentSpec?.advancementCriteria ?: "लगातार 2 दिन सही फॉर्म के साथ लक्ष्य पूरा करें।",
                whenToStaySame = "जब तकनीक पक्की हो रही हो या थकान ज़्यादा हो।",
                whenToRegress = "अगर फॉर्म बिगड़ने लगे या जोड़ में चुभन महसूस हो।",
                whenToDeload = movement.prescription.deloadGuidance,
                rationaleWhy = "अभी तक कोई प्रैक्टिस लॉग नहीं है। बिना असली डेटा के सिस्टम अपने आप लेवल नहीं बढ़ाता।",
                unlockRequirements = currentSpec?.advancementCriteria ?: "लेवल $levelIdx का अभ्यास लॉग करें।",
                safetyGateMessage = if (lowReadiness) "आज आपकी तैयारी कम है ($latestReadinessScore/10) — बिल्कुल हल्के से शुरुआत करें।" else null
            )
        }

        return when (movement.programmingModel) {
            ProgrammingModel.STRENGTH_HYPERTROPHY -> evaluateStrengthHypertrophy(
                levelIdx, currentSpec, nextSpec, latest!!, recentLogs, lowReadiness, latestReadinessScore
            )
            ProgrammingModel.STATIC_SKILL -> evaluateStaticSkill(
                movement, levelIdx, currentSpec, nextSpec, latest!!, progress, recentLogs, lowReadiness
            )
            ProgrammingModel.MOBILITY -> evaluateMobility(
                movement, levelIdx, currentSpec, nextSpec, latest!!, recentLogs
            )
            ProgrammingModel.CARDIO -> evaluateCardio(
                levelIdx, currentSpec, nextSpec, latest!!, recentLogs, lowReadiness
            )
            ProgrammingModel.SPRINT_POWER -> evaluateSprintPower(
                levelIdx, currentSpec, nextSpec, latest!!, lowReadiness, latestReadinessScore
            )
            ProgrammingModel.BALANCE_COORDINATION -> evaluateBalance(
                levelIdx, currentSpec, nextSpec, recentLogs
            )
            ProgrammingModel.BREATHING -> evaluateBreathing(
                levelIdx, currentSpec, nextSpec, latest!!
            )
        }
    }

    private fun evaluateStrengthHypertrophy(
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        latest: LoggedSessionSummary,
        logs: List<LoggedSessionSummary>,
        lowReadiness: Boolean,
        readiness: Int?
    ): ProgressionRecommendation {
        val (status, action, why) = when {
            lowReadiness -> Triple(
                "हल्का रखें / फॉर्म पर ध्यान",
                "वज़न ${formatKg(latest.loadKg)} ही रखें और 1 सेट कम करें (2-3 रैप्स बाकी छोड़ें) क्योंकि आज तैयारी $readiness/10 है।",
                "आज थकान ज़्यादा है; हल्के वॉल्यूम से मसल्स बनी रहेंगी और शरीर रिकवर भी हो पाएगा।"
            )
            latest.rir >= 3 && latest.reps > 0 -> Triple(
                "रैप्स बढ़ाएँ (अगला कदम)",
                "वज़न ${formatKg(latest.loadKg)} पर ही प्रति सेट 1-2 साफ़ रैप्स बढ़ाएँ (1-2 रैप बाकी छोड़ें), उसके बाद वज़न बढ़ाएँ।",
                "पिछले सेशन में आपके पास ${latest.rir} रैप की गुंजाइश बाकी थी — पहले रैप्स बढ़ाना सुरक्षित है।"
            )
            latest.rir in 1..2 && logs.size >= 2 && logs[1].rir in 1..2 && latest.reps >= 8 -> Triple(
                "थोड़ा वज़न बढ़ाएँ",
                "सबसे छोटा वज़न (+1.25 से +2.5 kg) बढ़ाएँ और रैप्स रेंज के निचले हिस्से से फिर शुरू करें।",
                "लगातार 2 सेशन में आपने 1-2 RIR के साथ लक्ष्य पूरा किया है — अब हल्का वज़न बढ़ा सकते हैं।"
            )
            latest.rir == 0 -> Triple(
                "थकान संभालें",
                "${formatKg(latest.loadKg)} बनाए रखें और हर सेट में कम से कम 1-2 रैप की ताकत बचाकर रखें।",
                "हर सेट में 0 RIR तक जाने से थकान तेजी से बढ़ती है और रिकवरी बिगड़ती है।"
            )
            else -> Triple(
                "नियमित अभ्यास जारी रखें",
                "${formatKg(latest.loadKg)} के साथ ${latest.sets} सेट x ${latest.reps.coerceAtLeast(6)} रैप्स पूरी रेंज में दोहराएँ।",
                "एक ही वज़न पर फॉर्म पक्का करने से सुरक्षित प्रगति होती है।"
            )
        }

        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = status,
            nextSessionAction = action,
            howToKnowImproving = "एक ही वज़न पर ज़्यादा रैप्स आना, या एक जैसे रैप्स पर ज़्यादा वज़न उठाना।",
            whenToIncreaseDifficulty = "लगातार 2 सेशन में पूरे सेट 1-2 RIR के साथ लक्ष्य के ऊपरी सिरे पर हों।",
            whenToStaySame = "शुरुआती सेट आसान हों पर आखिरी सेट में रैप्स गिर जाएँ।",
            whenToRegress = "जब फॉर्म टूटे, झटका लगाना पड़े या जोड़ में दर्द हो।",
            whenToDeload = "2-3 हफ्ते में थकान ज़्यादा हो तो 1 हफ्ते के लिए सेट्स 40-50% कम करें।",
            rationaleWhy = why,
            unlockRequirements = currentSpec?.advancementCriteria ?: "1-2 RIR पर लक्ष्य पूरा करें।",
            safetyGateMessage = null
        )
    }

    private fun evaluateStaticSkill(
        movement: MovementDetail,
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        latest: LoggedSessionSummary,
        progress: MovementProgressSnapshot?,
        logs: List<LoggedSessionSummary>,
        lowReadiness: Boolean
    ): ProgressionRecommendation {
        val bestHold = maxOf(latest.holdSeconds, progress?.bestHoldSeconds ?: 0)
        val (status, action, why) = when {
            lowReadiness -> Triple(
                "सिर्फ कंट्रोल व छोटा होल्ड",
                "पूरी क्षमता से होल्ड न करें; ${(bestHold * 0.6).toInt().coerceAtLeast(5)} सेकंड के साफ़ छोटे होल्ड करें।",
                "थकान में बैलेंस और कंधों का कंट्रोल जल्दी गिरता है।"
            )
            bestHold >= 20 && logs.size >= 2 && nextSpec != null -> Triple(
                "अगला लेवल आज़माएँ",
                "वर्तमान होल्ड से वॉर्म-अप करें और ${nextSpec.title} के 2-3 छोटे सेट शुरू करें।",
                "कई दिनों से ${bestHold}+ सेकंड का साफ़ होल्ड दिखाता है कि आपके जोड़ तैयार हैं।"
            )
            else -> Triple(
                "होल्ड समय थोड़ा बढ़ाएँ",
                "${(latest.holdSeconds + 2).coerceIn(6, 20)} सेकंड के 4-5 साफ़ सेट करें और बीच में 90-150 सेकंड आराम लें।",
                "सही पोस्चर में समय बढ़ाने से नसें और टेंडन मजबूत होते हैं।"
            )
        }

        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = status,
            nextSessionAction = action,
            howToKnowImproving = "एक जैसी पोजीशन में ज़्यादा देर रुक पाना और शरीर कम कांपना।",
            whenToIncreaseDifficulty = currentSpec?.advancementCriteria ?: "दो अलग दिनों में 15-20 सेकंड के साफ़ सेट पूरा करें।",
            whenToStaySame = "10 सेकंड बाद कमर झुकने लगे या कलाई में खिंचाव महसूस हो।",
            whenToRegress = currentSpec?.regressionOption ?: "पिछले आसान लेवल पर लौटें।",
            whenToDeload = movement.prescription.deloadGuidance,
            rationaleWhy = why,
            unlockRequirements = currentSpec?.advancementCriteria ?: "साफ़ लाइन व निर्धारित होल्ड समय।",
            safetyGateMessage = null
        )
    }

    private fun evaluateMobility(
        movement: MovementDetail,
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        latest: LoggedSessionSummary,
        logs: List<LoggedSessionSummary>
    ): ProgressionRecommendation {
        val status = if (latest.romScore >= 4 && logs.size >= 3 && nextSpec != null) "सक्रिय रेंज बढ़ाएँ" else "मौजूदा रेंज पक्की करें"
        val action = if (latest.romScore >= 4 && nextSpec != null) {
            "बिना किसी चुभन के ${nextSpec.title} आज़माएँ और अंतिम सिरे पर हल्का कंट्रोल रखें।"
        } else {
            "${movement.prescription.volumeAndSets} का अभ्यास शांत नाक की साँस के साथ करें।"
        }
        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = status,
            nextSessionAction = action,
            howToKnowImproving = "जॉइंट में आराम से ज़्यादा गहराई मिलना और शरीर का तनाव कम होना।",
            whenToIncreaseDifficulty = "जब मौजूदा खिंचाव शांत लगे और साँस न रुके।",
            whenToStaySame = "जब खिंचाव के लिए साँस रोकनी पड़े।",
            whenToRegress = "गहराई 10-15% कम करें या योगा ब्लॉक का सहारा लें।",
            whenToDeload = "अधिक जकड़न लगे तो हफ्ते में 2 हल्के सेशन करें।",
            rationaleWhy = "मोबिलिटी जबरदस्ती खींचने से नहीं, बल्कि शांत नर्वस सिस्टम और कंट्रोल से बढ़ती है।",
            unlockRequirements = currentSpec?.advancementCriteria ?: "बिना दर्द के आराम से रेंज पाना।",
            safetyGateMessage = null
        )
    }

    private fun evaluateCardio(
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        latest: LoggedSessionSummary,
        logs: List<LoggedSessionSummary>,
        lowReadiness: Boolean
    ): ProgressionRecommendation {
        val nextDuration = (latest.durationMinutes * 1.1).toInt().coerceAtMost(latest.durationMinutes + 8)
        val (status, action, why) = when {
            lowReadiness -> Triple(
                "सिर्फ आसान रिकवरी कार्डियो",
                "आज केवल 20-30 मिनट टहलें या बिल्कुल धीमी रफ़्तार रखें (बात करने में आसानी हो)।",
                "कम तैयारी में तेज़ कार्डियो करने से थकान और बढ़ेगी।"
            )
            latest.rpe <= 5 && logs.size >= 2 -> Triple(
                "समय धीरे-धीरे बढ़ाएँ",
                "रफ़्तार वही रखकर समय ${latest.durationMinutes} से ~$nextDuration मिनट करें (रफ़्तार और समय दोनों एक साथ न बढ़ाएँ)।",
                "कम थकान दिखाती है कि आपका दिल और फेफड़े अच्छी तरह अनुकूल हो रहे हैं।"
            )
            else -> Triple(
                "समय और रफ़्तार स्थिर रखें",
                "बात करने लायक रफ़्तार (RPE 4-5) पर ~${latest.durationMinutes} मिनट दोहराएँ ताकि पैरों की मसल्स रिकवर हो सकें।",
                "सही नियंत्रण रखने से कार्डियो और मसल्स ट्रेनिंग साथ-साथ चल सकते हैं।"
            )
        }
        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = status,
            nextSessionAction = action,
            howToKnowImproving = "उसी रफ़्तार पर साँस कम फूलना या कम हार्ट रेट रहना।",
            whenToIncreaseDifficulty = "2-3 हफ्ते तक बिना अतिरिक्त थकान के अभ्यास पूरा करने के बाद।",
            whenToStaySame = "अगर पैर भारी लगें या स्ट्रेंथ सेशन प्रभावित हों।",
            whenToRegress = "समय 20-30% घटाएँ या दौड़ की जगह साइकिल चुनें।",
            whenToDeload = "हर चौथे हफ्ते कार्डियो का समय 25-35% कम करें।",
            rationaleWhy = why,
            unlockRequirements = currentSpec?.advancementCriteria ?: "आरामदायक कार्डियो गति और रिकवरी।",
            safetyGateMessage = null
        )
    }

    private fun evaluateSprintPower(
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        latest: LoggedSessionSummary,
        lowReadiness: Boolean,
        readiness: Int?
    ): ProgressionRecommendation {
        if (lowReadiness) {
            return ProgressionRecommendation(
                currentLevelIndex = levelIdx,
                currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
                nextLevelTitle = nextSpec?.title,
                statusBadge = "तेज़ प्रयास रोकें",
                nextSessionAction = "आज पूरी ताकत से स्प्रिंट या डेप्थ जंप न करें (तैयारी $readiness/10 है)। इसके बजाय 60-70% रफ़्तार में आसान A-Skip या ड्रिल्स करें।",
                howToKnowImproving = "ताज़ा महसूस होने पर तेज़ स्प्रिंट टाइम और अगले दिन पैरों में दर्द न होना।",
                whenToIncreaseDifficulty = "सिर्फ जब तैयारी >= 7/10 हो और पैर हल्के व तेज़ लगें।",
                whenToStaySame = "जब कदम भारी लगने लगें।",
                whenToRegress = "वॉल ड्रिल्स या धीमी टेम्पो ड्रिल्स पर लौटें।",
                whenToDeload = "डीलोज़ हफ्ते में भारी जंप व स्प्रिंट हटा दें।",
                rationaleWhy = "तेज़ स्प्रिंट और जंप के लिए ताज़ा नर्वस सिस्टम ज़रूरी है; थकान में चोट का खतरा बढ़ता है।",
                unlockRequirements = currentSpec?.advancementCriteria ?: "पूरी ताज़गी और सही तकनीक।",
                safetyGateMessage = "तैयारी कम ($readiness/10) होने से तेज़ स्प्रिंट/जंप सुरक्षा के लिए रोके गए हैं।"
            )
        }

        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = "पूरे आराम के साथ तेज़ प्रयास",
            nextSessionAction = "${latest.sets.coerceIn(3, 5)} छोटे पर पूरी तेज़ी के प्रयास करें और हर प्रयास के बीच 2-4 मिनट पूरा आराम लें। गति कम होते ही सेशन रोक दें।",
            howToKnowImproving = "कम समय में तेज़ दौड़, ज़्यादा ऊँचा/लंबा जंप और जमीन से तेज़ उछाल।",
            whenToIncreaseDifficulty = "2-3 हफ्ते तक एक जैसी फुर्ती बने रहने के बाद ही 1 प्रयास या 5-10 मीटर बढ़ाएँ।",
            whenToStaySame = "अगर आखिरी प्रयासों में रफ़्तार गिर जाए या पिंडली/हैमस्ट्रिंग सख्त लगे।",
            whenToRegress = "गति 80-85% पर रखें या बॉक्स की ऊँचाई कम करें।",
            whenToDeload = "हर 3-4 हफ्ते में स्प्रिंट की संख्या आधी कर दें।",
            rationaleWhy = "स्पीड हमेशा ताज़ा रहकर तेज प्रयास करने से बढ़ती है, थकाने वाली एक्सरसाइज से नहीं।",
            unlockRequirements = currentSpec?.advancementCriteria ?: "पूरे आराम के साथ साफ़ स्पीड।",
            safetyGateMessage = null
        )
    }

    private fun evaluateBalance(
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        logs: List<LoggedSessionSummary>
    ): ProgressionRecommendation {
        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = if (logs.size >= 3 && nextSpec != null) "कठिनाई बढ़ाएँ" else "संतुलन पक्का करें",
            nextSessionAction = "बिना कमर या टखने हिलाए साफ़ अभ्यास करें; संतुलन स्थिर होने पर ही धीमा टेम्पो या नया पैटर्न जोड़ें।",
            howToKnowImproving = "पैरों का स्थिर रहना, बिना आवाज़ सॉफ्ट लैंडिंग और कम कंपन।",
            whenToIncreaseDifficulty = "लगातार 2 सेशन बिना संतुलन खोए पूरा करने पर।",
            whenToStaySame = "अगर बार-बार पैर नीचे रखना पड़े या घुटना अंदर झुके।",
            whenToRegress = "दीवार का हल्का सहारा लें या कदम/कूद छोटा रखें।",
            whenToDeload = "हल्का संतुलन अभ्यास रोज़ कर सकते हैं; थकान में जंप कम करें।",
            rationaleWhy = "संतुलन हमेशा शांत ध्यान और सटीक नियंत्रण से सुधरता है।",
            unlockRequirements = currentSpec?.advancementCriteria ?: "सभी सेट में स्थिर संतुलन।",
            safetyGateMessage = null
        )
    }

    private fun evaluateBreathing(
        levelIdx: Int,
        currentSpec: ProgressionLevelSpec?,
        nextSpec: ProgressionLevelSpec?,
        latest: LoggedSessionSummary
    ): ProgressionRecommendation {
        return ProgressionRecommendation(
            currentLevelIndex = levelIdx,
            currentLevelTitle = currentSpec?.title ?: "लेवल $levelIdx",
            nextLevelTitle = nextSpec?.title,
            statusBadge = "आरामदायक अभ्यास जारी रखें",
            nextSessionAction = "${latest.durationMinutes.coerceIn(5, 15)} मिनट नाक से शांत और सहज साँस लें। साँस को कभी ज़बरदस्ती न रोकें।",
            howToKnowImproving = "साँस सहज लगना, मन शांत होना और कसरत के बाद जल्दी सामान्य होना।",
            whenToIncreaseDifficulty = "पूरी तरह सहज लगे तभी समय 1-2 मिनट बढ़ाएँ।",
            whenToStaySame = "अगर हल्का चक्कर या घबराहट लगे तो गति कम करें।",
            whenToRegress = "सामान्य स्वाभाविक साँस लेने लगें।",
            whenToDeload = "साँस के अभ्यास हमेशा हल्के और आराम देने वाले होते हैं।",
            rationaleWhy = "सही साँस लेना रिकवरी को तेज करता है; ज़ोर ज़बरदस्ती नुकसानदायक हो सकती है।",
            unlockRequirements = "बिना किसी बेचैनी के सहज आरामदायक साँस।",
            safetyGateMessage = null
        )
    }

    private fun improvementMetricFor(model: ProgrammingModel): String = when (model) {
        ProgrammingModel.STRENGTH_HYPERTROPHY -> "एक ही वज़न पर ज़्यादा रैप्स, या ज़्यादा वज़न पर साफ़ फॉर्म।"
        ProgrammingModel.STATIC_SKILL -> "बिना हिले ज़्यादा सेकंड तक होल्ड कर पाना।"
        ProgrammingModel.MOBILITY -> "बिना चुभन या साँस रोके ज़्यादा गहराई व कंट्रोल।"
        ProgrammingModel.CARDIO -> "उसी रफ़्तार पर कम थकान या आराम से ज़्यादा समय चलना/दौड़ना।"
        ProgrammingModel.SPRINT_POWER -> "कम समय में तेज़ दौड़ व ज़्यादा ऊँचा/लंबा जंप।"
        ProgrammingModel.BALANCE_COORDINATION -> "स्थिर पैर और शांत, संतुलित लैंडिंग।"
        ProgrammingModel.BREATHING -> "गहरी शांति और अभ्यास के बाद जल्दी रिकवरी।"
    }

    private fun formatKg(kg: Double): String = if (kg <= 0.0) "शरीर के वज़न / बेसिक भार" else "${"%.1f".format(kg)} kg"
}
