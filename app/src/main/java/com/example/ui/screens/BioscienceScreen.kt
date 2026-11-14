package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting
import com.example.engine.bio.BioscienceModule
import com.example.ui.components.MinimalFeedCardView
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun BioscienceScreen(
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  onAskDeepDive: (String) -> Unit,
  onToggleSave: (FeedCard) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val bioCards = remember { BioscienceModule.getBioscienceCuratedCards() }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
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
          tint = EmeraldMastery
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "BIOSCIENCE & COGNITIVE SKILLS",
          color = EmeraldMastery,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Molecular Genetics, Synaptic Biophysics & Optogenetics",
          color = SilverMuted,
          fontSize = 10.sp
        )
      }
    }

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .testTag("bioscience_lazy_column"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ObsidianSurface)
            .border(1.dp, EmeraldMastery.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Science, contentDescription = null, tint = EmeraldMastery)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "ADVANCED BIOLOGICAL COGNITIVE MODULE",
              color = EmeraldMastery,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Curated molecular pathways engineered for instantaneous conceptual mastery. Explores the physical mechanisms governing gene expression, optogenetic ion channels, and synaptic LTP.",
            color = SilverMuted,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        }
      }

      items(bioCards, key = { it.id }) { card ->
        MinimalFeedCardView(
          card = card,
          cringeFilterEnabled = cringeFilterEnabled,
          currentLanguage = currentLanguage,
          onAskDeepDive = { onAskDeepDive(card.id) },
          onToggleSave = { onToggleSave(card) }
        )
      }
    }
  }
}
