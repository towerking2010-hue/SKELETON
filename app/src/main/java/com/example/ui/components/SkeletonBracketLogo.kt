package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyanNeon

/**
 * SKELETON ARCHITECTURAL LOGO MARK
 * Minimalist Single Structural Axis / Precision Bracket Reticle.
 * Zero literal skulls or cheesy AI motifs. Clean, high-end, razor-sharp.
 */
@Composable
fun SkeletonBracketLogo(
  modifier: Modifier = Modifier,
  size: Dp = 26.dp,
  accentColor: Color = CyanNeon
) {
  Canvas(modifier = modifier.size(size)) {
    val w = this.size.width
    val h = this.size.height
    val strokeWidth = 1.6f.dp.toPx()
    val tickSize = 4f.dp.toPx()
    val gap = 3f.dp.toPx()

    // 1. Central Vertical Structural Axis
    drawLine(
      color = accentColor,
      start = Offset(w / 2f, gap),
      end = Offset(w / 2f, h - gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )

    // 2. Center Cross Axis Hairline (Optical Alignment Notch)
    val notchLen = 2.5f.dp.toPx()
    drawLine(
      color = accentColor.copy(alpha = 0.9f),
      start = Offset(w / 2f - notchLen, h / 2f),
      end = Offset(w / 2f + notchLen, h / 2f),
      strokeWidth = 1.2f.dp.toPx(),
      cap = StrokeCap.Square
    )

    // 3. Top-Left Bracket Reticle
    drawLine(
      color = accentColor,
      start = Offset(gap, gap + tickSize),
      end = Offset(gap, gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )
    drawLine(
      color = accentColor,
      start = Offset(gap, gap),
      end = Offset(gap + tickSize, gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )

    // 4. Top-Right Bracket Reticle
    drawLine(
      color = accentColor,
      start = Offset(w - gap - tickSize, gap),
      end = Offset(w - gap, gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )
    drawLine(
      color = accentColor,
      start = Offset(w - gap, gap),
      end = Offset(w - gap, gap + tickSize),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )

    // 5. Bottom-Left Bracket Reticle
    drawLine(
      color = accentColor,
      start = Offset(gap, h - gap - tickSize),
      end = Offset(gap, h - gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )
    drawLine(
      color = accentColor,
      start = Offset(gap, h - gap),
      end = Offset(gap + tickSize, h - gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )

    // 6. Bottom-Right Bracket Reticle
    drawLine(
      color = accentColor,
      start = Offset(w - gap - tickSize, h - gap),
      end = Offset(w - gap, h - gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )
    drawLine(
      color = accentColor,
      start = Offset(w - gap, h - gap - tickSize),
      end = Offset(w - gap, h - gap),
      strokeWidth = strokeWidth,
      cap = StrokeCap.Square
    )
  }
}
