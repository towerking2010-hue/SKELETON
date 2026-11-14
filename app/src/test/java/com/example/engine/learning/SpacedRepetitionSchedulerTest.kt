package com.example.engine.learning

import org.junit.Assert.*
import org.junit.Test

class SpacedRepetitionSchedulerTest {
    private val day = 86_400_000L

    @Test fun successfulRecallAdvancesTheSchedule() {
        var stage = 0
        val intervals = listOf(3L, 7L, 14L, 30L)
        intervals.forEachIndexed { index, days ->
            val result = SpacedRepetitionScheduler.review(stage, 0, true, 1_000L)
            stage = result.stage
            assertEquals(index + 1, result.stage)
            assertEquals(1_000L + days * day, result.nextReviewTimestamp)
        }
        assertEquals(4, stage)
    }

    @Test fun failedRecallResetsAndSchedulesRetry() {
        val result = SpacedRepetitionScheduler.review(3, 2, false, 5_000L)
        assertEquals(0, result.stage)
        assertEquals(3, result.weakRecallCount)
        assertEquals(5_000L + SpacedRepetitionScheduler.RETRY_AFTER_MILLIS, result.nextReviewTimestamp)
    }

    @Test fun dueCheckUsesTheIntervalBoundary() {
        val dueAt = 10_000L + 3L * day
        assertFalse(SpacedRepetitionScheduler.isDue(1, 10_000L, dueAt - 1L))
        assertTrue(SpacedRepetitionScheduler.isDue(1, 10_000L, dueAt))
    }

    @Test fun neverReviewedCardsAreDueImmediately() {
        assertTrue(SpacedRepetitionScheduler.isDue(0, 0L, 1L))
    }
}
