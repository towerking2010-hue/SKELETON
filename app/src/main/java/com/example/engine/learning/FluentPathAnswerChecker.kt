package com.example.engine.learning

object FluentPathAnswerChecker {
  fun matches(answer: String, expected: String, alternatives: List<String> = emptyList()): Boolean {
    val submitted = normalize(answer)
    if (submitted.isEmpty()) return false
    return (listOf(expected) + alternatives).any { normalize(it).equals(submitted, ignoreCase = true) }
  }

  private fun normalize(value: String): String = value.trim().replace(Regex("\\s+"), " ")
}
