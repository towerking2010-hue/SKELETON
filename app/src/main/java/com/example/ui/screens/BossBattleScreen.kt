package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CrimsonAdrenal
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import kotlinx.coroutines.delay

@Composable
fun BossBattleScreen(
  vaultCards: List<FeedCard>,
  currentLanguage: LanguageSetting,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val activeCard = remember(vaultCards) { vaultCards.randomOrNull() }
  var timerSeconds by remember { mutableIntStateOf(30) }
  var battleActive by remember { mutableStateOf(activeCard != null) }
  var selectedChoiceIndex by remember { mutableStateOf<Int?>(null) }
  var score by remember { mutableIntStateOf(0) }

  val questionTitle = activeCard?.let { "STRESS TEST // " + it.title } ?: "NO VAULT CONCEPT AVAILABLE"
  val questionScenario = activeCard?.challengeScenario?.takeIf { it.isNotBlank() }
    ?: activeCard?.let { "Explain the central mechanism of " + it.title + " and identify the condition under which it can fail." }
    ?: "Save or create a learning card first, then launch Boss Battle."
  val expectedKey = activeCard?.challengeExpectedKey?.takeIf { it.isNotBlank() }
    ?: "Identify the governing mechanism, its invariant, and the relevant failure boundary."
  val options = listOf(
    "A) Ignore the input conditions and assume the result is always valid.",
    "B) Apply the governing mechanism: " + expectedKey,
    "C) Repeat the title without explaining the underlying rule.",
    "D) Skip validation and infer the outcome from intuition alone."
  )
  val correctIndex = 1

  LaunchedEffect(battleActive) {
    if (battleActive) {
      while (timerSeconds > 0 && battleActive) {
        delay(1000L)
        timerSeconds--
      }
      if (timerSeconds == 0) {
        battleActive = false
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .navigationBarsPadding()
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(Color(0xFF1E0E12))
        .border(0.8.dp, CrimsonAdrenal.copy(alpha = 0.5f))
        .padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = CrimsonAdrenal
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "MODE [BOSS_BATTLE]",
          color = CrimsonAdrenal,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Multi-Variable Stress Test",
          color = SilverMuted,
          fontSize = 10.sp
        )
      }
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Countdown Bar
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(ObsidianSurface)
          .border(1.dp, CrimsonAdrenal.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "STRESS CLOCK",
            color = CrimsonAdrenal,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${timerSeconds}s REMAINING",
            color = if (timerSeconds <= 10) CrimsonAdrenal else AmberAccent,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
          progress = { timerSeconds / 30f },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = if (timerSeconds <= 10) CrimsonAdrenal else CyanNeon,
          trackColor = ObsidianSurfaceElevated
        )
      }

      // Scenario Question Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(22.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp))
          .padding(18.dp)
      ) {
        Text(
          text = questionTitle,
          color = SilverText,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = questionScenario,
          color = SilverMuted,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Multiple choice options
        options.forEachIndexed { index, option ->
          val isSelected = selectedChoiceIndex == index
          val isCorrect = index == correctIndex
          val cardBorder = when {
            selectedChoiceIndex == null -> ObsidianBorder
            isSelected && isCorrect -> EmeraldMastery
            isSelected && !isCorrect -> CrimsonAdrenal
            isCorrect -> EmeraldMastery
            else -> ObsidianBorder
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) ObsidianSurfaceElevated else Color(0xFF0F0F14))
              .border(1.dp, cardBorder, RoundedCornerShape(12.dp))
              .clickable(enabled = battleActive && selectedChoiceIndex == null) {
                selectedChoiceIndex = index
                battleActive = false
                if (index == correctIndex) {
                  score += 100
                }
              }
              .padding(12.dp)
              .testTag("boss_option_$index")
          ) {
            Text(
              text = option,
              color = if (isSelected) SilverText else SilverMuted,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              lineHeight = 16.sp
            )
          }
        }

        if (selectedChoiceIndex != null || timerSeconds == 0) {
          Spacer(modifier = Modifier.height(16.dp))
          val won = selectedChoiceIndex == correctIndex
          Text(
            text = if (won) "✓ MASTERED: Correct mechanism selected (+100 PTS)" else "✗ FAILED: Revisit this concept and inspect its failure boundary.",
            color = if (won) EmeraldMastery else CrimsonAdrenal,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          ElevatedButton(
            onClick = {
              timerSeconds = 30
              battleActive = true
              selectedChoiceIndex = null
            },
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = CrimsonAdrenal,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "RELOAD STRESS TEST",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }
  }
}
