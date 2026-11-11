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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
import com.example.ui.DrawerMode
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CrimsonAdrenal
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
import com.example.ui.theme.SinhalaPillGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsystemDrawer(
  isOpen: Boolean,
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  adrenalMode: Boolean,
  onDismiss: () -> Unit,
  onSelectMode: (DrawerMode) -> Unit,
  onToggleCringeFilter: () -> Unit,
  onToggleAdrenalMode: () -> Unit,
  onSetLanguage: (LanguageSetting) -> Unit,
  modifier: Modifier = Modifier
) {
  if (!isOpen) return

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = ObsidianSurface,
    contentColor = SilverText,
    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    modifier = modifier.testTag("subsystem_modal_sheet")
  ) {
      val scrollState = rememberScrollState()
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
          .padding(horizontal = 24.dp)
          .padding(bottom = 32.dp)
          .navigationBarsPadding()
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "SUBSYSTEM ENGINES",
              color = CyanNeon,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 2.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "SKELETON Cognitive Core",
              color = SilverMuted,
              fontSize = 13.sp
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close Subsystems",
              tint = SilverDim
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tool: MODE [BIOSCIENCE LAB]
        SubsystemRowItem(
          title = "MODE [BIOSCIENCE LAB]",
          description = "Curated bioscience lessons and mechanism-focused study",
          icon = Icons.Filled.Science,
          accentColor = EmeraldMastery,
          testTag = "mode_bioscience",
          onClick = { onSelectMode(DrawerMode.BIOSCIENCE) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool: MODE [VOICE CONVERSATION]
        SubsystemRowItem(
          title = "MODE [VOICE CONVERSATION]",
          description = "On-device speech input, CORE replies and device text-to-speech",
          icon = Icons.Filled.RecordVoiceOver,
          accentColor = CyanNeon,
          testTag = "mode_live_voice",
          onClick = { onSelectMode(DrawerMode.LIVE_VOICE) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool: MODE [IMAGE STUDIO]
        SubsystemRowItem(
          title = "MODE [IMAGE STUDIO]",
          description = "Unavailable in the $0 build — no paid image service is called",
          icon = Icons.Filled.Image,
          accentColor = AmberAccent,
          testTag = "mode_image_studio",
          onClick = { onSelectMode(DrawerMode.IMAGE_STUDIO) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool: MODE [VIDEO STUDIO]
        SubsystemRowItem(
          title = "MODE [VIDEO STUDIO]",
          description = "Unavailable in the $0 build — no paid video service is called",
          icon = Icons.Filled.Videocam,
          accentColor = CyanNeon,
          testTag = "mode_video_studio",
          onClick = { onSelectMode(DrawerMode.VIDEO_STUDIO) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool: MODE [AUDIO TRANSCRIBE]
        SubsystemRowItem(
          title = "MODE [AUDIO TRANSCRIBE]",
          description = "Device speech recognition; supported languages vary by phone",
          icon = Icons.Filled.Mic,
          accentColor = EmeraldMastery,
          testTag = "mode_audio_transcribe",
          onClick = { onSelectMode(DrawerMode.AUDIO_TRANSCRIBE) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool: MODE [SEARCH & MAPS GROUNDING]
        SubsystemRowItem(
          title = "MODE [SEARCH & MAPS GROUNDING]",
          description = "Not active in the $0 offline-first build",
          icon = Icons.Filled.TravelExplore,
          accentColor = CyanNeon,
          testTag = "mode_grounding",
          onClick = { onSelectMode(DrawerMode.GROUNDING) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 1: MODE [YGGDRASIL / ATTRACTOR_GRAPH]
        SubsystemRowItem(
          title = "MODE [YGGDRASIL / GRAPH]",
          description = "Explore saved concepts and their cross-links",
          icon = Icons.Filled.AltRoute,
          accentColor = CyanNeon,
          testTag = "mode_yggdrasil",
          onClick = { onSelectMode(DrawerMode.YGGDRASIL) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 2: MODE [CONCEPT_COLLIDER]
        SubsystemRowItem(
          title = "MODE [CONCEPT_COLLIDER]",
          description = "Combine two saved concepts into one synthesis card",
          icon = Icons.Filled.AutoAwesome,
          accentColor = AmberAccent,
          testTag = "mode_concept_collider",
          onClick = { onSelectMode(DrawerMode.CONCEPT_COLLIDER) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 3: MODE [GHOST_SNIPER_OCR]
        SubsystemRowItem(
          title = "MODE [GHOST_SNIPER_OCR]",
          description = "Camera or photo OCR; Latin-script recognition",
          icon = Icons.Filled.CameraAlt,
          accentColor = EmeraldMastery,
          testTag = "mode_ghost_sniper",
          onClick = { onSelectMode(DrawerMode.GHOST_SNIPER_OCR) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 4: MODE [SPACED_REPETITION_ENGINE]
        SubsystemRowItem(
          title = "MODE [SPACED_REPETITION]",
          description = "3 / 7 / 14 / 30-day review intervals with retry",
          icon = Icons.Filled.QueryBuilder,
          accentColor = Color(0xFFA78BFA),
          testTag = "mode_spaced_repetition",
          onClick = { onSelectMode(DrawerMode.SPACED_REPETITION) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 5: MODE [BOSS_BATTLE]
        SubsystemRowItem(
          title = "MODE [BOSS_BATTLE]",
          description = "Recall challenges generated from your learning vault",
          icon = Icons.Filled.Shield,
          accentColor = CrimsonAdrenal,
          testTag = "mode_boss_battle",
          onClick = { onSelectMode(DrawerMode.BOSS_BATTLE) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 6: MODE [AR_LENS]
        SubsystemRowItem(
          title = "MODE [AR_LENS]",
          description = "Live camera text recognition and Vault capture; no object recognition",
          icon = Icons.Filled.CenterFocusStrong,
          accentColor = CyanNeon,
          testTag = "mode_ar_lens",
          onClick = { onSelectMode(DrawerMode.AR_LENS) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 7: MODE [AUDIOBOOK_CAPTION_ENGINE]
        SubsystemRowItem(
          title = "MODE [AUDIOBOOK_CAPTION]",
          description = "Read TXT, EPUB and text-based PDF with sentence highlighting",
          icon = Icons.Filled.MenuBook,
          accentColor = AmberAccent,
          testTag = "mode_audiobook_caption",
          onClick = { onSelectMode(DrawerMode.AUDIOBOOK_CAPTION) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 8: MODE [DREAM_STATE_ENGINE]
        SubsystemRowItem(
          title = "MODE [DREAM_STATE_ENGINE]",
          description = "10-minute review briefing narrated with device text-to-speech",
          icon = Icons.Filled.Bedtime,
          accentColor = Color(0xFFA78BFA),
          testTag = "mode_dream_state",
          onClick = { onSelectMode(DrawerMode.DREAM_STATE) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 9: MODE [LANGUAGE_TEACHER]
        SubsystemRowItem(
          title = "MODE [LANGUAGE_TEACHER]",
          description = "Language drills and device pronunciation where voices are installed",
          icon = Icons.Filled.School,
          accentColor = EmeraldMastery,
          testTag = "mode_language_teacher",
          onClick = { onSelectMode(DrawerMode.LANGUAGE_TEACHER) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool 10: MODE [GMAIL_VAULT_MAILER]
        SubsystemRowItem(
          title = "MODE [GMAIL_VAULT_MAILER]",
          description = "Prepare a digest email draft for you to review and send",
          icon = Icons.Filled.Email,
          accentColor = CyanNeon,
          testTag = "mode_vault_mailer",
          onClick = { onSelectMode(DrawerMode.VAULT_MAILER) }
        )

      Spacer(modifier = Modifier.height(18.dp))

      // System Controls Divider Box
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
          .padding(16.dp)
      ) {
        Text(
          text = "DYNAMIC ADAPTIVE CONTROLS",
          color = SilverMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Cringe Filter Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "MODE [CRINGE_FILTER]",
              color = SilverText,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "Strip buzzwords, corporate fluff & AI padding",
              color = SilverDim,
              fontSize = 11.sp
            )
          }
          Switch(
            checked = cringeFilterEnabled,
            onCheckedChange = { onToggleCringeFilter() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = CyanNeon,
              checkedTrackColor = ObsidianBg
            ),
            modifier = Modifier.testTag("switch_cringe_filter")
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Adrenal Shifter Mode
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "MODE [ADRENAL_SHIFTER]",
              color = if (adrenalMode) CrimsonAdrenal else SilverText,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "Hyper-contrast HUD, 0ms latency display",
              color = SilverDim,
              fontSize = 11.sp
            )
          }
          Switch(
            checked = adrenalMode,
            onCheckedChange = { onToggleAdrenalMode() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = CrimsonAdrenal,
              checkedTrackColor = ObsidianBg
            ),
            modifier = Modifier.testTag("switch_adrenal_mode")
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Language Settings (Pure English OR Pure Sinhala)
        Text(
          text = "SYSTEM UI LANGUAGE SETTING",
          color = SilverMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          LanguageOptionButton(
            label = "Pure English",
            code = "en",
            selected = currentLanguage == LanguageSetting.ENGLISH,
            modifier = Modifier.weight(1f),
            onClick = { onSetLanguage(LanguageSetting.ENGLISH) }
          )

          LanguageOptionButton(
            label = "Pure Sinhala (සිංහල)",
            code = "si",
            selected = currentLanguage == LanguageSetting.SINHALA,
            modifier = Modifier.weight(1f),
            onClick = { onSetLanguage(LanguageSetting.SINHALA) }
          )
        }
      }
    }
  }
}

@Composable
private fun SubsystemRowItem(
  title: String,
  description: String,
  icon: ImageVector,
  accentColor: Color,
  testTag: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(ObsidianSurfaceElevated)
      .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(14.dp)
      .testTag(testTag),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(accentColor.copy(alpha = 0.15f))
        .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = accentColor,
        modifier = Modifier.size(20.dp)
      )
    }

    Spacer(modifier = Modifier.width(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = SilverText,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace
      )
      Text(
        text = description,
        color = SilverMuted,
        fontSize = 11.sp,
        lineHeight = 15.sp
      )
    }

    Text(
      text = "→",
      color = accentColor,
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
private fun LanguageOptionButton(
  label: String,
  code: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (selected) CyanNeon.copy(alpha = 0.15f) else ObsidianBg)
      .border(
        1.dp,
        if (selected) CyanNeon else ObsidianBorder,
        RoundedCornerShape(12.dp)
      )
      .clickable { onClick() }
      .padding(vertical = 10.dp, horizontal = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      color = if (selected) CyanNeon else SilverMuted,
      fontSize = 11.sp,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
      fontFamily = FontFamily.Monospace
    )
  }
}
