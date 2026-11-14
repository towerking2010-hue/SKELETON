package com.example.engine.arena

data class ArenaChallenge(
  val id: String,
  val mode: String,
  val title: String,
  val domain: String,
  val difficulty: String,
  val prompt: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanation: String,
  val rewardXp: Int,
  val skillId: String
)

data class ArenaSkillEvidence(val attempts: Int = 0, val correct: Int = 0) {
  val accuracy: Int get() = if (attempts == 0) 0 else (correct * 100 / attempts).coerceIn(0, 100)
  val isEstablished: Boolean get() = attempts >= 3
}

data class ArenaSkill(val id: String, val label: String, val description: String, val evidence: ArenaSkillEvidence)

data class ArenaProfile(
  val xp: Int = 0,
  val completedChallengeIds: Set<String> = emptySet(),
  val skillEvidence: Map<String, ArenaSkillEvidence> = emptyMap()
) {
  val level: Int get() = (xp / 100) + 1
  val levelProgress: Int get() = xp % 100
  val rank: String get() = when {
    level >= 20 -> "ARCHITECT"
    level >= 12 -> "STRATEGIST"
    level >= 7 -> "SPECIALIST"
    level >= 3 -> "APPRENTICE"
    else -> "INITIATE"
  }
  val nextRankXp: Int get() = when {
    level < 3 -> 200
    level < 7 -> 600
    level < 12 -> 1100
    level < 20 -> 1900
    else -> 2000
  }
}

object ArenaEngine {
  val skillCatalog = listOf(
    ArenaSkill("critical_reasoning", "Critical reasoning", "Separating evidence, correlation and causal claims.", ArenaSkillEvidence()),
    ArenaSkill("bias_detection", "Bias detection", "Noticing assumptions, framing and cognitive shortcuts.", ArenaSkillEvidence()),
    ArenaSkill("strategic_judgment", "Strategic judgment", "Choosing actions under constraints and uncertainty.", ArenaSkillEvidence()),
    ArenaSkill("scientific_reasoning", "Scientific reasoning", "Applying scientific models and checking mechanisms.", ArenaSkillEvidence()),
    ArenaSkill("memory_learning", "Memory & learning", "Using retrieval, spacing and durable learning strategies.", ArenaSkillEvidence()),
    ArenaSkill("probabilistic_thinking", "Probabilistic thinking", "Using base rates and numerical uncertainty.", ArenaSkillEvidence())
  )

  fun skillsFor(profile: ArenaProfile): List<ArenaSkill> = skillCatalog.map { skill ->
    skill.copy(evidence = profile.skillEvidence[skill.id] ?: ArenaSkillEvidence())
  }

  fun weakestSkillId(profile: ArenaProfile): String? = skillsFor(profile)
    .filter { it.evidence.attempts > 0 }
    .minWithOrNull(compareBy<ArenaSkill> { it.evidence.accuracy }.thenByDescending { it.evidence.attempts })
    ?.id

  val modes = listOf("ALL", "MIND TRIALS", "CASE FILES", "STRATEGY", "CONQUEST", "MEMORY LAB")

  val challenges = listOf(
    ArenaChallenge(
      id = "mind_correlation_01", mode = "MIND TRIALS", title = "Correlation Trap",
      domain = "Statistics & Psychology", difficulty = "EASY",
      prompt = "A town notices that ice-cream sales and sunburn cases rise together. What can you conclude from this observation alone?",
      options = listOf("Ice cream causes sunburn", "Sunburn causes ice-cream sales", "The correlation alone does not establish causation", "There must be no relationship"),
      correctIndex = 2,
      explanation = "A third factor—hot, sunny weather—can increase both. Correlation describes association; it does not by itself identify a causal mechanism.",
      rewardXp = 20,
      skillId = "critical_reasoning"
    ),
    ArenaChallenge(
      id = "case_confirmation_01", mode = "CASE FILES", title = "The Witness",
      domain = "Cognitive Science", difficulty = "MEDIUM",
      prompt = "An investigator believes a suspect is guilty and remembers every detail that supports that belief while dismissing conflicting evidence. Which bias is most relevant?",
      options = listOf("Confirmation bias", "Anchoring effect", "Availability heuristic", "Sunk-cost fallacy"),
      correctIndex = 0,
      explanation = "Confirmation bias is the tendency to seek, interpret or recall information in ways that support an existing belief. A sound investigation actively searches for disconfirming evidence.",
      rewardXp = 35,
      skillId = "bias_detection"
    ),
    ArenaChallenge(
      id = "strategy_negotiation_01", mode = "STRATEGY", title = "The Negotiation",
      domain = "Decision Theory", difficulty = "MEDIUM",
      prompt = "You are negotiating a contract. Before discussing price, what most improves your ability to judge whether an offer is acceptable?",
      options = listOf("Make the first offer as quickly as possible", "Identify your best alternative if no agreement is reached", "Reveal your minimum acceptable price", "Assume the other side has the same priorities"),
      correctIndex = 1,
      explanation = "Your BATNA—best alternative to a negotiated agreement—provides a reference point for evaluating offers and deciding when to walk away.",
      rewardXp = 35,
      skillId = "strategic_judgment"
    ),
    ArenaChallenge(
      id = "conquest_energy_01", mode = "CONQUEST", title = "Capture: Energy",
      domain = "Physics", difficulty = "HARD",
      prompt = "A closed system undergoes a process. Which statement best expresses the first law of thermodynamics?",
      options = listOf("Energy is created whenever work is done", "The total energy of an isolated system is conserved", "Heat always flows from cold to hot", "Entropy must decrease"),
      correctIndex = 1,
      explanation = "The first law is energy conservation: changes in a system's internal energy are accounted for by energy transferred as heat and work, under the chosen sign convention.",
      rewardXp = 50,
      skillId = "scientific_reasoning"
    ),
    ArenaChallenge(
      id = "memory_spacing_01", mode = "MEMORY LAB", title = "Recall Advantage",
      domain = "Learning Science", difficulty = "EASY",
      prompt = "You need to remember a concept for a month. Which practice is generally more effective than rereading it repeatedly in one sitting?",
      options = listOf("One long rereading session", "Spaced retrieval practice across several days", "Highlighting every sentence", "Reading while distracted"),
      correctIndex = 1,
      explanation = "Retrieving information over spaced intervals strengthens long-term retention more reliably than massed rereading alone. Feedback helps correct errors.",
      rewardXp = 20,
      skillId = "memory_learning"
    ),
    ArenaChallenge(
      id = "mind_probability_02", mode = "MIND TRIALS", title = "Base Rate Check",
      domain = "Probability", difficulty = "HARD",
      prompt = "A rare condition affects 1 in 1,000 people. A test is 99% sensitive and has a 1% false-positive rate. A person tests positive. Is the chance they have the condition 99%?",
      options = listOf("Yes, sensitivity is 99%", "No. The base rate matters; the probability is about 9%", "Yes, every positive is almost certain", "It is exactly 50%"),
      correctIndex = 1,
      explanation = "Out of 100,000 people, about 100 have the condition and 99 test positive. Of 99,900 without it, about 999 test falsely positive. So roughly 99 of 1,098 positive results are true positives—about 9%.",
      rewardXp = 50,
      skillId = "probabilistic_thinking"
    )
  )

  fun challengesFor(mode: String, profile: ArenaProfile): List<ArenaChallenge> {
    val remaining = challenges.filter { mode == "ALL" || it.mode == mode }
    val unassessed = remaining.filter { (profile.skillEvidence[it.skillId]?.attempts ?: 0) == 0 }
    if (unassessed.isNotEmpty()) return unassessed
    return remaining.sortedWith(compareBy<ArenaChallenge> { profile.skillEvidence[it.skillId]?.accuracy ?: 0 }.thenBy { profile.skillEvidence[it.skillId]?.attempts ?: 0 })
  }

  fun unlockedTerritories(xp: Int): Int = when {
    xp >= 500 -> 5
    xp >= 300 -> 4
    xp >= 150 -> 3
    xp >= 50 -> 2
    else -> 1
  }
}
