package com.example.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.data.model.IntelligenceMode
import com.example.data.model.LanguageSetting
import com.example.ui.theme.CrimsonAdrenal
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import com.example.ui.theme.SinhalaPillGold

@Composable
fun TopHeaderBar(
  currentLanguage: LanguageSetting,
  adrenalMode: Boolean,
  selectedIntelligenceMode: IntelligenceMode = IntelligenceMode.CORE,
  onToggleIntelligenceMode: () -> Unit = {},
  onToggleLanguage: () -> Unit,
  onToggleAdrenalMode: () -> Unit,
  onOpenDrawer: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth()
      .background(ObsidianBg)
      .statusBarsPadding()
      .height(68.dp)
      .padding(horizontal = 18.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier.size(38.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, if (adrenalMode) CrimsonAdrenal else CyanNeon.copy(alpha = 0.65f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
      ) {
        SkeletonBracketLogo(size = 23.dp, accentColor = if (adrenalMode) CrimsonAdrenal else CyanNeon)
      }
      Spacer(Modifier.width(11.dp))
      Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Text(
          "SKELETON",
          color = SilverText,
          fontSize = 15.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.8.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
          Box(Modifier.size(5.dp).clip(CircleShape).background(CyanNeon))
          Text("PERSONAL KNOWLEDGE SYSTEM", color = SilverMuted, fontSize = 8.sp, letterSpacing = 0.8.sp)
        }
      }
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
      Box(
        modifier = Modifier.clip(RoundedCornerShape(10.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
          .clickable(onClick = onToggleIntelligenceMode)
          .padding(horizontal = 9.dp, vertical = 8.dp)
          .testTag("intelligence_mode_toggle"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          if (selectedIntelligenceMode == IntelligenceMode.CORE) "CORE" else "DEEPDIVE",
          color = CyanNeon,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
      }

      Box(
        modifier = Modifier.clip(RoundedCornerShape(10.dp))
          .background(ObsidianSurface)
          .border(1.dp, if (currentLanguage == LanguageSetting.SINHALA) SinhalaPillGold else ObsidianBorder, RoundedCornerShape(10.dp))
          .clickable(onClick = onToggleLanguage)
          .padding(horizontal = 9.dp, vertical = 8.dp)
          .testTag("language_toggle_pill"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          if (currentLanguage == LanguageSetting.SINHALA) "සිං" else "EN",
          color = if (currentLanguage == LanguageSetting.SINHALA) SinhalaPillGold else SilverText,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }

      IconButton(onClick = onOpenDrawer, modifier = Modifier.size(40.dp).testTag("subsystem_drawer_button")) {
        Icon(Icons.Filled.Menu, contentDescription = "Open subsystems", tint = SilverText, modifier = Modifier.size(22.dp))
      }
    }
  }
}
