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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun SpacedRepetitionScreen(
  cards: List<FeedCard>,
  currentLanguage: LanguageSetting,
  onRecordRecall: (String, Boolean) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val stage0Count = cards.count { it.recallStage == 0 }
  val stage1Count = cards.count { it.recallStage == 1 }
  val stage2Count = cards.count { it.recallStage == 2 }
  val stage3Count = cards.count { it.recallStage >= 3 }

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
        .background(ObsidianSurface)
        .border(0.8.dp, ObsidianBorder)
        .padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color(0xFFA78BFA)
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "MODE [SPACED_REPETITION_ENGINE]",
          color = Color(0xFFA78BFA),
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "3-Day, 7-Day & 14-Day Memory Decay Intervals",
          color = SilverMuted,
          fontSize = 10.sp
        )
      }
    }

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Memory Decay Gauges
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          IntervalStatCard(
            label = "3-DAY",
            count = stage1Count,
            accentColor = AmberAccent,
            modifier = Modifier.weight(1f)
          )
          IntervalStatCard(
            label = "7-DAY",
            count = stage2Count,
            accentColor = CyanNeon,
            modifier = Modifier.weight(1f)
          )
          IntervalStatCard(
            label = "14-DAY+",
            count = stage3Count,
            accentColor = EmeraldMastery,
            modifier = Modifier.weight(1f)
          )
        }
      }

      item {
        Text(
          text = "CARDS QUEUED FOR RECALL VERIFICATION",
          color = SilverDim,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
      }

      items(cards, key = { it.id }) { card ->
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ObsidianSurface)
            .border(0.8.dp, ObsidianBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = card.title,
              color = SilverText,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )

            val stageBadge = when (card.recallStage) {
              1 -> "3d Interval"
              2 -> "7d Interval"
              3 -> "14d Interval"
              4 -> "Permanent"
              else -> "Fresh Node"
            }
            Text(
              text = stageBadge,
              color = if (card.recallStage >= 3) EmeraldMastery else AmberAccent,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Active Scenario: ${card.challengeScenario}",
            color = SilverMuted,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Drill Actions
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ElevatedButton(
              onClick = { onRecordRecall(card.id, true) },
              colors = ButtonDefaults.elevatedButtonColors(
                containerColor = EmeraldMastery,
                contentColor = ObsidianBg
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Recalled (+Interval)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            ElevatedButton(
              onClick = { onRecordRecall(card.id, false) },
              colors = ButtonDefaults.elevatedButtonColors(
                containerColor = ObsidianSurfaceElevated,
                contentColor = AmberAccent
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(imageVector = Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Decayed (Reset 3d)", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun IntervalStatCard(
  label: String,
  count: Int,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(ObsidianSurface)
      .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
      .padding(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = label,
      color = accentColor,
      fontSize = 10.sp,
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "$count",
      color = SilverText,
      fontSize = 20.sp,
      fontWeight = FontWeight.Black
    )
  }
}
