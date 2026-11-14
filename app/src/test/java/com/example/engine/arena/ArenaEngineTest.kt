package com.example.engine.arena

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArenaEngineTest {

  @Test
  fun progressionUsesEarnedXpAndUnlocksTerritories() {
    val profile = ArenaProfile(xp = 150)
    assertEquals(2, profile.level)
    assertEquals(50, profile.levelProgress)
    assertEquals(3, ArenaEngine.unlockedTerritories(profile.xp))
  }

  @Test
  fun completedChallengesRemainAvailableForPractice() {
    val first = ArenaEngine.challenges.first()
    val remaining = ArenaEngine.challengesFor("ALL", ArenaProfile(completedChallengeIds = setOf(first.id)))
    assertTrue(remaining.any { it.id == first.id })
    assertTrue(remaining.isNotEmpty())
  }

  @Test
  fun eachChallengeHasValidAnswerAndReward() {
    ArenaEngine.challenges.forEach { challenge ->
      assertTrue(challenge.correctIndex in challenge.options.indices)
      assertTrue(challenge.rewardXp > 0)
      assertTrue(challenge.explanation.isNotBlank())
    }
  }
  @Test
  fun skillAccuracyIsPersonalAndEvidenceBased() {
    val evidence = ArenaSkillEvidence(attempts = 4, correct = 3)
    assertEquals(75, evidence.accuracy)
    assertTrue(evidence.isEstablished)
    assertFalse(ArenaSkillEvidence(attempts = 2, correct = 2).isEstablished)
  }

  @Test
  fun adaptiveQueuePrioritizesUnassessedSkillsThenWeakestEvidence() {
    val first = ArenaEngine.challengesFor("ALL", ArenaProfile()).first()
    assertEquals(0, ArenaProfile().skillEvidence[first.skillId]?.attempts ?: 0)

    val profile = ArenaProfile(
      skillEvidence = mapOf(
        "critical_reasoning" to ArenaSkillEvidence(attempts = 4, correct = 4),
        "bias_detection" to ArenaSkillEvidence(attempts = 4, correct = 1),
        "strategic_judgment" to ArenaSkillEvidence(attempts = 4, correct = 3),
        "scientific_reasoning" to ArenaSkillEvidence(attempts = 4, correct = 4),
        "memory_learning" to ArenaSkillEvidence(attempts = 4, correct = 4),
        "probabilistic_thinking" to ArenaSkillEvidence(attempts = 4, correct = 4)
      )
    )
    assertEquals("bias_detection", ArenaEngine.challengesFor("ALL", profile).first().skillId)
  }

}
