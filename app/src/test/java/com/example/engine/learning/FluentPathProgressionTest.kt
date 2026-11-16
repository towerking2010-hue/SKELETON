package com.example.engine.learning

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FluentPathProgressionTest {

  @Test
  fun firstPerfectCompletionAwardsXpAndMastery() {
    val result = FluentPathProgression.completeLesson(
      FluentPathProgressState(),
      lessonId = "ja1",
      allAnswersCorrect = true
    )

    assertEquals(20, result.xpEarned)
    assertEquals(20, result.state.xp)
    assertTrue("ja1" in result.state.masteredLessonIds)
    assertTrue("ja1" !in result.state.reviewLessonIds)
  }

  @Test
  fun replayingMasteredLessonDoesNotAwardXpAgain() {
    val current = FluentPathProgressState(xp = 20, masteredLessonIds = setOf("ja1"))
    val result = FluentPathProgression.completeLesson(current, "ja1", allAnswersCorrect = true)

    assertEquals(0, result.xpEarned)
    assertEquals(20, result.state.xp)
    assertEquals(current.masteredLessonIds, result.state.masteredLessonIds)
  }

  @Test
  fun mistakeSchedulesReviewWithoutAwardingXpOrErasingPriorMastery() {
    val current = FluentPathProgressState(xp = 20, masteredLessonIds = setOf("ja1"))
    val result = FluentPathProgression.completeLesson(current, "ja1", allAnswersCorrect = false)

    assertEquals(0, result.xpEarned)
    assertEquals(20, result.state.xp)
    assertTrue("ja1" in result.state.masteredLessonIds)
    assertTrue("ja1" in result.state.reviewLessonIds)
  }

  @Test
  fun perfectReviewClearsReviewWithoutRepeatXp() {
    val current = FluentPathProgressState(
      xp = 20,
      masteredLessonIds = setOf("ja1"),
      reviewLessonIds = setOf("ja1")
    )
    val result = FluentPathProgression.completeLesson(current, "ja1", allAnswersCorrect = true)

    assertEquals(0, result.xpEarned)
    assertEquals(20, result.state.xp)
    assertTrue("ja1" !in result.state.reviewLessonIds)
  }

  @Test
  fun perfectRetryOfUnmasteredLessonAwardsXpOnce() {
    val failed = FluentPathProgression.completeLesson(
      FluentPathProgressState(),
      "de1",
      allAnswersCorrect = false
    ).state
    val passed = FluentPathProgression.completeLesson(failed, "de1", allAnswersCorrect = true)

    assertEquals(20, passed.xpEarned)
    assertEquals(20, passed.state.xp)
    assertTrue("de1" in passed.state.masteredLessonIds)
    assertTrue("de1" !in passed.state.reviewLessonIds)
  }

  @Test
  fun blankLessonIdCannotChangeProgress() {
    val current = FluentPathProgressState(xp = 60, masteredLessonIds = setOf("la1"))
    val result = FluentPathProgression.completeLesson(current, " ", allAnswersCorrect = true)

    assertEquals(current, result.state)
    assertEquals(0, result.xpEarned)
  }
}
