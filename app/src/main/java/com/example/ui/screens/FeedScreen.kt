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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import com.example.data.model.GhostChallenge
import com.example.data.model.LanguageSetting
import com.example.ui.components.MinimalFeedCardView
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
fun FeedScreen(
  cards: List<FeedCard>,
  ghostChallenges: List<GhostChallenge>,
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  currentUser: com.google.firebase.auth.FirebaseUser? = null,
  onSignInSuccess: (com.google.firebase.auth.FirebaseUser) -> Unit = {},
  onSignOut: () -> Unit = {},
  onAskDeepDive: (String) -> Unit,
  onToggleSave: (FeedCard) -> Unit,
  onTriggerGhostChallenge: () -> Unit,
  onAnswerGhostChallenge: (GhostChallenge, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val pendingChallenge = ghostChallenges.firstOrNull { it.status == "PENDING" }
  var challengeAnswerInput by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .testTag("feed_lazy_column"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Cloud Auth & Persistence Status Bar
    item(key = "auth_status_bar") {
      com.example.ui.components.AuthStatusBar(
        currentUser = currentUser,
        onSignInSuccess = onSignInSuccess,
        onSignOut = onSignOut
      )
    }
    // Ghosts of Mastery Alert Card (if triggered)
    if (pendingChallenge != null) {
      item(key = "ghost_challenge_${pendingChallenge.id}") {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF191222))
            .border(1.2.dp, AmberAccent, RoundedCornerShape(26.dp))
            .padding(16.dp)
            .testTag("ghost_challenge_card")
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(AmberAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Shield,
                  contentDescription = null,
                  tint = AmberAccent,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "GHOST OF MASTERY",
                color = AmberAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
              )
            }

            Text(
              text = "Micro-Recall",
              color = SilverDim,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = pendingChallenge.promptQuestion,
            color = SilverText,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Answer actions
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ElevatedButton(
              onClick = {
                onAnswerGhostChallenge(pendingChallenge, "Quorum consensus boundary / Invariant verified")
              },
              colors = ButtonDefaults.elevatedButtonColors(
                containerColor = AmberAccent,
                contentColor = ObsidianBg
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = "Submit Recall Proof",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }

            ElevatedButton(
              onClick = {
                onAnswerGhostChallenge(pendingChallenge, "Need review")
              },
              colors = ButtonDefaults.elevatedButtonColors(
                containerColor = ObsidianSurfaceElevated,
                contentColor = SilverMuted
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "Decay",
                fontSize = 11.sp
              )
            }
          }
        }
      }
    } else {
      // Small trigger button to test Ghost of Mastery
      item(key = "ghost_trigger_banner") {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface)
            .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            .clickable { onTriggerGhostChallenge() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "⚔️", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (currentLanguage == LanguageSetting.SINHALA)
                "GHOSTS OF MASTERY: Micro-challenge එකක් ආරම්භ කරන්න"
              else
                "GHOSTS OF MASTERY: Trigger Micro-Recall",
              color = SilverMuted,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }
          Text(text = "ACTIVATE", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
      }
    }

    // Feed Cards List
    if (cards.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No cognitive nodes in feed. Sync from CORE.",
            color = SilverDim,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    } else {
      items(cards, key = { it.id }) { card ->
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
