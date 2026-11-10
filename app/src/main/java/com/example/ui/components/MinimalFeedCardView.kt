package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting
import com.example.engine.SkeletonCognitiveEngine
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanGlow
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
fun MinimalFeedCardView(
  card: FeedCard,
  cringeFilterEnabled: Boolean,
  currentLanguage: LanguageSetting,
  onAskDeepDive: () -> Unit,
  onToggleSave: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showChallenge by remember { mutableStateOf(false) }

  val itchText = remember(card.itch, cringeFilterEnabled) {
    SkeletonCognitiveEngine.applyCringeFilter(card.itch, cringeFilterEnabled)
  }
  val skeletonText = remember(card.skeleton, cringeFilterEnabled) {
    SkeletonCognitiveEngine.applyCringeFilter(card.skeleton, cringeFilterEnabled)
  }
  val intuitionText = remember(card.intuition, cringeFilterEnabled) {
    SkeletonCognitiveEngine.applyCringeFilter(card.intuition, cringeFilterEnabled)
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(30.dp))
      .background(ObsidianSurface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(30.dp))
      .padding(20.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {

      // YGGDRASIL Node Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        val crossLink = card.crossLinkTarget?.let { " -> $it" } ?: ""
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ObsidianSurfaceElevated)
            .border(0.8.dp, ObsidianBorderLight, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "[NODE: ${card.branchDomain}$crossLink]",
            color = CyanNeon,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (card.recallStage > 0) {
            val stageLabel = when (card.recallStage) {
              1 -> "3d decay"
              2 -> "7d decay"
              3 -> "14d decay"
              else -> "Mastered"
            }
            Text(
              text = stageLabel,
              color = EmeraldMastery,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(end = 8.dp)
            )
          }

          IconButton(
            onClick = onToggleSave,
            modifier = Modifier
              .size(36.dp)
              .testTag("save_card_button_${card.id}")
          ) {
            Icon(
              imageVector = if (card.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
              contentDescription = if (card.isSaved) "Unsave card" else "Save card",
              tint = if (card.isSaved) AmberAccent else SilverDim
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Card Title
      Text(
        text = card.title,
        color = SilverText,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.5).sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // THE ITCH (The Core Tension)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(ObsidianSurfaceElevated)
          .padding(14.dp)
      ) {
        Text(
          text = if (currentLanguage == LanguageSetting.SINHALA) "THE ITCH / ප්‍රධාන ගැටලුව" else "THE ITCH",
          color = AmberAccent,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = itchText,
          color = SilverText,
          fontSize = 14.sp,
          lineHeight = 20.sp,
          fontWeight = FontWeight.Normal
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // SKELETON (Raw Structural Essence)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFF0F0F14))
          .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
          .padding(14.dp)
      ) {
        Text(
          text = if (currentLanguage == LanguageSetting.SINHALA) "SKELETON / මූලික නීතිය" else "SKELETON",
          color = CyanNeon,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = skeletonText,
          color = SilverText,
          fontSize = 13.sp,
          fontFamily = FontFamily.Monospace,
          lineHeight = 18.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // INTUITION (High-Impact Analogy)
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        Text(
          text = "⚡",
          fontSize = 13.sp,
          modifier = Modifier.padding(end = 6.dp, top = 1.dp)
        )
        Text(
          text = intuitionText,
          color = SilverMuted,
          fontSize = 13.sp,
          lineHeight = 19.sp
        )
      }

      // Interactive Scenario Challenge Preview
      if (card.challengeScenario.isNotBlank()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showChallenge = !showChallenge }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (showChallenge) "Hide Active Scenario ▲" else "Active Recall Scenario ▼",
            color = CyanGlow,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        AnimatedVisibility(
          visible = showChallenge,
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF14141E))
              .border(0.8.dp, CyanGlow.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
              .padding(12.dp)
          ) {
            Text(
              text = card.challengeScenario,
              color = SilverText,
              fontSize = 13.sp,
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Key: ${card.challengeExpectedKey}",
              color = SilverDim,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Primary Action: [ASK DEEPDIVE]
      ElevatedButton(
        onClick = onAskDeepDive,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("ask_deepdive_button_${card.id}"),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = CyanNeon,
          contentColor = ObsidianBg
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.Psychology,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (currentLanguage == LanguageSetting.SINHALA) "ASK DEEPDIVE / ගැඹුරින් විමසන්න" else "ASK DEEPDIVE",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          letterSpacing = 0.8.sp
        )
      }
    }
  }
}
