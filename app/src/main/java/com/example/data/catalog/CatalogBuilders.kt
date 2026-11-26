package com.example.data.catalog

import com.example.domain.model.*

object CatalogDisciplines {
    val all: List<DisciplineInfo> = listOf(
        DisciplineInfo(
            id = DisciplineId.YOGA,
            name = "योग (Yoga)",
            tagline = "जॉइंट कंट्रोल, उल्टा बैलेंस और शांत साँस",
            overview = "योग शरीर के लचीलेपन, जोड़ों के कंट्रोल और साँस के तालमेल को बढ़ाता है। यह स्ट्रेंथ ट्रेनिंग के साथ रिकवरी और पोस्चर सुधारने में मदद करता है।",
            signatureTitle = "एडवांस आसन व बैलेंस",
            signatureDescription = "हैंडस्टैंड, व्हील पोज़ और आर्म बैलेंस जैसे विशेष आसन। इनमें उच्च कौशल लगता है; ये सीधे मसल्स बढ़ाने का मुख्य तरीका नहीं हैं।",
            transferableTitle = "उपयोगी बेसिक अभ्यास",
            transferableDescription = "डाउनवर्ड डॉग, डीप स्क्वैट और थोरैसिक रोटेशन जैसे आसन जो लिफ्टिंग व दौड़ के लिए कमर, कूल्हों और कंधों को तैयार करते हैं।",
            skillBenefits = "ओवरहेड कंट्रोल, कलाई-कंधे की सहनशक्ति, रीढ़ की गतिशीलता और शांत ध्यान बढ़ाता है।",
            limitations = "सिर्फ योग करने से अधिकतम मसल्स (हाइपरट्रॉफी) या भारी ताकत नहीं बनती; इसके लिए रेजिस्टेंस ट्रेनिंग ज़रूरी है।",
            hypertrophyIntegrationNote = "आसान योग अभ्यास रिकवरी के दिन या वर्कआउट के वॉर्म-अप में रखें; कठिन आर्म-बैलेंस भारी अपर-बॉडी दिन से पहले न करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.CALISTHENICS,
            name = "कैलिस्थेनिक्स (Calisthenics)",
            tagline = "शरीर के वज़न पर पूरा कंट्रोल और ताकत",
            overview = "बाहरी वज़न के बिना, केवल शरीर के कोण और लीवरेज को बदलकर जबरदस्त ऊपरी ताकत और कोर स्थिरता विकसित करता है।",
            signatureTitle = "विशेष बॉडीवेट स्किल्स",
            signatureDescription = "प्लैंच, फ्रंट लीवर, आयरन क्रॉस और वन-आर्म पुल-अप। इनके लिए जोड़ों व टेंडन की महीनों की तैयारी ज़रूरी होती है।",
            transferableTitle = "ताकत व मसल्स के उपयोगी अभ्यास",
            transferableDescription = "स्कैपुलर पुल-अप, हॉलो होल्ड, डेड हैंग और डिप्स कंधे की सेहत, ग्रिप और ऊपरी शरीर की मसल्स बढ़ाते हैं।",
            skillBenefits = "शरीर के वज़न के अनुपात में बेहतरीन ताकत, कंधे की स्थिरता और सख्त कोर कंट्रोल देता है।",
            limitations = "पैरों की मसल्स बढ़ाने के लिए केवल बॉडीवेट सीमित पड़ता है; सीधी बाजू वाली स्किल्स कोहनी के टेंडन पर ज़्यादा दबाव डालती हैं।",
            hypertrophyIntegrationNote = "वेटेड डिप्स और पुल-अप्स से मसल्स बनाएं; लीवर होल्ड्स को हफ्ते में कम मात्रा में और बिल्कुल तरोताज़ा होकर करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.STRENGTH,
            name = "स्ट्रेंथ ट्रेनिंग (Strength)",
            tagline = "मसल्स बनाने और अधिकतम ताकत का मुख्य आधार",
            overview = "यह पूरे सिस्टम की नींव है। सही रेंज में वज़न धीरे-धीरे बढ़ाने से मांसपेशियाँ, हड्डियाँ और ताकत लगातार बढ़ती हैं।",
            signatureTitle = "मुख्य कंपाउंड लिफ्ट्स",
            signatureDescription = "स्क्वैट, डेडलिफ्ट, बेंच प्रेस, वेटेड पुल-अप और ओवरहेड प्रेस — जिनसे साफ और मापने लायक प्रगति होती है।",
            transferableTitle = "सपोर्ट व टिश्यू मजबूती अभ्यास",
            signatureLabelFixPlaceholder(false),
            skillBenefits = "मसल्स ग्रोथ और ताकत बढ़ाने का सबसे पक्का और मापने योग्य तरीका।",
            limitations = "सिर्फ भारी वज़न उठाने से तेज़ स्प्रिंट स्पीड, फुर्ती या दिल का स्टैमिना अपने आप नहीं बढ़ता।",
            hypertrophyIntegrationNote = "मसल्स प्राथमिकता: हर मुख्य मांसपेशी के लिए हफ्ते में 10-20 अच्छे सेट्स करें और 1-3 रैप्स बाकी रहने पर सेट रोकें।"
        ),
        DisciplineInfo(
            id = DisciplineId.CARDIO,
            name = "कार्डियो व स्टैमिना (Cardio)",
            tagline = "दिल-फेफड़ों की ताकत बिना मसल्स खोए",
            overview = "सही रफ़्तार का कार्डियो (ज़ोन 2) सेट्स के बीच रिकवरी तेज करता है और दिल की सेहत बढ़ाता है, वह भी मसल्स ग्रोथ को नुकसान पहुँचाए बिना।",
            signatureTitle = "मुख्य एंड्योरेंस लक्ष्य",
            signatureDescription = "5 किमी, 10 किमी, हाफ/फुल मैराथन या लंबी साइकिल। बहुत ज़्यादा दौड़ पैरों की मसल्स बढ़ाने में रुकावट बन सकती है।",
            transferableTitle = "आसान व सुरक्षित कार्डियो तरीके",
            transferableDescription = "ज़ोन 2 रनिंग, तेज वॉक/ढलान वॉक और साइकिलिंग जो जोड़ों को कम थकाकर स्टैमिना बढ़ाते हैं।",
            skillBenefits = "साँस फूलना कम करता है, आराम के समय हार्ट रेट बेहतर रखता है और वर्कआउट के बीच जल्दी रिकवरी देता है।",
            limitations = "हफ्ते में बहुत ज़्यादा कठिन दौड़ करने से पैरों की ताकत और मसल्स ग्रोथ धीमी पड़ सकती है।",
            hypertrophyIntegrationNote = "ज़्यादातर कार्डियो आसान रफ़्तार (बात करने लायक) या साइकिल/वॉक पर रखें, और भारी लेग-डे से अलग दिन करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.SPRINT_POWER,
            name = "स्प्रिंट व तेज़ी (Sprint & Power)",
            tagline = "विस्फोटक ताकत, फुर्ती और तेज़ रफ़्तार",
            overview = "कम समय में ज़्यादा से ज़्यादा ताकत पैदा करना सिखाता है। इसके लिए पूरे आराम और तरोताज़ा दिमाग-शरीर की ज़रूरत होती है।",
            signatureTitle = "मुख्य स्पीड व जंप स्किल्स",
            signatureDescription = "एक्सेलरेशन स्प्रिंट, टॉप स्पीड स्प्रिंट, बाउंडिंग और डेप्थ जंप। इन्हें बिना थकान के करना ज़रूरी है।",
            transferableTitle = "शुरुआती स्पीड व ड्रिल्स",
            transferableDescription = "ए-स्किप, वॉल स्प्रिंट ड्रिल, एंकलिंग, पोगो जंप और जंप स्क्वैट जो टखने और पैरों को सुरक्षित तरीके से स्प्रिंट के लिए तैयार करते हैं।",
            skillBenefits = "शरीर में स्प्रिंग जैसी उछाल, तेज़ शुरुआती कदम और असली एथलेटिक स्पीड विकसित करता है।",
            limitations = "इसे थकाने वाली कसरत की तरह नहीं करना चाहिए; थकान में तेज़ भागने से हैमस्ट्रिंग खिंचने का खतरा होता है।",
            hypertrophyIntegrationNote = "स्प्रिंट सेशन छोटे (15-25 मिनट) रखें, हर प्रयास के बीच 2-4 मिनट आराम दें और भारी स्क्वैट से 48 घंटे दूर रखें।",
            safetyWarning = "नए लोगों को या कम तैयारी (थकान) वाले दिन 100% तेज़ स्प्रिंट या ऊँचा डेप्थ जंप कभी न करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.MOBILITY,
            name = "मोबिलिटी व लचीलापन",
            tagline = "दर्द-मुक्त रेंज और सक्रिय जॉइंट कंट्रोल",
            overview = "सिर्फ शरीर खींचना ही काफी नहीं, बल्कि उस बढ़ी हुई रेंज में मांसपेशियों का कंट्रोल होना ही असली मोबिलिटी है।",
            signatureTitle = "एडवांस लचीलापन स्किल्स",
            signatureDescription = "फ्रंट स्प्लिट, मिडिल स्प्लिट, पैनकेक, जेफरसन कर्ल और जर्मन हैंग। इनमें महीनों के धैर्य की ज़रूरत होती है।",
            transferableTitle = "रोज़ाना के उपयोगी जॉइंट अभ्यास",
            transferableDescription = "दीवार तक घुटना (टखना), 90/90 हिप, शोल्डर व हिप CARs — जो तुरंत स्क्वैट और ओवरहेड लिफ्ट आसान बनाते हैं।",
            skillBenefits = "जोड़ों में फ्री मूवमेंट, बेहतर पोस्चर और लिफ्टिंग के दौरान कम जकड़न देता है।",
            limitations = "बिना ताकत के अत्यधिक ढीलेपन से परफॉर्मेंस नहीं बढ़ती; भारी जंप से ठीक पहले बहुत लंबी स्ट्रेचिंग पावर कम कर सकती है।",
            hypertrophyIntegrationNote = "कसरत से पहले डायनामिक मूवमेंट (CARs) करें और गहरी स्ट्रेचिंग वर्कआउट के बाद या आराम के दिन रखें।"
        ),
        DisciplineInfo(
            id = DisciplineId.CORE,
            name = "कोर ट्रेनिंग (Core)",
            tagline = "कमर की सुरक्षा, संतुलन और पेट की मजबूती",
            overview = "पेट, कमर और कूल्हों के चारों तरफ मजबूत सिलेंडर बनाता है ताकि निचले और ऊपरी शरीर के बीच ताकत सही से ट्रांसफर हो।",
            signatureTitle = "एडवांस कोर स्किल्स",
            signatureDescription = "ड्रैगन फ्लैग, एल-सिट, वी-सिट और हैंगिंग विंडशील्ड वाइपर्स।",
            transferableTitle = "बेसिक और जरूरी कोर अभ्यास",
            transferableDescription = "एब व्हील, पैलॉफ प्रेस, कोपनहेगन प्लैंक, डेड बग और सूटकेस कैरी — जो भारी लिफ्टिंग व दौड़ को सुरक्षित बनाते हैं।",
            skillBenefits = "स्क्वैट व प्रेस में कमर को स्थिर रखता है और पेट की मसल्स को मजबूत व सुडौल बनाता है।",
            limitations = "बिना वज़न या प्रोग्रेशन के सैकड़ों क्रंचेस करने से ज़्यादा ताकत या सुरक्षा नहीं मिलती।",
            hypertrophyIntegrationNote = "हफ्ते में 2-3 बार 6-15 रैप्स रेंज में एब व्हील, लेग रेज़ और कैरी अभ्यास करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.BALANCE_AGILITY,
            name = "बैलेंस व फुर्ती (Balance & Agility)",
            tagline = "संतुलन, दिशा बदलना और सुरक्षित लैंडिंग",
            overview = "ताकत को असली खेल-कूद से जोड़ता है — पैरों की पकड़, एक पैर पर स्थिरता और अचानक रुककर दिशा बदलना सिखाता है।",
            signatureTitle = "विशेष संतुलन व फुर्ती स्किल्स",
            signatureDescription = "स्लैकलाइन, वन-आर्म हैंडस्टैंड, दिशा बदलने की ड्रिल्स और रोटेशनल लैंडिंग।",
            transferableTitle = "उपयोगी कंट्रोल अभ्यास",
            transferableDescription = "सिंगल-लेग RDL, हॉप-एंड-स्टिक, स्टेप-डाउन और लेटरल स्केटर — जो घुटने व टखने का कंट्रोल बढ़ाते हैं।",
            skillBenefits = "जोड़ों की सजगता, सुरक्षित लैंडिंग और फुटवर्क की तेज़ी बढ़ाता है।",
            limitations = "अस्थिर गद्दों (वॉबल बोर्ड) पर खड़े होने से मसल्स या असली स्प्रिंट स्पीड नहीं बढ़ती।",
            hypertrophyIntegrationNote = "हल्के लेग-डे की शुरुआत में 5-10 मिनट ताज़ा दिमाग से लैंडिंग और बैलेंस ड्रिल्स करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.PREHAB,
            name = "जॉइंट सुरक्षा व प्रीहैब (Prehab)",
            tagline = "टेंडन की मजबूती और जोड़ों की लंबी उम्र",
            overview = "हैमस्ट्रिंग, घुटने, टखने, पिंडली और रोटेटर कफ जैसे नाजुक हिस्सों की लोड सहने की क्षमता धीरे-धीरे बढ़ाता है। यह चोट से बचाव में मददगार है, लेकिन किसी बीमारी या चोट का इलाज नहीं है।",
            signatureTitle = "विशेष टिश्यू कंडीशनिंग अभ्यास",
            signatureDescription = "नॉर्डिक कर्ल, कोपनहेगन प्लैंक, भारी काफ़ रेज़ और हॉप-एंड-स्टिक।",
            transferableTitle = "आसान जॉइंट सपोर्ट अभ्यास",
            transferableDescription = "वॉल स्लाइड, फुट इंट्रिंसिक्स, टखने की रेंज और हैमस्ट्रिंग ब्रिज।",
            skillBenefits = "धीरे-धीरे टेंडन और छोटे स्टेबलाइज़र मसल्स की सहनशक्ति बढ़ाता है।",
            limitations = "सामान्य प्रीहैब सक्रिय चोट का इलाज (रिहैब) नहीं है; लगातार दर्द होने पर डॉक्टर या फिजियो से जांच कराएं।",
            hypertrophyIntegrationNote = "वॉर्म-अप या वर्कआउट के अंत में 2-3 छोटे प्रीहैब अभ्यास जोड़ें।",
            safetyWarning = "यह अभ्यास टिश्यू को मजबूत करते हैं, पर 100% चोट न लगने की गारंटी नहीं देते और मेडिकल इलाज का विकल्प नहीं हैं।"
        ),
        DisciplineInfo(
            id = DisciplineId.OLYMPIC_LIFTING,
            name = "ओलंपिक लिफ्टिंग (Olympic)",
            tagline = "तेज़ बारबेल पावर और शरीर का तालमेल",
            overview = "स्नैच और क्लीन एंड जर्क जैसी लिफ्ट्स पूरे शरीर के एक साथ विस्फोटक विस्तार और संतुलन की परीक्षा लेती हैं।",
            signatureTitle = "मुख्य ओलंपिक लिफ्ट्स",
            signatureDescription = "स्नैच, क्लीन एंड जर्क, स्क्वैट क्लीन और स्प्लिट जर्क — जिनके लिए ऊँची मोबिलिटी व सही कोचिंग चाहिए।",
            transferableTitle = "आसान विस्फोटक विकल्प",
            transferableDescription = "ट्रैप-बार जंप, केटलबेल स्विंग, मेडिसिन बॉल थ्रो और डंबल पुश प्रेस — कम तकनीक में बराबर एथलेटिक पावर देते हैं।",
            skillBenefits = "ऊर्ध्वाधर (Vertical) पावर और पूरे शरीर के तेज़ तालमेल को निखारता है।",
            limitations = "फुल स्नैच/क्लीन सीखने में लंबा समय लगता है और शुद्ध मसल्स ग्रोथ के लिए ये ज़रूरी नहीं हैं।",
            hypertrophyIntegrationNote = "प्रतियोगिता न हो तो मसल्स के लिए स्क्वैट/RDL रखें और पावर के लिए केटलबेल स्विंग या जंप स्क्वैट चुनें।",
            safetyWarning = "फुल स्नैच और क्लीन एंड जर्क के लिए कंधे, कमर व टखने की पूरी मोबिलिटी और सही तकनीक ज़रूरी है।"
        ),
        DisciplineInfo(
            id = DisciplineId.MARTIAL_ARTS,
            name = "मार्शल आर्ट्स व मूवमेंट",
            tagline = "कमर का घुमाव, फुर्तीले कदम और तालमेल",
            overview = "शैडो बॉक्सिंग, फुटवर्क और कंट्रोल्ड किक्स जैसे सुरक्षित सोलो मूवमेंट अभ्यास को कॉन्टैक्ट स्पैरिंग से अलग रखता है।",
            signatureTitle = "विशेष किक व कॉम्बिनेशन",
            signatureDescription = "स्पिनिंग हुक किक, हाई राउंडहाउस, बटरफ्लाई किक और एडवांस बॉक्सिंग कॉम्बो।",
            transferableTitle = "फिटनेस व फुटवर्क ड्रिल्स",
            transferableDescription = "शैडो बॉक्सिंग, फुटवर्क, स्प्रॉल्स, हिप एस्केप्स और रिएक्शन ड्रिल्स — बेहतरीन कंडीशनिंग के लिए।",
            skillBenefits = "हाथ-पैर-आँख का तालमेल, कमर का घुमाव और बिना बोर हुए बढ़िया कार्डियो देता है।",
            limitations = "अकेले अभ्यास करना असली मुकाबले या सेल्फ-डिफेंस कोचिंग की जगह नहीं लेता।",
            hypertrophyIntegrationNote = "कार्डियो वाले दिन 10-15 मिनट शैडो बॉक्सिंग या फुटवर्क करना मजेदार और सुरक्षित है।",
            safetyWarning = "पार्टनर के साथ टेकडाउन, स्पैरिंग या हवा में कलाबाजी केवल गद्देदार मैट और योग्य कोच की देखरेख में ही करें।"
        ),
        DisciplineInfo(
            id = DisciplineId.BREATHING,
            name = "साँस व आराम (Breathing)",
            tagline = "नर्वस सिस्टम को शांत करना और गहरी रिकवरी",
            overview = "धीमी नाक की साँस और लंबी साँस छोड़ने से शरीर भारी कसरत के बाद तुरंत तनाव-मुक्त होकर रिकवरी मोड में आता है।",
            signatureTitle = "विशेष प्राणायाम व तकनीक",
            signatureDescription = "डायाफ्राम ब्रीदिंग, अनुलोम-विलोम, स्लो प्राणायाम और शांत साँस छोड़ना।",
            transferableTitle = "रोज़ाना के सरल अभ्यास",
            transferableDescription = "धीमी नाक से साँस, होठ सिकोड़कर साँस छोड़ना और लेटकर रिलैक्सिंग ब्रीदिंग।",
            skillBenefits = "दिल की धड़कन शांत करता है, नींद और रिकवरी सुधारता है।",
            limitations = "साँस के अभ्यास मसल्स नहीं बनाते; ज़्यादा देर साँस रोककर रिकॉर्ड बनाना सेहत का लक्ष्य नहीं है।",
            hypertrophyIntegrationNote = "वर्कआउट खत्म होने पर या सोने से पहले 5-10 मिनट करें।",
            safetyWarning = "साँस रोकने से पहले कभी तेज़-तेज़ गहरी साँस (Hyperventilation) न लें और पानी में या पानी के पास साँस कभी न रोकें।"
        )
    )

    fun byId(id: DisciplineId): DisciplineInfo = all.first { it.id == id }
}

private fun signatureLabelFixPlaceholder(dummy: Boolean): String =
    "एक-पैर के अभ्यास व जॉइंट सपोर्ट (बुल्गारियन स्प्लिट स्क्वैट, नॉर्डिक कर्ल, काफ़ रेज़, फेस पुल) जो शरीर को संतुलित और मजबूत रखते हैं।"

internal fun buildMovement(
    id: String,
    name: String,
    alt: List<String> = emptyList(),
    discipline: DisciplineId,
    category: MovementCategory,
    difficulty: DifficultyLevel,
    model: ProgrammingModel,
    primaryAbilities: List<String>,
    secondaryAbilities: List<String>,
    muscles: List<String>,
    regions: List<BodyRegion>,
    pattern: MovementPattern,
    purpose: String,
    equipment: List<String>,
    minutes: Int = 15,
    whatIsIt: String,
    mechanics: String,
    whyItMatters: String,
    doesNotImprove: String,
    primaryBenefits: List<String>,
    secondaryBenefits: List<String>,
    perfApps: List<String>,
    crossApps: List<String>,
    limitations: List<String>,
    misconceptions: List<String>,
    baselineStrength: String,
    jointMobility: String,
    stabilityBalance: String,
    techniquePrereqs: String,
    previousMovements: List<String>,
    readinessTests: List<ReadinessTest>,
    steps: List<String>,
    mistakes: List<CommonMistake>,
    injuryRisks: String,
    jointLoading: String,
    painWarningSigns: String = "जोड़ में तेज, चुभने वाला दर्द, झनझनाहट, सुन्नपन या चक्कर — इन्हें सामान्य मसल्स थकान से अलग समझें।",
    fatigueConsiderations: String,
    whenToStop: String = "जैसे ही फॉर्म बिगड़े, जोड़ में दर्द उभरे या चक्कर आए, अभ्यास तुरंत रोक दें।",
    supervision: String = "अधिकांश लोग वीडियो देखकर खुद सीख सकते हैं; जटिल भारी लिफ्ट या लगातार दर्द होने पर योग्य कोच/विशेषज्ञ से सलाह लें।",
    criticalSafety: String? = null,
    roadmap: List<ProgressionLevelSpec>,
    prescription: PracticePrescription,
    visual: VisualAlignmentSpec,
    evidence: EvidenceLevel = EvidenceLevel.MODERATE
): MovementDetail {
    return MovementDetail(
        id = id,
        name = name,
        alternativeNames = alt,
        discipline = discipline,
        category = category,
        difficulty = difficulty,
        programmingModel = model,
        primaryAbilities = primaryAbilities,
        secondaryAbilities = secondaryAbilities,
        targetMuscles = muscles,
        bodyRegions = regions,
        movementPattern = pattern,
        trainingPurpose = purpose,
        equipment = equipment,
        estimatedMinutes = minutes,
        experienceLevel = when (difficulty) {
            DifficultyLevel.BEGINNER -> "शुरुआती के लिए आसान"
            DifficultyLevel.INTERMEDIATE -> "मध्यम स्तर (3-12+ महीने अभ्यास)"
            DifficultyLevel.ADVANCED -> "कठिन स्तर (1-3+ साल की तैयारी)"
            DifficultyLevel.EXPERT -> "विशेषज्ञ स्तर (कई वर्षों का अभ्यास)"
        },
        whatIsIt = whatIsIt,
        bodyMechanics = mechanics,
        whyItMatters = whyItMatters,
        musclesAndJoints = "मुख्य मांसपेशियाँ: ${muscles.joinToString(", ")} | जुड़े हिस्से: ${regions.joinToString(", ") { it.label }}",
        developsWhat = primaryAbilities.joinToString(", "),
        doesNotImprove = doesNotImprove,
        primaryBenefits = primaryBenefits,
        secondaryBenefits = secondaryBenefits,
        performanceApplications = perfApps,
        crossTrainingApplications = crossApps,
        limitations = limitations,
        commonMisconceptions = misconceptions,
        baselineStrength = baselineStrength,
        jointMobility = jointMobility,
        stabilityBalance = stabilityBalance,
        techniquePrereqs = techniquePrereqs,
        previousMovements = previousMovements,
        readinessTests = readinessTests,
        steps = steps,
        commonMistakes = mistakes,
        injuryRisks = injuryRisks,
        jointLoading = jointLoading,
        painWarningSigns = painWarningSigns,
        fatigueConsiderations = fatigueConsiderations,
        whenToStop = whenToStop,
        professionalSupervision = supervision,
        criticalSafetyNotice = criticalSafety,
        progressionRoadmap = roadmap,
        prescription = prescription,
        visualGuide = visual,
        evidenceLevel = evidence
    )
}

internal fun standardRoadmap(
    skillName: String,
    model: ProgrammingModel,
    level0Prep: String,
    level1Foundation: String,
    level2Standard: String,
    level3Advanced: String
): List<ProgressionLevelSpec> {
    val freq = when (model) {
        ProgrammingModel.STRENGTH_HYPERTROPHY -> "हफ्ते में 2 बार"
        ProgrammingModel.STATIC_SKILL -> "हफ्ते में 2-3 बार (ताज़ा होकर)"
        ProgrammingModel.MOBILITY -> "हफ्ते में 3-5 बार"
        ProgrammingModel.CARDIO -> "हफ्ते में 2-4 बार"
        ProgrammingModel.SPRINT_POWER -> "हफ्ते में 1-2 बार (पूरे आराम से)"
        ProgrammingModel.BALANCE_COORDINATION -> "हफ्ते में 2-3 बार"
        ProgrammingModel.BREATHING -> "रोज़ या वर्कआउट के बाद"
    }
    return listOf(
        ProgressionLevelSpec(
            levelNumber = 0,
            title = "लेवल 0: शुरुआती तैयारी व जॉइंट सुरक्षा",
            objective = "ताकत या गति बढ़ाने से पहले जोड़ों को सुरक्षित रूप से तैयार करना।",
            exercises = level0Prep,
            prescription = when (model) {
                ProgrammingModel.STATIC_SKILL -> "3 सेट x 15-25 सेकंड होल्ड"
                ProgrammingModel.CARDIO -> "15-25 मिनट बिल्कुल आसान गति"
                ProgrammingModel.BREATHING -> "5 मिनट शांत नाक की साँस"
                ProgrammingModel.SPRINT_POWER -> "3-4 हल्के तकनीक अभ्यास (60% गति)"
                else -> "2-3 सेट x 8-12 नियंत्रित रैप्स"
            },
            rest = "सेट्स के बीच 60-90 सेकंड आराम",
            weeklyFrequency = freq,
            techniqueCues = "धीरे और पूरे कंट्रोल से करें; किसी भी जोड़ में चुभन न होने दें।",
            advancementCriteria = "लगातार 2 दिन बिना किसी दर्द या जकड़न के सभी सेट पूरे हों।",
            regressionOption = "रेंज कम रखें या दीवार/बेंच का सहारा लें।",
            commonFailurePoints = "जल्दबाज़ी करना या जबरदस्ती गहराई में जाना।"
        ),
        ProgressionLevelSpec(
            levelNumber = 1,
            title = "लेवल 1: बेसिक फॉर्म पक्का करना",
            objective = "कम दबाव में सही तकनीक और संतुलन को दोहराने की आदत बनाना।",
            exercises = level1Foundation,
            prescription = when (model) {
                ProgrammingModel.STATIC_SKILL -> "4 सेट x 10-20 सेकंड साफ़ होल्ड"
                ProgrammingModel.CARDIO -> "25-35 मिनट ज़ोन 2 (बात करने लायक गति)"
                ProgrammingModel.BREATHING -> "7-10 मिनट नियमित अभ्यास"
                ProgrammingModel.SPRINT_POWER -> "4-5 साफ़ प्रयास (75-85% गति, पूरा आराम)"
                else -> "3 सेट x 8-12 रैप्स (2-3 रैप्स बाकी छोड़ें)"
            },
            rest = if (model == ProgrammingModel.SPRINT_POWER) "2-3 मिनट पूरा आराम" else "90-120 सेकंड",
            weeklyFrequency = freq,
            techniqueCues = "दोनों तरफ बराबर संतुलन और शांत साँस बनाए रखें।",
            advancementCriteria = "बिना फॉर्म टूटे लगातार 2-3 सेशन में लक्ष्य पूरा करें।",
            regressionOption = "लेवल 0 पर लौटें या 1 सेट कम करें।",
            commonFailurePoints = "थकान आने पर झटके से रैप्स निकालना।"
        ),
        ProgressionLevelSpec(
            levelNumber = 2,
            title = "लेवल 2: मुख्य अभ्यास ($skillName)",
            objective = "पूरी रेंज और सही फॉर्म में सुरक्षित रूप से वज़न, समय या रैप्स बढ़ाना।",
            exercises = level2Standard,
            prescription = when (model) {
                ProgrammingModel.STATIC_SKILL -> "4-5 सेट x 10-20 सेकंड साफ़ होल्ड"
                ProgrammingModel.CARDIO -> "30-50 मिनट नियमित कार्डियो"
                ProgrammingModel.BREATHING -> "10-12 मिनट गहरी शांति का अभ्यास"
                ProgrammingModel.SPRINT_POWER -> "3-5 पूरे जोश के प्रयास (रफ़्तार गिरते ही रुकें)"
                else -> "3-4 सेट x 6-12 रैप्स (1-2 रैप्स बाकी)"
            },
            rest = if (model == ProgrammingModel.SPRINT_POWER) "3-4 मिनट पूरा आराम" else "90-180 सेकंड",
            weeklyFrequency = freq,
            techniqueCues = "पूरी रेंज, सीधी रीढ़ और एक जैसी रफ़्तार रखें।",
            advancementCriteria = "2+ हफ्ते तक बिना किसी दर्द के ऊपरी लक्ष्य आराम से पूरा हो।",
            regressionOption = "वज़न 10-15% घटाएं या लेवल 1 का सहारा लें।",
            commonFailurePoints = "सिर्फ नंबर बढ़ाने के लिए आधी रेंज में अभ्यास करना।"
        ),
        ProgressionLevelSpec(
            levelNumber = 3,
            title = "लेवल 3: एडवांस निखार और निरंतरता",
            objective = "थकान को संभालते हुए उच्च स्तर का कंट्रोल, वज़न या गति बनाए रखना।",
            exercises = level3Advanced,
            prescription = when (model) {
                ProgrammingModel.STATIC_SKILL -> "3-5 सेट x 5-15 सेकंड एडवांस होल्ड"
                ProgrammingModel.CARDIO -> "योजनाबद्ध कार्डियो (हफ्ते में 80% आसान, 20% तेज़)"
                ProgrammingModel.BREATHING -> "10-15 मिनट सहज दैनिक अभ्यास"
                ProgrammingModel.SPRINT_POWER -> "3-4 सर्वश्रेष्ठ क्वालिटी प्रयास"
                else -> "3-5 सेट (1-2 रैप बाकी) या पॉज़/टेम्पो बदलाव"
            },
            rest = if (model == ProgrammingModel.SPRINT_POWER) "3-5 मिनट" else "120-210 सेकंड",
            weeklyFrequency = freq,
            techniqueCues = "सूक्ष्म बैलेंस, शांत साँस और जोड़ों के सटीक कोण पर ध्यान।",
            advancementCriteria = "नियमित डीलोज़ और अच्छी रिकवरी के साथ लगातार प्रगति।",
            regressionOption = "वॉल्यूम 30-50% कम करें या लेवल 2 दोहराएं।",
            commonFailurePoints = "आराम/डीलोज़ न लेना और लगातार ज़्यादा भारी दिन रखना।"
        )
    )
}
