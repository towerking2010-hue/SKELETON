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
  fun completedChallengesAreNotOfferedAgain() {
    val first = ArenaEngine.challenges.first()
    val remaining = ArenaEngine.challengesFor("ALL", setOf(first.id))
    assertFalse(remaining.any { it.id == first.id })
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
}
