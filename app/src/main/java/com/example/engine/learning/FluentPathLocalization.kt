package com.example.engine.learning

import com.example.data.model.LanguageSetting

/**
 * Sinhala-facing labels for FluentPath. Target-language examples and tokens are
 * intentionally preserved; only learner-interface terminology is translated.
 */
object FluentPathLocalization {

  fun courseName(courseId: String, englishName: String, language: LanguageSetting): String {
    if (language != LanguageSetting.SINHALA) return englishName
    return when (courseId) {
      "ja" -> "ජපන්"
      "de" -> "ජර්මන්"
      "el" -> "පුරාතන ග්‍රීක"
      "la" -> "ලතින්"
      "si" -> "සිංහල"
      else -> englishName
    }
  }

  fun pattern(value: String, language: LanguageSetting): String {
    if (language != LanguageSetting.SINHALA) return value
    return when (value) {
      "Subject → Object → Verb" -> "කර්තෘ → කර්මය → ක්‍රියාව"
      "Topic + は + information" -> "මාතෘකාව + は + තොරතුරු"
      "Position 1 + verb + rest" -> "පළමු ස්ථානය + ක්‍රියාපදය + ඉතිරිය"
      "Nominative = subject" -> "නාමකාරක විභක්තිය = කර්තෘ"
      "Nominative = subject · Accusative = object" -> "නාමකාරක විභක්තිය = කර්තෘ · කර්මකාරක විභක්තිය = කර්මය"
      "Statement + か" -> "ප්‍රකාශය + か"
      "Time + object + verb (past)" -> "කාලය + කර්මය + ක්‍රියාව (අතීත)"
      "Subject + verb + accusative object" -> "කර්තෘ + ක්‍රියාව + කර්මය (කර්මකාරක)"
      "Subject + verb + kein(e) + noun" -> "කර්තෘ + ක්‍රියාව + kein(e) + නාම පදය"
      "Genitive = relationship / possession" -> "සම්බන්ධක විභක්තිය = සම්බන්ධය / අයිතිය"
      "Stem + personal ending" -> "ක්‍රියා මූලය + පුද්ගල අවසානය"
      "Subject + verb (3rd person singular)" -> "කර්තෘ + ක්‍රියාව (තුන්වන පුද්ගල ඒකවචන)"
      "Adjective agrees with noun" -> "විශේෂණය නාම පදයට ගැළපේ"
      else -> value
    }
  }

  fun choice(value: String, language: LanguageSetting): String {
    if (language != LanguageSetting.SINHALA) return value
    return when (value) {
      "Beginning" -> "මුල"
      "At the beginning" -> "වාක්‍යයේ මුලට"
      "After the noun only" -> "නාම පදයට පසුව පමණයි"
      "At the end" -> "වාක්‍යයේ අගට"
      "Before the verb" -> "ක්‍රියාපදයට පෙර"
      "Middle" -> "මැද"
      "End" -> "අග"
      "Omitted" -> "නොදක්වා ඇත"
      "Topic" -> "මාතෘකාව"
      "Past tense" -> "අතීත කාලය"
      "Question" -> "ප්‍රශ්නය"
      "Location" -> "ස්ථානය"
      "Subject" -> "කර්තෘ"
      "Direct object" -> "සෘජු කර්මය"
      "Verb" -> "ක්‍රියාපදය"
      "Time phrase" -> "කාල පදය"
      "Only the verb tense" -> "ක්‍රියා කාලය පමණයි"
      "The noun's gender, number and case" -> "නාම පදයේ ලිංගය, වචන සංඛ්‍යාව සහ විභක්තිය"
      "The speaker's age" -> "කථිකයාගේ වයස"
      "Position 1" -> "පළමු ස්ථානය"
      "Position 2" -> "දෙවන ස්ථානය"
      "Always last" -> "සෑම විටම අග"
      "Position 4" -> "සිව්වන ස්ථානය"
      "Yes" -> "ඔව්"
      "Never" -> "කිසිවිටෙක නැත"
      "Only questions" -> "ප්‍රශ්නවලදී පමණයි"
      "Only past" -> "අතීත කාලයේදී පමණයි"
      "Genitive" -> "සම්බන්ධක විභක්තිය"
      "Accusative" -> "කර්මකාරක විභක්තිය"
      "Nominative" -> "නාමකාරක විභක්තිය"
      "Vocative" -> "ආමන්ත්‍රණ විභක්තිය"
      "Word endings" -> "වචන අවසාන කොටස්"
      "Word colour" -> "වචනයේ වර්ණය"
      "Punctuation" -> "විරාම ලකුණු"
      "Word length" -> "වචනයේ දිග"
      "The word's colour" -> "වචනයේ වර්ණය"
      "The number of letters only" -> "අකුරු ගණන පමණයි"
      "The font size" -> "අකුරු ප්‍රමාණය"
      "First person" -> "පළමු පුද්ගලයා"
      "Second person" -> "දෙවන පුද්ගලයා"
      "Third person" -> "තුන්වන පුද්ගලයා"
      "Plural" -> "බහුවචනය"
      "First-person singular" -> "පළමු පුද්ගල ඒකවචනය"
      "Second-person singular" -> "දෙවන පුද්ගල ඒකවචනය"
      "Third-person singular" -> "තුන්වන පුද්ගල ඒකවචනය"
      "Only word order" -> "වචන පිළිවෙළ පමණයි"
      "Only the first letter" -> "පළමු අකුර පමණයි"
      "The noun it describes" -> "එය විස්තර කරන නාම පදය"
      "Sentence length" -> "වාක්‍යයේ දිග"
      "The punctuation" -> "විරාම ලකුණු"
      "Gender, number and case" -> "ලිංගය, වචන සංඛ්‍යාව සහ විභක්තිය"
      "Nothing", "None" -> "කිසිවක් නැත"
      else -> value // Preserve Japanese, Greek, Latin and other target-language tokens.
    }
  }
}
