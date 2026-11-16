package com.example.engine.learning

/** Offline spaced-review schedule for individual vocabulary items. */
data class FluentPathWordReview(
    val repetitions: Int = 0,
    val intervalDays: Int = 0,
    val dueAtMillis: Long = 0L
)

object FluentPathVocabularyScheduler {
    private val intervalsDays = listOf(1, 3, 7, 14, 30)
    const val RETRY_DELAY_MILLIS = 10 * 60 * 1000L

    fun remembered(current: FluentPathWordReview, nowMillis: Long): FluentPathWordReview {
        val interval = intervalsDays[current.repetitions.coerceIn(0, intervalsDays.lastIndex)]
        return FluentPathWordReview(
            repetitions = current.repetitions + 1,
            intervalDays = interval,
            dueAtMillis = nowMillis + interval * 24L * 60L * 60L * 1000L
        )
    }

    fun needsPracticeAgain(nowMillis: Long): FluentPathWordReview =
        FluentPathWordReview(0, 0, nowMillis + RETRY_DELAY_MILLIS)

    fun isDue(review: FluentPathWordReview?, nowMillis: Long): Boolean =
        review == null || review.dueAtMillis <= nowMillis

    fun dueCount(wordIds: Collection<String>, reviews: Map<String, FluentPathWordReview>, nowMillis: Long): Int =
        wordIds.count { isDue(reviews[it], nowMillis) }
}
