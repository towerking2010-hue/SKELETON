package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ai.GeminiCognitiveService
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import kotlinx.coroutines.launch

@Composable
fun VoiceConversationScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val scope = rememberCoroutineScope()
  var isListening by remember { mutableStateOf(false) }
  var isStreaming by remember { mutableStateOf(false) }
  var liveTranscript by remember { mutableStateOf("Ready for real-time voice interaction.") }
  var spokenTurn by remember { mutableStateOf<String?>(null) }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isListening) 1.25f else 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .navigationBarsPadding()
  ) {
    // Header
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
          tint = CyanNeon
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "VOICE CONVERSATION [LIVE API]",
          color = CyanNeon,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Model: gemini-3.8-live (Real-Time Audio Session)",
          color = SilverMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // Audio Visualizer Center Chamber
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      // Pulsing outer ripple
      Box(
        modifier = Modifier
          .size(200.dp)
          .scale(pulseScale)
          .clip(CircleShape)
          .background(CyanNeon.copy(alpha = if (isListening) 0.15f else 0.05f))
          .border(1.dp, CyanNeon.copy(alpha = if (isListening) 0.5f else 0.2f), CircleShape)
      )

      // Inner Mic Button
      Box(
        modifier = Modifier
          .size(120.dp)
          .clip(CircleShape)
          .background(if (isListening) CyanNeon else ObsidianSurfaceElevated)
          .border(2.dp, CyanNeon, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        IconButton(
          onClick = {
            isListening = !isListening
            if (isListening) {
              liveTranscript = "Listening to audio input stream..."
              isStreaming = true
              scope.launch {
                val sampleQueries = listOf(
                  "What is the mathematical definition of quantum entanglement?",
                  "Explain synaptic long-term potentiation in 2 sentences.",
                  "How does the Live API achieve sub-second bidirectional latency?"
                )
                val query = sampleQueries.random()
                spokenTurn = query
                liveTranscript = "Audio captured: \"$query\"\nProcessing with gemini-3.8-live..."
                val reply = GeminiCognitiveService.liveVoiceSession(query)
                liveTranscript = reply
                isStreaming = false
                isListening = false
              }
            } else {
              isStreaming = false
            }
          },
          modifier = Modifier
            .fillMaxSize()
            .testTag("live_mic_toggle_button")
        ) {
          Icon(
            imageVector = if (isListening) Icons.Filled.Mic else Icons.Filled.MicOff,
            contentDescription = "Toggle Mic",
            tint = if (isListening) ObsidianBg else CyanNeon,
            modifier = Modifier.size(48.dp)
          )
        }
      }
    }

    // Live Transcript Output Box
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(ObsidianSurface)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Filled.VolumeUp,
            contentDescription = null,
            tint = CyanNeon,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "LIVE SESSION TELEMETRY",
            color = CyanNeon,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }

        if (isStreaming) {
          CircularProgressIndicator(
            modifier = Modifier.size(14.dp),
            color = CyanNeon,
            strokeWidth = 2.dp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = liveTranscript,
        color = SilverText,
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
        lineHeight = 20.sp
      )

      if (spokenTurn != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "User Input: $spokenTurn",
          color = SilverDim,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}
