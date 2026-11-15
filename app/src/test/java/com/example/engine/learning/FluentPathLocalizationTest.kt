package com.example.engine.learning

import com.example.data.model.LanguageSetting
import org.junit.Assert.assertEquals
import org.junit.Test

class FluentPathLocalizationTest {

  @Test
  fun courseNamesAreShownInSinhalaInterfaceLanguage() {
    assertEquals("ජපන්", FluentPathLocalization.courseName("ja", "Japanese", LanguageSetting.SINHALA))
    assertEquals("ජර්මන්", FluentPathLocalization.courseName("de", "German", LanguageSetting.SINHALA))
    assertEquals("පුරාතන ග්‍රීක", FluentPathLocalization.courseName("el", "Ancient Greek", LanguageSetting.SINHALA))
    assertEquals("ලතින්", FluentPathLocalization.courseName("la", "Latin", LanguageSetting.SINHALA))
    assertEquals("සිංහල", FluentPathLocalization.courseName("si", "Sinhala", LanguageSetting.SINHALA))
  }

  @Test
  fun interfaceChoicesAndGrammarPatternsAreLocalized() {
    assertEquals("දෙවන ස්ථානය", FluentPathLocalization.choice("Position 2", LanguageSetting.SINHALA))
    assertEquals("නාමකාරක විභක්තිය", FluentPathLocalization.choice("Nominative", LanguageSetting.SINHALA))
    assertEquals("කර්තෘ → කර්මය → ක්‍රියාව", FluentPathLocalization.pattern("Subject → Object → Verb", LanguageSetting.SINHALA))
  }

  @Test
  fun targetLanguageTokensRemainIntactAndEnglishModeIsUnchanged() {
    assertEquals("を", FluentPathLocalization.choice("を", LanguageSetting.SINHALA))
    assertEquals("Japanese", FluentPathLocalization.courseName("ja", "Japanese", LanguageSetting.ENGLISH))
    assertEquals("Beginning", FluentPathLocalization.choice("Beginning", LanguageSetting.ENGLISH))
  }
}
