package com.example.data.catalog

import com.example.domain.model.*

object CatalogPart1YogaCalisthenics {

    private val handstandDetail = buildMovement(
        id = "yog_handstand",
        name = "हैंडस्टैंड (Handstand)",
        alt = listOf("Handstand", "Adho Mukha Vrksasana", "अधोमुख वृक्षासन"),
        discipline = DisciplineId.YOGA,
        category = MovementCategory.SIGNATURE_ADVANCED,
        difficulty = DifficultyLevel.ADVANCED,
        model = ProgrammingModel.STATIC_SKILL,
        primaryAbilities = listOf("उल्टा संतुलन", "कंधे की स्थिरता", "शरीर की सीधी लाइन"),
        secondaryAbilities = listOf("कलाई की मजबूती", "सहनशक्ति"),
        muscles = listOf("कंधे (Deltoids)", "ऊपरी पीठ (Traps)", "ट्राइसेप्स", "कलाई व बाजू", "कोर"),
        regions = listOf(BodyRegion.WRISTS, BodyRegion.SHOULDERS, BodyRegion.SCAPULA, BodyRegion.CORE, BodyRegion.THORACIC_SPINE),
        pattern = MovementPattern.INVERSION_BALANCE,
        purpose = "हाथों के बल सीधा उल्टा खड़े होकर संतुलन और शरीर पर कंट्रोल सीखना।",
        equipment = listOf("बिना सामान / खाली दीवार", "मैट"),
        minutes = 15,
        whatIsIt = "दोनों हाथों पर शरीर का पूरा वज़न रखकर पैरों को ऊपर सीधा टिकाने का अभ्यास।",
        mechanics = "शरीर का भार हथेली के बीच रहता है। आगे गिरने से रोकने के लिए उंगलियां दबाई जाती हैं और पीछे जाने पर हथेली का निचला हिस्सा संभालता है।",
        whyItMatters = "यह सिर से पैर तक एक सीधी लाइन और कंधों की मजबूत पकड़ सिखाता है।",
        doesNotImprove = "यह सीने या पैरों की मसल्स बढ़ाने का मुख्य अभ्यास नहीं है और न ही स्टैमिना बढ़ाता है।",
        primaryBenefits = listOf(
            "शरीर का संतुलन और सीधी लाइन बेहतर बनाता है।",
            "कंधों और कलाइयों की सहनशक्ति बढ़ाता है।",
            "उंगलियों से सूक्ष्म बैलेंस संभालना सिखाता है।"
        ),
        secondaryBenefits = listOf("कलाई और उंगलियों के जोड़ मजबूत करता है।"),
        perfApps = listOf("कैलिस्थेनिक्स हैंडस्टैंड पुश-अप की नींव", "योग फ्लो में नियंत्रण"),
        crossApps = listOf("ओवरहेड प्रेस में कंधों को सही जगह लॉक करने में मदद करता है।"),
        limitations = listOf(
            "इसके लिए कलाई और कंधों का पूरा खुलना ज़रूरी है।",
            "मसल्स बढ़ाने (Hypertrophy) के लिए भारी वज़न जितना प्रभावी नहीं है।"
        ),
        misconceptions = listOf(
            "गलतफ़हमी: इसमें सिर्फ बाजू की भारी ताकत चाहिए। सच्चाई: सही लाइन और कंधे का खुलापन ताकत से ज़्यादा काम आता है।",
            "गलतफ़हमी: बस जोर से किक मारकर सीखें। सच्चाई: दीवार की तरफ चेहरा रखकर अभ्यास करने से बैलेंस जल्दी बनता है।"
        ),
        baselineStrength = "60 सेकंड प्लैंक और 30 सेकंड ऊँचा पाइक होल्ड (कोहनियाँ सीधी)।",
        jointMobility = "हाथ बिना कमर मोड़े सीधे सिर के ऊपर जाएँ और कलाई में दर्द न हो।",
        stabilityBalance = "कमर बिना ज़्यादा मोड़े दीवार के सहारे सीधा रुकना।",
        techniquePrereqs = "गिरने की स्थिति में सुरक्षित तरीके से बगल से उतरना (Cartwheel bail) आना चाहिए।",
        previousMovements = listOf("डाउनवर्ड डॉग (Downward Dog)", "हॉलो बॉडी होल्ड", "शोल्डर CARs"),
        readinessTests = listOf(
            ReadinessTest("दीवार कंधा टेस्ट", "पीठ दीवार से सटाकर हाथ सीधे ऊपर ले जाएँ।", "अगर पसलियाँ आगे निकलती हैं, तो कंधे अभी थोड़े सख्त हैं।"),
            ReadinessTest("कलाई और प्लैंक चेक", "हाथ सीधे रखकर 45-60 सेकंड बिना दर्द के प्लैंक रोकें।", "यह बताता है कि कलाई भार उठाने के लिए तैयार है।")
        ),
        steps = listOf(
            "1. शुरुआत: कलाइयों को अच्छी तरह गरम करें; दीवार से 15-20 सेमी दूर कंधों के बराबर हाथ रखें।",
            "2. सेटअप: उंगलियां पूरी फैलाएँ, उंगलियों के पोरों से फर्श हल्का पकड़ें और कोहनियाँ बिल्कुल सीधी रखें।",
            "3. गति की शुरुआत: कंधों को हाथों के ठीक ऊपर लाएँ और आराम से एक पैर ऊपर उठाएँ।",
            "4. सही अभ्यास: हाथों से फर्श को जोर से नीचे धकेलें, पेट को हल्का अंदर खींचें और पैर मिलाएँ।",
            "5. साँस: नाक से धीमी और सहज साँस लेते रहें — साँस न रोकें।",
            "6. शरीर की सीध: कलाई, कंधे, कूल्हे और टखने एक सीधी लाइन में हों; नज़र दोनों अंगूठों के बीच रखें।",
            "7. मुख्य स्थिति: आगे झुकने पर उंगलियां दबाकर संतुलन रोकें।",
            "8. सुरक्षित समाप्ति: थकान से गिरने के बजाय एक-एक पैर आराम से नीचे उतारें या बगल से मुड़कर उतरें।"
        ),
        mistakes = listOf(
            CommonMistake("कमर ज़्यादा मुड़ना (Banana Back)", "पीठ कमान की तरह झुक जाना और पैर पीछे चले जाना।", "कंधों का कम खुलना या पेट ढीला छोड़ना।", "कमर पर दबाव आता है और बैलेंस टूटता है।", "चेस्ट-टू-वॉल होल्ड करें और पसलियाँ अंदर रखें।"),
            CommonMistake("कोहनियाँ मोड़ना", "हवा में हाथ थोड़े मुड़ जाना।", "ताकत कम होना और जल्दी फ्री होल्ड करना।", "ट्राइसेप्स जल्दी थकते हैं और गिरने का डर रहता है।", "कोहनियाँ हमेशा सीधी लॉक रखें या पाइक होल्ड पर लौटें।"),
            CommonMistake("झटके से किक मारना", "बहुत जोर से पैर दीवार पर फेंकना।", "हाथों के ऊपर कंधे पहले सेट न करना।", "डर बढ़ता है और सही लाइन नहीं बनती।", "पहले कंधे सेट करें, फिर हल्के से पैर उठाएँ।")
        ),
        injuryRisks = "बिना तैयारी के करने से कलाई में दर्द या गिरने की संभावना रहती है।",
        jointLoading = "पूरे शरीर का वज़न कलाई और कंधों पर आता है।",
        fatigueConsiderations = "हमेशा सेशन की शुरुआत में ताज़ा रहते हुए 10-15 मिनट अभ्यास करें। कलाई थकते ही रुक जाएँ।",
        criticalSafety = "बिना दीवार के अभ्यास से पहले सुरक्षित तरीके से बगल से नीचे उतरना (Cartwheel bail) ज़रूर सीख लें।",
        roadmap = listOf(
            ProgressionLevelSpec(0, "लेवल 0: कलाई की तैयारी", "कलाई को भार सहने के लिए तैयार करना।", "कलाई घुमाना, हथेली दबाना, टेबलटॉप आगे झुकना", "2-3 सेट x 10-15 धीमे झुकाव", "60s", "4-5 दिन/हफ्ता", "उंगलियां फैलाएँ, कोहनियाँ सीधी रखें।", "कलाई में बिना किसी चुभन के झुकाव हो।", "मुट्ठी या पैरालेट्स का सहारा लें।", "कलाई गरम किए बिना सीधे किक मारना।"),
            ProgressionLevelSpec(1, "लेवल 1: प्लैंक और कंधे का कंट्रोल", "सीधी भुजाओं से धक्का देना और पेट कसना।", "सीधा प्लैंक, कंधे उचकाना", "3 सेट x 30-45s", "60s", "3 दिन/हफ्ता", "फर्श को दूर धकेलें, कमर न झुकाएँ।", "45s तक साफ़ सीधा प्लैंक।", "हाथ किसी ऊँची बेंच पर रखें।", "कमर नीचे ढीली छोड़ देना।"),
            ProgressionLevelSpec(2, "लेवल 2: पाइक होल्ड (जमीन पर)", "कूल्हों को कंधों के ऊपर लाना।", "डाउनवर्ड डॉग पाइक होल्ड", "3 सेट x 20-30s", "60s", "3 दिन/हफ्ता", "घुटने हल्के मोड़ सकते हैं ताकि पीठ सीधी रहे।", "30s आराम से पाइक होल्ड।", "घुटने ज़्यादा मोड़ें।", "पीठ गोल कर लेना।"),
            ProgressionLevelSpec(3, "लेवल 3: ऊँचा पाइक होल्ड (बॉक्स पर)", "हाथों पर लगभग 75% वज़न लाना।", "पैर बॉक्स/कुर्सी पर रखकर पाइक होल्ड", "4 सेट x 20-30s", "90s", "3 दिन/हफ्ता", "हाथ बॉक्स के करीब लाएँ ताकि धड़ सीधा रहे।", "4x25s बिना कोहनियाँ मोड़े होल्ड।", "बॉक्स की ऊँचाई कम करें।", "कंधे हाथों से पीछे खिसक जाना।"),
            ProgressionLevelSpec(4, "लेवल 4: आधी दीवार पर चढ़ना", "उल्टा होने की आदत डालना।", "आधी दीवार वॉक (45-60 डिग्री)", "3 सेट x 20-30s", "90s", "2-3 दिन/हफ्ता", "आराम से पैर दीवार पर चलकर थोड़ा ऊपर जाएँ।", "बिना घबराए ऊपर जाना व नीचे आना।", "बॉक्स पाइक होल्ड दोहराएँ।", "जल्दबाजी में नीचे फिसलना।"),
            ProgressionLevelSpec(5, "लेवल 5: दीवार की तरफ चेहरा (Chest-to-Wall)", "पूरी सीधी लाइन की सहनशक्ति बनाना।", "दीवार से 15 सेमी दूर हाथ रखकर सीधा होल्ड", "4 सेट x 20-40s", "90-120s", "3 दिन/हफ्ता", "सिर्फ पंजों से दीवार हल्की छुएँ, कमर अंदर रखें।", "3x40s शांत साँस के साथ सीधी लाइन।", "हाथ दीवार से 30 सेमी दूर रखें।", "सीना दीवार से सटाकर कमर मोड़ना।"),
            ProgressionLevelSpec(6, "लेवल 6: दीवार पर बैलेंस (Toe Pulls)", "उंगलियों से बैलेंस संभालना सीखना।", "दीवार से उंगलियों के दम पर पैर हल्के हटाना", "4 सेट x 6-10 धीमे प्रयास", "90-120s", "3 दिन/हफ्ता", "किक न करें; सिर्फ उंगलियां दबाकर पैर 2 सेमी हटाएँ।", "हर बार 3-5s हवा में रुक जाना।", "होल्ड का समय बढ़ाएँ।", "पैर से दीवार को धक्का देकर हटना।"),
            ProgressionLevelSpec(7, "लेवल 7: पीठ दीवार की तरफ रखकर किक-अप", "हल्के किक से बैलेंस पकड़ना।", "दीवार के पास हल्के सिज़र किक", "5 सेट x 4-6 शांत प्रयास", "90s", "3 दिन/हफ्ता", "धीमे से पैर उठाएँ और उंगलियों से संतुलन रोकें।", "सेशन में 3+ बार 5-10s रुक जाना।", "चेस्ट-टू-वॉल पर वापस जाएँ।", "तेज़ किक से दीवार पर पैर मारना।"),
            ProgressionLevelSpec(8, "लेवल 8: बिना दीवार के अभ्यास", "खुले फर्श पर संतुलन और सुरक्षित उतरना।", "खुले में फ्री हैंडस्टैंड (10-15 मिनट)", "6-8 साफ प्रयास", "90s", "3-4 दिन/हफ्ता", "गिरने लगें तो बगल से उतर जाएँ।", "कई बार 10-20s तक स्थिर रहना।", "दीवार से 1 फुट दूर अभ्यास करें।", "कलाई थकने पर भी कोशिश जारी रखना।"),
            ProgressionLevelSpec(9, "लेवल 9: पक्का फ्रीस्टैंडिंग हैंडस्टैंड", "30+ सेकंड स्थिर रुकना और अलग आकार बनाना।", "फ्रीस्टैंडिंग होल्ड व शेप्स", "4-6 सेट x 20-45s", "120s", "3-4 दिन/हफ्ता", "शांत साँस, स्थिर नज़र और न्यूनतम हलचल।", "जब चाहें 30s+ आराम से रुक जाना।", "हल्के लाइन रिविजन।", "कलाई की रिकवरी नज़रअंदाज करना।")
        ),
        prescription = PracticePrescription(
            frequencyPerWeek = "3-4 दिन/हफ्ता (भारी एक्सरसाइज से पहले 10-15 मिनट)",
            volumeAndSets = "4-6 साफ़ होल्ड या 6-8 संतुलित प्रयास",
            targetEffortOrZone = "तकनीक पर पूरा ध्यान; लाइन बिगड़ते ही रुकें",
            restPeriods = "60-120 सेकंड",
            weeklyProgressionRule = "2 हफ्ते लगातार कंट्रोल रहने पर समय 3-5 सेकंड बढ़ाएँ।",
            deloadGuidance = "हर चौथे हफ्ते अभ्यास का समय आधा कर दें।"
        ),
        visual = VisualAlignmentSpec(
            archetype = DiagramArchetype.INVERSION_STACK,
            startPositionCue = "हथेली के ठीक ऊपर कंधे, उंगलियां खुली व कोहनियाँ सीधी।",
            executionAlignmentCue = "बाजू कानों के पास, पसलियाँ अंदर, कूल्हे और टखने सीधे।",
            finishPositionCue = "हाथ से पैर तक एक सीधी खड़ी रेखा।",
            keyJointAngles = listOf("कलाई ~90°", "कोहनी 180° सीधी", "कंधा 180° ऊपर", "कमर सीधी")
        ),
        evidence = EvidenceLevel.EXPERT_PRACTICE
    )

    private fun yogaSkill(
        id: String, name: String, alt: List<String>, cat: MovementCategory, diff: DifficultyLevel,
        model: ProgrammingModel, muscles: List<String>, regions: List<BodyRegion>, pattern: MovementPattern,
        purpose: String, equip: List<String>, what: String, mech: String, why: String, doesNot: String,
        benefits: List<String>, limits: List<String>, prereq: String, cues: List<String>,
        roadmap: List<ProgressionLevelSpec>, archetype: DiagramArchetype = DiagramArchetype.MOBILITY_ARC
    ): MovementDetail = buildMovement(
        id = id, name = name, alt = alt, discipline = DisciplineId.YOGA, category = cat,
        difficulty = diff, model = model,
        primaryAbilities = if (cat == MovementCategory.SIGNATURE_ADVANCED) listOf("शरीर पर कंट्रोल", "अंतिम रेंज की मजबूती") else listOf("जोड़ों का खुलापन", "लचीलापन"),
        secondaryAbilities = listOf("साँस पर नियंत्रण", "कमर व कूल्हे की समझ"),
        muscles = muscles, regions = regions, pattern = pattern, purpose = purpose, equipment = equip, minutes = 12,
        whatIsIt = what, mechanics = mech, whyItMatters = why, doesNotImprove = doesNot,
        primaryBenefits = benefits, secondaryBenefits = listOf("मन को शांत और साँस को लयबद्ध रखता है।"),
        perfApps = listOf("योग फ्लो व संतुलन", "कंधे व कूल्हे खोलना"),
        crossApps = listOf("भारी लिफ्टिंग के बाद शरीर की जकड़न कम करता है।"),
        limitations = limits,
        misconceptions = listOf("सिर्फ ज़्यादा झुक जाने से खेल की ताकत नहीं बढ़ती; उस रेंज में शरीर पर कंट्रोल होना ज़्यादा ज़रूरी है।"),
        baselineStrength = prereq,
        jointMobility = "जोड़ों में बिना किसी चुभन के आरामदायक रेंज।",
        stabilityBalance = "शांत साँस और हल्का पेट कसाव बनाए रखें।",
        techniquePrereqs = "गहरे आसन से पहले कलाई, पीठ और कूल्हों को हल्का गरम करें।",
        previousMovements = listOf("कैट-काउ (Cat-Cow)", "डाउनवर्ड डॉग"),
        readinessTests = listOf(ReadinessTest("आराम और साँस टेस्ट", "20 सेकंड तक बिना साँस रोके आराम से आसन की शुरुआत में रुकें।", "अगर साँस रोकनी पड़ रही है, तो थोड़ा आसान रूप चुनें।")),
        steps = listOf(
            "1. शुरुआत: ${cues.getOrElse(0) { "हाथ व पैर जमीन पर स्थिर टिकाएँ और पीठ सीधी रखें।" }}",
            "2. सेटअप: हल्का कोर टाइट करें और जोड़ों को सही सीध में रखें।",
            "3. गति की शुरुआत: ${cues.getOrElse(1) { "साँस छोड़ते हुए धीरे-धीरे आसन में जाएँ, झटका न दें।" }}",
            "4. सही अभ्यास: ${cues.getOrElse(2) { "जोड़ों पर ढीले लटकने के बजाय मसल्स से हल्का सहारा बनाए रखें।" }}",
            "5. साँस: नाक से धीमी और सहज साँस लें व छोड़ें।",
            "6. शरीर की सीध: गर्दन को ढीला रखें और कमर पर ज़्यादा दबाव न आने दें।",
            "7. मुख्य स्थिति: तय समय या 6-10 साँस तक स्थिर रुकें।",
            "8. सुरक्षित समाप्ति: धीरे-धीरे उसी रास्ते वापस आएं और शरीर को ढीला छोड़ें।"
        ),
        mistakes = listOf(
            CommonMistake("साँस रोकना और गर्दन अकड़ाना", "कंधे कानों की तरफ चढ़ जाना और दांत भींचना।", "जबरदस्ती गहरा जाने की कोशिश।", "बेकार थकान व गर्दन में जकड़न बढ़ती है।", "ब्लॉक का सहारा लें या कम गहराई तक जाएँ।"),
            CommonMistake("जोड़ों पर ढीला लटकना", "मसल्स के बिना सिर्फ लिगामेंट्स के सहारे टिकना।", "अंतिम सिरे पर कमजोरी।", "जोड़ में खिंचाव या दर्द हो सकता है।", "आसपास की मसल्स को हल्का सक्रिय रखें।")
        ),
        injuryRisks = "बिना वॉर्म-अप जल्दी करने पर कलाई, घुटने या निचली कमर में चुभन हो सकती है।",
        jointLoading = if (cat == MovementCategory.SIGNATURE_ADVANCED) "मध्यम से अधिक जोइंट दबाव।" else "हल्का और आरामदायक खिंचाव।",
        fatigueConsiderations = "आसान संतुलित अभ्यास करें; भारी स्क्वैट से ठीक पहले बहुत लंबे स्ट्रेच न करें।",
        roadmap = roadmap,
        prescription = PracticePrescription(
            frequencyPerWeek = if (cat == MovementCategory.SIGNATURE_ADVANCED) "2-3 दिन/हफ्ता" else "3-5 दिन/हफ्ता (या वॉर्म-अप में)",
            volumeAndSets = if (cat == MovementCategory.SIGNATURE_ADVANCED) "3-4 सेट x 10-25 सेकंड" else "2-3 सेट x 30-60 सेकंड",
            targetEffortOrZone = "हल्का-मध्यम खिंचाव (4-6/10), कभी तेज दर्द नहीं",
            restPeriods = "45-90 सेकंड",
            weeklyProgressionRule = "साँस शांत रहे तभी 5 सेकंड समय बढ़ाएँ।",
            deloadGuidance = "थकान ज़्यादा हो तो केवल आरामदायक रूप करें।"
        ),
        visual = VisualAlignmentSpec(archetype, cues.firstOrNull() ?: "स्थिर आधार", cues.getOrElse(2) { "सही सीध और कंट्रोल" }, "धीरे वापस आना", listOf("गर्दन आराम से", "शांत साँस"))
    )

    private fun calisthenicsMove(
        id: String, name: String, alt: List<String> = emptyList(), cat: MovementCategory, diff: DifficultyLevel,
        model: ProgrammingModel, muscles: List<String>, regions: List<BodyRegion>, pattern: MovementPattern,
        purpose: String, equip: List<String>, what: String, mech: String, why: String, doesNot: String,
        benefits: List<String>, limits: List<String>, prereq: String, roadmap: List<ProgressionLevelSpec>,
        archetype: DiagramArchetype = DiagramArchetype.HORIZONTAL_LEVER,
        safetyNote: String? = null
    ): MovementDetail = buildMovement(
        id = id, name = name, alt = alt, discipline = DisciplineId.CALISTHENICS, category = cat,
        difficulty = diff, model = model,
        primaryAbilities = listOf("बॉडीवेट ताकत", "कंधे व कोर का कंट्रोल"),
        secondaryAbilities = listOf("टेंडन की मजबूती", "जोड़ों का संतुलन"),
        muscles = muscles, regions = regions, pattern = pattern, purpose = purpose, equipment = equip, minutes = 15,
        whatIsIt = what, mechanics = mech, whyItMatters = why, doesNotImprove = doesNot,
        primaryBenefits = benefits,
        secondaryBenefits = listOf("पूरे शरीर को एक साथ कसकर काम करना सिखाता है।"),
        perfApps = listOf("बॉडीवेट स्किल प्रदर्शन", "रिंग्स व बार वर्कआउट"),
        crossApps = listOf("ग्रिप और कंधे की स्थिरता बढ़ाकर भारी वज़न उठाने में मदद करता है।"),
        limitations = limits,
        misconceptions = listOf("कठिन स्टैटिक होल्ड मसल्स बढ़ाने (Hypertrophy) के लिए भारी डिप्स/पुल-अप्स जितने असरदार नहीं होते।"),
        baselineStrength = prereq,
        jointMobility = "कंधे व कोहनी बिना दर्द के पूरे खुलने चाहिए।",
        stabilityBalance = "पेट और कमर को एक सीध में (Hollow brace) कसना आना चाहिए।",
        techniquePrereqs = "सीधी-बाजू (Straight-arm) स्किल्स से पहले साधारण पुल-अप, डिप्स और प्लैंक पक्के करें।",
        previousMovements = listOf("स्कैपुलर पुल-अप", "हॉलो बॉडी होल्ड", "डेड हैंग"),
        readinessTests = listOf(ReadinessTest("टेंडन और पकड़ चेक", "पिछले आसान लेवल को 15-20s बिना कोहनी या कंधे के दर्द के रोकें।", "सीधी भुजा वाले होल्ड में टेंडन धीरे मजबूत होते हैं, इसलिए जल्दबाजी न करें।")),
        steps = listOf(
            "1. शुरुआत: बार, रिंग्स या फर्श को मज़बूती से पकड़ें।",
            "2. सेटअप: कंधों को सही जगह सेट करें (नीचे दबाएँ या आगे धकेलें) और पेट-कूल्हे कसें।",
            "3. गति की शुरुआत: बिना झटका या किक मारे धीरे से पोजिशन में आएँ।",
            "4. सही अभ्यास: शरीर की सीधी लाइन या पूरा रेंज कंट्रोल में रखें।",
            "5. साँस: पेट कसा रखते हुए छोटी, शांत साँस लें।",
            "6. शरीर की सीध: कमर नीचे न झुकने दें और न पसलियाँ बाहर निकालें।",
            "7. मुख्य स्थिति: ऊपर या होल्ड में 1-2 सेकंड स्थिर रुकें।",
            "8. सुरक्षित समाप्ति: ताकत खत्म होने से पहले नियंत्रण में नीचे उतरें।"
        ),
        mistakes = listOf(
            CommonMistake("जल्दबाजी में सीधे हाथ वाला होल्ड करना", "पूरी तैयारी से पहले प्लांच या फ्रंट लीवर ट्राई करना।", "मसल्स ताकतवर महसूस होती हैं पर टेंडन तैयार नहीं होते।", "कोहनी के अंदरूनी हिस्से (Biceps tendon) में दर्द हो सकता है।", "4-8 हफ्ते पहले बेसिक लीन और हैंग पक्के करें।"),
            CommonMistake("कंधे ढीले छोड़ना", "कंधे कानों की तरफ उचक जाना।", "कमजोरी या थकान।", "कंधे के आगे के जोड़ पर दबाव आता है।", "हल्का वर्जन चुनें जहाँ कंधे नीचे सेट रह सकें।")
        ),
        injuryRisks = "जल्दबाजी करने पर कोहनी या बाइसेप्स के टेंडन में दर्द हो सकता है।",
        jointLoading = "कंधे, कोहनी और कलाई पर लीवर जैसा भारी दबाव।",
        fatigueConsiderations = "हार्ड स्किल्स हमेशा शुरुआत में ताज़ा रहते हुए (3-5 छोटे सेट) करें।",
        criticalSafety = safetyNote,
        roadmap = roadmap,
        prescription = if (model == ProgrammingModel.STATIC_SKILL) {
            PracticePrescription("2-3 दिन/हफ्ता (ताज़ा रहते हुए)", "4-5 सेट x 6-15 सेकंड के साफ़ होल्ड", "पूरा कंट्रोल, 2-3 सेकंड की ताकत बाकी छोड़ें", "90-150 सेकंड", "2 हफ्ते साफ़ होने पर 2 सेकंड बढ़ाएँ।", "हर चौथे हफ्ते सेट्स आधे करें।")
        } else {
            PracticePrescription("2-3 दिन/हफ्ता", "3-4 सेट x 5-12 साफ़ रैप्स", "1-2 RIR, पूरा रेंज", "90-150 सेकंड", "पहले रैप्स बढ़ाएँ, फिर वज़न या कठिन रूप।", "डीलोड हफ्ते में आधे सेट करें।")
        },
        visual = VisualAlignmentSpec(archetype, "कंधे और पेट की लाइन सेट करें", "मजबूत सीधी लाइन या पूरा रेंज बनाए रखें", "नियंत्रित तरीके से नीचे आएं", listOf("कोहनी सीधी/90°+", "कंधे सेट", "कमर सीधी"))
    )

    val movements: List<MovementDetail> = listOf(
        // === 1. YOGA ===
        handstandDetail,
        yogaSkill("yog_forearm_stand", "फोरआर्म स्टैंड (Forearm Stand)", listOf("Forearm Stand", "Pincha Mayurasana", "पिंचा मयूरासन"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STATIC_SKILL,
            listOf("कंधे", "ऊपरी पीठ", "कोर"), listOf(BodyRegion.SHOULDERS, BodyRegion.THORACIC_SPINE, BodyRegion.CORE), MovementPattern.INVERSION_BALANCE,
            "कोहनियों और अग्रबाहु पर उल्टा संतुलन, कलाई पर बिना दबाव के।", listOf("मैट", "योगा ब्लॉक"),
            "दोनों अग्रबाहु (फोरआर्म्स) को जमीन पर टिकाकर शरीर को ऊपर सीधा खड़ा करना।", "त्रिकोण जैसा मजबूत आधार मिलता है जिससे कलाई पर भार नहीं आता।", "ऊपरी पीठ और कंधों की स्थिरता बढ़ाता है।", "कंधे की मसल्स बढ़ाने के लिए भारी प्रेस की जगह नहीं ले सकता।",
            listOf("बिना कलाई दर्द के उल्टा संतुलन", "कंधे व पीठ खोलने में मदद"), listOf("इसके लिए कंधे और पीठ का खुला होना ज़रूरी है"), "60s डॉल्फिन प्लैंक।",
            listOf("दोनों फोरआर्म्स समानांतर जमीन पर रखें।", "पैर चलते हुए कूल्हे कंधों के ऊपर लाएँ।", "धीरे से एक पैर उठाकर सीधा ऊपर ले जाएँ।"),
            standardRoadmap("फोरआर्म स्टैंड", ProgrammingModel.STATIC_SKILL, "डॉल्फिन पोज़ होल्ड", "दीवार सहारे डॉल्फिन होल्ड", "चेस्ट-टू-वॉल फोरआर्म स्टैंड", "फ्रीस्टैंडिंग फोरआर्म स्टैंड"),
            DiagramArchetype.INVERSION_STACK
        ),
        yogaSkill("yog_wheel_pose", "चक्रासन / व्हील पोज़ (Wheel Pose)", listOf("Wheel Pose", "Urdhva Dhanurasana", "Chakrasana"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.INTERMEDIATE, ProgrammingModel.MOBILITY,
            listOf("पीठ", "कंधे", "ग्लूट्स", "क्वाड्स"), listOf(BodyRegion.THORACIC_SPINE, BodyRegion.SHOULDERS, BodyRegion.HIPS, BodyRegion.WRISTS), MovementPattern.END_RANGE_MOBILITY,
            "हाथों और पैरों पर पूरे शरीर का धनुषाकार खिंचाव।", listOf("मैट"),
            "पीठ के बल लेटकर हाथ और पैर दबाते हुए पूरे शरीर को ऊपर मेहराब की तरह उठाना।", "इसमें कूल्हों और ऊपरी पीठ का खुलना ज़रूरी है ताकि कमर न दबे।", "लंबे समय बैठने से जकड़ी पीठ और सीने को खोलता है।", "ताकत या मसल्स के लिए रोइंग/प्रेस का विकल्प नहीं।",
            listOf("पीठ, कंधे और कूल्हों को एक साथ खोलता है", "पीछे की ताकत का समन्वय"), listOf("कंधे सख्त होने पर निचली कमर में चुभन हो सकती है"), "ब्रिज पोज़ + कंधों का खुलना।",
            listOf("लेटकर घुटने मोड़ें और हथेलियाँ कानों के पास टिकाएँ।", "पैरों और हाथों से जोर लगाकर शरीर ऊपर उठाएँ।", "ग्लूट्स हल्के कसें और सीना आगे की ओर खोलें।"),
            standardRoadmap("चक्रासन", ProgrammingModel.MOBILITY, "ब्रिज पोज़ व टी-स्पाइन एक्सटेंशन", "हाथ ऊँचे ब्लॉक पर रखकर चक्रासन", "फर्श पर चक्रासन (3x20-30s)", "सीधे पैरों से गहरा चक्रासन")
        ),
        yogaSkill("yog_king_pigeon", "राजकपोतासन / किंग पिजन (King Pigeon Pose)", listOf("King Pigeon Pose", "Eka Pada Rajakapotasana"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.MOBILITY,
            listOf("हिप फ्लेक्सर्स", "ग्लूट्स", "क्वाड्स", "कंधे"), listOf(BodyRegion.HIPS, BodyRegion.THORACIC_SPINE, BodyRegion.SHOULDERS), MovementPattern.END_RANGE_MOBILITY,
            "कूल्हों और आगे की जांघों का गहरा खुलापन।", listOf("मैट", "योगा स्ट्रैप"),
            "एक पैर आगे मोड़कर दूसरे पैर को पीछे ले जाकर सिर की ओर पकड़ना।", "कूल्हों के घूमने और कंधों के पीछे जाने की पूरी परीक्षा है।", "जांघों और कूल्हों की गहरी जकड़न दूर करता है।", "पैरों की ताकत बढ़ाने वाला व्यायाम नहीं।",
            listOf("गहरा हिप और क्वाड खिंचाव"), listOf("कूल्हे तिरछे करने से आगे के घुटने पर दबाव आ सकता है"), "90/90 हिप और लो लंज।",
            listOf("आगे वाले पैर को आरामदायक कोण पर रखकर कूल्हे सीधे रखें।", "पीछे का घुटना धीरे मोड़ें।", "शुरुआत में पैर पकड़ने के लिए बेल्ट/स्ट्रैप का इस्तेमाल करें।"),
            standardRoadmap("किंग पिजन", ProgrammingModel.MOBILITY, "90/90 हिप व काउच स्ट्रेच", "स्ट्रैप की मदद से पिजन पोज़", "सीधे हाथ से पैर पकड़ने का अभ्यास", "पूर्ण राजकपोतासन")
        ),
        yogaSkill("yog_eight_angle", "अष्टावक्रासन (Eight-Angle Pose)", listOf("Eight-Angle Pose", "Astavakrasana"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STATIC_SKILL,
            listOf("ट्राइसेप्स", "चेस्ट", "कंधे", "ऑब्लिक्स"), listOf(BodyRegion.WRISTS, BodyRegion.SHOULDERS, BodyRegion.CORE, BodyRegion.HIPS), MovementPattern.INVERSION_BALANCE,
            "एक बाजू के ऊपर पैरों को फँसाकर हाथों पर तिरछा संतुलन बनाना।", listOf("मैट"),
            "हाथों पर पुश-अप जैसी स्थिति में एक बाजू के चारों ओर पैरों को लॉक करके उठाना।", "जांघों से बाजू को दबाने से शरीर हवा में लॉक हो जाता है।", "कोर ट्विस्ट और बाजू पर संतुलन सिखाता है।", "चेस्ट या बाजू की मसल्स के लिए डिप्स जितना प्रभावी नहीं।",
            listOf("कोर कंट्रोल व बाजू का संतुलन"), listOf("अच्छे हैमस्ट्रिंग खिंचाव और कोहनी कंट्रोल की ज़रूरत"), "साइड क्रो पोज़।",
            listOf("एक पैर को उसी तरफ के कंधे के ऊपर रखें।", "दोनों टखने फँसाकर जांघों को जोर से दबाएँ।", "आगे झुककर पैर बगल में सीधे फैलाएँ।"),
            standardRoadmap("अष्टावक्रासन", ProgrammingModel.STATIC_SKILL, "लिजर्ड लंज", "टखने क्रॉस करके उठाने का अभ्यास", "सीधे पैरों से अष्टावक्रासन", "आसान फ्लो मूवमेंट")
        ),
        yogaSkill("yog_peacock", "मयूरासन (Peacock Pose)", listOf("Peacock Pose", "Mayurasana"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STATIC_SKILL,
            listOf("फोरआर्म्स", "बाइसेप्स", "कोर", "पीठ"), listOf(BodyRegion.WRISTS, BodyRegion.ELBOWS, BodyRegion.CORE), MovementPattern.INVERSION_BALANCE,
            "कोहनियों को पेट पर टिकाकर शरीर को जमीन के समानांतर रोकना।", listOf("मैट"),
            "हथेलियाँ उल्टी रखकर कोहनियों के सहारे पूरा शरीर हवा में सीधा साधना।", "कलाई और कोहनियों पर काफी दबाव आता है, इसलिए धीरे-धीरे सीखें।", "कलाई, फोरआर्म और पूरे शरीर का तनाव सहना सिखाता है।", "अगर कलाई या कोहनी में दर्द हो तो यह आसन न करें।",
            listOf("फोरआर्म मजबूती व पूरा शरीर कसना"), listOf("कलाई पर अधिक दबाव"), "रिवर्स-पााम टेबलटॉप झुकाव।",
            listOf("घुटनों के बल बैठकर उंगलियां पैरों की तरफ घुमाकर हाथ टिकाएँ।", "कोहनियां पेट के नीचे पसलियों के पास लगाएँ।", "धीरे से आगे झुकें और पैर उठाएँ।"),
            standardRoadmap("मयूरासन", ProgrammingModel.STATIC_SKILL, "उल्टी हथेली से आगे झुकना", "घुटने मोड़कर पैर हल्के उठाना", "पैर फैलाकर होल्ड", "सीधे पैर मयूरासन")
        ),
        yogaSkill("yog_firefly", "टिट्टिभासन (Firefly Pose)", listOf("Firefly Pose", "Tittibhasana"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STATIC_SKILL,
            listOf("हैमस्ट्रिंग्स", "हिप फ्लेक्सर्स", "कोर", "ट्राइसेप्स"), listOf(BodyRegion.HAMSTRINGS, BodyRegion.HIPS, BodyRegion.WRISTS, BodyRegion.CORE), MovementPattern.INVERSION_BALANCE,
            "हाथों के आगे दोनों पैर सीधे फैलाकर संतुलन बनाना।", listOf("मैट", "योगा ब्लॉक"),
            "जांघों को बाजू के पीछे टिकाकर पूरे शरीर को हाथों पर उठाना और पैर सीधे आगे करना।", "इसके लिए कमर व हैमस्ट्रिंग्स का लचीला होना ज़रूरी है।", "हिप कम्प्रेशन और कंधों की मजबूती बढ़ाता है।", "पैरों की बड़ी ताकत बढ़ाने वाला व्यायाम नहीं।",
            listOf("पाइक लचीलापन व सीधी बाजू संतुलन"), listOf("हैमस्ट्रिंग सख्त होने पर पीछे गिरने का डर"), "मलासन व पैनकेक स्ट्रेच।",
            listOf("पैरों के बीच कंधे अंदर लाकर हाथ पीछे टिकाएँ।", "जांघों को ऊपरी बाजू पर बैठाएँ और कूल्हे हल्के उठाएँ।", "धीरे से दोनों पैर आगे की ओर सीधे करें।"),
            standardRoadmap("टिट्टिभासन", ProgrammingModel.STATIC_SKILL, "मलासन + पाइक स्ट्रेच", "ब्लॉक पर हाथ रखकर घुटने मोड़ होल्ड", "एक-एक पैर सीधा करना", "सीधे पैर टिट्टिभासन")
        ),
        yogaSkill("yog_side_crow", "पार्श्व बकासन / साइड क्रो (Side Crow)", listOf("Side Crow", "Parsva Bakasana"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.INTERMEDIATE, ProgrammingModel.STATIC_SKILL,
            listOf("ऑब्लिक्स", "चेस्ट", "ट्राइसेप्स", "कंधे"), listOf(BodyRegion.WRISTS, BodyRegion.CORE, BodyRegion.THORACIC_SPINE), MovementPattern.INVERSION_BALANCE,
            "धड़ को घुमाकर मुड़ी हुई बाजू पर शरीर को तिरछा रोकना।", listOf("मैट"),
            "उकड़ूं बैठकर कमर घुमाना और दोनों घुटनों को बाजू के सहारे टिकाकर पैर हवा में उठाना।", "कंधों को थोड़ा आगे झुकाने से पैर आसानी से हवा में आ जाते हैं।", "संतुलन और पेट के साइड हिस्से को मजबूत करता है।", "मसल्स बनाने का प्रमुख अभ्यास नहीं।",
            listOf("कोर का घुमाव व शरीर का केंद्र संभालना"), listOf("कलाई गरम न करने पर तनाव"), "क्रो पोज़ (बकासन)।",
            listOf("पैर मिलाकर बैठें और कमर एक तरफ घुमाएँ।", "दोनों हाथ कंधों के बराबर जमीन पर टिकाएँ।", "कंधे आगे झुकाएँ जब तक पैर ऊपर न उठ जाएँ।"),
            standardRoadmap("साइड क्रो", ProgrammingModel.STATIC_SKILL, "चेयर ट्विस्ट व क्रो लीन", "ब्लॉक सहारे साइड क्रो", "बिना सहारे साइड क्रो (10-20s)", "सीधे पैरों का फ्लो")
        ),

        // YOGA CATEGORY B: TRANSFERABLE
        yogaSkill("yog_downward_dog", "डाउनवर्ड डॉग (Downward Dog)", listOf("Downward Dog", "Adho Mukha Svanasana", "अधोमुख श्वानासन"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.MOBILITY,
            listOf("कंधे", "पिंडली", "हैमस्ट्रिंग्स", "रीढ़"), listOf(BodyRegion.SHOULDERS, BodyRegion.CALVES, BodyRegion.HAMSTRINGS, BodyRegion.THORACIC_SPINE), MovementPattern.END_RANGE_MOBILITY,
            "हाथ-पैर टिकाकर उल्टा 'V' बनाकर कंधे, कमर और पिंडली खोलना।", listOf("बिना सामान / मैट"),
            "हथेलियों और पंजों पर V आकार बनाकर कूल्हे ऊपर-पीछे ले जाना।", "घुटने हल्के मोड़ने से पीठ सीधी रहती है और कंधे अच्छे से खुलते हैं।", "स्क्वैट, दौड़ या ओवरहेड प्रेस से पहले बढ़िया वॉर्म-अप।", "भारी ताकत या मसल्स नहीं बढ़ाता।",
            listOf("कंधे और पिंडली की जकड़न दूर करता है", "हैंडस्टैंड की आसान शुरुआत"), listOf("घुटने जबरदस्ती सीधे रखने से कमर गोल होती है"), "साधारण प्लैंक।",
            listOf("हाथ व घुटने जमीन पर रखकर उंगलियां फैलाएँ।", "कूल्हे ऊपर और पीछे उठाएँ।", "पीठ लंबी रखने के लिए घुटने हल्के मुड़े रख सकते हैं।"),
            standardRoadmap("डाउनवर्ड डॉग", ProgrammingModel.MOBILITY, "एक-एक एड़ी चलाना", "मुड़े घुटने लंबा बैक डाउनवर्ड डॉग", "सीधा डाउनवर्ड डॉग (30-60s)", "एक-पैर ऊपर पाइक")
        ),
        yogaSkill("yog_low_lunge", "लो लंज (Low Lunge)", listOf("Low Lunge", "Anjaneyasana", "अंजनेयासन"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.MOBILITY,
            listOf("हिप फ्लेक्सर्स", "क्वाड्स", "ग्लूट्स"), listOf(BodyRegion.HIPS, BodyRegion.ANKLES), MovementPattern.END_RANGE_MOBILITY,
            "कूल्हे के आगे का हिस्सा (Hip Flexor) और टखना खोलना।", listOf("मैट / घुटने के नीचे पैड"),
            "एक पैर आगे रखकर पीछे का घुटना जमीन पर टिकाना और कूल्हे धीरे आगे ले जाना।", "पीछे का ग्लूट कसने से निचली कमर पर दबाव आए बिना कूल्हा खुलता है।", "दौड़ने और गहरे स्क्वैट की क्षमता सुधारता है।", "बिना वज़न के पैरों में बड़ी मसल्स नहीं बनाता।",
            listOf("बैठने से हुई कूल्हों की जकड़न खोलता है", "टखने की मोबिलिटी बढ़ाता है"), listOf("कमर पीछे झुकाने से सही जगह खिंचाव नहीं मिलता"), "आरामदायक हाफ-नीलिंग।",
            listOf("एक पैर 90 डिग्री आगे रखें और पीछे के घुटने के नीचे कपड़ा/पैड लगाएँ।", "पीछे वाले ग्लूट को हल्का कसें।", "कमर सीधी रखते हुए हल्का आगे बढ़ें।"),
            standardRoadmap("लो लंज", ProgrammingModel.MOBILITY, "सीधे खड़े पेल्विक टिल्ट", "ग्लूट कसकर लो लंज (30-45s)", "हाथ ऊपर उठाकर लो लंज", "बेंच पर पैर रखकर गहरा स्ट्रेच")
        ),
        yogaSkill("yog_malasana", "मलासन / गहरा स्क्वैट (Malasana Deep Squat)", listOf("Malasana", "Deep Squat", "Garland Pose"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.MOBILITY,
            listOf("टखने", "कूल्हे", "अंदरूनी जांघ"), listOf(BodyRegion.ANKLES, BodyRegion.HIPS, BodyRegion.KNEES, BodyRegion.THORACIC_SPINE), MovementPattern.SQUAT,
            "नीचे पूरा बैठकर टखने, घुटने और कूल्हों को आराम से खोलना।", listOf("बिना सामान"),
            "बिना वज़न के एड़ियों पर पूरा नीचे बैठना और छाती सीधी रखना।", "यह टखने, घुटने और कूल्हों की संयुक्त गति सुधारता है।", "बार्बेल बैक स्क्वैट में गहराई लाने में सीधी मदद करता है।", "मसल्स के आकार (Hypertrophy) के लिए भारी वज़न चाहिए।",
            listOf("डीप स्क्वैट पोजिशन आरामदायक बनाता है", "कमर व कूल्हों की जकड़न खोलता है"), listOf("एड़ी उठ रही हो तो शुरुआत में नीचे पतली किताब/प्लेट रख सकते हैं"), "कुर्सी पर बैठ-उठ पाना।",
            listOf("पैर कंधों से थोड़े चौड़े रखकर पंजे हल्के बाहर रखें।", "आराम से पूरा नीचे बैठें।", "कोहनियों से घुटनों को हल्का बाहर दबाकर छाती सीधी करें।"),
            standardRoadmap("मलासन डीप स्क्वैट", ProgrammingModel.MOBILITY, "एड़ी के नीचे हल्का सहारा देकर बैठना", "सीधे पैर पूरा मलासन (30-60s)", "बैठे-बैठे सीना घुमाना", "2 मिनट सहज गहरा स्क्वैट")
        ),
        yogaSkill("yog_cat_cow", "कैट-काउ / मार्जरी आसन (Cat-Cow)", listOf("Cat-Cow", "Marjaryasana", "Bitilasana"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.MOBILITY,
            listOf("रीढ़ की हड्डी", "स्कैपुला"), listOf(BodyRegion.THORACIC_SPINE, BodyRegion.SCAPULA, BodyRegion.NECK), MovementPattern.END_RANGE_MOBILITY,
            "साँस के साथ रीढ़ को हल्का मोड़ना और फैलाना।", listOf("बिना सामान / मैट"),
            "हाथ-घुटनों पर रहकर साँस लेते हुए कमर हल्की नीचे और छोड़ते हुए ऊपर गोल करना।", "पूरी पीठ को बिना झटके चिकनाई और गति देता है।", "किसी भी वर्कआउट से पहले बेहतरीन वॉर्म-अप।", "ताकत या मसल्स बढ़ाने के लिए नहीं।",
            listOf("रीढ़ की जकड़न कम करता है और पोस्चर सुधरता है"), listOf("लोडेड एक्सरसाइज नहीं है"), "कोई नहीं।",
            listOf("कलाई कंधों के नीचे व घुटने कूल्हों के नीचे रखें।", "साँस अंदर लेते हुए छाती हल्की आगे निकालें।", "साँस छोड़ते हुए फर्श धकेलें और पीठ ऊपर गोल करें।"),
            standardRoadmap("कैट-काउ", ProgrammingModel.MOBILITY, "हल्की छोटी गति", "साँस के साथ 8-12 बार", "सिर्फ ऊपरी पीठ पर फोकस", "कंधे व पीठ का कंट्रोल")
        ),
        yogaSkill("yog_thoracic_rotation", "ऊपरी पीठ का घुमाव (Thoracic Rotation)", listOf("Thoracic Rotation", "Open Book"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.MOBILITY,
            listOf("ऊपरी पीठ", "ऑब्लिक्स", "कंधे"), listOf(BodyRegion.THORACIC_SPINE, BodyRegion.SHOULDERS, BodyRegion.SCAPULA), MovementPattern.END_RANGE_MOBILITY,
            "निचली कमर स्थिर रखकर ऊपरी पीठ को घुमाना।", listOf("मैट"),
            "करवट से लेटकर या बैठकर छाती को एक तरफ से दूसरी तरफ घुमाना।", "कूल्हे स्थिर रखने से खिंचाव सिर्फ ऊपरी पीठ में आता है।", "हाथ ऊपर उठाने, तैरने और मुक्के/किक में बहुत मददगार।", "कोर की बड़ी ताकत बढ़ाने वाला अभ्यास नहीं।",
            listOf("सख्त ऊपरी पीठ खोलता है और कंधों पर दबाव घटाता है"), listOf("कमर न घूमे, सिर्फ सीना घुमाएँ"), "कोई नहीं।",
            listOf("एड़ियों पर बैठें या घुटने मोड़कर करवट लें।", "एक हाथ सिर के पीछे रखें।", "सीने को धीरे-धीरे छत की तरफ खोलें।"),
            standardRoadmap("थोरेसिक रोटेशन", ProgrammingModel.MOBILITY, "करवट लेटकर ओपन बुक", "घुटनों के बल हाथ सिर पीछे (8-10/तरफ)", "रुककर गहरी साँस के साथ घुमाव", "ओवरहेड स्क्वैट तैयारी")
        ),
        yogaSkill("yog_warrior_iii", "वीरभद्रासन 3 / वॉरियर 3 (Warrior III)", listOf("Warrior III", "Virabhadrasana III"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.INTERMEDIATE, ProgrammingModel.BALANCE_COORDINATION,
            listOf("ग्लूट्स", "हैमस्ट्रिंग्स", "पैर", "रीढ़"), listOf(BodyRegion.HIPS, BodyRegion.GLUTES, BodyRegion.HAMSTRINGS, BodyRegion.ANKLES, BodyRegion.FEET), MovementPattern.SINGLE_LEG,
            "एक पैर पर खड़े होकर संतुलन और कूल्हे की पकड़ बनाना।", listOf("बिना सामान"),
            "एक पैर पर खड़े होकर शरीर आगे झुकाना और दूसरा पैर पीछे हवा में सीधा रखना।", "एक तरफ झुकने से रोकने के लिए कूल्हे और पैर के पंजे मेहनत करते हैं।", "स्प्रिंट और एक-पैर RDL में सीधा फायदा मिलता है।", "पैरों में बड़ी मसल्स बनाने के लिए पर्याप्त वज़न नहीं।",
            listOf("एक-पैर संतुलन और कूल्हों की मजबूती"), listOf("वज़न न होने से ताकत की सीमा"), "30s एक पैर पर खड़ा रहना।",
            listOf("एक पैर पर हल्का घुटना ढीला रखकर खड़े हों।", "कूल्हे सीधे रखते हुए आगे झुकें और पीछे का पैर उठाएँ।", "हाथ सामने या कमर पर रखें।"),
            standardRoadmap("वॉरियर III", ProgrammingModel.BALANCE_COORDINATION, "दीवार के सहारे अभ्यास", "कमर पर हाथ रखकर (15-20s/पैर)", "हाथ आगे करके पूरा होल्ड (30s/पैर)", "डम्बल सिंगल-लेग RDL")
        ),
        yogaSkill("yog_bridge_pose", "सेतुबंधासन / ग्लूट ब्रिज (Bridge Pose)", listOf("Bridge Pose", "Glute Bridge", "Setu Bandhasana"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.MOBILITY,
            listOf("ग्लूट्स", "हैमस्ट्रिंग्स", "पीठ"), listOf(BodyRegion.GLUTES, BodyRegion.HIPS, BodyRegion.THORACIC_SPINE), MovementPattern.HIP_HINGE,
            "पीठ के बल लेटकर कूल्हे ऊपर उठाना और ग्लूट्स सक्रिय करना।", listOf("मैट"),
            "घुटने मोड़कर एड़ियों से फर्श दबाते हुए कूल्हों को सीधी लाइन तक उठाना।", "निचली कमर मोड़े बिना ग्लूट्स से ताकत लगाना सिखाता है।", "स्क्वैट, डेडलिफ्ट और स्प्रिंट से पहले बढ़िया वॉर्म-अप।", "एक बार आसान लगने के बाद बिना वज़न बड़ी मसल्स नहीं बनतीं।",
            listOf("आसान ग्लूट सक्रियता व आगे का हल्का खिंचाव"), listOf("बड़ी ग्रोथ के लिए वज़न वाला हिप थ्रस्ट चुनें"), "कोई नहीं।",
            listOf("पीठ के बल लेटें, पैर कूल्हों जितने चौड़े रखें।", "पेट हल्का कसकर एड़ियों से जोर लगाएँ व कूल्हे उठाएँ।", "ऊपर ग्लूट्स कसें और आराम से नीचे लाएँ।"),
            standardRoadmap("ग्लूट ब्रिज", ProgrammingModel.MOBILITY, "दोनों पैरों से ब्रिज (2x12)", "ऊपर 2s रुककर ब्रिज (3x15)", "एक-पैर ब्रिज", "बार्बेल हिप थ्रस्ट")
        ),
        yogaSkill("yog_childs_pose", "बालासन / चाइल्ड्स पोज़ (Child's Pose)", listOf("Child's Pose", "Balasana"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.BREATHING,
            listOf("पीठ", "कूल्हे", "टखने"), listOf(BodyRegion.THORACIC_SPINE, BodyRegion.HIPS, BodyRegion.ANKLES), MovementPattern.RESPIRATORY,
            "वर्कआउट के बाद रीढ़ को आराम देना और शांत साँस लेना।", listOf("बिना सामान / मैट"),
            "एड़ियों पर बैठकर माथा नीचे टिकाना और हाथ आगे फैलाकर पीठ में साँस लेना।", "शरीर और नर्वस सिस्टम को तुरंत शांत करता है।", "रविवार रिकवरी और कूल-डाउन का सबसे सरल अभ्यास।", "इससे ताकत या मसल्स नहीं बढ़तीं।",
            listOf("दिमागी और शारीरिक थकान कम करता है"), listOf("घुटने में तकलीफ हो तो नीचे तकिया रख लें"), "कोई नहीं।",
            listOf("घुटने हल्के चौड़े रखकर एड़ियों पर बैठें।", "हाथ आगे फैलाएँ और सिर आराम से नीचे टिकाएँ।", "पीठ में हवा भरते हुए 6-12 धीमी साँस लें।"),
            standardRoadmap("बालासन", ProgrammingModel.BREATHING, "तकिए के सहारे बालासन", "सामान्य बालासन (1-2 मिनट)", "साइड में हाथ ले जाकर लैट स्ट्रेच", "नियमित रिकवरी ब्रीदिंग")
        ),

        // === 2. CALISTHENICS ===
        calisthenicsMove("cal_planche", "फुल प्लांच (Full Planche)", listOf("Full Planche", "Planche Hold"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.EXPERT, ProgrammingModel.STATIC_SKILL,
            listOf("कंधे आगे का हिस्सा", "चेस्ट", "फोरआर्म्स", "कोर"), listOf(BodyRegion.SHOULDERS, BodyRegion.WRISTS, BodyRegion.ELBOWS, BodyRegion.CORE), MovementPattern.HORIZONTAL_PUSH,
            "सीधी बाजू पर पूरे शरीर को हवा में जमीन के समानांतर रोकना।", listOf("फर्श / पैरालेट्स"),
            "हाथों पर कंधे बहुत आगे झुकाकर पूरे शरीर को हवा में सीधा समानांतर साधना।", "इसमें आगे के कंधों और बाइसेप्स टेंडन पर अत्यधिक ताकत लगती है।", "कैलिस्थेनिक्स पुशिंग का सर्वोच्च कौशल।", "चेस्ट मसल्स बढ़ाने के लिए कठिन व धीमा तरीका; जल्दबाजी से कलाई/कोहनी में चोट लग सकती है।",
            listOf("अद्भुत सीधी-बाजू धक्का देने की ताकत"), listOf("सीखने में वर्षों लगते हैं; कलाई व कोहनी पर भारी तनाव"),
            "60s प्लांच लीन + 15 डिप्स + हैंडस्टैंड पुश-अप।",
            standardRoadmap("फुल प्लांच", ProgrammingModel.STATIC_SKILL, "प्लांच लीन और स्यूडो-प्लांच पुश-अप्स", "टक प्लांच (3-5x10-15s)", "एडवांस टक व स्ट्रैडल प्लांच", "फुल प्लांच (5-10s)"),
            DiagramArchetype.HORIZONTAL_LEVER,
            safetyNote = "कोहनी के टेंडन और कलाई को बचाने के लिए महीनों तक प्लांच लीन पक्के करने के बाद ही पैर हवा में उठाएँ।"
        ),
        calisthenicsMove("cal_front_lever", "फ्रंट लीवर (Front Lever)", listOf("Front Lever"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.EXPERT, ProgrammingModel.STATIC_SKILL,
            listOf("लैट्स", "पीठ", "कोर", "फोरआर्म्स"), listOf(BodyRegion.SHOULDERS, BodyRegion.SCAPULA, BodyRegion.CORE, BodyRegion.ELBOWS), MovementPattern.HORIZONTAL_PULL,
            "बार के नीचे सीधी बाजू से शरीर को आसमान की तरफ सीधा लटकाना।", listOf("पुल-अप बार / रिंग्स"),
            "लटककर कोहनियाँ सीधी रखते हुए शरीर को ऊपर खींचकर जमीन के समानांतर रोकना।", "लैट्स और पेट को एक साथ पूरा जोर लगाना पड़ता है।", "खींचने की ताकत और कोर स्थिरता की पहचान।", "पीठ की चौड़ाई के लिए वेटेड पुल-अप या रोइंग ज़्यादा सरल और असरदार हैं।",
            listOf("कमर की सीधी पकड़ और लैट्स की भारी ताकत"), listOf("लंबे पैरों वालों के लिए और कठिन; कोहनी पर ध्यान दें"),
            "12+ साफ़ पुल-अप्स; 30s हॉलो होल्ड।",
            standardRoadmap("फ्रंट लीवर", ProgrammingModel.STATIC_SKILL, "स्कैपुलर पुल व टक आइसोमेट्रिक", "टक फ्रंट लीवर (4x10-15s)", "सिंगल-लेग व स्ट्रैडल लीवर", "फुल फ्रंट लीवर (5-12s)"),
            DiagramArchetype.HORIZONTAL_LEVER
        ),
        calisthenicsMove("cal_human_flag", "ह्यूमन फ्लैग (Human Flag)", listOf("Human Flag"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.EXPERT, ProgrammingModel.STATIC_SKILL,
            listOf("ऑब्लिक्स", "लैट्स", "कंधे", "कोर"), listOf(BodyRegion.SHOULDERS, BodyRegion.CORE, BodyRegion.SCAPULA), MovementPattern.CARRY_ANTI_ROTATION,
            "सीधे खंभे (पोल) को पकड़कर झंडे की तरह बगल में सीधा रुकना।", listOf("खड़ा पोल / स्टॉल बार"),
            "नीचे वाले हाथ से धक्का और ऊपर वाले हाथ से खींचकर शरीर को हवा में क्षैतिज रोकना।", "एक साथ कंधे का पुश-पुल और पेट की साइड ताकत मांगता है।", "पूरे शरीर के बगल वाले कंट्रोल का बेहतरीन कौशल।", "रोज़ाना मसल्स ग्रोथ के लिए उपयोगी नहीं।",
            listOf("साइड कोर व कंधे की गजब स्थिरता"), listOf("सही खंभा और महीनों की तैयारी ज़रूरी"),
            "15 पुल-अप्स; 30s हैंडस्टैंड; 45s साइड प्लैंक।",
            standardRoadmap("ह्यूमन फ्लैग", ProgrammingModel.STATIC_SKILL, "साइड प्लैंक व पोल सपोर्ट", "वर्टिकल व टक फ्लैग", "स्ट्रैडल फ्लैग (5-8s)", "फुल ह्यूमन फ्लैग")
        ),
        calisthenicsMove("cal_iron_cross", "आयरन क्रॉस (Iron Cross)", listOf("Iron Cross", "Ring Cross"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.EXPERT, ProgrammingModel.STATIC_SKILL,
            listOf("लैट्स", "चेस्ट", "कंधे", "बाइसेप्स टेंडन"), listOf(BodyRegion.SHOULDERS, BodyRegion.ELBOWS, BodyRegion.SCAPULA), MovementPattern.VERTICAL_PULL,
            "रिंग्स पर दोनों हाथ बगल में सीधे फैलाकर शरीर लटकाना।", listOf("जिमनास्टिक रिंग्स"),
            "दोनों भुजाएं T आकार में सीधी फैलाकर रिंग्स के बीच लटकना।", "कंधे और कोहनी के जोड़ पर अत्यधिक दबाव आता है।", "विशुद्ध जिमनास्टिक रिंग कौशल।", "बिना कई साल के अभ्यास के करने से बाइसेप्स टेंडन फटने का खतरा रहता है।",
            listOf("रिंग्स पर सीधी बाजू की चरम ताकत"), listOf("चोट का सबसे अधिक जोखिम; सामान्य फिटनेस के लिए बिल्कुल ज़रूरी नहीं"),
            "30s RTO रिंग सपोर्ट + मसल-अप + भारी वेटेड पुल-अप।",
            standardRoadmap("आयरन क्रॉस", ProgrammingModel.STATIC_SKILL, "RTO रिंग सपोर्ट", "बैंड सहारे हल्का क्रॉस होल्ड", "आंशिक क्रॉस प्रेस", "पूर्ण आयरन क्रॉस (3-5s)"),
            DiagramArchetype.HORIZONTAL_LEVER,
            safetyNote = "चेतावनी: जल्दबाजी में बाइसेप्स टेंडन या कंधे में गंभीर चोट लग सकती है। इसे सालों की तैयारी से ही सीखें।"
        ),
        calisthenicsMove("cal_one_arm_pullup", "एक-हाथ पुल-अप (One-Arm Pull-up)", listOf("One-Arm Pull-up", "OAP"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.EXPERT, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("लैट्स", "बाइसेप्स", "फोरआर्म्स"), listOf(BodyRegion.SHOULDERS, BodyRegion.ELBOWS, BodyRegion.SCAPULA), MovementPattern.VERTICAL_PULL,
            "केवल एक हाथ से पूरे शरीर को बार के ऊपर खींचना।", listOf("पुल-अप बार / रिंग"),
            "एक हाथ से लटककर बिना किक मारे ठोड़ी को बार के ऊपर ले जाना।", "एक तरफ के लैट्स, बाइसेप्स और ग्रिप पर पूरा लोड आता है।", "खींचने की ताकत का शिखर।", "पीठ बढ़ाने के लिए दोनों हाथों के वेटेड पुल-अप ज़्यादा सुरक्षित हैं।",
            listOf("एक तरफ की चरम खींचने की ताकत"), listOf("ज़्यादा करने से कोहनी में दर्द हो सकता है"),
            "शरीर के 50-60% वज़न के साथ साफ़ वेटेड पुल-अप।",
            standardRoadmap("वन-आर्म पुल-अप", ProgrammingModel.STRENGTH_HYPERTROPHY, "भारी वेटेड पुल-अप", "आर्चर व तौलिया सहारे पुल-अप", "एक हाथ से धीरे नीचे आना (5-8s)", "पूरा एक-हाथ पुल-अप")
        ),
        calisthenicsMove("cal_hspu", "हैंडस्टैंड पुश-अप (Handstand Push-up)", listOf("Handstand Push-up", "HSPU"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("कंधे", "ट्राइसेप्स", "ऊपरी पीठ"), listOf(BodyRegion.SHOULDERS, BodyRegion.WRISTS, BodyRegion.ELBOWS), MovementPattern.VERTICAL_PUSH,
            "उल्टा हैंडस्टैंड में नीचे जाकर वापस ऊपर धकेलना।", listOf("खाली दीवार", "पैरालेट्स"),
            "दीवार के सहारे हैंडस्टैंड में सिर को हल्के से जमीन तक लाना और ऊपर पुश करना।", "शरीर के पूरे वज़न को कंधों से ऊपर धकेलना सिखाता है।", "कंधे और ट्राइसेप्स को बहुत मजबूत बनाता है।", "फर्श पर रेंज थोड़ी छोटी रहती है और गर्दन पर ध्यान देना ज़रूरी है।",
            listOf("कंधों और ट्राइसेप्स की भारी बॉडीवेट ताकत"), listOf("नियंत्रण न रहे तो गर्दन दब सकती है"),
            "45s चेस्ट-टू-वॉल हैंडस्टैंड + ओवरहेड प्रेस।",
            standardRoadmap("हैंडस्टैंड पुश-अप", ProgrammingModel.STRENGTH_HYPERTROPHY, "पाइक पुश-अप और बॉक्स पाइक पुश-अप", "दीवार सहारे धीरे नीचे आना (3-5s)", "दीवार सहारे पूरा HSPU", "फ्रीस्टैंडिंग / गहरा HSPU")
        ),
        calisthenicsMove("cal_muscle_up", "मसल-अप (Muscle-up)", listOf("Muscle-up", "Bar Muscle-up", "Ring Muscle-up"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("लैट्स", "चेस्ट", "ट्राइसेप्स", "फोरआर्म्स"), listOf(BodyRegion.SHOULDERS, BodyRegion.ELBOWS, BodyRegion.WRISTS), MovementPattern.VERTICAL_PULL,
            "बार के नीचे तेज़ पुल-अप करके ऊपर डिप्स पोजिशन में आ जाना।", listOf("पुल-अप बार / रिंग्स"),
            "सीने तक जोर से पुल-अप करके कलाइयां पलटना और सीधे ऊपर पुश करना।", "खींचने और धक्का देने की ताकत को एक तेज़ लय में जोड़ता है।", "शानदार ऊपरी शरीर का कौशल।", "मसल्स के लिए अलग-अलग वेटेड पुल-अप व डिप्स ज़्यादा बेहतर हैं।",
            listOf("ऊपरी शरीर की तेज़ ताकत और ट्रांजिशन कंट्रोल"), listOf("एक-एक करके कोहनी ऊपर चढ़ाना (Chicken wing) कंधे के लिए गलत है"),
            "10-12 चेस्ट-टू-बार पुल-अप + 12 गहरे डिप्स।",
            standardRoadmap("मसल-अप", ProgrammingModel.STRENGTH_HYPERTROPHY, "ऊँचे पुल-अप्स व गहरे डिप्स", "बैंड सहारे ट्रांजिशन अभ्यास", "साफ़ रिंग मसल-अप", "साफ़ बार मसल-अप")
        ),
        calisthenicsMove("cal_back_lever", "बैक लीवर (Back Lever)", listOf("Back Lever"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STATIC_SKILL,
            listOf("कंधे आगे", "चेस्ट", "बाइसेप्स टेंडन", "पीठ"), listOf(BodyRegion.SHOULDERS, BodyRegion.ELBOWS, BodyRegion.CORE), MovementPattern.HORIZONTAL_PUSH,
            "बार के नीचे पेट जमीन की तरफ रखकर सीधी बाजू पर लटकना।", listOf("रिंग्स / बार"),
            "पीछे से घूमकर जमीन की ओर मुँह रखते हुए शरीर को समानांतर सीधा रोकना।", "कंधे के आगे का खिंचाव और बाइसेप्स टेंडन पर लोड देता है।", "रिंग स्ट्रेन्थ के लिए शुरुआती लीवर कौशल।", "बिना जर्मन हैंग पक्के किए करने से कोहनी में दर्द हो सकता है।",
            listOf("कंधे का लचीलापन व सीधी बाजू की पकड़"), listOf("कोहनी के टेंडन पर सीधा दबाव"),
            "30s जर्मन हैंग + 20s इनवर्टेड हैंग।",
            standardRoadmap("बैक लीवर", ProgrammingModel.STATIC_SKILL, "जर्मन हैंग और स्किन-द-कैट", "टक बैक लीवर (3x10-15s)", "स्ट्रैडल बैक लीवर", "फुल बैक लीवर (5-10s)")
        ),
        calisthenicsMove("cal_v_sit", "वी-सिट (V-Sit)", listOf("V-Sit"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.ADVANCED, ProgrammingModel.STATIC_SKILL,
            listOf("हिप फ्लेक्सर्स", "पेट", "क्वाड्स", "ट्राइसेप्स"), listOf(BodyRegion.CORE, BodyRegion.HIPS, BodyRegion.HAMSTRINGS, BodyRegion.SHOULDERS), MovementPattern.INVERSION_BALANCE,
            "हथेलियों पर शरीर उठाकर सीधे पैरों को ऊपर 'V' आकार में रोकना।", listOf("फर्श / पैरालेट्स"),
            "सीधी बाजू पर टिककर दोनों सीधे पैरों को 45 डिग्री से ऊपर सीने के करीब लाना।", "कूल्हों के जोड़ने की ताकत और हैमस्ट्रिंग के लचीलेपन की जरूरत होती है।", "कोर कम्प्रेशन का उच्चतम स्तर।", "पेट की सामान्य मसल्स के लिए हैंगिंग लेग रेज़ पर्याप्त हैं।",
            listOf("जबरदस्त कोर कम्प्रेशन व बाजू दबाव"), listOf("शुरुआत में जांघ में हल्की ऐंठन आ सकती है"),
            "30s साफ़ L-Sit + सीटेड पाइक लिफ्ट्स।",
            standardRoadmap("वी-सिट", ProgrammingModel.STATIC_SKILL, "L-Sit और पाइक कम्प्रेशन", "टक V-Sit लिफ्ट", "45° लो V-Sit होल्ड", "हाई V-Sit (75°+)")
        ),
        calisthenicsMove("cal_pistol_squat", "पिस्टल स्क्वैट (Pistol Squat)", listOf("Pistol Squat", "Single-Leg Squat"), MovementCategory.SIGNATURE_ADVANCED, DifficultyLevel.INTERMEDIATE, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("क्वाड्स", "ग्लूट्स", "टखने"), listOf(BodyRegion.KNEES, BodyRegion.ANKLES, BodyRegion.HIPS, BodyRegion.FEET), MovementPattern.SINGLE_LEG,
            "एक पैर आगे सीधा फैलाकर दूसरे पैर पर पूरा नीचे बैठकर उठना।", listOf("बिना सामान"),
            "एक पैर हवा में सीधा रखते हुए दूसरे पैर पर एड़ी बिना उठाए पूरा बैठना और खड़े होना।", "घुटने की ताकत, संतुलन और टखने के लचीलेपन को एक साथ परखता है।", "बिना जिम के पैरों को मजबूत बनाने का बढ़िया कौशल।", "ज़्यादा मसल्स ग्रोथ के लिए बुल्गारियन स्प्लिट स्क्वैट ज़्यादा स्थिर और बेहतर है।",
            listOf("एक पैर की ताकत और टखने का संतुलन"), listOf("टखना सख्त होने पर नीचे कमर मुड़ जाती है"),
            "डीप स्क्वैट होल्ड + 12 स्प्लिट स्क्वैट।",
            standardRoadmap("पिस्टल स्क्वैट", ProgrammingModel.STRENGTH_HYPERTROPHY, "बॉक्स पर एक-पैर स्क्वैट", "एड़ी के नीचे प्लेट व हल्का वज़न सामने पकड़कर", "खंभे/पट्टी के सहारे पिस्टल", "पूरा पिस्टल स्क्वैट"),
            DiagramArchetype.SQUAT_DEPTH
        ),

        // CALISTHENICS CATEGORY B: TRANSFERABLE
        calisthenicsMove("cal_scapular_pullup", "स्कैपुलर पुल-अप (Scapular Pull-up)", listOf("Scapular Pull-up"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("लोअर ट्रैप्स", "लैट्स"), listOf(BodyRegion.SCAPULA, BodyRegion.SHOULDERS), MovementPattern.VERTICAL_PULL,
            "कोहनियाँ बिना मोड़े सिर्फ कंधों को नीचे खींचकर ऊपर उठना।", listOf("पुल-अप बार"),
            "बार से लटककर हाथ सीधे रखते हुए कंधों की हड्डियों (Scapulae) को नीचे व पास खींचना।", "सही पुल-अप की शुरुआत और कंधों की सुरक्षा सिखाता है।", "हर पुल-अप सीखने वाले के लिए ज़रूरी नींव।", "बाइसेप्स या पूरी पीठ बढ़ाने का मुख्य अभ्यास नहीं।",
            listOf("पुल-अप की सही शुरुआत व कंधे की सुरक्षा"), listOf("छोटी गति; वॉर्म-अप की तरह करें"),
            "20s डेड हैंग।",
            standardRoadmap("स्कैपुलर पुल-अप", ProgrammingModel.STRENGTH_HYPERTROPHY, "एक्टिव और ढीला हैंग बदलना", "स्कैपुलर पुल-अप्स (3x8-10)", "ऊपर 2s रुकना", "वेटेड स्कैपुलर पुल-अप्स")
        ),
        calisthenicsMove("cal_hollow_hold", "हॉलो बॉडी होल्ड (Hollow Body Hold)", listOf("Hollow Body Hold"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.STATIC_SKILL,
            listOf("पेट", "डीप कोर", "हिप फ्लेक्सर्स"), listOf(BodyRegion.CORE, BodyRegion.HIPS), MovementPattern.CARRY_ANTI_ROTATION,
            "पीठ के बल लेटकर निचली कमर जमीन से सटाए रखना और हाथ-पैर हल्के उठाना।", listOf("बिना सामान / मैट"),
            "कमर के नीचे कोई गैप छोड़े बिना कंधे व पैर जमीन से कुछ इंच ऊपर रोकना।", "पसलियों और कूल्हों को आपस में कसना सिखाता है।", "हैंडस्टैंड, लीवर और ओवरहेड प्रेस के लिए सीधा फायदेमंद।", "पेट की मसल्स बढ़ाने के लिए रोलआउट या लेग रेज़ ज़्यादा अच्छे हैं।",
            listOf("कोर कसने की असली तकनीक सिखाता है"), listOf("पैर ज़्यादा नीचे ले जाने पर कमर तुरंत उठने लगती है"),
            "डेड बग 3x10।",
            standardRoadmap("हॉलो बॉडी होल्ड", ProgrammingModel.STATIC_SKILL, "घुटने मुड़े हॉलो होल्ड", "एक पैर सीधा हॉलो होल्ड", "पूरे हाथ पीछे सीधे हॉलो होल्ड (3x30-45s)", "हॉलो बॉडी रॉक्स")
        ),
        calisthenicsMove("cal_arch_hold", "आर्च बॉडी होल्ड / सुपरमैन (Arch Body Hold)", listOf("Arch Body Hold", "Superman Hold"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.STATIC_SKILL,
            listOf("पीठ", "ग्लूट्स", "कंधे पीछे"), listOf(BodyRegion.THORACIC_SPINE, BodyRegion.GLUTES), MovementPattern.HIP_HINGE,
            "पेट के बल लेटकर छाती और सीधे पैर हल्के ऊपर उठाना।", listOf("बिना सामान / मैट"),
            "पेट के बल लेटकर ग्लूट्स और पीठ से सीना व पैर थोड़ा ऊपर रोकना।", "सामने वाले कोर (हॉलो) के उलट पीछे की मांसपेशियों को संतुलित करता है।", "कमर व कंधे की सहनशक्ति बढ़ाता है।", "भारी रोइंग या RDL की जगह नहीं लेता।",
            listOf("पीठ के निचले व ऊपरी हिस्से की सहनशक्ति"), listOf("गर्दन ऊपर न मोड़ें, नजर नीचे रखें"),
            "हल्का कोबरा पोज़।",
            standardRoadmap("आर्च होल्ड", ProgrammingModel.STATIC_SKILL, "प्रोन Y/T उठाना", "हाथ मोड़कर आर्च होल्ड (3x20-30s)", "हाथ आगे सीधे आर्च होल्ड", "आर्च रॉक्स")
        ),
        calisthenicsMove("cal_dead_hang", "डेड हैंग (Dead Hang)", listOf("Dead Hang"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.STATIC_SKILL,
            listOf("फोरआर्म्स", "ग्रिप", "कंधे"), listOf(BodyRegion.WRISTS, BodyRegion.SHOULDERS, BodyRegion.SCAPULA), MovementPattern.VERTICAL_PULL,
            "सीधे हाथों से बार पकड़कर लटकना ताकि ग्रिप और कंधे मजबूत हों।", listOf("पुल-अप बार"),
            "बार को मजबूती से पकड़कर शरीर को नीचे सीधा लटकाना।", "उंगलियों की पकड़ मजबूत करता है और कंधों को आराम से खोलता है।", "सभी पुल-अप्स और भारी डेडलिफ्ट के लिए उपयोगी।", "पीठ की मसल्स बढ़ाने के लिए काफी नहीं।",
            listOf("मजबूत ग्रिप व कंधे का आराम"), listOf("कंधे में हल्की चुभन लगे तो कंधे थोड़े सक्रिय रखें"),
            "कोई नहीं।",
            standardRoadmap("डेड हैंग", ProgrammingModel.STATIC_SKILL, "पैर हल्के जमीन पर टिकाकर लटकना", "3x30-45s दो हाथों से लटकना", "60s आराम से हैंग", "एक हाथ से दूसरे हाथ पर वज़न बदलना")
        ),
        calisthenicsMove("cal_hanging_knee_raise", "हैंगिंग नी रेज़ (Hanging Knee Raise)", listOf("Hanging Knee Raise"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.BEGINNER, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("पेट (Abs)", "हिप फ्लेक्सर्स", "फोरआर्म्स"), listOf(BodyRegion.CORE, BodyRegion.HIPS), MovementPattern.CARRY_ANTI_ROTATION,
            "बार से लटककर घुटने सीने की तरफ मोड़कर लाना।", listOf("पुल-अप बार"),
            "लटकते हुए बिना झूला झूले कूल्हों को आगे मोड़कर घुटने ऊपर उठाना।", "कूल्हे अंदर मोड़ने से सीधा पेट की मसल्स पर काम होता है।", "सीधे पैर उठाने (Leg Raise) का आसान पहला कदम।", "बिना कमर मोड़े सिर्फ पैर हिलाने से पेट पर असर कम होता है।",
            listOf("आसान पेट और ग्रिप की ताकत"), listOf("कभी-कभी पेट से पहले हाथ की पकड़ थकती है"),
            "30s डेड हैंग।",
            standardRoadmap("हैंगिंग नी रेज़", ProgrammingModel.STRENGTH_HYPERTROPHY, "जमीन पर रिवर्स क्रंच", "साफ़ हैंगिंग नी रेज़ (3x10-15)", "ऊपर 1s रुककर नी रेज़", "हैंगिंग लेग रेज़")
        ),
        calisthenicsMove("cal_hanging_leg_raise", "हैंगिंग लेग रेज़ (Hanging Leg Raise)", listOf("Hanging Leg Raise", "Toes to Bar"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.INTERMEDIATE, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("पेट (Abs)", "ऑब्लिक्स", "ग्रिप"), listOf(BodyRegion.CORE, BodyRegion.HIPS, BodyRegion.HAMSTRINGS), MovementPattern.CARRY_ANTI_ROTATION,
            "बार से लटककर दोनों सीधे पैर ऊपर बार की तरफ ले जाना।", listOf("पुल-अप बार"),
            "लटकते हुए बिना झटका खाए सीधे पैरों को कम से कम 90° या बार तक ऊपर उठाना।", "लंबा लीवर होने से पेट की मांसपेशियों को बेहतरीन तनाव मिलता है।", "एब्स बनाने (Hypertrophy) के सबसे अच्छे बॉडीवेट अभ्यासों में एक।", "सीधे पैर उठाने के लिए हैमस्ट्रिंग्स का थोड़ा लचीला होना ज़रूरी है।",
            listOf("पेट की मसल्स का बेहतरीन विकास"), listOf("शरीर को आगे-पीछे न झुलाएँ"),
            "12 हैंगिंग नी रेज़।",
            standardRoadmap("हैंगिंग लेग रेज़", ProgrammingModel.STRENGTH_HYPERTROPHY, "नी रेज़ + जमीन पर लेग लिफ्ट", "90° सीधे पैर उठाना", "पूरा टोज़-टू-बार (3x8-12)", "धीरे-धीरे नीचे लाना (3s)")
        ),
        calisthenicsMove("cal_ring_support", "रिंग सपोर्ट होल्ड (Ring Support Hold)", listOf("Ring Support Hold"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.INTERMEDIATE, ProgrammingModel.STATIC_SKILL,
            listOf("चेस्ट", "ट्राइसेप्स", "कंधे"), listOf(BodyRegion.SHOULDERS, BodyRegion.ELBOWS, BodyRegion.SCAPULA), MovementPattern.VERTICAL_PUSH,
            "जिमनास्टिक रिंग्स के ऊपर कोहनियाँ सीधी रखकर टिकना।", listOf("जिमनास्टिक रिंग्स"),
            "रिंग्स के ऊपर हाथ सीधे रखकर कंधों को नीचे दबाते हुए स्थिर रुकना।", "हिलते हुए रिंग्स कंधे की हर छोटी मसल को सक्रिय कर देते हैं।", "रिंग डिप्स और मसल-अप से पहले अनिवार्य।", "मसल्स के आकार के लिए डिप्स ज़्यादा बेहतर हैं।",
            listOf("कंधे और कोहनी की अद्भुत स्थिरता"), listOf("ज़्यादा कांपने लगे तो पैर जमीन पर हल्के टिकाएँ"),
            "30s समानांतर बार सपोर्ट।",
            standardRoadmap("रिंग सपोर्ट", ProgrammingModel.STATIC_SKILL, "पैर के सहारे रिंग सपोर्ट", "सामान्य रिंग सपोर्ट (3x20-30s)", "हथेलियां 45° बाहर घुमाकर (RTO)", "90° RTO सपोर्ट")
        ),
        calisthenicsMove("cal_dips", "पैरेलल बार डिप्स (Dips)", listOf("Dips", "Parallel Bar Dips"), MovementCategory.TRANSFERABLE_FUNCTIONAL, DifficultyLevel.INTERMEDIATE, ProgrammingModel.STRENGTH_HYPERTROPHY,
            listOf("चेस्ट निचला हिस्सा", "ट्राइसेप्स", "आगे के कंधे"), listOf(BodyRegion.SHOULDERS, BodyRegion.ELBOWS), MovementPattern.VERTICAL_PUSH,
            "दो बार के बीच नीचे उतरकर शरीर को ऊपर धकेलना।", listOf("डिप बार / रिंग्स"),
            "कोहनियाँ 90° मोड़कर नियंत्रण में नीचे आना और हाथ सीधे होने तक ऊपर धक्का देना।", "हल्का आगे झुकने से चेस्ट पर और सीधा रहने से ट्राइसेप्स पर ज़्यादा असर पड़ता है।", "ऊपरी शरीर की ताकत व मसल्स के लिए बेहतरीन।", "ऊपरी पीठ के लिए इसे रोइंग/पुल-अप्स के साथ संतुलित करें।",
            listOf("चेस्ट और ट्राइसेप्स के लिए शानदार मसल्स ग्रोथ"), listOf("बहुत ज़्यादा नीचे जाने पर कंधे में खिंचाव हो सकता है"),
            "15 साफ़ पुश-अप्स।",
            standardRoadmap("डिप्स", ProgrammingModel.STRENGTH_HYPERTROPHY, "बेंच डिप्स व धीरे नीचे आना", "साफ़ बार डिप्स (3x6-10)", "3x12-15 साफ़ डिप्स", "वेटेड डिप्स")
        )
    )
}
