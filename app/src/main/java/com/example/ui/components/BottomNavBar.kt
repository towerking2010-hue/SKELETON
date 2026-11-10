package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
import com.example.ui.MainTab
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun BottomNavBar(
  currentTab: MainTab,
  currentLanguage: LanguageSetting,
  onSelectTab: (MainTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(ObsidianBg)
      .border(0.8.dp, ObsidianBorder)
      .navigationBarsPadding()
      .padding(horizontal = 24.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clip(RoundedCornerShape(28.dp))
        .background(ObsidianSurfaceElevated)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(28.dp))
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Feed
      val feedLabel = if (currentLanguage == LanguageSetting.SINHALA) "Feed" else "Feed"
      BottomNavItem(
        label = feedLabel,
        selected = currentTab == MainTab.FEED,
        selectedIcon = Icons.Filled.DynamicFeed,
        unselectedIcon = Icons.Outlined.DynamicFeed,
        testTag = "bottom_nav_feed",
        onClick = { onSelectTab(MainTab.FEED) }
      )

      // 2. CORE
      val coreLabel = if (currentLanguage == LanguageSetting.SINHALA) "CORE" else "CORE"
      BottomNavItem(
        label = coreLabel,
        selected = currentTab == MainTab.CORE,
        selectedIcon = Icons.Filled.Psychology,
        unselectedIcon = Icons.Outlined.Psychology,
        testTag = "bottom_nav_core",
        onClick = { onSelectTab(MainTab.CORE) }
      )

      // 3. Saved
      val savedLabel = if (currentLanguage == LanguageSetting.SINHALA) "Saved" else "Saved"
      BottomNavItem(
        label = savedLabel,
        selected = currentTab == MainTab.SAVED,
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder,
        testTag = "bottom_nav_saved",
        onClick = { onSelectTab(MainTab.SAVED) }
      )
    }
  }
}

@Composable
private fun BottomNavItem(
  label: String,
  selected: Boolean,
  selectedIcon: ImageVector,
  unselectedIcon: ImageVector,
  testTag: String,
  onClick: () -> Unit
) {
  val iconTint by animateColorAsState(
    targetValue = if (selected) CyanNeon else SilverDim,
    label = "iconTint"
  )
  val textColor by animateColorAsState(
    targetValue = if (selected) SilverText else SilverDim,
    label = "textColor"
  )

  Column(
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag(testTag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = if (selected) selectedIcon else unselectedIcon,
      contentDescription = label,
      tint = iconTint,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      color = textColor,
      fontSize = 10.sp,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
      fontFamily = FontFamily.Monospace,
      letterSpacing = 0.5.sp
    )
  }
}
