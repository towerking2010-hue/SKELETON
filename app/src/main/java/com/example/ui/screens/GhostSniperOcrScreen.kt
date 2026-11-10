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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashOn
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
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
fun GhostSniperOcrScreen(
  currentLanguage: LanguageSetting,
  onExtract: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  var rawScanText by remember {
    mutableStateOf(
      "In distributed consensus, the Raft protocol divides time into terms of arbitrary length. Terms are numbered with consecutive integers. Each term begins with an election, in which one or more candidates attempt to become leader. If a candidate wins the election, then it serves as leader for the rest of the term. In some situations an election will result in a split vote, in which case the term ends with no leader; a new term (and a new election) will begin shortly. Raft ensures that there is at most one leader in a given term."
    )
  }
  var successFeedback by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .padding(top = 8.dp)
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
          tint = EmeraldMastery
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "MODE [GHOST_SNIPER_OCR]",
          color = EmeraldMastery,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Extract Itch + Skeleton from Textbook Captures",
          color = SilverMuted,
          fontSize = 10.sp
        )
      }
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Scanned Document Reticle Frame
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurface)
          .border(1.2.dp, EmeraldMastery.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = null, tint = EmeraldMastery, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "OPTICAL BUFFER CAPTURE",
              color = EmeraldMastery,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "486 CHARS",
            color = SilverDim,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        BasicTextField(
          value = rawScanText,
          onValueChange = { rawScanText = it },
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurfaceElevated)
            .border(0.8.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("ocr_text_input"),
          textStyle = TextStyle(
            color = SilverText,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 17.sp
          ),
          cursorBrush = SolidColor(EmeraldMastery)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Preset Samples
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Presets:",
            color = SilverDim,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.CenterVertically)
          )

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(ObsidianSurfaceElevated)
              .clickable {
                rawScanText = "ක්වොන්ටම් යාන්ත්‍ර විද්‍යාවේ ශ්‍රෝඩිංගර් සමීකරණය මඟින් අංශුවක තරංග ශ්‍රිතය (Wave function) කාලය සමඟ විකාශනය වන ආකාරය පැහැදිලි කරයි. තරංග ශ්‍රිතයේ වර්ගඵලය මඟින් අංශුවක් අවකාශයේ හමුවීමේ සම්භාවිතා ඝනත්වය ලබා දෙයි."
              }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(text = "සිංහල Doc (Quantum)", color = CyanNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(ObsidianSurfaceElevated)
              .clickable {
                rawScanText = "Thermodynamic laws dictate that in any isolated system, entropy must non-decrease (dS >= 0). The second law defines the arrow of time and prohibits perpetual motion machines of the second kind."
              }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(text = "Entropy (EN)", color = SilverMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
          onClick = {
            onExtract(rawScanText)
            successFeedback = true
          },
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = EmeraldMastery,
            contentColor = ObsidianBg
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("execute_ocr_sniper_button")
        ) {
          Icon(imageVector = Icons.Filled.FlashOn, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "SNIPE ITCH + SKELETON INTO FEED",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        if (successFeedback) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "✓ Extracted into minimalist micro-learning card! Synced to Feed.",
            color = EmeraldMastery,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}
