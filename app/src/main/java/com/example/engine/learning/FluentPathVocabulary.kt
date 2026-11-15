package com.example.engine.learning

data class FluentPathWord(
    val id: String,
    val word: String,
    val meaningEnglish: String,
    val meaningSinhala: String,
    val example: String
)

/** Curated starter vocabulary with learner-language glosses and contextual examples. */
object FluentPathVocabulary {
    private val decks = mapOf(
        "ja" to listOf(
            FluentPathWord("ja_v01","私 (わたし)","I / me","මම","私は学生です。"),
            FluentPathWord("ja_v02","あなた","you","ඔබ","あなたは先生ですか。"),
            FluentPathWord("ja_v03","人 (ひと)","person","පුද්ගලයා","あの人は学生です。"),
            FluentPathWord("ja_v04","水 (みず)","water","වතුර","水を飲みます。"),
            FluentPathWord("ja_v05","本 (ほん)","book","පොත","本を読みます。"),
            FluentPathWord("ja_v06","食べる (たべる)","to eat","කනවා","りんごを食べます。"),
            FluentPathWord("ja_v07","飲む (のむ)","to drink","බොනවා","水を飲みます。"),
            FluentPathWord("ja_v08","行く (いく)","to go","යනවා","学校へ行きます。"),
            FluentPathWord("ja_v09","今日 (きょう)","today","අද","今日は暑いです。"),
            FluentPathWord("ja_v10","明日 (あした)","tomorrow","හෙට","明日、行きます。"),
            FluentPathWord("ja_v11","学校 (がっこう)","school","පාසල","学校で勉強します。"),
            FluentPathWord("ja_v12","友達 (ともだち)","friend","මිතුරා / මිතුරිය","友達と話します。")
        ),
        "de" to listOf(
            FluentPathWord("de_v01","ich","I","මම","Ich lerne Deutsch."),
            FluentPathWord("de_v02","du","you (informal singular)","ඔයා","Du bist hier."),
            FluentPathWord("de_v03","das Haus","the house","ගෙදර","Das Haus ist groß."),
            FluentPathWord("de_v04","das Wasser","the water","වතුර","Ich trinke Wasser."),
            FluentPathWord("de_v05","das Buch","the book","පොත","Das Buch ist interessant."),
            FluentPathWord("de_v06","essen","to eat","කනවා","Wir essen Reis."),
            FluentPathWord("de_v07","trinken","to drink","බොනවා","Sie trinkt Tee."),
            FluentPathWord("de_v08","gehen","to go","යනවා","Ich gehe nach Hause."),
            FluentPathWord("de_v09","heute","today","අද","Heute lerne ich."),
            FluentPathWord("de_v10","morgen","tomorrow","හෙට","Morgen arbeite ich."),
            FluentPathWord("de_v11","die Schule","the school","පාසල","Die Schule ist groß."),
            FluentPathWord("de_v12","der Freund / die Freundin","friend","මිතුරා / මිතුරිය","Mein Freund kommt heute.")
        ),
        "el" to listOf(
            FluentPathWord("el_v01","ἐγώ","I","මම","ἐγώ εἰμι μαθητής."),
            FluentPathWord("el_v02","σύ","you (singular)","ඔබ","σύ εἶ φίλος."),
            FluentPathWord("el_v03","ἄνθρωπος","human / person","මිනිසා","ὁ ἄνθρωπος βλέπει."),
            FluentPathWord("el_v04","ὕδωρ","water","වතුර","τὸ ὕδωρ καλόν ἐστιν."),
            FluentPathWord("el_v05","βιβλίον","book","පොත","τὸ βιβλίον μικρόν ἐστιν."),
            FluentPathWord("el_v06","λύω","I loosen / release","මම ලිහිල් කරමි","λύω τὸν δεσμόν."),
            FluentPathWord("el_v07","γράφω","I write","මම ලියමි","γράφω λόγον."),
            FluentPathWord("el_v08","λέγω","I say / speak","මම කියමි","λέγω τὴν ἀλήθειαν."),
            FluentPathWord("el_v09","ἡμέρα","day","දවස","ἡ ἡμέρα καλή ἐστιν."),
            FluentPathWord("el_v10","φίλος","friend","මිතුරා","ὁ φίλος ἔρχεται."),
            FluentPathWord("el_v11","οἶκος","house / home","නිවස","ὁ οἶκος μέγας ἐστίν."),
            FluentPathWord("el_v12","καλός","good / beautiful","හොඳ / ලස්සන","ὁ λόγος καλός ἐστιν.")
        ),
        "la" to listOf(
            FluentPathWord("la_v01","ego","I","මම","Ego discipulus sum."),
            FluentPathWord("la_v02","tu","you (singular)","ඔබ","Tu amicus es."),
            FluentPathWord("la_v03","aqua","water","වතුර","Aquam bibo."),
            FluentPathWord("la_v04","liber","book","පොත","Librum lego."),
            FluentPathWord("la_v05","puella","girl","ගැහැණු ළමයා","Puella cantat."),
            FluentPathWord("la_v06","puer","boy","පිරිමි ළමයා","Puer legit."),
            FluentPathWord("la_v07","amo","I love","මම ආදරය කරමි","Te amo."),
            FluentPathWord("la_v08","video","I see","මම දකිමි","Amicum video."),
            FluentPathWord("la_v09","hodie","today","අද","Hodie laboro."),
            FluentPathWord("la_v10","cras","tomorrow","හෙට","Cras veniam."),
            FluentPathWord("la_v11","amicus","friend","මිතුරා","Amicus venit."),
            FluentPathWord("la_v12","domus","house / home","නිවස","Domus magna est.")
        ),
        "si" to listOf(
            FluentPathWord("si_v01","මම","I","I","මම පාසලට යනවා."),
            FluentPathWord("si_v02","ඔබ","you (polite)","you","ඔබ කොහෙද යන්නේ?"),
            FluentPathWord("si_v03","ඔයා","you (informal)","you","ඔයා අද එනවද?"),
            FluentPathWord("si_v04","වතුර","water","water","මට වතුර ටිකක් දෙන්න."),
            FluentPathWord("si_v05","කෑම","food / meal","food","අපි කෑම කමු."),
            FluentPathWord("si_v06","යනවා","to go","go","මම ගෙදර යනවා."),
            FluentPathWord("si_v07","එනවා","to come","come","ඇය හෙට එනවා."),
            FluentPathWord("si_v08","කියනවා","to say / tell","say","ඔහු ඇත්ත කියනවා."),
            FluentPathWord("si_v09","අද","today","today","අද වැස්ස වැටෙනවා."),
            FluentPathWord("si_v10","හෙට","tomorrow","tomorrow","හෙට අපි හමුවෙමු."),
            FluentPathWord("si_v11","පොත","book","book","මම පොතක් කියවනවා."),
            FluentPathWord("si_v12","මිතුරා","friend","friend","මගේ මිතුරා මෙහෙ ඉන්නවා.")
        )
    )
    fun forCourse(courseId: String): List<FluentPathWord> = decks[courseId].orEmpty()
    fun supportedCourseIds(): Set<String> = decks.keys
}
