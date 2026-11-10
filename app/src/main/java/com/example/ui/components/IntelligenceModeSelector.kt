package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IntelligenceMode
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

/**
 * High-performance, tactile segmented switch component allowing users to
 * seamlessly switch between CORE and DEEPDIVE intelligence modes with smooth
 * animated sliding indicators and live cognitive telemetry.
 */
@Composable
fun IntelligenceModeSelector(
  selectedMode: IntelligenceMode,
  onModeSelected: (IntelligenceMode) -> Unit,
  modifier: Modifier = Modifier,
  activeTopicName: String? = null,
  showTelemetry: Boolean = true
) {
  // Pulsing animation for active live neural indicator
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.45f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  val activeAccentColor by animateColorAsState(
    targetValue = if (selectedMode == IntelligenceMode.CORE) CyanNeon else AmberAccent,
    animationSpec = tween(300),
    label = "accent_color"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("intelligence_mode_selector")
  ) {
    // Outer Segmented Track
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .clip(RoundedCornerShape(26.dp))
        .background(ObsidianSurfaceElevated)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(26.dp))
        .padding(4.dp)
    ) {
      val trackWidth = maxWidth
      val pillWidth = trackWidth / 2

      val targetOffsetFraction by animateFloatAsState(
        targetValue = if (selectedMode == IntelligenceMode.CORE) 0f else 1f,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "pill_offset"
      )

      val animatedOffset: Dp = pillWidth * targetOffsetFraction

      // Sliding Selection Pill
      Box(
        modifier = Modifier
          .offset(x = animatedOffset)
          .width(pillWidth)
          .fillMaxHeight()
          .clip(RoundedCornerShape(22.dp))
          .background(
            Brush.horizontalGradient(
              colors = if (selectedMode == IntelligenceMode.CORE) {
                listOf(CyanNeon.copy(alpha = 0.22f), CyanNeon.copy(alpha = 0.12f))
              } else {
                listOf(AmberAccent.copy(alpha = 0.24f), AmberAccent.copy(alpha = 0.14f))
              }
            )
          )
          .border(
            width = 1.2.dp,
            color = activeAccentColor.copy(alpha = 0.85f),
            shape = RoundedCornerShape(22.dp)
          )
      )

      // Interactive Click Targets & Labels Row
      Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // CORE Mode Segment
        ModeSegmentButton(
          title = "CORE",
          badge = "TWIN",
          icon = Icons.Filled.Psychology,
          isSelected = selectedMode == IntelligenceMode.CORE,
          activeColor = CyanNeon,
          pulseAlpha = if (selectedMode == IntelligenceMode.CORE) pulseAlpha else 0f,
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .testTag("mode_switch_core"),
          onClick = { onModeSelected(IntelligenceMode.CORE) }
        )

        // DEEPDIVE Mode Segment
        ModeSegmentButton(
          title = "DEEPDIVE",
          badge = "ISOLATED",
          icon = Icons.Filled.CenterFocusStrong,
          isSelected = selectedMode == IntelligenceMode.DEEPDIVE,
          activeColor = AmberAccent,
          pulseAlpha = if (selectedMode == IntelligenceMode.DEEPDIVE) pulseAlpha else 0f,
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .testTag("mode_switch_deepdive"),
          onClick = { onModeSelected(IntelligenceMode.DEEPDIVE) }
        )
      }
    }

    // Secondary Telemetry & Context Strip
    if (showTelemetry) {
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f, fill = false)
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(activeAccentColor.copy(alpha = pulseAlpha))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = when (selectedMode) {
              IntelligenceMode.CORE -> "MODE: CORE // Casual Twin • Global Vault • Feed Sync"
              IntelligenceMode.DEEPDIVE -> {
                if (activeTopicName != null) {
                  "MODE: DEEPDIVE // [TOPIC: $activeTopicName]"
                } else {
                  "MODE: DEEPDIVE // Strict Isolation • 2-Layer Breakdown"
                }
              }
            },
            color = SilverMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp,
            maxLines = 1
          )
        }

        Text(
          text = if (selectedMode == IntelligenceMode.CORE) "[META-AI]" else "[ZERO-SLOP]",
          color = activeAccentColor.copy(alpha = 0.8f),
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }
  }
}

/**
 * Individual mode button segment inside the track.
 */
@Composable
private fun ModeSegmentButton(
  title: String,
  badge: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  activeColor: Color,
  pulseAlpha: Float,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val contentColor by animateColorAsState(
    targetValue = if (isSelected) SilverText else SilverMuted,
    animationSpec = tween(200),
    label = "content_color"
  )

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(22.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true, color = activeColor),
        role = Role.Tab,
        onClick = onClick
      ),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.padding(horizontal = 8.dp)
    ) {
      // Icon
      Icon(
        imageVector = icon,
        contentDescription = "$title Mode",
        tint = if (isSelected) activeColor else SilverDim,
        modifier = Modifier.size(18.dp)
      )

      Spacer(modifier = Modifier.width(6.dp))

      // Title
      Text(
        text = title,
        color = contentColor,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.width(6.dp))

      // Micro Badge Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(
            if (isSelected) activeColor.copy(alpha = 0.25f) else ObsidianSurface
          )
          .border(
            0.6.dp,
            if (isSelected) activeColor.copy(alpha = 0.6f) else ObsidianBorder,
            RoundedCornerShape(6.dp)
          )
          .padding(horizontal = 5.dp, vertical = 2.dp)
      ) {
        Text(
          text = badge,
          color = if (isSelected) activeColor else SilverDim,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 0.5.sp
        )
      }

      // Glowing dot for active mode
      if (isSelected) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
          modifier = Modifier
            .size(5.dp)
            .clip(CircleShape)
            .background(activeColor.copy(alpha = pulseAlpha))
        )
      }
    }
  }
}

/**
 * Compact pill-shaped switch for TopHeaderBar, floating action bars, or compact toolbars.
 */
@Composable
fun CompactIntelligenceModeSwitch(
  selectedMode: IntelligenceMode,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val activeColor by animateColorAsState(
    targetValue = if (selectedMode == IntelligenceMode.CORE) CyanNeon else AmberAccent,
    animationSpec = tween(250),
    label = "compact_switch_color"
  )

  Box(
    modifier = modifier
      .height(34.dp)
      .clip(RoundedCornerShape(17.dp))
      .background(ObsidianSurfaceElevated)
      .border(1.dp, activeColor.copy(alpha = 0.8f), RoundedCornerShape(17.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(color = activeColor),
        role = Role.Switch,
        onClick = onToggle
      )
      .padding(horizontal = 10.dp)
      .testTag("compact_intelligence_mode_switch"),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(activeColor)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = selectedMode.name,
        color = SilverText,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = if (selectedMode == IntelligenceMode.CORE) "⇄ DEEP" else "⇄ CORE",
        color = activeColor,
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace
      )
    }
  }
}
