package com.example.data.model

enum class LanguageSetting(val code: String, val displayName: String, val nativeName: String) {
  ENGLISH("en", "Pure English", "English"),
  SINHALA("si", "Pure Sinhala", "සිංහල");

  fun toggle(): LanguageSetting = when (this) {
    ENGLISH -> SINHALA
    SINHALA -> ENGLISH
  }
}
