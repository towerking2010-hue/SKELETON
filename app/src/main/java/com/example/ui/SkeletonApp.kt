package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.IntelligenceMode
import com.example.ui.components.BottomNavBar
import com.example.ui.components.SubsystemDrawer
import com.example.ui.components.TopHeaderBar
import com.example.ui.screens.ArLensScreen
import com.example.ui.screens.AudioTranscribeScreen
import com.example.ui.screens.AudiobookCaptionScreen
import com.example.ui.screens.BioscienceScreen
import com.example.ui.screens.BossBattleScreen
import com.example.ui.screens.ConceptColliderScreen
import com.example.ui.screens.CoreScreen
import com.example.ui.screens.DeepDiveScreen
import com.example.ui.screens.DreamStateEngineScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.GhostSniperOcrScreen
import com.example.ui.screens.GroundingScreen
import com.example.ui.screens.ImageStudioScreen
import com.example.ui.screens.LanguageTeacherScreen
import com.example.ui.screens.SavedVaultScreen
import com.example.ui.screens.SpacedRepetitionScreen
import com.example.ui.screens.VaultMailerScreen
import com.example.ui.screens.VideoStudioScreen
import com.example.ui.screens.VoiceConversationScreen
import com.example.ui.screens.YggdrasilGraphScreen
import com.example.ui.theme.ObsidianBg

@Composable
fun SkeletonApp(
  viewModel: SkeletonViewModel = viewModel()
) {
  val systemLanguage by viewModel.systemLanguage.collectAsState()
  val cringeFilterEnabled by viewModel.cringeFilterEnabled.collectAsState()
  val adrenalMode by viewModel.adrenalMode.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()
  val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
  val activeDrawerMode by viewModel.activeDrawerMode.collectAsState()
  val activeDeepDiveCardId by viewModel.activeDeepDiveCardId.collectAsState()
  val activeDeepDiveCard by viewModel.activeDeepDiveCard.collectAsState()
  val activeDeepDiveMessages by viewModel.activeDeepDiveMessages.collectAsState()
  val isProcessingAi by viewModel.isProcessingAi.collectAsState()

  val feedCards by viewModel.feedCards.collectAsState()
  val dueReviewCards by viewModel.dueReviewCards.collectAsState()
  val savedCards by viewModel.savedCards.collectAsState()
  val coreMessages by viewModel.coreMessages.collectAsState()
  val ghostChallenges by viewModel.ghostChallenges.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  val selectedModelTier by viewModel.selectedModelTier.collectAsState()
  val selectedAiRole by viewModel.selectedAiRole.collectAsState()
  val intelligenceMode by viewModel.intelligenceMode.collectAsState()

  // 1. If DeepDive is active, show isolated per-card DeepDive screen
  if (activeDeepDiveCardId != null) {
    DeepDiveScreen(
      card = activeDeepDiveCard,
      messages = activeDeepDiveMessages,
      currentLanguage = systemLanguage,
      cringeFilterEnabled = cringeFilterEnabled,
      isProcessing = isProcessingAi,
      selectedIntelligenceMode = intelligenceMode,
      onSwitchIntelligenceMode = { mode -> viewModel.switchIntelligenceMode(mode) },
      onSendMessage = { query ->
        activeDeepDiveCardId?.let { cardId ->
          viewModel.sendDeepDiveMessage(cardId, query)
        }
      },
      onBack = { viewModel.closeDeepDive() }
    )
    return
  }

  // 2. If a Subsystem mode was opened from Drawer, display it
  when (activeDrawerMode) {
    DrawerMode.BIOSCIENCE -> {
      BioscienceScreen(
        currentLanguage = systemLanguage,
        cringeFilterEnabled = cringeFilterEnabled,
        onAskDeepDive = { cardId ->
          viewModel.closeDrawerMode()
          viewModel.openDeepDive(cardId)
        },
        onToggleSave = { card -> viewModel.toggleSaveCard(card) },
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.LIVE_VOICE -> {
      VoiceConversationScreen(
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.IMAGE_STUDIO -> {
      ImageStudioScreen(
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.VIDEO_STUDIO -> {
      VideoStudioScreen(
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.AUDIO_TRANSCRIBE -> {
      AudioTranscribeScreen(
        onBack = { viewModel.closeDrawerMode() },
        onAddToFeed = { transcript -> viewModel.runSniperOcr(transcript) }
      )
      return
    }
    DrawerMode.GROUNDING -> {
      GroundingScreen(
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.YGGDRASIL -> {
      YggdrasilGraphScreen(
        cards = feedCards,
        onOpenDeepDive = { cardId ->
          viewModel.closeDrawerMode()
          viewModel.openDeepDive(cardId)
        },
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.CONCEPT_COLLIDER -> {
      ConceptColliderScreen(
        vaultCards = savedCards.ifEmpty { feedCards },
        currentLanguage = systemLanguage,
        onCollide = { cardA, cardB ->
          viewModel.runConceptCollider(cardA, cardB)
        },
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.GHOST_SNIPER_OCR -> {
      GhostSniperOcrScreen(
        currentLanguage = systemLanguage,
        onExtract = { rawText ->
          viewModel.runSniperOcr(rawText)
        },
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.SPACED_REPETITION -> {
      SpacedRepetitionScreen(
        cards = dueReviewCards,
        currentLanguage = systemLanguage,
        onRecordRecall = { cardId, success ->
          viewModel.recordSpacedRepetition(cardId, success)
        },
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.BOSS_BATTLE -> {
      BossBattleScreen(
        vaultCards = savedCards.ifEmpty { feedCards },
        currentLanguage = systemLanguage,
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.AR_LENS -> {
      ArLensScreen(
        currentLanguage = systemLanguage,
        onSaveToVault = { card -> viewModel.saveCapturedCard(card) },
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.AUDIOBOOK_CAPTION -> {
      AudiobookCaptionScreen(
        currentLanguage = systemLanguage,
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.DREAM_STATE -> {
      DreamStateEngineScreen(
        weakCards = feedCards.filter { it.recallStage in 1..2 || it.weakRecallCount > 0 }.ifEmpty { feedCards },
        currentLanguage = systemLanguage,
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.LANGUAGE_TEACHER -> {
      LanguageTeacherScreen(
        currentLanguage = systemLanguage,
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.VAULT_MAILER -> {
      VaultMailerScreen(
        vaultCards = savedCards.ifEmpty { feedCards },
        currentLanguage = systemLanguage,
        userEmail = currentUser?.email ?: "towerking2010@gmail.com",
        userName = currentUser?.displayName ?: "Dasun",
        onBack = { viewModel.closeDrawerMode() }
      )
      return
    }
    DrawerMode.SETTINGS -> {
      // Settings are handled right inside the drawer
    }
    null -> Unit
  }

  // 3. Primary App Shell: TopBar, Tab Content, BottomBar, and Slide-Up Subsystem Drawer
  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBg),
    topBar = {
      TopHeaderBar(
        currentLanguage = systemLanguage,
        adrenalMode = adrenalMode,
        selectedIntelligenceMode = intelligenceMode,
        onToggleIntelligenceMode = {
          val nextMode = if (intelligenceMode == IntelligenceMode.CORE) {
            IntelligenceMode.DEEPDIVE
          } else {
            IntelligenceMode.CORE
          }
          viewModel.switchIntelligenceMode(nextMode)
        },
        onToggleLanguage = { viewModel.toggleSystemLanguage() },
        onToggleAdrenalMode = { viewModel.toggleAdrenalMode() },
        onOpenDrawer = { viewModel.openDrawer() }
      )
    },
    bottomBar = {
      BottomNavBar(
        currentTab = currentTab,
        currentLanguage = systemLanguage,
        onSelectTab = { tab -> viewModel.selectTab(tab) }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        MainTab.FEED -> {
          FeedScreen(
            cards = feedCards,
            ghostChallenges = ghostChallenges,
            currentLanguage = systemLanguage,
            cringeFilterEnabled = cringeFilterEnabled,
            currentUser = currentUser,
            onSignInSuccess = {},
            onSignOut = {},
            onAskDeepDive = { cardId -> viewModel.openDeepDive(cardId) },
            onToggleSave = { card -> viewModel.toggleSaveCard(card) },
            onTriggerGhostChallenge = { viewModel.triggerMicroChallenge() },
            onAnswerGhostChallenge = { challenge, answer ->
              viewModel.answerGhostChallenge(challenge, answer)
            }
          )
        }
        MainTab.CORE -> {
          CoreScreen(
            messages = coreMessages,
            currentLanguage = systemLanguage,
            cringeFilterEnabled = cringeFilterEnabled,
            isProcessing = isProcessingAi,
            selectedIntelligenceMode = intelligenceMode,
            onSwitchIntelligenceMode = { mode -> viewModel.switchIntelligenceMode(mode) },
            selectedModelTier = selectedModelTier,
            onSelectModelTier = { viewModel.setModelTier(it) },
            selectedAiRole = selectedAiRole,
            onSelectAiRole = { viewModel.setAiRole(it) },
            onSendMessage = { text -> viewModel.sendCoreMessage(text) },
            onAcceptFeedSync = { msg, topic -> viewModel.acceptFeedSync(msg, topic) }
          )
        }
        MainTab.SAVED -> {
          SavedVaultScreen(
            savedCards = savedCards,
            currentLanguage = systemLanguage,
            cringeFilterEnabled = cringeFilterEnabled,
            onAskDeepDive = { cardId -> viewModel.openDeepDive(cardId) },
            onToggleSave = { card -> viewModel.toggleSaveCard(card) },
            onLaunchCollider = { viewModel.selectDrawerMode(DrawerMode.CONCEPT_COLLIDER) }
          )
        }
      }
    }

    // Slide-Up Subsystem Drawer (`≡`)
    SubsystemDrawer(
      isOpen = isDrawerOpen,
      currentLanguage = systemLanguage,
      cringeFilterEnabled = cringeFilterEnabled,
      adrenalMode = adrenalMode,
      onDismiss = { viewModel.closeDrawer() },
      onSelectMode = { mode -> viewModel.selectDrawerMode(mode) },
      onToggleCringeFilter = { viewModel.toggleCringeFilter() },
      onToggleAdrenalMode = { viewModel.toggleAdrenalMode() },
      onSetLanguage = { lang -> viewModel.setSystemLanguage(lang) }
    )
  }
}
