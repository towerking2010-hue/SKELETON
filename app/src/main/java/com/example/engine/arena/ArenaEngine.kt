package com.example.engine.arena

import com.example.data.model.LanguageSetting

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
    ArenaSkill("probabilistic_thinking", "Probabilistic thinking", "Using base rates and numerical uncertainty.", ArenaSkillEvidence()),
    ArenaSkill("attention_control", "Attention control", "Protecting task focus and recovering from interruptions.", ArenaSkillEvidence()),
    ArenaSkill("systems_thinking", "Systems thinking", "Recognizing feedback loops, delays and second-order effects.", ArenaSkillEvidence())
  )

  fun skillsFor(profile: ArenaProfile, language: LanguageSetting = LanguageSetting.ENGLISH): List<ArenaSkill> = skillCatalog.map { skill ->
    val label = if (language == LanguageSetting.SINHALA) when (skill.id) {
      "critical_reasoning" -> "විවේචනාත්මක තර්කය"
      "bias_detection" -> "පක්ෂපාතීත්වය හඳුනාගැනීම"
      "strategic_judgment" -> "උපායමාර්ගික තීරණ"
      "scientific_reasoning" -> "විද්‍යාත්මක තර්කය"
      "memory_learning" -> "මතකය සහ ඉගෙනීම"
      "probabilistic_thinking" -> "සම්භාවිතා සිතීම"
      "attention_control" -> "අවධානය පාලනය"
      else -> "පද්ධතිමය සිතීම"
    } else skill.label
    skill.copy(label = label, evidence = profile.skillEvidence[skill.id] ?: ArenaSkillEvidence())
  }

  fun modeLabel(mode: String, language: LanguageSetting): String =
    if (language == LanguageSetting.ENGLISH) mode else when (mode) {
      "ALL" -> "සියල්ල"
      "MIND TRIALS" -> "චින්තන පරීක්ෂණ"
      "CASE FILES" -> "සිද්ධි විමර්ශන"
      "STRATEGY" -> "උපායමාර්ග"
      "CONQUEST" -> "ජයග්‍රහණ"
      "MEMORY LAB" -> "මතක පුහුණුව"
      else -> mode
    }

  private fun localize(challenge: ArenaChallenge, language: LanguageSetting): ArenaChallenge {
    if (language == LanguageSetting.ENGLISH) return challenge
    val localized = when (challenge.id) {
      "mind_correlation_01" -> challenge.copy(
        title = "එකට වෙනස් වීම හේතුවක්ද?",
        domain = "සංඛ්‍යාලේඛන සහ මනෝවිද්‍යාව",
        prompt = "නගරයක අයිස්ක්‍රීම් අලෙවියත් අව්වට සම පිළිස්සීම්ත් එකම කාලයේ වැඩි වේ. මේ නිරීක්ෂණයෙන් පමණක් නිගමනය කළ හැක්කේ කුමක්ද?",
        options = listOf("අයිස්ක්‍රීම් නිසා සම පිළිස්සෙයි", "සම පිළිස්සීම නිසා අයිස්ක්‍රීම් අලෙවිය වැඩිවේ", "එකට වැඩිවීමෙන් හේතු-ඵල සම්බන්ධයක් ඔප්පු නොවේ", "ඒ දෙක අතර කිසිම සම්බන්ධයක් නැත"),
        explanation = "උණුසුම්, අව්ව සහිත කාලගුණය දෙකටම පොදු හේතුව විය හැකියි. එකට වෙනස් වීමෙන් එකක් අනෙක ඇති කළ බව ඔප්පු නොවේ."
      )
      "case_confirmation_01" -> challenge.copy(
        title = "සාක්ෂිකරුගේ වැරැද්ද",
        domain = "සංජානන විද්‍යාව",
        prompt = "විමර්ශකයෙකු සැකකරුවෙකු වැරදිකරු යැයි කලින්ම සිතයි. තම අදහසට ගැළපෙන සාක්ෂි පමණක් මතක තබාගෙන විරුද්ධ සාක්ෂි නොසලකා හරියි. මෙය කුමන මානසික නැඹුරුවද?",
        options = listOf("තම අදහස තහවුරු කරන දේ පමණක් තෝරාගැනීම", "මුලින් ඇසූ තොරතුරට අධික බර දීම", "මතකයට පහසුවෙන් එන දේ වැඩියෙන් විශ්වාස කිරීම", "දැනට වැය කළ දේ නිසාම දිගටම යාම"),
        explanation = "තමන් දැනට විශ්වාස කරන දෙයට ගැළපෙන තොරතුරු පමණක් සොයාගැනීම හෝ පිළිගැනීම මෙහි ලක්ෂණයයි. හොඳ විමර්ශනයක් තම අදහස වැරදි බව පෙන්විය හැකි සාක්ෂිද සොයයි."
      )
      "strategy_negotiation_01" -> challenge.copy(
        title = "සාකච්ඡාවේ බලය",
        domain = "තීරණ ගැනීම",
        prompt = "ඔබ ගිවිසුමක් ගැන සාකච්ඡා කරයි. මිල ගැන කතා කිරීමට පෙර, ලැබෙන යෝජනාව පිළිගත යුතුදැයි තීරණය කිරීමට වඩාත් උපකාරී වන්නේ කුමක්ද?",
        options = listOf("ඉක්මනින්ම පළමු මිල ඉදිරිපත් කිරීම", "ගිවිසුමක් නොලැබුණොත් ඇති හොඳම විකල්පය හඳුනාගැනීම", "ඔබ පිළිගන්නා අවම මිල හෙළි කිරීම", "අනෙක් පාර්ශ්වයටත් ඔබේ අවශ්‍යතාම ඇතැයි සිතීම"),
        explanation = "ගිවිසුමක් නොලැබුණොත් ඔබට කළ හැකි හොඳම විකල්පය, ලැබෙන යෝජනාව ඇගයීමට මිනුමක් සපයයි. එය පිළිගන්නවාද නැද්ද යන්න තීරණය කිරීමට උපකාරී වේ."
      )
      "conquest_energy_01" -> challenge.copy(
        title = "ශක්තියේ නියමය",
        domain = "භෞතික විද්‍යාව",
        prompt = "වෙන්වූ පද්ධතියක් තුළ ක්‍රියාවලියක් සිදුවේ. තාපගති විද්‍යාවේ පළමු නියමය නිවැරදිව පෙන්වන්නේ කුමන ප්‍රකාශයද?",
        options = listOf("කාර්යයක් කරන විට ශක්තිය අලුතින් නිර්මාණය වේ", "වෙන්වූ පද්ධතියක මුළු ශක්තිය සංරක්ෂිත වේ", "තාපය සැමවිටම සීතලෙන් උණුසුමට ගලා යයි", "අවුල්සහගතභාවය සැමවිටම අඩු විය යුතුය"),
        explanation = "ශක්තිය නිර්මාණය හෝ විනාශ නොවේ. පද්ධතියක ශක්ති වෙනස තාපය සහ කාර්යය හරහා ඇතුළු වන හෝ පිටවන ශක්තියෙන් පැහැදිලි කළ හැක."
      )
      "memory_spacing_01" -> challenge.copy(
        title = "මතකයේ වාසිය",
        domain = "ඉගෙනීමේ විද්‍යාව",
        prompt = "මාසයකට පසුත් අදහසක් මතක තබාගැනීමට අවශ්‍යයි. එකම දවසේ නැවත නැවත කියවීමට වඩා සාමාන්‍යයෙන් හොඳ ක්‍රමය කුමක්ද?",
        options = listOf("එක දිගටම දිගු වේලාවක් කියවීම", "දින කිහිපයක් පුරා කාල පරතර තබා මතකයෙන් පිළිතුරු දීම", "සෑම වාක්‍යයක්ම පාට කිරීම", "අවධානය වෙනත් දේවලට යොමු කරගෙන කියවීම"),
        explanation = "කාල පරතර තබා මතකයෙන් තොරතුරු නැවත ලබාගැනීම දිගුකාලීන මතකය ශක්තිමත් කරයි. වැරදි පිළිතුරු නිවැරදි කරගැනීමද වැදගත්ය."
      )
      "mind_probability_02" -> challenge.copy(
        title = "මූලික අනුපාතය",
        domain = "සම්භාවිතාව",
        prompt = "දුර්ලභ රෝගයක් පුද්ගලයන් දහසකින් එක් අයෙකුට ඇත. පරීක්ෂණය රෝගීන්ගෙන් 99%ක් හඳුනාගනී; නිරෝගී අයගෙන් 1%කට වැරදි ධනාත්මක ප්‍රතිඵලයක් දෙයි. ප්‍රතිඵලය ධනාත්මක නම් රෝගය ඇති සම්භාවිතාව 99%ද?",
        options = listOf("ඔව්, පරීක්ෂණයේ නිරවද්‍යතාව 99%යි", "නැහැ. රෝගය දුර්ලභ බව සලකා බැලූ විට එය ආසන්න වශයෙන් 9%යි", "ඔව්, ධනාත්මක සියල්ලන්ටම රෝගය ඇත", "එය හරියටම 50%යි"),
        explanation = "රෝගය ඉතා දුර්ලභ නිසා වැරදි ධනාත්මක ප්‍රතිඵල ගණන සැබෑ රෝගීන්ට වඩා වැඩි විය හැක. එබැවින් පරීක්ෂණයේ සංවේදීතාව පමණක් නොව රෝගය පැතිරී ඇති මූලික අනුපාතයද සලකා බැලිය යුතුය."
      )
      "mind_attention_01" -> challenge.copy(
        title = "අවධානය රැකගැනීම",
        domain = "අවධානය සහ සිත පාලනය",
        prompt = "ඔබට විනාඩි 25ක් පුරා අවධානයෙන් සිතීමට අවශ්‍ය කාර්යයක් ඇත. නිතර දැනුම්දීම් පෙන්වයි. අවධානය රැකගැනීමට හොඳම ක්‍රියාව කුමක්ද?",
        options = listOf("සෑම දැනුම්දීමක්ම වහාම බැලීම", "උනන්දුව තබාගැනීමට නිතර කාර්ය මාරු කිරීම", "බාධා නිහඬ කර හදිසි දේ සටහන් කර නියමිත විවේකයේදී බැලීම", "සියලු යෙදුම් විවෘතව තබා ඒවා නිරීක්ෂණය කිරීම"),
        explanation = "බාධා අඩු කිරීම සහ හදිසි නොවන දේ එකවර පසුව බැලීම දිගටම අවධානය යොමු කිරීමට උපකාරී වේ."
      )
      "case_systems_01" -> challenge.copy(
        title = "ප්‍රමාද වන ප්‍රතිචාරය",
        domain = "පද්ධතිමය සිතීම",
        prompt = "ගොදුරු සතුන් වැඩි වූ විට ඔවුන් අනුභව කරන විලෝපික සතුන්ද වැඩිවේ. පසුව ගොදුරු සතුන් අඩුවන අතර තවත් කලකට පසු විලෝපිකයන්ද අඩුවේ. මේ ප්‍රමාදය පැහැදිලි කරන්නේ කුමක්ද?",
        options = listOf("කාල ප්‍රමාදයක් සහිත ප්‍රතිචාර වටයක්", "එකිනෙකට සම්බන්ධ නොවූ සිදුවීමක්", "අන්තර්ක්‍රියා නැති පද්ධතියක්", "කිසිදා වෙනස් නොවන සරල ප්‍රවණතාවක්"),
        explanation = "ගොදුරු සහ විලෝපික ජනගහණ එකිනෙකට බලපාන අතර ඒ බලපෑම් ප්‍රමාද වී පෙනිය හැක. පද්ධතියක් තේරුම් ගැනීමට සම්බන්ධතා, ප්‍රතිචාර වට සහ ප්‍රමාදයන් සලකා බැලිය යුතුය."
      )
      else -> challenge
    }
    return localized
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
    ),
    ArenaChallenge(
      id = "mind_attention_01", mode = "MIND TRIALS", title = "Signal Through Noise",
      domain = "Attention & Executive Control", difficulty = "EASY",
      prompt = "You have a 25-minute task requiring careful reasoning. Notifications keep appearing. Which response best protects focused work?",
      options = listOf("Check every notification immediately", "Keep switching tasks to stay stimulated", "Silence interruptions, note anything urgent, and return at a planned break", "Leave every app open and monitor them"),
      correctIndex = 2,
      explanation = "Reducing interruptions and batching non-urgent checks protects sustained attention. This tests a practical strategy, not a clinical measure of attention capacity.",
      rewardXp = 20,
      skillId = "attention_control"
    ),
    ArenaChallenge(
      id = "case_systems_01", mode = "CASE FILES", title = "The Delayed Loop",
      domain = "Systems Thinking", difficulty = "MEDIUM",
      prompt = "A predator population grows after prey become abundant. The prey then decline, followed later by a predator decline. What concept best explains the lag?",
      options = listOf("A delayed feedback loop", "A single isolated event", "A system with no interactions", "A guaranteed linear trend"),
      correctIndex = 0,
      explanation = "Predator and prey populations influence each other, often with time delays. Systems thinking looks for relationships, feedback and delayed consequences rather than one-step causes.",
      rewardXp = 35,
      skillId = "systems_thinking"
    )
  )

  fun challengesFor(mode: String, profile: ArenaProfile, language: LanguageSetting = LanguageSetting.ENGLISH): List<ArenaChallenge> {
    val remaining = challenges.filter { mode == "ALL" || it.mode == mode }
    val unassessed = remaining.filter { (profile.skillEvidence[it.skillId]?.attempts ?: 0) == 0 }
    val ordered = if (unassessed.isNotEmpty()) unassessed else remaining.sortedWith(compareBy<ArenaChallenge> { profile.skillEvidence[it.skillId]?.accuracy ?: 0 }.thenBy { profile.skillEvidence[it.skillId]?.attempts ?: 0 })
    return ordered.map { localize(it, language) }
  }

  fun unlockedTerritories(xp: Int): Int = when {
    xp >= 500 -> 5
    xp >= 300 -> 4
    xp >= 150 -> 3
    xp >= 50 -> 2
    else -> 1
  }
}
