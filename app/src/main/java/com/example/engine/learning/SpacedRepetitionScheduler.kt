package com.example.engine.learning

/**
 * A transparent, deterministic review schedule for short learning cards.
 * Successful recall advances 3, 7, 14, then 30-day intervals. Failed recall
 * resets the stage and schedules a short retry.
 */
object SpacedRepetitionScheduler {
    private const val DAY = 86_400_000L
    const val RETRY_AFTER_MILLIS = 600_000L
    private val intervals = longArrayOf(3L * DAY, 7L * DAY, 14L * DAY, 30L * DAY)

    data class ReviewResult(
        val stage: Int,
        val weakRecallCount: Int,
        val lastReviewedTimestamp: Long,
        val nextReviewTimestamp: Long
    )

    fun review(stage: Int, weakCount: Int, successful: Boolean, now: Long): ReviewResult {
        val safeStage = stage.coerceIn(0, intervals.size)
        val nextStage = if (successful) (safeStage + 1).coerceAtMost(intervals.size) else 0
        val delay = if (successful) intervals[nextStage - 1] else RETRY_AFTER_MILLIS
        return ReviewResult(
            stage = nextStage,
            weakRecallCount = if (successful) weakCount.coerceAtLeast(0) else weakCount.coerceAtLeast(0) + 1,
            lastReviewedTimestamp = now,
            nextReviewTimestamp = now + delay
        )
    }

    fun nextReviewTimestamp(stage: Int, lastReviewed: Long): Long {
        if (lastReviewed <= 0L) return 0L
        val safeStage = stage.coerceIn(0, intervals.size)
        val delay = if (safeStage == 0) RETRY_AFTER_MILLIS else intervals[safeStage - 1]
        return lastReviewed + delay
    }

    fun isDue(stage: Int, lastReviewed: Long, now: Long): Boolean =
        nextReviewTimestamp(stage, lastReviewed) <= now
}
