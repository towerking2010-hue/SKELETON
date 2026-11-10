package com.example

import com.example.data.model.LanguageSetting
import com.example.engine.LanguageDetector
import com.example.engine.SkeletonCognitiveEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testSinhalaScriptDetection() {
    val sinhalaInput = "මේ topic එක feed එකට එකතු කරන්නද?"
    val detected = LanguageDetector.detectDynamicOverride(sinhalaInput)
    assertEquals(LanguageSetting.SINHALA, detected)
  }

  @Test
  fun testExplicitSinhalaRequestDetection() {
    val input = "Explain quantum mechanics in sinhala"
    val detected = LanguageDetector.detectDynamicOverride(input)
    assertEquals(LanguageSetting.SINHALA, detected)
  }

  @Test
  fun testExplicitEnglishRequestDetection() {
    val input = "Explain in english please"
    val detected = LanguageDetector.detectDynamicOverride(input)
    assertEquals(LanguageSetting.ENGLISH, detected)
  }

  @Test
  fun testCringeFilterStripsFluff() {
    val fluff = "In today's fast-paced digital world, CAP Theorem states you cannot have all three."
    val filtered = SkeletonCognitiveEngine.applyCringeFilter(fluff, true)
    assertTrue(!filtered.contains("In today's fast-paced digital world,"))
  }

  @Test
  fun testFeedSyncTopicDetection() {
    val (reply, topic) = SkeletonCognitiveEngine.generateCoreResponse(
      userText = "Can you explain Raft consensus protocol?",
      targetLanguage = LanguageSetting.ENGLISH,
      existingTopics = emptyList()
    )
    assertNotNull(reply)
    assertTrue(reply.contains("[FEED SYNC]"))
  }

  @Test
  fun testIntelligenceModeProperties() {
    val coreMode = com.example.data.model.IntelligenceMode.CORE
    val deepDiveMode = com.example.data.model.IntelligenceMode.DEEPDIVE

    assertEquals("CORE", coreMode.label)
    assertEquals("DEEPDIVE", deepDiveMode.label)
    assertEquals("GLOBAL STREAM", coreMode.badge)
    assertTrue(coreMode.subtitle.contains("Omniscient") && coreMode.subtitle.contains("Twin"))
    assertTrue(coreMode.telemetry.contains("Meta-AI") || coreMode.telemetry.contains("Feed Sync"))
    assertTrue(deepDiveMode.telemetry.contains("Zero Slop") || deepDiveMode.telemetry.contains("Breakdown"))
  }
}
