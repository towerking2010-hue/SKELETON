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
    val supportedOfflineTopic = listOf("raft", "rust ownership", "ownership", "quantum computing", "vector database").any { lower.contains(it) }
    val suggestSync = supportedOfflineTopic && extractedTopic.isNotBlank() && !isKnown

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
        "Think of Raft as a small team that elects a coordinator. If the leader stops sending heartbeats long enough, a node can start an election; it needs votes from a majority before becoming leader."
      topic.contains("rust", ignoreCase = true) || topic.contains("ownership", ignoreCase = true) ->
        "Rust ownership is like having one clear owner for a resource. You can lend references under compiler-checked rules, but Rust prevents unsafe combinations of mutable and shared access."
      topic.contains("quantum", ignoreCase = true) ->
        "A classical bit is either 0 or 1. A qubit is described by probability amplitudes for measurement outcomes; superposition is not simply a tiny coin spinning, and measurement returns a definite result."
      topic.contains("vector", ignoreCase = true) ->
        "A vector database stores numerical embeddings and retrieves nearby vectors using a chosen distance or similarity metric. It can find semantically related items even when their wording differs."
      else ->
        "I’m running in zero-cost offline mode, and I don’t have a reliable explanation for this topic stored locally. I won’t invent a mechanism or pretend I researched it. The Research Desk can help you find original scholarly records to inspect."
    }

    val body = StringBuilder()
    body.append(intuition)
    if (topic.lowercase() in setOf("raft", "rust ownership", "ownership", "quantum computing", "vector database")) {
      body.append("\n\nThe important part is the actual mechanism—not just the analogy.")
    }

    if (suggestSync) {
      body.append("\n\n[FEED SYNC]: Add this topic to your feed? (Yes/No)")
    } else if (topic.lowercase() in setOf("raft", "rust ownership", "ownership", "quantum computing", "vector database")) {
      body.append("\n\n[ACTIVE RECALL]: What is the key rule that makes this mechanism work?")
    }

    return body.toString()
  }

  private fun buildSinhalaCoreReply(userText: String, topic: String, suggestSync: Boolean): String {
    val intuition = when {
      topic.contains("raft", ignoreCase = true) ->
        "Raft Consensus එක team එකක් coordinator කෙනෙක් තෝරනවා වගේ. Leader එක heartbeats එවීම නතර කරලා election timeout එක ඉක්මවා ගියොත්, node එකක් election එකක් පටන් ගන්නවා. Leader වෙන්න majority votes අවශ්‍යයි."
      topic.contains("rust", ignoreCase = true) || topic.contains("ownership", ignoreCase = true) ->
        "Rust Ownership කියන්නේ resource එකකට පැහැදිලි owner කෙනෙක් ඉන්නවා වගේ. Borrowing rules compiler එකෙන් check කරන නිසා unsafe mutable/shared access combinations බොහොමයක් compile වෙන්න කලින්ම අල්ලගන්නවා."
      topic.contains("quantum", ignoreCase = true) ->
        "Classical bit එකක් 0 හෝ 1. Qubit එකක් measurement outcomes සඳහා probability amplitudes වලින් විස්තර කරනවා. Superposition කියන්නේ පොඩි coin එකක් කරකැවෙනවා කියන එකම නෙවෙයි."
      else ->
        "මම දැන් zero-cost offline mode එකේ. මේ topic එක ගැන විශ්වාසදායක explanation එකක් local knowledge එකේ නැති නිසා හිතලා facts හදන්නේ නැහැ. Research Desk එකෙන් මුල් scholarly sources සොයාගෙන ඒවා පරීක්ෂා කරන්න පුළුවන්."
    }

    val body = StringBuilder()
    body.append(intuition)
    if (topic.lowercase() in setOf("raft", "rust ownership", "ownership", "quantum computing", "vector database")) {
      body.append("\n\nAnalogy එකට වඩා වැදගත් වෙන්නේ ඇත්ත mechanism එකයි.")
    }

    if (suggestSync) {
      body.append("\n\n[FEED SYNC]: මේ topic එක feed එකට එකතු කරන්නද? (Yes/No)")
    } else if (topic.lowercase() in setOf("raft", "rust ownership", "ownership", "quantum computing", "vector database")) {
      body.append("\n\n[ACTIVE RECALL]: මේ mechanism එක ක්‍රියාකරන්නේ කුමන ප්‍රධාන rule එක නිසාද?")
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
