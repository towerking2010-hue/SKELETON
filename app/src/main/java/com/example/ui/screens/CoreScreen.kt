package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import com.example.data.model.CoreChatMessage
import com.example.data.model.IntelligenceMode
import com.example.data.model.LanguageSetting
import com.example.engine.SkeletonCognitiveEngine
import com.example.ui.components.IntelligenceModeSelector
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianBorderLight
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun CoreScreen(
  messages: List<CoreChatMessage>,
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  isProcessing: Boolean,
  selectedIntelligenceMode: IntelligenceMode = IntelligenceMode.CORE,
  onSwitchIntelligenceMode: (IntelligenceMode) -> Unit = {},
  selectedModelTier: com.example.engine.ai.ModelTier = com.example.engine.ai.ModelTier.GENERAL,
  onSelectModelTier: (com.example.engine.ai.ModelTier) -> Unit = {},
  selectedAiRole: com.example.engine.ai.AiRole = com.example.engine.ai.AiRole.SKELETON_CORE,
  onSelectAiRole: (com.example.engine.ai.AiRole) -> Unit = {},
  onSendMessage: (String) -> Unit,
  onAcceptFeedSync: (CoreChatMessage, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  val quickPrompts = if (currentLanguage == LanguageSetting.SINHALA) {
    listOf(
      "අපි බය වුණාම කාලය හෙමින් යනවා වගේ දැනෙන්නේ ඇයි?",
      "Golden Ratio එක සරල උදාහරණයකින් කියන්න පුළුවන්ද?",
      "අපි දෙයක් ඇත්තටම මතක තබාගන්නේ කොහොමද?",
      "Psychology ගැන පුදුම හිතෙන දෙයක් කියන්න",
      "මිනිත්තු දෙකකින් අලුත් දෙයක් උගන්වන්න"
    )
  } else {
    listOf(
      "Why does time feel slower when we're scared?",
      "Can you explain the golden ratio without a lecture?",
      "How do people actually learn something deeply?",
      "What's one strange idea in psychology worth exploring?",
      "Teach me something surprising in two minutes."
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .padding(bottom = 76.dp)
      .imePadding()
  ) {
    // Mode Switcher: CORE vs DEEPDIVE with animated indicator
    IntelligenceModeSelector(
      selectedMode = selectedIntelligenceMode,
      onModeSelected = onSwitchIntelligenceMode,
      modifier = Modifier.padding(top = 2.dp)
    )

    // Model Tier & Role Selector Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      com.example.engine.ai.ModelTier.values().forEach { tier ->
        val isSelected = tier == selectedModelTier
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CyanNeon.copy(alpha = 0.2f) else ObsidianSurfaceElevated)
            .border(0.8.dp, if (isSelected) CyanNeon else ObsidianBorder, RoundedCornerShape(12.dp))
            .clickable { onSelectModelTier(tier) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = when (tier) {
              com.example.engine.ai.ModelTier.FAST -> "FAST"
              com.example.engine.ai.ModelTier.GENERAL -> "STANDARD"
              com.example.engine.ai.ModelTier.REASONING -> "DEEP REASONING"
            },
            color = if (isSelected) CyanNeon else SilverMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        }
      }
    }

    // Quick Concept Chips Bar
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(quickPrompts) { prompt ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurfaceElevated)
            .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            .clickable { onSendMessage(prompt) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = prompt,
            color = SilverMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Chat Messages
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        CoreMessageBubble(
          message = msg,
          cringeFilterEnabled = cringeFilterEnabled,
          currentLanguage = currentLanguage,
          onAcceptFeedSync = { topic -> onAcceptFeedSync(msg, topic) }
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
              text = if (currentLanguage == LanguageSetting.SINHALA) "CORE synthesizing..." else "CORE synthesizing...",
              color = SilverDim,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }

    // Bottom Input Box
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(ObsidianSurfaceElevated)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp))
        .padding(horizontal = 14.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      BasicTextField(
        value = inputText,
        onValueChange = { inputText = it },
        modifier = Modifier
          .weight(1f)
          .padding(vertical = 8.dp)
          .testTag("core_input_field"),
        textStyle = TextStyle(
          color = SilverText,
          fontSize = 14.sp
        ),
        cursorBrush = SolidColor(CyanNeon),
        decorationBox = { innerTextField ->
          if (inputText.isEmpty()) {
            Text(
              text = if (currentLanguage == LanguageSetting.SINHALA)
                "CORE ගෙන් ඕනෑම concept එකක් අහන්න..."
              else
                "What are you curious about? Let’s talk…",
              color = SilverDim,
              fontSize = 13.sp
            )
          }
          innerTextField()
        }
      )

      IconButton(
        onClick = {
          if (inputText.isNotBlank()) {
            val text = inputText
            inputText = ""
            onSendMessage(text)
          }
        },
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(CyanNeon)
          .testTag("core_send_button")
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
private fun CoreMessageBubble(
  message: CoreChatMessage,
  cringeFilterEnabled: Boolean,
  currentLanguage: LanguageSetting,
  onAcceptFeedSync: (String) -> Unit
) {
  val isUser = message.isUser
  val cleanText = remember(message.text, cringeFilterEnabled) {
    SkeletonCognitiveEngine.applyCringeFilter(message.text, cringeFilterEnabled)
  }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
  ) {
    Box(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = if (isUser) 20.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 20.dp
          )
        )
        .background(if (isUser) Color(0xFF1E293B) else ObsidianSurface)
        .border(
          0.8.dp,
          if (isUser) ObsidianBorderLight else ObsidianBorder,
          RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = if (isUser) 20.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 20.dp
          )
        )
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      Text(
        text = cleanText,
        color = SilverText,
        fontSize = 14.sp,
        lineHeight = 20.sp
      )
    }

    // Feed Sync Protocol Button
    if (!isUser && message.feedSyncTopic != null) {
      Spacer(modifier = Modifier.height(8.dp))
      if (message.feedSyncAdded) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(EmeraldMastery.copy(alpha = 0.15f))
            .border(0.8.dp, EmeraldMastery, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = EmeraldMastery,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (currentLanguage == LanguageSetting.SINHALA)
              "[FEED SYNC]: Feed එකට එකතු කරන ලදී ✓"
            else
              "[FEED SYNC]: Synced to Feed ✓",
            color = EmeraldMastery,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }
      } else {
        ElevatedButton(
          onClick = { onAcceptFeedSync(message.feedSyncTopic) },
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = AmberAccent,
            contentColor = ObsidianBg
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("feed_sync_accept_button")
        ) {
          Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (currentLanguage == LanguageSetting.SINHALA)
              "Feed එකට එක්කරන්න [${message.feedSyncTopic}]"
            else
              "Add [${message.feedSyncTopic}] to Feed",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}
