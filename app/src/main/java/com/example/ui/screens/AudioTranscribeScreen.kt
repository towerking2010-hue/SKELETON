package com.example.ui.screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AudioTranscribeScreen(onBack: () -> Unit, onAddToFeed: (String) -> Unit = {}, modifier: Modifier = Modifier) {
  BackHandler { onBack() }
  val context = LocalContext.current
  var listening by remember { mutableStateOf(false) }
  var busy by remember { mutableStateOf(false) }
  var output by remember { mutableStateOf("") }
  var status by remember { mutableStateOf("Android speech recognition · English") }
  var recognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

  val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
    if (!granted) { status = "Microphone permission denied."; return@rememberLauncherForActivityResult }
    val engine = recognizer
    if (engine == null) { status = "No Android speech recognition service is available."; return@rememberLauncherForActivityResult }
    output = ""; listening = true; busy = true; status = "Listening…"
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
      putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
      putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
      putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
    }
    engine.startListening(intent)
  }

  DisposableEffect(context) {
    if (SpeechRecognizer.isRecognitionAvailable(context)) {
      val engine = SpeechRecognizer.createSpeechRecognizer(context)
      engine.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) { status = "Listening…" }
        override fun onBeginningOfSpeech() { status = "Speech detected…" }
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() { listening = false; status = "Processing…" }
        override fun onError(error: Int) {
          listening = false; busy = false
          status = when (error) {
            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Try again."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected. Try again."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is required."
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "The device speech service requires a network connection."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech service busy. Wait and retry."
            else -> "Speech recognition failed (error $error)."
          }
        }
        override fun onResults(results: Bundle?) {
          output = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull().orEmpty()
          status = if (output.isBlank()) "No words returned. Try again." else "Transcription complete · review before saving."
          listening = false; busy = false
        }
        override fun onPartialResults(partial: Bundle?) {
          output = partial?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: output
        }
        override fun onEvent(eventType: Int, params: Bundle?) {}
      })
      recognizer = engine
    } else status = "No Android speech recognition service installed."
    onDispose { recognizer?.cancel(); recognizer?.destroy(); recognizer = null }
  }

  Column(modifier.fillMaxSize().background(ObsidianBg).navigationBarsPadding()) {
    Row(Modifier.fillMaxWidth().height(60.dp).background(ObsidianSurface).border(.8.dp, ObsidianBorder).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = EmeraldMastery) }
      Spacer(Modifier.width(8.dp))
      Column {
        Text("AUDIO TRANSCRIBE", color = EmeraldMastery, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text("Android device speech recognition · English", color = SilverMuted, fontSize = 10.sp)
      }
    }
    Column(Modifier.weight(1f).fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
      Column(Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(ObsidianSurface).border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp)).padding(18.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Filled.TextFields, null, tint = EmeraldMastery, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("TRANSCRIPTION", color = EmeraldMastery, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold) }
          if (busy) CircularProgressIndicator(Modifier.size(14.dp), color = EmeraldMastery, strokeWidth = 2.dp)
        }
        Spacer(Modifier.height(14.dp))
        Text(output.ifBlank { "Recognized speech appears here. No sample text is inserted." }, color = if (output.isBlank()) SilverMuted else SilverText, fontSize = 14.sp, fontFamily = FontFamily.Monospace, lineHeight = 21.sp)
      }
      Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(ObsidianSurfaceElevated).border(1.dp, EmeraldMastery.copy(alpha = .4f), RoundedCornerShape(22.dp)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (listening) "MICROPHONE ACTIVE" else "MICROPHONE INPUT", color = if (listening) AmberAccent else SilverMuted, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        IconButton(onClick = { if (listening) { recognizer?.stopListening(); listening = false; status = "Finishing transcription…" } else permission.launch(android.Manifest.permission.RECORD_AUDIO) }, modifier = Modifier.size(72.dp).clip(CircleShape).background(if (listening) AmberAccent else EmeraldMastery).testTag("transcribe_mic_button")) {
          Icon(if (listening) Icons.Filled.Stop else Icons.Filled.Mic, "Start or stop microphone", tint = ObsidianBg, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(status, color = SilverDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        if (output.isNotBlank()) {
          Spacer(Modifier.height(12.dp))
          ElevatedButton(onClick = { onAddToFeed(output); status = "Transcript sent to Feed." }, colors = ButtonDefaults.elevatedButtonColors(containerColor = EmeraldMastery, contentColor = ObsidianBg), shape = RoundedCornerShape(12.dp)) { Text("SAVE TRANSCRIPT TO FEED", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
        }
      }
    }
  }
}
