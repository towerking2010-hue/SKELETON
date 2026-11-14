package com.example.data.repository

import com.example.data.local.SkeletonDao
import com.example.data.model.CoreChatMessage
import com.example.data.model.DeepDiveMessage
import com.example.data.model.FeedCard
import com.example.data.model.GhostChallenge
import com.example.data.model.LanguageSetting
import com.example.engine.SkeletonCognitiveEngine
import com.example.engine.ai.AiRole
import com.example.engine.ai.GeminiCognitiveService
import com.example.engine.ai.ModelTier
import com.example.engine.learning.SpacedRepetitionScheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.util.UUID

class SkeletonRepository(private val dao: SkeletonDao) {

  val feedCards: Flow<List<FeedCard>> = dao.getAllFeedCards()
  /** Re-evaluates wall-clock due times every minute while still reacting immediately to Room changes. */
  @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
  fun dueReviewCards(): Flow<List<FeedCard>> = flow {
    while (true) {
      emit(System.currentTimeMillis())
      delay(60_000L)
    }
  }.flatMapLatest { now -> dao.getDueReviewCards(now) }
  val savedCards: Flow<List<FeedCard>> = dao.getSavedCards()
  val coreMessages: Flow<List<CoreChatMessage>> = dao.getAllCoreMessages()
  val ghostChallenges: Flow<List<GhostChallenge>> = dao.getAllGhostChallenges()

  suspend fun initializeStarterCardsIfEmpty() {
    val existing = dao.getAllFeedCards().firstOrNull()
    if (existing.isNullOrEmpty()) {
      val starter = SkeletonCognitiveEngine.getStarterCards() + com.example.engine.bio.BioscienceModule.getBioscienceCuratedCards()
      dao.insertFeedCards(starter)
      dao.insertCoreMessage(
        CoreChatMessage(
          id = UUID.randomUUID().toString(),
          isUser = false,
          text = "Hey! I’m here. What’s something you’ve been curious about lately? We can figure it out together, one small piece at a time."
        )
      )
    }
  }

  suspend fun switchCurriculumLanguage(lang: LanguageSetting) {
    val existingById = dao.getAllFeedCards().firstOrNull().orEmpty().associateBy { it.id }
    val localizedNodes = com.example.engine.MasterCurriculumMatrix.getNodes(lang).map { localized ->
      val existing = existingById[localized.id]
      if (existing == null) localized else localized.copy(
        isSaved = existing.isSaved,
        timestamp = existing.timestamp,
        recallStage = existing.recallStage,
        lastReviewedTimestamp = existing.lastReviewedTimestamp,
        weakRecallCount = existing.weakRecallCount
      )
    }
    dao.insertFeedCards(localizedNodes)
  }

  fun getFeedCard(id: String): Flow<FeedCard?> = dao.getFeedCardById(id)

  fun getDeepDiveMessages(cardId: String): Flow<List<DeepDiveMessage>> =
    dao.getDeepDiveMessages(cardId)

  suspend fun toggleSaveCard(card: FeedCard) {
    dao.updateSavedState(card.id, !card.isSaved)
  }

  suspend fun insertFeedCard(card: FeedCard) {
    dao.insertFeedCard(card)
  }

  suspend fun deleteFeedCard(card: FeedCard) {
    dao.deleteFeedCard(card)
  }

  suspend fun sendCoreUserMessage(
    text: String,
    systemLanguage: LanguageSetting,
    tier: ModelTier = ModelTier.GENERAL,
    role: AiRole = AiRole.SKELETON_CORE
  ) {
    dao.insertCoreMessage(
      CoreChatMessage(id = UUID.randomUUID().toString(), isUser = true, text = text)
    )

    val cards = dao.getAllFeedCards().firstOrNull().orEmpty()
    val existingTitles = cards.map { it.title }
    val (_, novelTopic) = SkeletonCognitiveEngine.generateCoreResponse(
      userText = text,
      targetLanguage = systemLanguage,
      existingTopics = existingTitles
    )

    // Include recent turns explicitly so context survives app restarts without relying on
    // an in-memory SDK chat session.
    val history = dao.getAllCoreMessages().firstOrNull().orEmpty()
      .takeLast(13)
      .joinToString("\n") { message ->
        "${if (message.isUser) "USER" else "CORE"}: ${message.text}"
      }
    val languageRule = if (systemLanguage == LanguageSetting.SINHALA) {
      "Default educational language: natural, fluent Sinhala script. Keep established technical terms in English where useful."
    } else {
      "Default educational language: clear, natural English."
    }
    val prompt = """
      $languageRule
      If the user's latest message is written in another language, answer that turn in that language.
      You are a genuinely conversational learning companion, not a lecturer, textbook, search-result list, or customer-support bot.
      Talk with the learner like a sharp, warm, curious friend: natural language, contractions, a little personality, and direct reactions to what they actually said.
      Keep each reply to one small conversational step by default (usually 2–5 short sentences). Never dump a full lesson, numbered outline, glossary, or wall of facts unless the learner explicitly asks for a complete explanation, list, or deep technical breakdown.
      Start by responding to the learner's point or question. Teach one idea at a time using a simple analogy or concrete example, then pause and invite the learner to think, react, or ask the next question. Ask at most one natural follow-up question; do not tack on a quiz every time.
      Adapt to the learner's level and pace. If they seem confused, simplify and try a different example. If they are curious, build on that curiosity. If they are joking or chatting, chat back naturally instead of forcing a lesson.
      Keep the conversation coherent using recent history. Do not repeat facts they already understand.
      Be accurate: separate established evidence from hypotheses and symbolic/esoteric interpretations. Admit uncertainty and never invent sources.
      Only when a genuinely useful new learning insight emerged, append this exact final marker on its own line: [FEED SYNC: <short topic>]. Do not append it to ordinary chat, greetings, follow-up questions, or every reply.
      
      RECENT CONVERSATION:
      $history
      
      Answer the latest USER message. Do not repeat the conversation transcript.
    """.trimIndent()

    val generated = GeminiCognitiveService.chatGenerate(
      userPrompt = prompt,
      role = role,
      tier = tier
    )
    val offlineFallback = generated.startsWith("Inference error") ||
      generated.startsWith("Synthesis complete without output")
    val aiReplyText = if (offlineFallback) {
      val (fallback, _) = SkeletonCognitiveEngine.generateCoreResponse(
        userText = text,
        targetLanguage = systemLanguage,
        existingTopics = existingTitles
      )
      "$fallback\n\n[OFFLINE MODE] Cloud AI is unavailable; this is the local fallback response."
    } else {
      generated
    }

    val generatedTopic = if (offlineFallback) null else {
      Regex("""\[FEED\s+SYNC:\s*(.+?)\]""", RegexOption.IGNORE_CASE)
        .find(generated)?.groupValues?.getOrNull(1)?.trim()?.takeIf { it.isNotBlank() }
    }
    val syncTopic = generatedTopic ?: if (offlineFallback) novelTopic else null
    dao.insertCoreMessage(
      CoreChatMessage(
        id = UUID.randomUUID().toString(),
        isUser = false,
        text = aiReplyText,
        feedSyncTopic = syncTopic,
        feedSyncAdded = false
      )
    )
  }

  suspend fun executeFeedSync(message: CoreChatMessage, topic: String, lang: LanguageSetting) {
    val newCard = SkeletonCognitiveEngine.createFeedSyncCard(topic, lang)
    dao.insertFeedCard(newCard)
    dao.markFeedSyncAdded(message.id)
  }

  suspend fun sendDeepDiveMessage(
    cardId: String,
    userQuery: String,
    systemLanguage: LanguageSetting,
    tier: ModelTier = ModelTier.GENERAL
  ) {
    dao.insertDeepDiveMessage(
      DeepDiveMessage(
        id = UUID.randomUUID().toString(),
        cardId = cardId,
        isUser = true,
        text = userQuery
      )
    )

    val card = dao.getFeedCardByIdDirect(cardId) ?: return
    val history = dao.getDeepDiveMessages(cardId).firstOrNull().orEmpty()
      .takeLast(13)
      .joinToString("\n") { message ->
        "${if (message.isUser) "LEARNER" else "DEEPDIVE"}: ${message.text}"
      }
    val languageRule = if (systemLanguage == LanguageSetting.SINHALA) {
      "Write all educational explanation in natural Sinhala script. Keep technical keywords, equations and standard academic terms in English."
    } else {
      "Write in clear, precise English."
    }
    val prompt = """
      You are in an isolated DEEPDIVE thread for this one lesson only.
      $languageRule
      If the learner's latest query is in another language, answer this turn in that language.
      Lesson title: ${card.title}
      Subject/domain: ${card.branchDomain}
      Intuition seed: ${card.intuition}
      Core mechanism: ${card.skeleton}
      This is a conversation with a curious learner, not a report-writing task.
      Be warm, natural, responsive and intellectually playful. React to the exact question first. Teach in small turns: one useful insight, one vivid example or analogy, then stop and let the learner respond.
      Do not automatically produce headings, numbered lists, equations, exhaustive mechanisms, or long explanations. Bring in rigorous mechanisms, equations, evidence and assumptions gradually, only when the learner asks for depth or the concept genuinely needs it.
      Use Socratic teaching gently: ask one thoughtful question when it helps reveal what the learner understands, but never interrogate them or end every message with a test.
      If the learner is stuck, change the analogy and explain the missing step. If they ask for a full breakdown, then provide it.
      Maintain continuity with this lesson's conversation only. Never invent citations or claim certainty without evidence. Keep symbolic/esoteric claims clearly distinguished from empirical findings.
      End naturally. Do not force a quiz or recall prompt into every answer.
      
      THREAD HISTORY:
      $history
      
      Answer the latest LEARNER message, not the entire transcript.
    """.trimIndent()

    val generated = GeminiCognitiveService.chatGenerate(
      userPrompt = prompt,
      role = AiRole.DEEPDIVE_SCIENTIST,
      tier = tier
    )
    val offlineFallback = generated.startsWith("Inference error") ||
      generated.startsWith("Synthesis complete without output")
    val answer = if (offlineFallback) {
      val (intuition, technical, challenge) = SkeletonCognitiveEngine.generateDeepDiveResponse(
        card = card,
        userQuery = userQuery,
        targetLanguage = systemLanguage
      )
      "$intuition\n\n$technical\n\n$challenge\n\n[OFFLINE MODE] Cloud AI is unavailable; this is the local fallback."
    } else {
      generated
    }

    dao.insertDeepDiveMessage(
      DeepDiveMessage(
        id = UUID.randomUUID().toString(),
        cardId = cardId,
        isUser = false,
        text = answer,
        technicalBreakdown = answer
      )
    )
  }

  suspend fun evaluateSpacedRepetition(cardId: String, wasSuccessful: Boolean) {
    val card = dao.getFeedCardByIdDirect(cardId) ?: return
    val review = SpacedRepetitionScheduler.review(
      stage = card.recallStage,
      weakCount = card.weakRecallCount,
      successful = wasSuccessful,
      now = System.currentTimeMillis()
    )
    dao.updateSpacedRepetition(
      cardId,
      review.stage,
      review.weakRecallCount,
      review.lastReviewedTimestamp
    )
  }

  suspend fun createGhostChallenge(card: FeedCard, lang: LanguageSetting) {
    val prompt = "Mastery Challenge [${card.title}]: ${card.challengeScenario}"
    val challenge = GhostChallenge(
      id = UUID.randomUUID().toString(),
      cardId = card.id,
      cardTitle = card.title,
      promptQuestion = prompt,
      expectedKeywords = card.challengeExpectedKey,
      language = lang.code
    )
    dao.insertGhostChallenge(challenge)
  }

  suspend fun answerGhostChallenge(challenge: GhostChallenge, answer: String): Boolean {
    // Conservative local evaluation: require meaningful overlap with the stored answer key.
    // A semantic evaluator can replace this once the AI evaluation endpoint is wired and tested.
    val expectedTokens = challenge.expectedKeywords.lowercase()
      .split(Regex("[^\\p{L}\\p{N}]+"))
      .filter { it.length >= 4 }
      .toSet()
    val answerTokens = answer.lowercase()
      .split(Regex("[^\\p{L}\\p{N}]+"))
      .filter { it.length >= 4 }
      .toSet()
    val overlap = expectedTokens.intersect(answerTokens).size
    val passed = expectedTokens.isNotEmpty() && overlap >= minOf(2, expectedTokens.size)
    val updated = challenge.copy(
      status = if (passed) "CLEARED" else "FAILED",
      userResponse = answer
    )
    dao.updateGhostChallenge(updated)
    evaluateSpacedRepetition(challenge.cardId, passed)
    return passed
  }
}
