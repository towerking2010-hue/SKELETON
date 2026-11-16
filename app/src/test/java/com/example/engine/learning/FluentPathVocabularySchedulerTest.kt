package com.example.engine.learning

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FluentPathVocabularySchedulerTest {
    @Test fun rememberedWordsMoveThroughIncreasingIntervals() {
        val now = 1_800_000_000_000L
        val first = FluentPathVocabularyScheduler.remembered(FluentPathWordReview(), now)
        assertEquals(1, first.repetitions)
        assertEquals(1, first.intervalDays)
        assertEquals(now + 86_400_000L, first.dueAtMillis)
        val second = FluentPathVocabularyScheduler.remembered(first, now)
        assertEquals(2, second.repetitions)
        assertEquals(3, second.intervalDays)
        val third = FluentPathVocabularyScheduler.remembered(second, now)
        assertEquals(7, third.intervalDays)
    }

    @Test fun forgottenWordReturnsForShortRetry() {
        val now = 1_800_000_000_000L
        val retry = FluentPathVocabularyScheduler.needsPracticeAgain(now)
        assertEquals(0, retry.repetitions)
        assertEquals(0, retry.intervalDays)
        assertEquals(now + FluentPathVocabularyScheduler.RETRY_DELAY_MILLIS, retry.dueAtMillis)
    }

    @Test fun dueCountIncludesNewAndOverdueWordsOnly() {
        val now = 1_800_000_000_000L
        val reviews = mapOf(
            "tomorrow" to FluentPathWordReview(1, 1, now + 1_000L),
            "overdue" to FluentPathWordReview(1, 1, now - 1_000L)
        )
        assertEquals(2, FluentPathVocabularyScheduler.dueCount(listOf("new", "tomorrow", "overdue"), reviews, now))
        assertFalse(FluentPathVocabularyScheduler.isDue(reviews["tomorrow"], now))
        assertTrue(FluentPathVocabularyScheduler.isDue(null, now))
    }
}
