package com.example.engine.ai

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class ZeroBillingPolicyTest {

  @Test
  fun cloudInferenceIsBlockedBeforeAnyProviderRequest() = runBlocking {
    val result = GeminiCognitiveService.chatGenerate(
      userPrompt = "This request must never reach a paid provider.",
      role = AiRole.SKELETON_CORE,
      tier = ModelTier.GENERAL
    )

    assertTrue(result.startsWith("Inference error [zero-billing policy]"))
    assertTrue(result.contains("No model request was sent"))
  }
}
