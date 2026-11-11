package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.IntelligenceMode
import com.example.data.model.LanguageSetting
import com.example.ui.components.CompactIntelligenceModeSwitch
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CrimsonAdrenal
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import com.example.ui.theme.SinhalaPillGold

import androidx.compose.foundation.layout.statusBarsPadding

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
    modifier = modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(top = 8.dp)
      .height(60.dp)
      .background(ObsidianBg)
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // App Emblem & Name
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, if (adrenalMode) CrimsonAdrenal else CyanNeon, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
      ) {
        SkeletonBracketLogo(
          size = 20.dp,
          accentColor = if (adrenalMode) CrimsonAdrenal else CyanNeon
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "SKELETON",
            color = SilverText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 2.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "// DASUN",
            color = CyanNeon,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
        }
        Text(
          text = if (adrenalMode) "MODE: ADRENALINE [MAX]" else "PERSONAL KNOWLEDGE SYSTEM",
          color = if (adrenalMode) CrimsonAdrenal else SilverMuted,
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
      }
    }

    // Right Controls: Intelligence Mode Switch + Language Pill + Adrenal toggle + Drawer (≡)
    Row(verticalAlignment = Alignment.CenterVertically) {

      // Language Switcher Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurfaceElevated)
          .border(
            1.dp,
            if (currentLanguage == LanguageSetting.SINHALA) SinhalaPillGold else CyanNeon,
            RoundedCornerShape(20.dp)
          )
          .clickable { onToggleLanguage() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("language_toggle_pill")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(if (currentLanguage == LanguageSetting.SINHALA) SinhalaPillGold else CyanNeon)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (currentLanguage == LanguageSetting.SINHALA) "සිංහල" else "EN",
            color = SilverText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Slide-Up Subsystem Drawer Button (≡)
      IconButton(
        onClick = onOpenDrawer,
        modifier = Modifier
          .size(44.dp)
          .testTag("subsystem_drawer_button")
      ) {
        Icon(
          imageVector = Icons.Filled.Menu,
          contentDescription = "Subsystems Menu",
          tint = CyanNeon,
          modifier = Modifier.size(26.dp)
        )
      }
    }
  }
}
