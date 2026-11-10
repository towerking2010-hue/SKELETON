package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.PurpleAether
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import android.os.Bundle
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.sin

enum class BinauralMode(val title: String, val freq: String, val targetState: String) {
  THETA("Theta Waves", "6.0 Hz", "Memory Consolidation & Synaptic Rewiring"),
  DELTA("Delta Rest", "2.5 Hz", "Slow-Wave Deep Sleep & Cellular Repair"),
  OBSIDIAN_DRONE("Obsidian Drone", "110 Hz", "Cranial Resonance & Neural Calming")
}

@Composable
fun DreamStateEngineScreen(
  weakCards: List<FeedCard>,
  currentLanguage: LanguageSetting,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var tts by remember { mutableStateOf<TextToSpeech?>(null) }
  var ttsReady by remember { mutableStateOf(false) }
  var voiceStatus by remember { mutableStateOf("Preparing narration…") }

  DisposableEffect(currentLanguage) {
    val engine = TextToSpeech(context) { result ->
      if (result == TextToSpeech.SUCCESS) {
        val locale = if (currentLanguage == LanguageSetting.SINHALA) Locale("si", "LK") else Locale.US
        val availability = tts?.isLanguageAvailable(locale) ?: TextToSpeech.LANG_NOT_SUPPORTED
        if (availability >= TextToSpeech.LANG_AVAILABLE) {
          tts?.language = locale
          ttsReady = true
          voiceStatus = if (currentLanguage == LanguageSetting.SINHALA) "Sinhala narration ready" else "English narration ready"
        } else {
          ttsReady = false
          voiceStatus = if (currentLanguage == LanguageSetting.SINHALA) "Sinhala voice unavailable on this device." else "English voice unavailable."
        }
      } else {
        ttsReady = false
        voiceStatus = "Device speech engine unavailable."
      }
    }
    tts = engine
    onDispose {
      engine.stop()
      engine.shutdown()
      tts = null
      ttsReady = false
    }
  }

  // Select top 5 hardest concepts
  val consolidationNodes = remember(weakCards) {
    if (weakCards.isNotEmpty()) {
      weakCards.take(5)
    } else {
      emptyList()
    }
  }

  var activeMode by remember { mutableStateOf(BinauralMode.THETA) }
  var isPlaying by remember { mutableStateOf(false) }
  var activeNodeIndex by remember { mutableIntStateOf(0) }
  var remainingSeconds by remember { mutableIntStateOf(600) } // 10 minutes default

  // Animated wave visualization
  val transition = rememberInfiniteTransition(label = "binaural_wave")
  val wavePhase by transition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "phase"
  )

  // Timer countdown & whisper progression
  LaunchedEffect(isPlaying, remainingSeconds) {
    if (isPlaying && remainingSeconds > 0) {
      delay(1000L)
      remainingSeconds--
      if (remainingSeconds % 45 == 0 && consolidationNodes.isNotEmpty()) {
        activeNodeIndex = (activeNodeIndex + 1) % consolidationNodes.size
      }
      if (remainingSeconds == 0) isPlaying = false
    }
  }

  val activeCard = consolidationNodes.getOrNull(activeNodeIndex)

  LaunchedEffect(isPlaying, activeCard?.id) {
    if (isPlaying && activeCard != null && ttsReady) {
      val narration = "${activeCard.title}. ${activeCard.itch}. ${activeCard.skeleton}. ${activeCard.intuition}"
      tts?.speak(narration, TextToSpeech.QUEUE_FLUSH, Bundle(), "dream_${activeCard.id}")
    } else {
      tts?.stop()
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("dream_state_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      // Top Navigation
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.size(42.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SilverText
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          SkeletonBracketLogo(size = 18.dp, accentColor = PurpleAether)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "DREAM STATE // CONSOLIDATION",
            color = PurpleAether,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        Icon(
          imageVector = Icons.Filled.Bedtime,
          contentDescription = "Sleep Consolidation",
          tint = if (isPlaying) PurpleAether else SilverDim,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Header Brief
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "TARGET: DASUN'S HARDEST 5 CONCEPTS",
            color = PurpleAether,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${consolidationNodes.size} NODES QUEUED",
            color = SilverMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "10-minute spoken recall briefing. Frequency visuals are optional; binaural audio is not played in this build.",
          color = SilverText,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. Binaural Waveform Visualizer
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .clip(RoundedCornerShape(24.dp))
          .background(Color(0xFF07070A))
          .border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height
          val midY = h / 2f

          val path = Path()
          val path2 = Path()
          val numPoints = 80

          for (i in 0..numPoints) {
            val x = (i / numPoints.toFloat()) * w
            val normalizedX = (i / numPoints.toFloat()) * 4f * Math.PI.toFloat()
            val amp = if (isPlaying) 28f.dp.toPx() else 8f.dp.toPx()

            val y1 = midY + sin(normalizedX + wavePhase) * amp
            val y2 = midY + sin(normalizedX * 1.5f - wavePhase) * (amp * 0.7f)

            if (i == 0) {
              path.moveTo(x, y1)
              path2.moveTo(x, y2)
            } else {
              path.lineTo(x, y1)
              path2.lineTo(x, y2)
            }
          }

          // Primary Theta Wave
          drawPath(
            path = path,
            color = PurpleAether,
            style = Stroke(width = 2.2f.dp.toPx(), cap = StrokeCap.Round)
          )

          // Secondary Resonant Frequency
          drawPath(
            path = path2,
            color = CyanNeon.copy(alpha = 0.5f),
            style = Stroke(width = 1.4f.dp.toPx(), cap = StrokeCap.Round)
          )
        }

        // Center Frequency Readout
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xCC000000))
            .border(0.8.dp, PurpleAether.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.GraphicEq,
              contentDescription = null,
              tint = PurpleAether,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${activeMode.title}: ${activeMode.freq}",
              color = SilverText,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 3. Active Memory Whisper Box
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .clip(RoundedCornerShape(26.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(26.dp))
          .padding(20.dp)
      ) {
        if (activeCard != null) {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "[NODE ${activeNodeIndex + 1}/${consolidationNodes.size}]: ${activeCard.branchDomain}",
                  color = PurpleAether,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "REVIEW BRIEFING",
                  color = EmeraldMastery,
                  fontSize = 9.sp,
                  fontFamily = FontFamily.Monospace
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = activeCard.title,
                color = SilverText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "KEY QUESTION",
                color = AmberAccent,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = activeCard.itch,
                color = SilverText,
                fontSize = 13.sp,
                lineHeight = 18.sp
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "CORE MECHANISM",
                color = CyanNeon,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = activeCard.skeleton,
                color = SilverMuted,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 16.sp
              )
            }

            // Intuition Whisper
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "🌙", fontSize = 14.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = activeCard.intuition,
                color = PurpleAether,
                fontSize = 11.sp,
                lineHeight = 15.sp
              )
            }
          }
        } else {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No review cards are queued yet. Add and review concepts to populate Dream State.",
              color = SilverMuted,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(voiceStatus, color = SilverMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
      // 4. Timer, Mode Selector & Play Control
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Binaural Waveform Selector
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          BinauralMode.values().forEach { mode ->
            val isSelected = mode == activeMode
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) PurpleAether.copy(alpha = 0.2f) else ObsidianSurface)
                .border(
                  0.8.dp,
                  if (isSelected) PurpleAether else ObsidianBorder,
                  RoundedCornerShape(10.dp)
                )
                .clickable { activeMode = mode }
                .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
              Text(
                text = mode.freq,
                color = if (isSelected) PurpleAether else SilverMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Remaining Timer
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Filled.Timer,
            contentDescription = null,
            tint = SilverDim,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          val minutes = remainingSeconds / 60
          val seconds = remainingSeconds % 60
          Text(
            text = String.format("%02d:%02d", minutes, seconds),
            color = SilverText,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }

        // Play / Pause Circle Button
        Box(
          modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(PurpleAether)
            .clickable {
              if (!isPlaying && remainingSeconds == 0) remainingSeconds = 600
              isPlaying = !isPlaying
            }
            .testTag("dream_state_play_pause"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Start Consolidation",
            tint = ObsidianBg,
            modifier = Modifier.size(26.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}
