package com.example.engine.learning

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FluentPathAnswerCheckerTest {
  @Test
  fun acceptsCaseInsensitiveLatinAnswersAndWhitespace() {
    assertTrue(FluentPathAnswerChecker.matches("  CANTAT ", "cantat"))
  }

  @Test
  fun acceptsExplicitAlternatives() {
    assertTrue(FluentPathAnswerChecker.matches("ka", "か", alternatives = listOf("ka")))
  }

  @Test
  fun rejectsBlankAndIncorrectAnswers() {
    assertFalse(FluentPathAnswerChecker.matches(" ", "kein"))
    assertFalse(FluentPathAnswerChecker.matches("nicht", "kein"))
  }
}
