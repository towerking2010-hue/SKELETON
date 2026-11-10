package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting
import com.example.ui.components.SkeletonBracketLogo
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
import java.util.UUID

data class ArScanTarget(
  val id: String,
  val category: String,
  val label: String,
  val confidence: Float,
  val domainNode: String,
  val itch: String,
  val skeleton: String,
  val intuition: String
)

@Composable
fun ArLensScreen(
  currentLanguage: LanguageSetting,
  onSaveToVault: (FeedCard) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scanTargets = remember {
    listOf(
      ArScanTarget(
        id = "target_ionic_column",
        category = "Classical Architecture",
        label = "Ionic Volute Entablature",
        confidence = 0.984f,
        domainNode = "Freemasonry & Architectural Allegory",
        itch = "Architectural scrolls are not decorative ornaments; they are load-distribution springs governed by logarithmic spiral physics.",
        skeleton = "Spiral r = a * e^(b * theta). Stress is redirected from the vertical pillar axis into torsional elasticity, preventing structural seismic fracture.",
        intuition = "Think of a curved car suspension leaf spring carved out of solid marble."
      ),
      ArScanTarget(
        id = "target_sacred_nautilus",
        category = "Biological Morphometry",
        label = "Nautilus Pompilius Spiral (ϕ)",
        confidence = 0.992f,
        domainNode = "Sacred Geometry & Universal Ratios",
        itch = "The shell grows exponentially without ever altering its center-of-mass or hydrodynamic drag profile.",
        skeleton = "Equiangular spiral: constant angle alpha between tangent and radial line. Ratio of successive chamber widths = phi = 1.6180339887.",
        intuition = "An expanding apartment building where each new room is added in such exact proportion that the building never tips over."
      ),
      ArScanTarget(
        id = "target_greek_inscription",
        category = "Ancient Epigraphy",
        label = "Delphic Inscription (Gnothi Seauton)",
        confidence = 0.967f,
        domainNode = "Etymology & Linguistic Software",
        itch = "To know thyself was an epistemic command to distinguish the immortal witness from temporary biological sensory input.",
        skeleton = "Gnothi (Aorist imperative: timeless execution) + Seauton (Reflexive pronoun). Greek isopsephic value = 1419. Root of cognitive metacognition.",
        intuition = "Recalibrating a dirty telescope before trying to log astronomical data."
      ),
      ArScanTarget(
        id = "target_electromagnetic_spectrum",
        category = "Spectrometry HUD",
        label = "Visible Photonic Wavelength",
        confidence = 0.978f,
        domainNode = "Colour Theory & Electromagnetic Wave Spectra",
        itch = "Photon emissions have zero subjective color; wavelengths are converted into emotional valence by neural cone opsin ratios.",
        skeleton = "lambda = 532nm (Monochromatic green). Planck-Einstein relation: E = h * nu = 2.33 eV. Retinal 11-cis-retinal photoisomerization -> Rhodopsin cascade.",
        intuition = "A barcode scanner reading a price tag; the price is not in the ink lines, but in the store's database lookup."
      )
    )
  }

  var selectedTarget by remember { mutableStateOf(scanTargets.first()) }
  var isFlashOn by remember { mutableStateOf(false) }
  var isSaved by remember { mutableStateOf(false) }

  // Scanning animation
  val infiniteTransition = rememberInfiniteTransition(label = "ar_scanner")
  val scanProgress by infiniteTransition.animateFloat(
    initialValue = 0.1f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scan_laser"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("ar_lens_screen")
  ) {
    // 1. Live Optical Viewfinder Simulation Canvas
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val reticleW = w * 0.78f
      val reticleH = h * 0.42f
      val reticleLeft = (w - reticleW) / 2f
      val reticleTop = h * 0.16f

      // Darkened viewfinder border
      drawRect(
        color = Color(0x66000000),
        size = size
      )

      // Optical Target Area Box
      drawRect(
        color = Color(0x1A000000),
        topLeft = Offset(reticleLeft, reticleTop),
        size = Size(reticleW, reticleH)
      )

      // Reticle Border Hairline
      drawRect(
        color = CyanNeon.copy(alpha = 0.35f),
        topLeft = Offset(reticleLeft, reticleTop),
        size = Size(reticleW, reticleH),
        style = Stroke(width = 1.2f.dp.toPx())
      )

      // Corner Precision Brackets
      val bracketLen = 22f.dp.toPx()
      val strokePx = 2.4f.dp.toPx()

      // Top-Left
      drawLine(CyanNeon, Offset(reticleLeft, reticleTop), Offset(reticleLeft + bracketLen, reticleTop), strokePx, StrokeCap.Square)
      drawLine(CyanNeon, Offset(reticleLeft, reticleTop), Offset(reticleLeft, reticleTop + bracketLen), strokePx, StrokeCap.Square)

      // Top-Right
      drawLine(CyanNeon, Offset(reticleLeft + reticleW - bracketLen, reticleTop), Offset(reticleLeft + reticleW, reticleTop), strokePx, StrokeCap.Square)
      drawLine(CyanNeon, Offset(reticleLeft + reticleW, reticleTop), Offset(reticleLeft + reticleW, reticleTop + bracketLen), strokePx, StrokeCap.Square)

      // Bottom-Left
      drawLine(CyanNeon, Offset(reticleLeft, reticleTop + reticleH - bracketLen), Offset(reticleLeft, reticleTop + reticleH), strokePx, StrokeCap.Square)
      drawLine(CyanNeon, Offset(reticleLeft, reticleTop + reticleH), Offset(reticleLeft + bracketLen, reticleTop + reticleH), strokePx, StrokeCap.Square)

      // Bottom-Right
      drawLine(CyanNeon, Offset(reticleLeft + reticleW - bracketLen, reticleTop + reticleH), Offset(reticleLeft + reticleW, reticleTop + reticleH), strokePx, StrokeCap.Square)
      drawLine(CyanNeon, Offset(reticleLeft + reticleW, reticleTop + reticleH - bracketLen), Offset(reticleLeft + reticleW, reticleTop + reticleH), strokePx, StrokeCap.Square)

      // Sweeping Laser Beam
      val laserY = reticleTop + reticleH * scanProgress
      drawLine(
        color = CyanNeon,
        start = Offset(reticleLeft + 6.dp.toPx(), laserY),
        end = Offset(reticleLeft + reticleW - 6.dp.toPx(), laserY),
        strokeWidth = 2f.dp.toPx(),
        cap = StrokeCap.Round
      )
    }

    // 2. HUD Controls & Telemetry Overlay
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      // Top Navigation & Mode Indicator
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier
            .size(42.dp)
            .testTag("ar_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back to Feed",
            tint = SilverText
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          SkeletonBracketLogo(size = 18.dp, accentColor = CyanNeon)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "AR LENS // RETICLE HUD",
            color = CyanNeon,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        IconButton(
          onClick = { isFlashOn = !isFlashOn },
          modifier = Modifier.size(42.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.FlashOn,
            contentDescription = "Torch",
            tint = if (isFlashOn) AmberAccent else SilverDim
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Real-time Optical Confidence Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xCC121216))
            .border(1.dp, CyanNeon.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(EmeraldMastery)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "TARGET LOCK: ${selectedTarget.label} [${(selectedTarget.confidence * 100).toInt()}%]",
              color = SilverText,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // Target Selector Tabs (Horizontal)
      Text(
        text = "ACTIVE OPTICAL TELEMETRY TARGETS",
        color = SilverMuted,
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(scanTargets) { target ->
          val isSelected = target.id == selectedTarget.id
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) CyanNeon.copy(alpha = 0.15f) else ObsidianSurface)
              .border(
                1.dp,
                if (isSelected) CyanNeon else ObsidianBorder,
                RoundedCornerShape(14.dp)
              )
              .clickable {
                selectedTarget = target
                isSaved = false
              }
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Column {
              Text(
                text = target.category,
                color = if (isSelected) CyanNeon else SilverMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = target.label,
                color = SilverText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 3. Extracted Ontological Analysis Card (Itch + Skeleton)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(28.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(28.dp))
          .padding(18.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "[NODE: ${selectedTarget.domainNode}]",
              color = CyanNeon,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "DASUN // VAULT SCAN",
              color = EmeraldMastery,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // ITCH
          Text(
            text = "THE ITCH",
            color = AmberAccent,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = selectedTarget.itch,
            color = SilverText,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          // SKELETON
          Text(
            text = "SKELETON",
            color = CyanNeon,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = selectedTarget.skeleton,
            color = SilverText,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Action Button: Save to Vault Feed
          ElevatedButton(
            onClick = {
              val newCard = FeedCard(
                id = "ar_" + UUID.randomUUID().toString().take(8),
                title = selectedTarget.label,
                itch = selectedTarget.itch,
                skeleton = selectedTarget.skeleton,
                intuition = selectedTarget.intuition,
                branchDomain = selectedTarget.domainNode,
                crossLinkTarget = "AR Optical Telemetry",
                isSaved = true
              )
              onSaveToVault(newCard)
              isSaved = true
            },
            enabled = !isSaved,
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("ar_save_vault_button"),
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = if (isSaved) EmeraldMastery else CyanNeon,
              contentColor = ObsidianBg
            ),
            shape = RoundedCornerShape(16.dp)
          ) {
            Icon(
              imageVector = if (isSaved) Icons.Filled.Refresh else Icons.Filled.Add,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isSaved) "SAVED TO DASUN'S VAULT" else "SAVE AS FEED CARD",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              letterSpacing = 0.5.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}
