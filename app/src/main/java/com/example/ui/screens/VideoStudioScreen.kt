package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ai.GeminiCognitiveService
import com.example.engine.ai.VideoAspectRatio
import com.example.ui.components.SkeletonBracketLogo
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianBorderLight
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.PurpleAether
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

enum class VideoStudioTab(val title: String) {
  TEXT_TO_VIDEO("Text to Video"),
  ANIMATE_PHOTO("Animate Photo")
}

@Composable
fun VideoStudioScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val scope = rememberCoroutineScope()
  var currentTab by remember { mutableStateOf(VideoStudioTab.TEXT_TO_VIDEO) }
  var aspectRatio by remember { mutableStateOf(VideoAspectRatio.LANDSCAPE_16_9) }
  var promptText by remember {
    mutableStateOf("Quantum wavefunction collapse in 3D: High-dimensional probability density cloud decaying into a single discrete eigenvalue point in spacetime.")
  }
  var isRendering by remember { mutableStateOf(false) }
  var renderingResult by remember { mutableStateOf<String?>(null) }
  var isPlayingVideo by remember { mutableStateOf(true) }
  var timelineProgress by remember { mutableFloatStateOf(0.45f) }

  // Sample photo selection for photo animation
  var uploadedPhotoLabel by remember { mutableStateOf("Sunflower Fibonacci Lattice.png") }

  val promptPresets = remember {
    listOf(
      "Quantum wavefunction collapse in 3D",
      "DNA double-helix unzipping & helicase transcription",
      "Sacred Geometry: Rotating 3D Dodecahedron into Icosahedron",
      "Electromagnetic field wave propagation through vacuum",
      "Nautilus shell expanding along Golden Ratio spiral (ϕ)"
    )
  }

  // Animated visual wave/motion simulation for the video canvas
  val transition = rememberInfiniteTransition(label = "video_motion")
  val motionPhase by transition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "motion_phase"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .padding(top = 8.dp)
      .navigationBarsPadding()
      .testTag("video_studio_screen")
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
      IconButton(
        onClick = onBack,
        modifier = Modifier.testTag("video_studio_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = CyanNeon
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        SkeletonBracketLogo(size = 18.dp, accentColor = PurpleAether)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "VIDEO STUDIO // GEMINI-OMNI-1.1-FLASH",
            color = PurpleAether,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
          Text(
            text = "Text-to-Video & Photo Animation via Interactions API",
            color = SilverMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    val scrollState = rememberScrollState()
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .verticalScroll(scrollState)
        .padding(16.dp)
    ) {
      // Sub-Tabs: Text to Video vs Animate Photo
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        VideoStudioTab.values().forEach { tab ->
          val isSelected = tab == currentTab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) PurpleAether.copy(alpha = 0.2f) else ObsidianSurfaceElevated)
              .border(1.dp, if (isSelected) PurpleAether else ObsidianBorder, RoundedCornerShape(14.dp))
              .clickable { currentTab = tab }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (tab == VideoStudioTab.TEXT_TO_VIDEO) Icons.Filled.Videocam else Icons.Filled.Movie,
                contentDescription = null,
                tint = if (isSelected) PurpleAether else SilverMuted,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = tab.title,
                color = if (isSelected) PurpleAether else SilverText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Aspect Ratio Selector: 16:9 (landscape) vs 9:16 (portrait)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(ObsidianSurface)
          .border(0.8.dp, ObsidianBorder, RoundedCornerShape(14.dp))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ASPECT RATIO:",
          color = SilverMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(start = 6.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          VideoAspectRatio.values().forEach { ratio ->
            val isSelected = ratio == aspectRatio
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) CyanNeon.copy(alpha = 0.15f) else ObsidianSurfaceElevated)
                .border(1.dp, if (isSelected) CyanNeon else ObsidianBorder, RoundedCornerShape(10.dp))
                .clickable { aspectRatio = ratio }
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("aspect_ratio_${ratio.ratioString.replace(':', '_')}")
            ) {
              Text(
                text = ratio.label,
                color = if (isSelected) CyanNeon else SilverMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. Video Player / Simulation Canvas Preview
      val canvasAspectRatio = if (aspectRatio == VideoAspectRatio.LANDSCAPE_16_9) 16f / 9f else 9f / 16f
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(canvasAspectRatio)
          .clip(RoundedCornerShape(24.dp))
          .background(Color(0xFF040407))
          .border(1.dp, PurpleAether.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height
          val cx = w / 2f
          val cy = h / 2f

          // Animated particle/geometric orbital wireframe
          val numRings = 4
          for (r in 1..numRings) {
            val radius = (h * 0.12f) * r
            val alpha = (0.25f / r).coerceIn(0.08f, 0.4f)
            drawCircle(
              color = PurpleAether.copy(alpha = alpha),
              radius = radius,
              center = Offset(cx, cy),
              style = Stroke(width = 1.2f.dp.toPx())
            )
          }

          // Rotating 3D Lissajous orbit
          val orbitPath = Path()
          for (deg in 0..360 step 6) {
            val rad = Math.toRadians(deg.toDouble()).toFloat()
            val ox = cx + cos(rad + motionPhase) * (w * 0.35f)
            val oy = cy + sin(rad * 2f + motionPhase) * (h * 0.28f)
            if (deg == 0) orbitPath.moveTo(ox, oy) else orbitPath.lineTo(ox, oy)
          }
          drawPath(
            path = orbitPath,
            color = CyanNeon.copy(alpha = 0.7f),
            style = Stroke(width = 1.8f.dp.toPx(), cap = StrokeCap.Round)
          )

          // Center Geometric Core
          drawCircle(
            color = PurpleAether,
            radius = 6f.dp.toPx(),
            center = Offset(cx, cy)
          )
        }

        // Overlay HUD Metrics
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xCC000000))
                .border(0.8.dp, PurpleAether, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "MODEL: gemini-omni-1.1-flash",
                color = PurpleAether,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xCC000000))
                .border(0.8.dp, CyanNeon, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "${aspectRatio.ratioString} // 60 FPS",
                color = CyanNeon,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          // Playback / Timeline Control Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(Color(0xCC121216))
              .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
              .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = { isPlayingVideo = !isPlayingVideo },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = if (isPlayingVideo) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = CyanNeon,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Slider(
              value = timelineProgress,
              onValueChange = { timelineProgress = it },
              colors = SliderDefaults.colors(
                thumbColor = PurpleAether,
                activeTrackColor = PurpleAether,
                inactiveTrackColor = ObsidianBorderLight
              ),
              modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
              text = "${(timelineProgress * 6.0).toInt()}s / 6s",
              color = SilverMuted,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 3. Mode Input Controls
      if (currentTab == VideoStudioTab.ANIMATE_PHOTO) {
        // Photo Attachment Box
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ObsidianSurfaceElevated)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Filled.AddPhotoAlternate,
                contentDescription = null,
                tint = AmberAccent,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "SOURCE PHOTO FOR ANIMATION",
                color = AmberAccent,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldMastery.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(text = "ATTACHED", color = EmeraldMastery, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = uploadedPhotoLabel,
            color = SilverText,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
      }

      // Prompt Preset Chips
      Text(
        text = "PROMPT MOTION PRESETS",
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
        items(promptPresets) { preset ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(ObsidianSurfaceElevated)
              .border(0.8.dp, ObsidianBorder, RoundedCornerShape(12.dp))
              .clickable { promptText = preset }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = preset,
              color = SilverMuted,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Prompt Text Input
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
          .padding(14.dp)
      ) {
        Text(
          text = if (currentTab == VideoStudioTab.TEXT_TO_VIDEO) "CINEMATOGRAPHIC PROMPT" else "MOTION & ANIMATION DYNAMICS",
          color = CyanNeon,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
          value = promptText,
          onValueChange = { promptText = it },
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .testTag("video_prompt_input"),
          textStyle = TextStyle(
            color = SilverText,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 18.sp
          ),
          cursorBrush = SolidColor(PurpleAether)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Generation Button
      ElevatedButton(
        onClick = {
          scope.launch {
            isRendering = true
            renderingResult = if (currentTab == VideoStudioTab.TEXT_TO_VIDEO) {
              GeminiCognitiveService.generateVideo(promptText, aspectRatio)
            } else {
              val sampleBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
              GeminiCognitiveService.animateImageToVideo(sampleBitmap, promptText, aspectRatio)
            }
            isRendering = false
          }
        },
        enabled = !isRendering && promptText.isNotBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("render_video_button"),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = PurpleAether,
          contentColor = ObsidianBg
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        if (isRendering) {
          CircularProgressIndicator(color = ObsidianBg, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
          Spacer(modifier = Modifier.width(8.dp))
          Text("GENERATING VIDEO MANIFEST...", fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        } else {
          Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (currentTab == VideoStudioTab.TEXT_TO_VIDEO) "GENERATE ${aspectRatio.ratioString} VIDEO" else "ANIMATE PHOTO TO ${aspectRatio.ratioString} VIDEO",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      // Result Feedback Box
      renderingResult?.let { res ->
        Spacer(modifier = Modifier.height(12.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0F0B18))
            .border(1.dp, PurpleAether.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(14.dp)
        ) {
          Column {
            Text(
              text = "GEMINI-OMNI-1.1-FLASH OUTPUT [${aspectRatio.ratioString}]",
              color = PurpleAether,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = res,
              color = SilverText,
              fontSize = 12.sp,
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
