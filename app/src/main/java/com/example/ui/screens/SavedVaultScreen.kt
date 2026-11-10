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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.MinimalFeedCardView
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun SavedVaultScreen(
  savedCards: List<FeedCard>,
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  onAskDeepDive: (String) -> Unit,
  onToggleSave: (FeedCard) -> Unit,
  onLaunchCollider: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .testTag("saved_vault_lazy_column"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Vault Banner + Concept Collider Shortcut
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurface)
          .border(1.dp, AmberAccent.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Bookmark,
              contentDescription = null,
              tint = AmberAccent,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (currentLanguage == LanguageSetting.SINHALA) "SAVED VAULT" else "SAVED VAULT",
              color = AmberAccent,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${savedCards.size} ${if (currentLanguage == LanguageSetting.SINHALA) "ස්ථීර ඥාන ඛණ්ඩ" else "permanent cognitive nodes"}",
            color = SilverMuted,
            fontSize = 12.sp
          )
        }

        ElevatedButton(
          onClick = onLaunchCollider,
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = ObsidianSurfaceElevated,
            contentColor = AmberAccent
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("vault_collider_button")
        ) {
          Icon(
            imageVector = Icons.Filled.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Collide",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    if (savedCards.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Vault is empty",
              color = SilverDim,
              fontSize = 14.sp,
              fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = if (currentLanguage == LanguageSetting.SINHALA)
                "Feed එකෙන් වැදගත් cards bookmark කරන්න"
              else
                "Bookmark high-leverage cards from Feed",
              color = SilverMuted,
              fontSize = 12.sp
            )
          }
        }
      }
    } else {
      items(savedCards, key = { it.id }) { card ->
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
