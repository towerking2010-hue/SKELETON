package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun ConceptColliderScreen(
  vaultCards: List<FeedCard>,
  currentLanguage: LanguageSetting,
  onCollide: (FeedCard, FeedCard) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val candidateCards = if (vaultCards.size >= 2) vaultCards else vaultCards + vaultCards
  var cardA by remember { mutableStateOf<FeedCard?>(candidateCards.getOrNull(0)) }
  var cardB by remember { mutableStateOf<FeedCard?>(candidateCards.getOrNull(1)) }
  var collisionResultTitle by remember { mutableStateOf<String?>(null) }

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
          tint = AmberAccent
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "MODE [CONCEPT_COLLIDER]",
          color = AmberAccent,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Synthesize 2 Vault Concepts into 30s Insight",
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
      // Collider Chamber UI
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(ObsidianSurface)
            .border(1.dp, AmberAccent.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .padding(18.dp)
        ) {
          Text(
            text = "COLLISION CHAMBER",
            color = AmberAccent,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Card A Slot
          SelectedColliderSlot(
            label = "COMPONENT ALPHA",
            card = cardA,
            borderColor = CyanNeon
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Collision Icon
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ObsidianSurfaceElevated)
                .border(1.dp, AmberAccent, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "⊗", color = AmberAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Card B Slot
          SelectedColliderSlot(
            label = "COMPONENT BETA",
            card = cardB,
            borderColor = AmberAccent
          )

          Spacer(modifier = Modifier.height(18.dp))

          // Collide Action Button
          ElevatedButton(
            onClick = {
              if (cardA != null && cardB != null) {
                onCollide(cardA!!, cardB!!)
                collisionResultTitle = "${cardA!!.title} ⊗ ${cardB!!.title}"
              }
            },
            enabled = cardA != null && cardB != null && cardA?.id != cardB?.id,
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = AmberAccent,
              contentColor = ObsidianBg
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("execute_collision_button")
          ) {
            Icon(imageVector = Icons.Filled.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "EXECUTE SYNTHESIS (COLLIDE)",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )
          }

          if (collisionResultTitle != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "✓ Collided into: $collisionResultTitle (Added to Feed & Vault!)",
              color = CyanNeon,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      item {
        Text(
          text = "TAP TO ASSIGN NODES FROM VAULT",
          color = SilverDim,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
      }

      items(vaultCards) { card ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurfaceElevated)
            .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
            .clickable {
              if (cardA == null || cardA?.id == card.id) {
                cardA = card
              } else {
                cardB = card
              }
            }
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = card.title,
              color = SilverText,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = card.branchDomain,
              color = CyanNeon,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }

          Text(
            text = if (cardA?.id == card.id) "[ALPHA]" else if (cardB?.id == card.id) "[BETA]" else "Assign",
            color = if (cardA?.id == card.id) CyanNeon else if (cardB?.id == card.id) AmberAccent else SilverDim,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
private fun SelectedColliderSlot(
  label: String,
  card: FeedCard?,
  borderColor: Color
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(ObsidianSurfaceElevated)
      .border(0.8.dp, borderColor, RoundedCornerShape(14.dp))
      .padding(12.dp)
  ) {
    Text(
      text = label,
      color = borderColor,
      fontSize = 9.sp,
      fontFamily = FontFamily.Monospace,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = card?.title ?: "Select a node",
      color = if (card != null) SilverText else SilverDim,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold
    )
    if (card != null) {
      Text(
        text = card.branchDomain,
        color = SilverMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace
      )
    }
  }
}
