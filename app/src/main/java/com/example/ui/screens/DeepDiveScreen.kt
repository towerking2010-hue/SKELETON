package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeepDiveMessage
import com.example.data.model.FeedCard
import com.example.data.model.IntelligenceMode
import com.example.data.model.LanguageSetting
import com.example.engine.SkeletonCognitiveEngine
import com.example.ui.components.IntelligenceModeSelector
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianBorderLight
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun DeepDiveScreen(
  card: FeedCard?,
  messages: List<DeepDiveMessage>,
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  isProcessing: Boolean,
  selectedIntelligenceMode: IntelligenceMode = IntelligenceMode.DEEPDIVE,
  onSwitchIntelligenceMode: (IntelligenceMode) -> Unit = {},
  onSendMessage: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  if (card == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(ObsidianBg),
      contentAlignment = Alignment.Center
    ) {
      Text("Card not found", color = SilverDim)
    }
    return
  }

  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size)
    }
  }

  val probeChips = listOf(
    "Edge case failure modes",
    "Show exact code syntax",
    "Mathematical invariant proof",
    "How does it break at scale?"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .padding(top = 8.dp)
      .imePadding()
  ) {
    // Mode Switcher: CORE vs DEEPDIVE with animated indicator
    IntelligenceModeSelector(
      selectedMode = selectedIntelligenceMode,
      onModeSelected = { mode ->
        if (mode == IntelligenceMode.CORE) {
          onBack()
        } else {
          onSwitchIntelligenceMode(mode)
        }
      },
      activeTopicName = card.title,
      modifier = Modifier.padding(top = 2.dp)
    )

    // Header Bar with Card Title & Node Tag
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(ObsidianSurface)
        .border(0.8.dp, ObsidianBorder)
        .padding(horizontal = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.testTag("deepdive_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = CyanNeon
        )
      }

      Spacer(modifier = Modifier.width(6.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "DEEPDIVE: ${card.title}",
          color = SilverText,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1
        )
        Text(
          text = "[ISOLATED THREAD: ${card.branchDomain}]",
          color = CyanNeon,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // Probing Chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(probeChips) { chip ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(ObsidianSurfaceElevated)
            .border(0.8.dp, ObsidianBorder, RoundedCornerShape(14.dp))
            .clickable { onSendMessage(chip) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = chip,
            color = SilverMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Messages List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Pinned Root Card Context Box (so user always sees the base skeleton)
      item(key = "root_card_context") {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0D0D14))
            .border(1.dp, CyanNeon.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(14.dp)
        ) {
          Text(
            text = "ORIGINAL SKELETON ANCHOR",
            color = CyanNeon,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = card.skeleton,
            color = SilverText,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 17.sp
          )
        }
      }

      // If no messages yet, show isolated prompt
      if (messages.isEmpty()) {
        item(key = "empty_thread_indicator") {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Thread isolated to ${card.title}. Ask any deep question.",
              color = SilverDim,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      // History of isolated messages
      items(messages, key = { it.id }) { msg ->
        DeepDiveMessageBubble(
          message = msg,
          cringeFilterEnabled = cringeFilterEnabled
        )
      }

      if (isProcessing) {
        item {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(16.dp),
              color = CyanNeon,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "DEEPDIVE resolving invariants...",
              color = SilverDim,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(ObsidianSurface)
        .border(0.8.dp, ObsidianBorder)
        .navigationBarsPadding()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      BasicTextField(
        value = inputText,
        onValueChange = { inputText = it },
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurfaceElevated)
          .border(0.8.dp, ObsidianBorder, RoundedCornerShape(20.dp))
          .padding(horizontal = 14.dp, vertical = 10.dp)
          .testTag("deepdive_input_field"),
        textStyle = TextStyle(
          color = SilverText,
          fontSize = 14.sp
        ),
        cursorBrush = SolidColor(CyanNeon),
        decorationBox = { innerTextField ->
          if (inputText.isEmpty()) {
            Text(
              text = "Inquire into ${card.title}...",
              color = SilverDim,
              fontSize = 13.sp
            )
          }
          innerTextField()
        }
      )

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          if (inputText.isNotBlank()) {
            val text = inputText
            inputText = ""
            onSendMessage(text)
          }
        },
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(CyanNeon)
          .testTag("deepdive_send_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Send,
          contentDescription = "Send",
          tint = ObsidianBg,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
private fun DeepDiveMessageBubble(
  message: DeepDiveMessage,
  cringeFilterEnabled: Boolean
) {
  val isUser = message.isUser

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
  ) {
    if (isUser) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(18.dp))
          .background(Color(0xFF1E293B))
          .border(0.8.dp, ObsidianBorderLight, RoundedCornerShape(18.dp))
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        Text(
          text = message.text,
          color = SilverText,
          fontSize = 14.sp
        )
      }
    } else {
      // 2-Layer Breakdown View
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(22.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp))
          .padding(16.dp)
      ) {
        // Layer 1: Intuition Bridge
        if (!message.intuitionBridge.isNullOrBlank()) {
          Text(
            text = "LAYER 1 // INTUITION BRIDGE",
            color = CyanNeon,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = SkeletonCognitiveEngine.applyCringeFilter(message.intuitionBridge, cringeFilterEnabled),
            color = SilverText,
            fontSize = 13.sp,
            lineHeight = 19.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
        }

        // Layer 2: Raw Technical Rules / Code Syntax
        if (!message.technicalBreakdown.isNullOrBlank()) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF0D0D12))
              .border(0.8.dp, ObsidianBorder, RoundedCornerShape(14.dp))
              .padding(12.dp)
          ) {
            Text(
              text = "LAYER 2 // RAW TECHNICAL RULES & SYNTAX",
              color = AmberAccent,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = SkeletonCognitiveEngine.applyCringeFilter(message.technicalBreakdown, cringeFilterEnabled),
              color = SilverText,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              lineHeight = 17.sp
            )
          }
          Spacer(modifier = Modifier.height(12.dp))
        }

        // Layer 3: Active Scenario Challenge
        if (!message.scenarioChallenge.isNullOrBlank()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF161420))
              .border(0.8.dp, Color(0xFFA78BFA), RoundedCornerShape(12.dp))
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "⚡",
              fontSize = 14.sp,
              modifier = Modifier.padding(end = 6.dp)
            )
            Text(
              text = message.scenarioChallenge,
              color = SilverText,
              fontSize = 12.sp,
              lineHeight = 17.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Fallback for raw text if layers are null
        if (message.intuitionBridge.isNullOrBlank() && message.technicalBreakdown.isNullOrBlank()) {
          Text(
            text = SkeletonCognitiveEngine.applyCringeFilter(message.text, cringeFilterEnabled),
            color = SilverText,
            fontSize = 13.sp,
            lineHeight = 19.sp
          )
        }
      }
    }
  }
}
