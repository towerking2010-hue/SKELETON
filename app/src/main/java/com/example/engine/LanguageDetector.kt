package com.example.engine

import com.example.data.model.LanguageSetting

object LanguageDetector {

  private val sinhalaRegex = Regex("[\\u0D80-\\u0DFF]")

  /**
   * Evaluates dynamic user override:
   * Returns SINHALA if user typed Sinhala script or requested Sinhala.
   * Returns ENGLISH if user explicitly demanded English.
   * Returns null if no explicit turn-level override is detected (so UI setting applies).
   */
  fun detectDynamicOverride(input: String): LanguageSetting? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return null

    // Check for Sinhala script characters
    if (sinhalaRegex.containsMatchIn(trimmed)) {
      return LanguageSetting.SINHALA
    }

    val lower = trimmed.lowercase()
    if (lower.contains("in sinhala") || lower.contains("sinhala please") || lower.contains("sinhala version") || lower.endsWith("sinhala")) {
      return LanguageSetting.SINHALA
    }

    if (lower.contains("in english") || lower.contains("english please") || lower.contains("english version") || lower.endsWith("english")) {
      return LanguageSetting.ENGLISH
    }

    return null
  }

  fun resolveEffectiveLanguage(
    systemSetting: LanguageSetting,
    userPrompt: String
  ): LanguageSetting {
    return detectDynamicOverride(userPrompt) ?: systemSetting
  }
}
