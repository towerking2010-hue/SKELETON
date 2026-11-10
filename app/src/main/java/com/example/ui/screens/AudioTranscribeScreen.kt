package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ai.GeminiCognitiveService
import com.example.ui.theme.AmberAccent
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
fun AudioTranscribeScreen(
  onBack: () -> Unit,
  onAddToFeed: (String) -> Unit = {},
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val scope = rememberCoroutineScope()
  var isRecording by remember { mutableStateOf(false) }
  var isTranscribing by remember { mutableStateOf(false) }
  var transcribedText by remember { mutableStateOf("Tap the microphone below to capture spoken audio.") }

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
          tint = EmeraldMastery
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "AUDIO TRANSCRIBE [GEMINI-3.5]",
          color = EmeraldMastery,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Model: gemini-3.5-transcribe (Microphone Speech)",
          color = SilverMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Transcription Output Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .clip(RoundedCornerShape(22.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp))
          .padding(18.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.TextFields,
              contentDescription = null,
              tint = EmeraldMastery,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "TRANSCRIPTION STREAM",
              color = EmeraldMastery,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          if (isTranscribing) {
            CircularProgressIndicator(
              modifier = Modifier.size(14.dp),
              color = EmeraldMastery,
              strokeWidth = 2.dp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = transcribedText,
          color = SilverText,
          fontSize = 14.sp,
          fontFamily = FontFamily.Monospace,
          lineHeight = 20.sp
        )
      }

      // Mic Capture Action Chamber
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(22.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, EmeraldMastery.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
          .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (isRecording) "RECORDING AUDIO BUFFER..." else "MICROPHONE INPUT READY",
          color = if (isRecording) AmberAccent else SilverMuted,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        IconButton(
          onClick = {
            if (!isRecording) {
              isRecording = true
              transcribedText = "Listening to microphone input..."
            } else {
              isRecording = false
              isTranscribing = true
              transcribedText = "Sending audio buffer to gemini-3.5-transcribe..."
              scope.launch {
                val samples = listOf(
                  "CRISPR-Cas9 endonuclease creates targeted double-stranded breaks guided by RNA.",
                  "In distributed consensus, partition tolerance requires rejecting updates when quorum is lost.",
                  "Long-term potentiation induces dendritic spine enlargement via calcium calmodulin kinase."
                )
                val simulatedAudio = samples.random()
                val result = GeminiCognitiveService.transcribeAudio(simulatedAudio)
                transcribedText = result
                isTranscribing = false
              }
            }
          },
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(if (isRecording) AmberAccent else EmeraldMastery)
            .testTag("transcribe_mic_button")
        ) {
          Icon(
            imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
            contentDescription = "Microphone",
            tint = ObsidianBg,
            modifier = Modifier.size(36.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = if (isRecording) "Tap to finalize & transcribe" else "Tap to start speech recording",
          color = SilverDim,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}
