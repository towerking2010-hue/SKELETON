package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedCard
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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun YggdrasilGraphScreen(
  cards: List<FeedCard>,
  onOpenDeepDive: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  var selectedNodeId by remember { mutableStateOf<String?>(cards.firstOrNull()?.id) }
  val selectedCard = cards.find { it.id == selectedNodeId }
  val semanticEdges = remember(cards) {
    buildList {
      for (i in cards.indices) {
        for (j in i + 1 until cards.size) {
          val a = cards[i]
          val b = cards[j]
          val aLink = a.crossLinkTarget.orEmpty()
          val bLink = b.crossLinkTarget.orEmpty()
          val explicitLink =
            (aLink.isNotBlank() && (b.title.contains(aLink, ignoreCase = true) || b.branchDomain.contains(aLink, ignoreCase = true))) ||
            (bLink.isNotBlank() && (a.title.contains(bLink, ignoreCase = true) || a.branchDomain.contains(bLink, ignoreCase = true)))
          val sharedDomain = a.branchDomain.equals(b.branchDomain, ignoreCase = true)
          if (explicitLink || sharedDomain) add(i to j)
        }
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .navigationBarsPadding()
  ) {
    // Top Bar
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
          tint = CyanNeon
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "MODE [YGGDRASIL / ATTRACTOR GRAPH]",
          color = SilverText,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "${cards.size} Linked Knowledge Nodes",
          color = CyanNeon,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // 3D-Like Attractor Canvas
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(ObsidianSurface)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp))
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(cards) {
            detectTapGestures { tapOffset ->
              val centerX = size.width / 2f
              val centerY = size.height / 2f
              val radius = size.width.coerceAtMost(size.height) * 0.38f
              cards.forEachIndexed { index, card ->
                val angle = (2 * Math.PI * index / cards.size) - Math.PI / 2
                val nodeX = centerX + (radius * cos(angle)).toFloat()
                val nodeY = centerY + (radius * sin(angle)).toFloat()
                val dist = (tapOffset.x - nodeX) * (tapOffset.x - nodeX) +
                  (tapOffset.y - nodeY) * (tapOffset.y - nodeY)
                if (dist < 40f * 40f) {
                  selectedNodeId = card.id
                }
              }
            }
          }
      ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = size.width.coerceAtMost(size.height) * 0.38f

        // Draw central attractor core
        drawCircle(
          color = CyanNeon.copy(alpha = 0.08f),
          radius = radius * 1.1f,
          center = Offset(centerX, centerY)
        )
        drawCircle(
          color = CyanNeon.copy(alpha = 0.2f),
          radius = 16f,
          center = Offset(centerX, centerY)
        )

        // Draw nodes and cross links
        val nodePositions = cards.mapIndexed { index, _ ->
          val angle = (2 * Math.PI * index / cards.size) - Math.PI / 2
          Offset(
            x = centerX + (radius * cos(angle)).toFloat(),
            y = centerY + (radius * sin(angle)).toFloat()
          )
        }

        // Connections reflect explicit cross-links or shared learning domains.
        semanticEdges.forEach { (from, to) ->
          val selectedConnection = cards[from].id == selectedNodeId || cards[to].id == selectedNodeId
          drawLine(
            color = if (selectedConnection) CyanNeon.copy(alpha = 0.72f) else EmeraldMastery.copy(alpha = 0.22f),
            start = nodePositions[from],
            end = nodePositions[to],
            strokeWidth = if (selectedConnection) 2.2f else 1.0f
          )
        }

        // Draw each node circle
        nodePositions.forEachIndexed { index, pos ->
          val card = cards[index]
          val isSelected = card.id == selectedNodeId
          val color = if (isSelected) CyanNeon else EmeraldMastery

          drawCircle(
            color = color.copy(alpha = if (isSelected) 0.9f else 0.4f),
            radius = if (isSelected) 22f else 14f,
            center = pos
          )
          drawCircle(
            color = Color.White,
            radius = if (isSelected) 8f else 4f,
            center = pos
          )
        }
      }

      // Legend overlay
      Box(
        modifier = Modifier
          .padding(12.dp)
          .align(Alignment.TopStart)
      ) {
        Text(
          text = "Concept links · shared domains · cross-references",
          color = SilverDim,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // Selected Node Inspector Card
    if (selectedCard != null) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurface)
          .border(1.dp, CyanNeon.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
          .padding(16.dp)
      ) {
        Text(
          text = "[TREE_NODE: ${selectedCard.branchDomain} | ${selectedCard.title} | ${selectedCard.crossLinkTarget ?: "Root"}]",
          color = CyanNeon,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = selectedCard.title,
          color = SilverText,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = selectedCard.itch,
          color = SilverMuted,
          fontSize = 12.sp,
          lineHeight = 16.sp,
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        ElevatedButton(
          onClick = { onOpenDeepDive(selectedCard.id) },
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = CyanNeon,
            contentColor = ObsidianBg
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("yggdrasil_inspect_deepdive")
        ) {
          Icon(
            imageVector = Icons.Filled.Psychology,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "DEEPDIVE INTO NODE",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}
