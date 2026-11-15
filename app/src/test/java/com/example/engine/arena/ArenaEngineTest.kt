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
  fun unfinishedChallengesArePrioritizedBeforeCompletedPractice() {
    val first = ArenaEngine.challenges.first()
    val queue = ArenaEngine.challengesFor("ALL", ArenaProfile(completedChallengeIds = setOf(first.id)))
    assertTrue(queue.isNotEmpty())
    assertTrue(queue.first().id != first.id)
    assertTrue(queue.any { it.id == first.id })
  }

  @Test
  fun fullyCompletedCategoryFallsBackToPracticeWithoutChangingCompletionHistory() {
    val completed = ArenaEngine.challenges.map { it.id }.toSet()
    val profile = ArenaProfile(completedChallengeIds = completed)
    val practice = ArenaEngine.challengesFor("ALL", profile)
    assertEquals(ArenaEngine.challenges.size, practice.size)
    assertTrue(practice.all { it.id in profile.completedChallengeIds })
  }

  @Test
  fun modeQueueContainsOnlyChallengesFromSelectedMode() {
    val queue = ArenaEngine.challengesFor("CASE FILES", ArenaProfile())
    assertTrue(queue.isNotEmpty())
    assertTrue(queue.all { it.mode == "CASE FILES" })
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
  fun everyArenaModeHasEnoughChallengesToFeelLikeARealTrack() {
    ArenaEngine.modes.filterNot { it == "ALL" }.forEach { mode ->
      assertTrue("$mode should contain at least three trials", ArenaEngine.challenges.count { it.mode == mode } >= 3)
    }
  }

  @Test
  fun everyChallengeHasSinhalaLearningContent() {
    val english = ArenaEngine.challenges.associateBy { it.id }
    val sinhala = ArenaEngine.challengesFor("ALL", ArenaProfile(), LanguageSetting.SINHALA)
    assertEquals(english.size, sinhala.size)
    sinhala.forEach { translated ->
      val source = english.getValue(translated.id)
      assertTrue("Missing Sinhala title for ${translated.id}", translated.title != source.title)
      assertTrue("Missing Sinhala prompt for ${translated.id}", translated.prompt != source.prompt)
      assertTrue("Missing Sinhala explanation for ${translated.id}", translated.explanation != source.explanation)
      assertEquals(4, translated.options.size)
      assertTrue(translated.correctIndex in translated.options.indices)
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
        "probabilistic_thinking" to ArenaSkillEvidence(attempts = 4, correct = 4),
        "attention_control" to ArenaSkillEvidence(attempts = 4, correct = 4),
        "systems_thinking" to ArenaSkillEvidence(attempts = 4, correct = 4)
      )
    )
    assertEquals("bias_detection", ArenaEngine.challengesFor("ALL", profile).first().skillId)
  }

}
