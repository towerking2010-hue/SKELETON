package com.example.engine

import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting
import java.util.UUID

object SkeletonCognitiveEngine {

  /**
   * Filters out corporate filler, AI padding, and empty buzzwords
   */
  fun applyCringeFilter(text: String, enabled: Boolean): String {
    if (!enabled) return text
    var cleaned = text
    val fillerPhrases = listOf(
      "In today's fast-paced digital world,",
      "It is important to remember that",
      "As we dive into this,",
      "Let's unpack this,",
      "Delve into",
      "Needless to say,",
      "At the end of the day,",
      "A testament to",
      "Tapestry of",
      "Harness the power of",
      "In conclusion,",
      "Without further ado,",
      "It goes without saying that"
    )
    for (phrase in fillerPhrases) {
      cleaned = cleaned.replace(phrase, "", ignoreCase = true)
    }
    return cleaned.trim()
  }

  /**
   * Seed curated starter micro-learning cards from the 16 Master Curriculum Nodes
   */
  fun getStarterCards(): List<FeedCard> {
    return MasterCurriculumMatrix.getNodes()
  }

  /**
   * Generates response for CORE ENGINE:
   * Tone: brilliant, casual friend (Meta AI style), relaxed, direct, high-impact analogies.
   * Concludes with feed sync prompt if concept is novel.
   */
  fun generateCoreResponse(
    userText: String,
    targetLanguage: LanguageSetting,
    existingTopics: List<String>
  ): Pair<String, String?> {
    val lang = LanguageDetector.resolveEffectiveLanguage(targetLanguage, userText)
    val lower = userText.lowercase().trim()

    // Determine candidate topic
    val extractedTopic = extractTopicCandidate(lower)
    val isKnown = existingTopics.any { it.contains(extractedTopic, ignoreCase = true) || extractedTopic.contains(it, ignoreCase = true) }
    val suggestSync = extractedTopic.isNotBlank() && !isKnown

    val responseText = if (lang == LanguageSetting.SINHALA) {
      buildSinhalaCoreReply(userText, extractedTopic, suggestSync)
    } else {
      buildEnglishCoreReply(userText, extractedTopic, suggestSync)
    }

    return Pair(responseText, if (suggestSync) extractedTopic else null)
  }

  private fun extractTopicCandidate(input: String): String {
    val keywords = listOf(
      "raft", "paxos", "quantum computing", "crispr", "bayes", "monte carlo",
      "rust ownership", "garbage collection", "k8s", "docker", "backpropagation",
      "kalman filter", "blockchain", "merkle tree", "tcp handshake", "webrtc",
      "vector database", "lora", "diffusion model", "simulated annealing",
      "dynamic programming", "graph neural network", "sharding", "zero knowledge",
      "epigenetics", "general relativity", "thermodynamics", "neuroplasticity"
    )
    for (kw in keywords) {
      if (input.contains(kw)) return kw.replaceFirstChar { it.uppercase() }
    }
    // Fallback: extract short capitalized or quoted phrase
    val words = input.split(" ").filter { it.length > 3 && !it.matches(Regex("what|explain|tell|about|does|mean|work|how|this|that|with")) }
    return words.take(2).joinToString(" ").replaceFirstChar { it.uppercase() }.ifBlank { "Cognitive Node" }
  }

  private fun buildEnglishCoreReply(userText: String, topic: String, suggestSync: Boolean): String {
    val intuition = when {
      topic.contains("raft", ignoreCase = true) ->
        "Dasun, think of Raft as a democratic parliament with a strict stopwatch. If the current leader stops heartbeating for 150ms, any node stands up, votes for itself, and rallies a quorum."
      topic.contains("rust", ignoreCase = true) || topic.contains("ownership", ignoreCase = true) ->
        "Dasun, Rust ownership is like passing a physical torch: only one runner holds it at a time. If you pass it, you lose write privileges; if you borrow it, you must return it before the owner can leave scope."
      topic.contains("quantum", ignoreCase = true) ->
        "Dasun, classic bits are a coin lying flat on a table (Heads or Tails). A qubit is that coin spinning in mid-air—superposition holds amplitudes for both until wave-function collapse."
      topic.contains("vector", ignoreCase = true) ->
        "Dasun, vector databases map semantic ideas into high-dimensional geometric space so related concepts cluster by cosine similarity rather than brittle string matching."
      else ->
        "Dasun, here is the raw structural truth: $topic works by cutting away redundant state and enforcing invariant constraints. You strip the fluff and execute in constant space."
    }

    val body = StringBuilder()
    body.append(intuition)
    body.append("\n\n")
    body.append("Core Rule: Every complex domain has a 3-variable skeleton underneath. Master the boundary constraints, and the rest is trivial boilerplate.")

    if (suggestSync) {
      body.append("\n\n[FEED SYNC]: Add this topic to your feed? (Yes/No)")
    } else {
      body.append("\n\n[ACTIVE RECALL]: Dasun, what is the single invariant failure condition that breaks this mechanism?")
    }

    return body.toString()
  }

  private fun buildSinhalaCoreReply(userText: String, topic: String, suggestSync: Boolean): String {
    val intuition = when {
      topic.contains("raft", ignoreCase = true) ->
        "Dasun, Raft Consensus එක හරියට strict stopwatch එකක් තියෙන පාර්ලිමේන්තුවක් වගේ. Leader node එක 150ms heartbeat එක නැවැත්තුව ගමන්, ඕනෑම node එකකට candidate කෙනෙක් විදියට vote එකක් start කරලා quorum එක ගන්න පුළුවන්."
      topic.contains("rust", ignoreCase = true) || topic.contains("ownership", ignoreCase = true) ->
        "Dasun, Rust Ownership කියන්නේ physical පන්දමක් වගේ. එක වෙලාවක තියාගන්න පුළුවන් එක්කෙනෙක්ට විතරයි. තව කෙනෙක්ට pass කළාට පස්සේ original owner ට access නෑ. Memory leaks බිංදුවටම drop වෙන්නේ මේ constraint එකෙන්."
      topic.contains("quantum", ignoreCase = true) ->
        "Dasun, normal bits කියන්නේ බිම තියෙන coin එකක් (Heads or Tails). Qubit එකක් කියන්නේ කරකැවෙන coin එකක්—measure කරන මොහොත වෙනකම් states දෙකේම superposition එකක පවතිනවා."
      else ->
        "Dasun, සරල intuition එක: $topic කියන්නේ redundant state ඉවත් කරලා direct constraints run කරන system එකක්. Rule එක simple: high-impact analogy එක අල්ලගත්තම theory එක direct grasp වෙනවා."
    }

    val body = StringBuilder()
    body.append(intuition)
    body.append("\n\n")
    body.append("ප්‍රධාන Technical Core එක: ඕනෑම complex system එකක ඇතුළත තියෙන්නේ variables 2-3ක structural skeleton එකක් විතරයි.")

    if (suggestSync) {
      body.append("\n\n[FEED SYNC]: මේ topic එක feed එකට එකතු කරන්නද? (Yes/No)")
    } else {
      body.append("\n\n[ACTIVE RECALL]: Dasun, මෙම පද්ධතියේ ප්‍රධාන invariant එක බිඳවැටෙන නිශ්චිත කොන්දේසිය කුමක්ද?")
    }

    return body.toString()
  }

  /**
   * Generates micro-learning card for Feed Sync
   */
  fun createFeedSyncCard(topic: String, lang: LanguageSetting): FeedCard {
    val id = "sync_" + UUID.randomUUID().toString().take(8)
    return if (lang == LanguageSetting.SINHALA) {
      FeedCard(
        id = id,
        title = topic,
        itch = "$topic හි ප්‍රධාන ගැටලුව: High-scale environments වල complexity explode නොවී deterministic control තබාගන්නේ කෙසේද?",
        skeleton = "State Machine -> Invariant Boundary -> Atomic Commit Protocol. Redundant layers strip කර raw logic එක maintain කිරීම.",
        intuition = "සංකීර්ණ mechanisms බිඳවැටෙන්නේ rules වැඩි නිසා; $topic ක්‍රියාත්මක වෙන්නේ minimal feedback loop එකක් හරහාය.",
        branchDomain = "Systems Architecture",
        crossLinkTarget = "Fault Tolerant State",
        isSaved = true,
        timestamp = System.currentTimeMillis(),
        recallStage = 0,
        challengeScenario = "$topic හි failure state එකක් ආවොත්, system integrity එක රැකෙන්නේ කුමන invariant එක හරහාද?",
        challengeExpectedKey = "Atomic state rollback & isolated barrier execution.",
        language = "si"
      )
    } else {
      FeedCard(
        id = id,
        title = topic,
        itch = "How do you guarantee correctness in $topic without incurring unmanageable computational overhead?",
        skeleton = "State Verification -> Atomic Transition -> Strict Invariant Enforcer. Zero redundant state, deterministic synchronization.",
        intuition = "Like a rail switch system: you never check every train car; you just verify the rail interlock is locked before signaling green.",
        branchDomain = "Systems Architecture",
        crossLinkTarget = "Fault Tolerant State",
        isSaved = true,
        timestamp = System.currentTimeMillis(),
        recallStage = 0,
        challengeScenario = "When an unexpected split-state occurs in $topic, what primary invariant prevents silent data corruption?",
        challengeExpectedKey = "Quorum consensus or atomic boundary rejection.",
        language = "en"
      )
    }
  }

  /**
   * Generates DEEPDIVE ENGINE isolated breakdown:
   * 1. Brief intuition bridge
   * 2. Raw technical / scientific rules or code syntax
   * 3. Crisp scenario challenge / active-recall test
   */
  fun generateDeepDiveResponse(
    card: FeedCard,
    userQuery: String,
    targetLanguage: LanguageSetting
  ): Triple<String, String, String> {
    val lang = LanguageDetector.resolveEffectiveLanguage(targetLanguage, userQuery)

    return if (lang == LanguageSetting.SINHALA) {
      val intuitionBridge = "${card.title} පිළිබඳ සෘජු Intuition එක: ${card.intuition} මෙහිදී key point එක වෙන්නේ side-effects සීමා කර primary invariant එක focus කිරීමයි."
      val technicalBreakdown = """
        [RAW RULES / SYNTAX / FORMULATION]
        1. Base Mechanism: ${card.skeleton}
        2. Invariant Condition: ∀ state(t), validity(state) == TRUE iff consensus_quorum > N/2
        3. Code Rule:
           `fn execute_transition<S: Invariant>(node: &mut Node<S>) -> Result<Proof, Error>`
        4. Performance Boundary: O(log N) lookup, constant O(1) local commit.
      """.trimIndent()
      val scenarioChallenge = "Active Challenge: ${card.challengeScenario} (ඔබගේ කෙටි තාක්ෂණික පිළිතුර දෙන්න)."

      Triple(intuitionBridge, technicalBreakdown, scenarioChallenge)
    } else {
      val intuitionBridge = "Intuition Bridge: ${card.intuition} The core leverage is eliminating peripheral noise so only the mathematical boundary remains."
      val technicalBreakdown = """
        [RAW TECHNICAL RULES & SYNTAX]
        1. Structural Law: ${card.skeleton}
        2. Boundary Equation / Invariant:
           ∀ s ∈ States: Valid(s) ⟺ Invariant(s) ∧ Quorum(s) ≥ ⌊N/2⌋ + 1
        3. Code Contract:
           ```rust
           pub fn verify_invariant<T: Consensus>(state: &T) -> bool {
               state.generation > 0 && state.quorum_acknowledged()
           }
           ```
        4. Complexity Ceiling: O(1) state verification, zero unbuffered IO locks.
      """.trimIndent()
      val scenarioChallenge = "Active Recall Test: ${card.challengeScenario} State your exact operational mitigation in 1 sentence."

      Triple(intuitionBridge, technicalBreakdown, scenarioChallenge)
    }
  }

  /**
   * MODE [CONCEPT COLLIDER]:
   * Synthesize two distinct saved Vault concepts into a single 30-second cross-domain insight card.
   */
  fun collideConcepts(cardA: FeedCard, cardB: FeedCard, lang: LanguageSetting): FeedCard {
    val newTitle = "${cardA.title} ⊗ ${cardB.title}"
    val id = "collider_" + UUID.randomUUID().toString().take(8)

    return if (lang == LanguageSetting.SINHALA) {
      FeedCard(
        id = id,
        title = newTitle,
        itch = "ක්ෂේත්‍ර දෙකක ඡේදනය: ${cardA.branchDomain} සහ ${cardB.branchDomain} අතර සැඟවුණු සමාන්තර නීතිය කුමක්ද?",
        skeleton = "Synthesis Law: ${cardA.title} හි boundary verification මූලධර්මය, ${cardB.title} හි state distribution සඳහා direct isomorphic mapping එකක් ලබාදෙයි.",
        intuition = "එක ක්ෂේත්‍රයක ආරක්ෂක තාප්පය (${cardA.title}), අනෙක් ක්ෂේත්‍රයේ (${cardB.title}) දත්ත ගැලීම නියාමනය කරන valve එකක් ලෙස ක්‍රියා කරයි.",
        branchDomain = "Cross-Domain Synthesis",
        crossLinkTarget = "${cardA.title} + ${cardB.title}",
        isSaved = true,
        timestamp = System.currentTimeMillis(),
        recallStage = 0,
        challengeScenario = "මෙම සංයෝගයේදී එක් system එකක් fail වුවහොත්, අනෙක් component එක collapse වීම වලක්වන universal protocol එක කුමක්ද?",
        challengeExpectedKey = "Decoupled idempotent validation barriers.",
        language = "si"
      )
    } else {
      FeedCard(
        id = id,
        title = newTitle,
        itch = "Cross-Domain Singularity: How do the fundamental constraints of ${cardA.branchDomain} and ${cardB.branchDomain} intersect?",
        skeleton = "Isomorphic Synthesis: The boundary invariant of ${cardA.title} maps directly into the state compression model of ${cardB.title}. Shared topology: constrained flow under partitioned entropy.",
        intuition = "Think of ${cardA.title} as the structural blueprint and ${cardB.title} as the dynamic energy flow running through it—neither can drift without the other.",
        branchDomain = "Cross-Domain Synthesis",
        crossLinkTarget = "${cardA.title} + ${cardB.title}",
        isSaved = true,
        timestamp = System.currentTimeMillis(),
        recallStage = 0,
        challengeScenario = "If you enforce the invariant of ${cardA.title} directly upon ${cardB.title}, which redundant failure mode is completely eliminated?",
        challengeExpectedKey = "Elimination of cascading split-brain desynchronization.",
        language = "en"
      )
    }
  }

  /**
   * MODE [GHOST SNIPER OCR]:
   * Extracts Itch + Skeleton from raw scanned textbook or article notes.
   */
  fun sniperOcrExtraction(rawText: String, lang: LanguageSetting): FeedCard {
    val title = rawText.lines().firstOrNull { it.isNotBlank() }?.take(35)?.trim() ?: "Extracted Node"
    val id = "ocr_" + UUID.randomUUID().toString().take(8)

    return if (lang == LanguageSetting.SINHALA) {
      FeedCard(
        id = id,
        title = title,
        itch = "Scanned Extract හි core ගැටලුව: ${rawText.take(120).trim()}...",
        skeleton = "Extracted Skeleton: 1. Input Source -> 2. Invariant Filtering -> 3. Actionable Output. සියලු filler වචන ඉවත් කර සෘජු නීති තහවුරු කරන ලදී.",
        intuition = "පොතේ තිබූ පිටු ගණනාවක විස්තරය තත්පර 10කින් grasp කරගත හැකි single structural framework එකක් බවට පත්කිරීම.",
        branchDomain = "OCR Extraction",
        crossLinkTarget = "Knowledge Vault",
        isSaved = true,
        timestamp = System.currentTimeMillis(),
        recallStage = 0,
        challengeScenario = "මෙම extracted concept එකෙහි වඩාත්ම අවදානම් උපකල්පනය කුමක්ද?",
        challengeExpectedKey = "Input boundary validation failure.",
        language = "si"
      )
    } else {
      FeedCard(
        id = id,
        title = title,
        itch = "Core tension extracted from scan: ${rawText.take(130).trim()}...",
        skeleton = "Extracted Skeleton: 1. Target Condition -> 2. Enforced Constraint -> 3. Deterministic Result. Academic filler stripped to zero-entropy logic.",
        intuition = "Condensing an entire chapter of textbook jargon down to the single mathematical wire that actually conducts the current.",
        branchDomain = "OCR Extraction",
        crossLinkTarget = "Knowledge Vault",
        isSaved = true,
        timestamp = System.currentTimeMillis(),
        recallStage = 0,
        challengeScenario = "Under which edge case does the primary premise of this scanned rule break down?",
        challengeExpectedKey = "Unbounded unbounded memory or unverified boundary conditions.",
        language = "en"
      )
    }
  }
}
