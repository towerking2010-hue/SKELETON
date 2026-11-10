package com.example.data.repository

import com.example.data.local.SkeletonDao
import com.example.data.model.CoreChatMessage
import com.example.data.model.DeepDiveMessage
import com.example.data.model.FeedCard
import com.example.data.model.GhostChallenge
import com.example.data.model.LanguageSetting
import com.example.engine.SkeletonCognitiveEngine
import com.example.engine.learning.SpacedRepetitionScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class SkeletonRepository(private val dao: SkeletonDao) {

  val feedCards: Flow<List<FeedCard>> = dao.getAllFeedCards()
  fun dueReviewCards(now: Long): Flow<List<FeedCard>> = dao.getDueReviewCards(now)
  val savedCards: Flow<List<FeedCard>> = dao.getSavedCards()
  val coreMessages: Flow<List<CoreChatMessage>> = dao.getAllCoreMessages()
  val ghostChallenges: Flow<List<GhostChallenge>> = dao.getAllGhostChallenges()

  suspend fun initializeStarterCardsIfEmpty() {
    val existing = dao.getAllFeedCards().firstOrNull()
    if (existing.isNullOrEmpty()) {
      val starter = SkeletonCognitiveEngine.getStarterCards() + com.example.engine.bio.BioscienceModule.getBioscienceCuratedCards()
      dao.insertFeedCards(starter)
      // Also add initial welcome message from CORE
      dao.insertCoreMessage(
        CoreChatMessage(
          id = UUID.randomUUID().toString(),
          isUser = false,
          text = "SKELETON initialized. Welcome, Dasun. Omniscient Cognitive Engine, 19 Master Curriculum Keys & Gemini AI active."
        )
      )
    }
  }

  suspend fun switchCurriculumLanguage(lang: LanguageSetting) {
    val localizedNodes = com.example.engine.MasterCurriculumMatrix.getNodes(lang)
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

  suspend fun sendCoreUserMessage(text: String, systemLanguage: LanguageSetting) {
    val userMsg = CoreChatMessage(
      id = UUID.randomUUID().toString(),
      isUser = true,
      text = text
    )
    dao.insertCoreMessage(userMsg)

    // Query existing topics to detect if Feed Sync should trigger
    val cards = dao.getAllFeedCards().firstOrNull().orEmpty()
    val existingTitles = cards.map { it.title }

    val (aiReplyText, novelTopic) = SkeletonCognitiveEngine.generateCoreResponse(
      userText = text,
      targetLanguage = systemLanguage,
      existingTopics = existingTitles
    )

    val aiMsg = CoreChatMessage(
      id = UUID.randomUUID().toString(),
      isUser = false,
      text = aiReplyText,
      feedSyncTopic = novelTopic,
      feedSyncAdded = false
    )
    dao.insertCoreMessage(aiMsg)
  }

  suspend fun executeFeedSync(message: CoreChatMessage, topic: String, lang: LanguageSetting) {
    val newCard = SkeletonCognitiveEngine.createFeedSyncCard(topic, lang)
    dao.insertFeedCard(newCard)
    dao.markFeedSyncAdded(message.id)
  }

  suspend fun sendDeepDiveMessage(
    cardId: String,
    userQuery: String,
    systemLanguage: LanguageSetting
  ) {
    // 1. Save user query
    val userMsg = DeepDiveMessage(
      id = UUID.randomUUID().toString(),
      cardId = cardId,
      isUser = true,
      text = userQuery
    )
    dao.insertDeepDiveMessage(userMsg)

    // 2. Load card
    val card = dao.getFeedCardByIdDirect(cardId) ?: return

    // 3. Generate isolated 2-layer breakdown + scenario challenge
    val (intuition, technical, challenge) = SkeletonCognitiveEngine.generateDeepDiveResponse(
      card = card,
      userQuery = userQuery,
      targetLanguage = systemLanguage
    )

    val aiMsg = DeepDiveMessage(
      id = UUID.randomUUID().toString(),
      cardId = cardId,
      isUser = false,
      text = "$intuition\n\n$technical\n\n$challenge",
      intuitionBridge = intuition,
      technicalBreakdown = technical,
      scenarioChallenge = challenge
    )
    dao.insertDeepDiveMessage(aiMsg)
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
    val prompt = if (lang == LanguageSetting.SINHALA) {
      "Mastery Challenge [${card.title}]: ${card.challengeScenario}"
    } else {
      "Mastery Challenge [${card.title}]: ${card.challengeScenario}"
    }
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
    val passed = answer.length > 5
    val updated = challenge.copy(
      status = if (passed) "CLEARED" else "FAILED",
      userResponse = answer
    )
    dao.updateGhostChallenge(updated)
    evaluateSpacedRepetition(challenge.cardId, passed)
    return passed
  }
}
