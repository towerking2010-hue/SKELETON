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
      else -> value
    }
  }

  fun choice(value: String, language: LanguageSetting): String {
    if (language != LanguageSetting.SINHALA) return value
    return when (value) {
      "Beginning" -> "මුල"
      "Middle" -> "මැද"
      "End" -> "අග"
      "Omitted" -> "නොදක්වා ඇත"
      "Topic" -> "මාතෘකාව"
      "Past tense" -> "අතීත කාලය"
      "Question" -> "ප්‍රශ්නය"
      "Location" -> "ස්ථානය"
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
      "None" -> "කිසිවක් නැත"
      else -> value // Preserve Japanese, Greek, Latin and other target-language tokens.
    }
  }
}
