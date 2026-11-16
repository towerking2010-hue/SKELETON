package com.example.engine

import com.example.data.model.LanguageSetting
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SkeletonCognitiveEngineTest {

  @Test
  fun unknownTopicDoesNotInventExplanationOrAutoCreateFeedCard() {
    val (reply, feedTopic) = SkeletonCognitiveEngine.generateCoreResponse(
      userText = "Explain the history of the Antikythera mechanism",
      targetLanguage = LanguageSetting.ENGLISH,
      existingTopics = emptyList()
    )

    assertNull(feedTopic)
    assertTrue(reply.contains("local-only response mode", ignoreCase = true))
    assertTrue(reply.contains("invent", ignoreCase = true))
    assertFalse(reply.contains("Every complex domain has a 3-variable skeleton"))
  }

  @Test
  fun offlineTutorSupportsAdditionalScienceTopicsInBothLanguages() {
    val cases = listOf(
      Triple("Explain Bayes reasoning", "Bayesian reasoning", "Bayesian reasoning"),
      Triple("Explain thermodynamics", "Thermodynamics", "තාපගති විද්‍යාව"),
      Triple("Explain neuroplasticity", "Neuroplasticity", "ස්නායු ප්ලාස්ටිසිටි")
    )
    cases.forEach { (prompt, englishMarker, sinhalaMarker) ->
      val (englishReply, englishFeed) = SkeletonCognitiveEngine.generateCoreResponse(
        userText = prompt,
        targetLanguage = LanguageSetting.ENGLISH,
        existingTopics = emptyList()
      )
      assertTrue("Missing English lesson for $prompt", englishReply.contains(englishMarker, ignoreCase = true))
      assertTrue("Expected feed sync for $prompt", englishFeed != null)

      val (sinhalaReply, sinhalaFeed) = SkeletonCognitiveEngine.generateCoreResponse(
        userText = prompt,
        targetLanguage = LanguageSetting.SINHALA,
        existingTopics = emptyList()
      )
      assertTrue("Missing Sinhala lesson for $prompt", sinhalaReply.contains(sinhalaMarker, ignoreCase = true))
      assertTrue("Expected feed sync for $prompt", sinhalaFeed != null)
    }
  }

  @Test
  fun knownOfflineTopicGetsLocalExplanation() {
    val (reply, _) = SkeletonCognitiveEngine.generateCoreResponse(
      userText = "Explain quantum computing",
      targetLanguage = LanguageSetting.ENGLISH,
      existingTopics = emptyList()
    )

    assertTrue(reply.contains("probability amplitudes", ignoreCase = true))
    assertFalse(reply.contains("zero-cost offline mode", ignoreCase = true))
  }
}
