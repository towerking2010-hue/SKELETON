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
  LIVE_VOICE("VOICE CONVERSATION", "Real-Time Model: gemini-3.8-live"),
  IMAGE_STUDIO("IMAGE STUDIO", "Create & Edit: gemini-nano-banana-2.1"),
  VIDEO_STUDIO("VIDEO STUDIO", "Text-to-Video & Animate Photo (gemini-omni-1.1-flash)"),
  AUDIO_TRANSCRIBE("AUDIO TRANSCRIBE", "Model: gemini-3.5-transcribe"),
  GROUNDING("SEARCH & MAPS GROUNDING", "Model: gemini-3.8-flash"),
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
  val savedCards: StateFlow<List<FeedCard>>
  val coreMessages: StateFlow<List<CoreChatMessage>>
  val ghostChallenges: StateFlow<List<GhostChallenge>>

  val activeDeepDiveCard: StateFlow<FeedCard?>
  val activeDeepDiveMessages: StateFlow<List<DeepDiveMessage>>

  init {
    val dao = SkeletonDatabase.getDatabase(application).skeletonDao()
    repository = SkeletonRepository(dao)
    firestoreRepo = FirestoreKnowledgeRepository.create(application)

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
        repository.sendCoreUserMessage(text.trim(), _systemLanguage.value)
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
        repository.sendDeepDiveMessage(cardId, query.trim(), _systemLanguage.value)
      } finally {
        _isProcessingAi.value = false
      }
    }
  }

  fun runConceptCollider(cardA: FeedCard, cardB: FeedCard) {
    viewModelScope.launch {
      val collided = SkeletonCognitiveEngine.collideConcepts(cardA, cardB, _systemLanguage.value)
      repository.insertFeedCard(collided)
      if (currentUser.value != null) {
        try { firestoreRepo.saveCardToCloud(collided) } catch (_: Exception) {}
      }
    }
  }

  fun runSniperOcr(scannedText: String) {
    if (scannedText.isBlank()) return
    viewModelScope.launch {
      val extracted = SkeletonCognitiveEngine.sniperOcrExtraction(scannedText, _systemLanguage.value)
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
