package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SkeletonDarkScheme = darkColorScheme(
  primary = CyanNeon,
  onPrimary = ObsidianBg,
  primaryContainer = ObsidianSurfaceHighlight,
  onPrimaryContainer = CyanNeon,
  secondary = SilverMuted,
  onSecondary = ObsidianBg,
  secondaryContainer = ObsidianSurfaceElevated,
  onSecondaryContainer = SilverText,
  tertiary = AmberAccent,
  onTertiary = ObsidianBg,
  background = ObsidianBg,
  onBackground = SilverText,
  surface = ObsidianSurface,
  onSurface = SilverText,
  surfaceVariant = ObsidianSurfaceElevated,
  onSurfaceVariant = SilverMuted,
  outline = ObsidianBorder,
  outlineVariant = ObsidianBorderLight,
  error = CrimsonAdrenal,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  adrenalMode: Boolean = false,
  content: @Composable () -> Unit
) {
  val scheme = if (adrenalMode) {
    SkeletonDarkScheme.copy(
      primary = CrimsonAdrenal,
      primaryContainer = Color(0xFF2A1015),
      onPrimaryContainer = CrimsonAdrenal,
      outline = Color(0xFF4A1A22)
    )
  } else {
    SkeletonDarkScheme
  }

  MaterialTheme(
    colorScheme = scheme,
    typography = Typography,
    content = content
  )
}
