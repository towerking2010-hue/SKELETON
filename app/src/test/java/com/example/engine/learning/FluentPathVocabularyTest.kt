package com.example.engine.learning

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FluentPathVocabularyTest {
    @Test fun everyCourseHasAUsefulStarterDeck() {
        assertEquals(setOf("ja", "de", "el", "la", "si"), FluentPathVocabulary.supportedCourseIds())
        FluentPathVocabulary.supportedCourseIds().forEach { id ->
            val deck = FluentPathVocabulary.forCourse(id)
            assertTrue("$id should have at least 12 words", deck.size >= 12)
            assertTrue(deck.all { it.id.isNotBlank() && it.word.isNotBlank() && it.meaningEnglish.isNotBlank() && it.meaningSinhala.isNotBlank() && it.example.isNotBlank() })
            assertEquals(deck.size, deck.map { it.id }.distinct().size)
        }
    }
    @Test fun unknownCourseReturnsNoInventedVocabulary() {
        assertTrue(FluentPathVocabulary.forCourse("unknown").isEmpty())
    }
}
