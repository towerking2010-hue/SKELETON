package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.AuthManager
import com.example.data.local.SkeletonDatabase
import com.example.data.model.CoreChatMessage
import com.example.data.model.DeepDiveMessage
import com.example.data.model.FeedCard
import com.example.data.model.GhostChallenge
import com.example.data.model.IntelligenceMode
import com.example.data.model.LanguageSetting
import com.example.data.repository.FirestoreKnowledgeRepository
import com.example.data.repository.SkeletonRepository
import com.example.engine.SkeletonCognitiveEngine
import com.example.engine.ai.AiRole
import com.example.engine.ai.GeminiCognitiveService
import com.example.engine.ai.ModelTier
import com.example.engine.bio.BioscienceModule
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
  FEED,
  CORE,
  SAVED
}

enum class DrawerMode(val title: String, val subtitle: String) {
  YGGDRASIL("YGGDRASIL", "3D Dynamic Attractor Graph"),
  CONCEPT_COLLIDER("CONCEPT COLLIDER", "Synthesize 2 Vault Nodes"),
  AR_LENS("AR LENS", "Real-World Camera & Vault Cross-Reference"),
  AUDIOBOOK_CAPTION("AUDIOBOOK CAPTION ENGINE", "E-Book Player & Word-by-Word Highlighting"),
  DREAM_STATE("DREAM STATE ENGINE", "10-Min Ambient Briefing & Binaural Beats"),
  LANGUAGE_TEACHER("LANGUAGE TEACHER", "Dedicated Immersive Language Mastery"),
  VAULT_MAILER("GMAIL VAULT MAILER", "Dispatch Digests & Flashcards to Dasun"),
  BIOSCIENCE("BIOSCIENCE LAB", "Molecular & Cognitive Neuroscience"),
  LIVE_VOICE("VOICE CONVERSATION", "Android speech input + CORE + device TTS"),
  IMAGE_STUDIO("IMAGE STUDIO", "Local prompt lab · paid rendering disabled"),
  VIDEO_STUDIO("VIDEO STUDIO", "Preview only · video rendering disabled"),
  AUDIO_TRANSCRIBE("AUDIO TRANSCRIBE", "Android device speech recognition"),
  GROUNDING("SEARCH & MAPS", "External grounding disabled · zero-billing mode"),
  GHOST_SNIPER_OCR("GHOST SNIPER OCR", "Extract Itch + Skeleton from Text"),
  SPACED_REPETITION("SPACED REPETITION", "3d / 7d / 14d Decay Tracker"),
  BOSS_BATTLE("BOSS BATTLE", "Multi-Variable Stress Test"),
  SETTINGS("SETTINGS", "Language & Engine Controls")
}

class SkeletonViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: SkeletonRepository
  private val firestoreRepo: FirestoreKnowledgeRepository

  private val _systemLanguage = MutableStateFlow(LanguageSetting.ENGLISH)
  val systemLanguage: StateFlow<LanguageSetting> = _systemLanguage.asStateFlow()

  private val _cringeFilterEnabled = MutableStateFlow(true)
  val cringeFilterEnabled: StateFlow<Boolean> = _cringeFilterEnabled.asStateFlow()

  private val _adrenalMode = MutableStateFlow(false)
  val adrenalMode: StateFlow<Boolean> = _adrenalMode.asStateFlow()

  private val _currentTab = MutableStateFlow(MainTab.FEED)
  val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

  private val _activeDeepDiveCardId = MutableStateFlow<String?>(null)
  val activeDeepDiveCardId: StateFlow<String?> = _activeDeepDiveCardId.asStateFlow()

  private val _intelligenceMode = MutableStateFlow(IntelligenceMode.CORE)
  val intelligenceMode: StateFlow<IntelligenceMode> = _intelligenceMode.asStateFlow()

  private val _isDrawerOpen = MutableStateFlow(false)
  val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

  private val _activeDrawerMode = MutableStateFlow<DrawerMode?>(null)
  val activeDrawerMode: StateFlow<DrawerMode?> = _activeDrawerMode.asStateFlow()

  private val _isProcessingAi = MutableStateFlow(false)
  val isProcessingAi: StateFlow<Boolean> = _isProcessingAi.asStateFlow()

  // Gemini Role and Model Tier State
  private val _selectedModelTier = MutableStateFlow(ModelTier.GENERAL)
  val selectedModelTier: StateFlow<ModelTier> = _selectedModelTier.asStateFlow()

  private val _selectedAiRole = MutableStateFlow(AiRole.SKELETON_CORE)
  val selectedAiRole: StateFlow<AiRole> = _selectedAiRole.asStateFlow()

  val currentUser: StateFlow<FirebaseUser?> = AuthManager.currentUser

  val feedCards: StateFlow<List<FeedCard>>
  val dueReviewCards: StateFlow<List<FeedCard>>
  val savedCards: StateFlow<List<FeedCard>>
  val coreMessages: StateFlow<List<CoreChatMessage>>
  val ghostChallenges: StateFlow<List<GhostChallenge>>

  val activeDeepDiveCard: StateFlow<FeedCard?>
  val activeDeepDiveMessages: StateFlow<List<DeepDiveMessage>>

  init {
    val dao = SkeletonDatabase.getDatabase(application).skeletonDao()
    repository = SkeletonRepository(dao)
    firestoreRepo = FirestoreKnowledgeRepository.create(application)

    dueReviewCards = repository.dueReviewCards(System.currentTimeMillis()).stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    feedCards = repository.feedCards.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    savedCards = repository.savedCards.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    coreMessages = repository.coreMessages.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    ghostChallenges = repository.ghostChallenges.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    activeDeepDiveCard = _activeDeepDiveCardId.flatMapLatest { id ->
      if (id == null) flowOf(null) else repository.getFeedCard(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    activeDeepDiveMessages = _activeDeepDiveCardId.flatMapLatest { id ->
      if (id == null) flowOf(emptyList()) else repository.getDeepDiveMessages(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    viewModelScope.launch {
      currentUser.collectLatest { user ->
        if (user != null) {
          firestoreRepo.observeCloudCards().collectLatest { remoteCards ->
            remoteCards.forEach { repository.insertFeedCard(it) }
          }
        }
      }
    }

    viewModelScope.launch {
      repository.initializeStarterCardsIfEmpty()
      // Auto-sign in if credentials exist
      AuthManager.attemptAutoSignIn(application, viewModelScope)
    }
  }

  fun setModelTier(tier: ModelTier) {
    _selectedModelTier.value = tier
  }

  fun setAiRole(role: AiRole) {
    _selectedAiRole.value = role
  }

  fun setSystemLanguage(lang: LanguageSetting) {
    _systemLanguage.value = lang
    viewModelScope.launch {
      repository.switchCurriculumLanguage(lang)
    }
  }

  fun toggleSystemLanguage() {
    val next = _systemLanguage.value.toggle()
    _systemLanguage.value = next
    viewModelScope.launch {
      repository.switchCurriculumLanguage(next)
    }
  }

  fun toggleCringeFilter() {
    _cringeFilterEnabled.value = !_cringeFilterEnabled.value
  }

  fun toggleAdrenalMode() {
    _adrenalMode.value = !_adrenalMode.value
  }

  fun selectTab(tab: MainTab) {
    _currentTab.value = tab
    if (tab == MainTab.CORE) {
      _intelligenceMode.value = IntelligenceMode.CORE
    }
    _activeDeepDiveCardId.value = null
  }

  fun openDeepDive(cardId: String) {
    _intelligenceMode.value = IntelligenceMode.DEEPDIVE
    _activeDeepDiveCardId.value = cardId
  }

  fun closeDeepDive() {
    _intelligenceMode.value = IntelligenceMode.CORE
    _activeDeepDiveCardId.value = null
  }

  fun switchIntelligenceMode(mode: IntelligenceMode, targetCardId: String? = null) {
    _intelligenceMode.value = mode
    if (mode == IntelligenceMode.CORE) {
      _activeDeepDiveCardId.value = null
      _currentTab.value = MainTab.CORE
    } else {
      val cardId = targetCardId
        ?: _activeDeepDiveCardId.value
        ?: savedCards.value.firstOrNull()?.id
        ?: feedCards.value.firstOrNull()?.id
      if (cardId != null) {
        _activeDeepDiveCardId.value = cardId
      } else {
        // Fallback: stay in CORE if no cards available yet
        _intelligenceMode.value = IntelligenceMode.CORE
      }
    }
  }

  fun openDrawer() {
    _isDrawerOpen.value = true
  }

  fun closeDrawer() {
    _isDrawerOpen.value = false
  }

  fun selectDrawerMode(mode: DrawerMode) {
    _isDrawerOpen.value = false
    _activeDrawerMode.value = mode
  }

  fun closeDrawerMode() {
    _activeDrawerMode.value = null
  }

  fun toggleSaveCard(card: FeedCard) {
    viewModelScope.launch {
      repository.toggleSaveCard(card)
      // Sync to cloud if user is signed in
      if (currentUser.value != null) {
        try {
          firestoreRepo.saveCardToCloud(card.copy(isSaved = !card.isSaved))
        } catch (_: Exception) {}
      }
    }
  }

  fun sendCoreMessage(text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      _isProcessingAi.value = true
      try {
        repository.sendCoreUserMessage(text.trim(), _systemLanguage.value, _selectedModelTier.value, _selectedAiRole.value)
      } finally {
        _isProcessingAi.value = false
      }
    }
  }

  fun acceptFeedSync(message: CoreChatMessage, topic: String) {
    viewModelScope.launch {
      repository.executeFeedSync(message, topic, _systemLanguage.value)
    }
  }

  fun sendDeepDiveMessage(cardId: String, query: String) {
    if (query.isBlank()) return
    viewModelScope.launch {
      _isProcessingAi.value = true
      try {
        repository.sendDeepDiveMessage(cardId, query.trim(), _systemLanguage.value, _selectedModelTier.value)
      } finally {
        _isProcessingAi.value = false
      }
    }
  }

  fun runConceptCollider(cardA: FeedCard, cardB: FeedCard) {
    viewModelScope.launch {
      val lang = _systemLanguage.value
      val languageRule = if (lang == LanguageSetting.SINHALA) {
        "Write all learning content in natural Sinhala script, keeping technical terms in English."
      } else "Write in clear English."
      val prompt = """
        $languageRule
        Synthesize these two saved concepts into one accurate, memorable 30-second cross-domain insight.
        Concept A: ${cardA.title}
        Domain A: ${cardA.branchDomain}
        Core A: ${cardA.skeleton}
        Concept B: ${cardB.title}
        Domain B: ${cardB.branchDomain}
        Core B: ${cardB.skeleton}
        Do not invent a mathematical equivalence. State where the analogy holds and where it breaks.
        Return exactly three labeled sections:
        INTUITION: one vivid analogy
        MECHANISM: the actual shared mechanism and limits
        NEW INSIGHT: one useful implication or testable question
      """.trimIndent()
      val generated = GeminiCognitiveService.chatGenerate(prompt, AiRole.SKELETON_CORE, ModelTier.FAST)
      val collided = if (generated.startsWith("Inference error") || generated.startsWith("Synthesis complete without output")) {
        SkeletonCognitiveEngine.collideConcepts(cardA, cardB, lang)
      } else {
        fun section(label: String): String = Regex(
          """(?is)${label}:\s*(.*?)(?=\n(?:INTUITION|MECHANISM|NEW INSIGHT):|$)"""
        ).find(generated)?.groupValues?.getOrNull(1)?.trim().orEmpty()
        val intuition = section("INTUITION").ifBlank { generated.take(500) }
        val mechanism = section("MECHANISM").ifBlank { generated }
        val insight = section("NEW INSIGHT").ifBlank { "Ask CORE to test this connection against a counterexample." }
        FeedCard(
          id = "collider_" + java.util.UUID.randomUUID().toString().take(8),
          title = "${cardA.title} ⊗ ${cardB.title}",
          itch = insight,
          skeleton = mechanism,
          intuition = intuition,
          branchDomain = "Cross-Domain Synthesis",
          crossLinkTarget = "${cardA.title} + ${cardB.title}",
          isSaved = true,
          challengeScenario = "Give one real example where this cross-domain connection is useful, and one where it breaks.",
          challengeExpectedKey = "A correct explanation of the shared mechanism and its limits.",
          language = if (lang == LanguageSetting.SINHALA) "si" else "en"
        )
      }
      repository.insertFeedCard(collided)
      if (currentUser.value != null) {
        try { firestoreRepo.saveCardToCloud(collided) } catch (_: Exception) {}
      }
    }
  }

  fun saveCapturedCard(card: FeedCard) {
    viewModelScope.launch {
      val saved = card.copy(isSaved = true)
      repository.insertFeedCard(saved)
      if (currentUser.value != null) {
        try { firestoreRepo.saveCardToCloud(saved) } catch (_: Exception) {}
      }
    }
  }

  fun runSniperOcr(scannedText: String) {
    if (scannedText.isBlank()) return
    viewModelScope.launch {
      val lang = _systemLanguage.value
      val languageRule = if (lang == LanguageSetting.SINHALA) {
        "Write all educational fields in natural Sinhala script. Keep established technical terms and equations in English."
      } else "Write in clear English."
      val prompt = """
        $languageRule
        Turn the following OCR text into one accurate micro-learning card.
        Extract the central question, actual mechanism, an intuitive analogy, the best-fit subject/domain,
        and one active-recall challenge. Do not add facts that are not supported by the source.
        If the source is ambiguous or incomplete, say so instead of inventing details.
        Return exactly these labeled fields:
        TITLE: short title
        ITCH: the central question or tension
        SKELETON: the actual mechanism, rules, or equations
        INTUITION: a helpful analogy
        DOMAIN: one of the 19 SKELETON master domains or the closest accurate subject
        CHALLENGE: one recall question
        ANSWER KEY: essential points a correct answer must contain
        OCR SOURCE:
        ${scannedText.take(12000)}
      """.trimIndent()
      val generated = GeminiCognitiveService.chatGenerate(prompt, AiRole.SKELETON_CORE, ModelTier.FAST)
      val extracted = if (generated.startsWith("Inference error") || generated.startsWith("Synthesis complete without output")) {
        SkeletonCognitiveEngine.sniperOcrExtraction(scannedText, lang)
      } else {
        fun field(label: String): String = Regex(
          """(?is)^\s*${label}:\s*(.*?)(?=^\s*(?:TITLE|ITCH|SKELETON|INTUITION|DOMAIN|CHALLENGE|ANSWER KEY):|$)""",
          setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        ).find(generated)?.groupValues?.getOrNull(1)?.trim().orEmpty()
        val title = field("TITLE").ifBlank { scannedText.lineSequence().firstOrNull { it.isNotBlank() }?.take(70) ?: "OCR Learning Card" }
        FeedCard(
          id = "ocr_" + java.util.UUID.randomUUID().toString().take(8),
          title = title,
          itch = field("ITCH").ifBlank { "Question extracted from source text." },
          skeleton = field("SKELETON").ifBlank { generated },
          intuition = field("INTUITION").ifBlank { "Ask CORE to explain this concept with an analogy." },
          branchDomain = field("DOMAIN").ifBlank { "OCR Extraction" },
          crossLinkTarget = "Ghost Sniper OCR",
          isSaved = true,
          challengeScenario = field("CHALLENGE"),
          challengeExpectedKey = field("ANSWER KEY"),
          language = if (lang == LanguageSetting.SINHALA) "si" else "en"
        )
      }
      repository.insertFeedCard(extracted)
      if (currentUser.value != null) {
        try { firestoreRepo.saveCardToCloud(extracted) } catch (_: Exception) {}
      }
    }
  }

  fun recordSpacedRepetition(cardId: String, success: Boolean) {
    viewModelScope.launch {
      repository.evaluateSpacedRepetition(cardId, success)
    }
  }

  fun triggerMicroChallenge() {
    viewModelScope.launch {
      val saved = savedCards.value.ifEmpty { feedCards.value }
      val randomCard = saved.randomOrNull() ?: return@launch
      repository.createGhostChallenge(randomCard, _systemLanguage.value)
    }
  }

  fun answerGhostChallenge(challenge: GhostChallenge, answer: String) {
    viewModelScope.launch {
      repository.answerGhostChallenge(challenge, answer)
    }
  }
}
