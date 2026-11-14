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
    assertTrue(reply.contains("zero-cost offline mode", ignoreCase = true))
    assertTrue(reply.contains("invent", ignoreCase = true))
    assertFalse(reply.contains("Every complex domain has a 3-variable skeleton"))
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
