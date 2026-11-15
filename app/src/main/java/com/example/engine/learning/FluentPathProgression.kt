package com.example.engine.learning

data class FluentPathProgressState(
  val xp: Int = 0,
  val masteredLessonIds: Set<String> = emptySet(),
  val reviewLessonIds: Set<String> = emptySet()
)

data class FluentPathCompletion(
  val state: FluentPathProgressState,
  val xpEarned: Int
)

/**
 * Keeps lesson mastery and XP rules independent from Compose and SharedPreferences.
 * Replaying a mastered lesson never farms XP; a mistake schedules review without
 * erasing the fact that the learner earned mastery previously.
 */
object FluentPathProgression {
  const val FIRST_MASTERY_XP = 20

  fun completeLesson(
    current: FluentPathProgressState,
    lessonId: String,
    allAnswersCorrect: Boolean
  ): FluentPathCompletion {
    if (lessonId.isBlank()) return FluentPathCompletion(current, 0)

    if (!allAnswersCorrect) {
      return FluentPathCompletion(
        current.copy(reviewLessonIds = current.reviewLessonIds + lessonId),
        0
      )
    }

    val isFirstMastery = lessonId !in current.masteredLessonIds
    val earned = if (isFirstMastery) FIRST_MASTERY_XP else 0
    val next = current.copy(
      xp = (current.xp + earned).coerceAtLeast(0),
      masteredLessonIds = current.masteredLessonIds + lessonId,
      reviewLessonIds = current.reviewLessonIds - lessonId
    )
    return FluentPathCompletion(next, earned)
  }
}
