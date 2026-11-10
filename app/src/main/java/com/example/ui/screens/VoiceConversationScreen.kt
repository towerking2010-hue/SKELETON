package com.example.ui.screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.SkeletonCognitiveEngine
import com.example.engine.ai.AiRole
import com.example.engine.ai.GeminiCognitiveService
import com.example.engine.ai.ModelTier
import com.example.data.model.LanguageSetting
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun VoiceConversationScreen(currentLanguage: LanguageSetting, onBack: () -> Unit, modifier: Modifier = Modifier) {
  BackHandler { onBack() }
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  var listening by remember { mutableStateOf(false) }
  var processing by remember { mutableStateOf(false) }
  var transcript by remember { mutableStateOf("Tap the microphone and speak. Your actual words will be sent to CORE.") }
  var reply by remember { mutableStateOf("") }
  var status by remember { mutableStateOf("Device speech recognition ready · English") }
  var recognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
  var speech by remember { mutableStateOf<TextToSpeech?>(null) }
  var ttsReady by remember { mutableStateOf(false) }

  val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
    if (!granted) { status = "Microphone permission denied."; return@rememberLauncherForActivityResult }
    val engine = recognizer
    if (engine == null) { status = "No Android speech recognition service is installed."; return@rememberLauncherForActivityResult }
    transcript = ""; reply = ""; listening = true; processing = false; status = "Listening…"
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
      putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (currentLanguage == LanguageSetting.SINHALA) "si-LK" else "en-US")
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
        override fun onEndOfSpeech() { listening = false; processing = true; status = "Thinking with CORE…" }
        override fun onError(error: Int) {
          listening = false; processing = false
          status = when (error) {
            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Try again."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected."
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Device speech service needs a network connection."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required."
            else -> "Speech recognition failed (error $error)."
          }
        }
        override fun onResults(results: Bundle?) {
          val words = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull().orEmpty()
          transcript = words
          if (words.isBlank()) { processing = false; status = "No words recognized."; return }
          processing = true; status = "CORE is thinking…"
          scope.launch {
            val languageInstruction = if (currentLanguage == LanguageSetting.SINHALA) {
              "Respond in natural Sinhala script, keeping technical terms in English where useful.\n"
            } else ""
            val cloudAnswer = GeminiCognitiveService.chatGenerate(
              userPrompt = languageInstruction + words,
              role = AiRole.SKELETON_CORE,
              tier = ModelTier.FAST
            )
            val usedOfflineFallback = cloudAnswer.startsWith("Inference error") ||
              cloudAnswer.startsWith("Synthesis complete without output")
            val answer = if (usedOfflineFallback) {
              SkeletonCognitiveEngine.generateCoreResponse(words, currentLanguage, emptyList()).first
            } else cloudAnswer
            reply = answer
            processing = false
            status = if (usedOfflineFallback) "Offline CORE fallback · no cloud response" else "CORE replied · device voice playback"
            if (ttsReady) speech?.speak(answer, TextToSpeech.QUEUE_FLUSH, Bundle(), "skeleton_voice_reply")
          }
        }
        override fun onPartialResults(partial: Bundle?) {
          transcript = partial?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: transcript
        }
        override fun onEvent(eventType: Int, params: Bundle?) {}
      })
      recognizer = engine
    } else status = "No Android speech recognition service is installed."

    val tts = TextToSpeech(context) { result ->
      ttsReady = result == TextToSpeech.SUCCESS
      if (ttsReady) {
        val locale = if (currentLanguage == LanguageSetting.SINHALA) Locale("si", "LK") else Locale.US
        val available = speech?.isLanguageAvailable(locale) ?: TextToSpeech.LANG_NOT_SUPPORTED
        if (available >= TextToSpeech.LANG_AVAILABLE) {
          speech?.language = locale
        } else {
          ttsReady = false
          status = if (currentLanguage == LanguageSetting.SINHALA) "Sinhala TTS voice is not installed on this device." else "English TTS voice unavailable."
        }
      }
    }
    speech = tts
    onDispose {
      recognizer?.cancel(); recognizer?.destroy(); recognizer = null
      speech?.stop(); speech?.shutdown(); speech = null
      ttsReady = false
    }
  }

  val transition = rememberInfiniteTransition(label = "voice_pulse")
  val pulse by transition.animateFloat(1f, if (listening) 1.22f else 1.04f,
    infiniteRepeatable(tween(850, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pulse")

  Column(modifier.fillMaxSize().background(ObsidianBg).navigationBarsPadding()) {
    Row(Modifier.fillMaxWidth().height(60.dp).background(ObsidianSurface).border(.8.dp, ObsidianBorder).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = { recognizer?.cancel(); speech?.stop(); onBack() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = CyanNeon)
      }
      Spacer(Modifier.width(8.dp))
      Column {
        Text("VOICE CONVERSATION", color = CyanNeon, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text("Android speech recognition + CORE + device TTS", color = SilverMuted, fontSize = 10.sp)
      }
    }

    Column(Modifier.weight(1f).fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.size(190.dp).scale(pulse).clip(CircleShape).background(CyanNeon.copy(alpha = if (listening) .16f else .05f)).border(1.dp, CyanNeon.copy(alpha = .4f), CircleShape))
        Box(Modifier.size(116.dp).clip(CircleShape).background(if (listening) CyanNeon else ObsidianSurfaceElevated).border(2.dp, CyanNeon, CircleShape), contentAlignment = Alignment.Center) {
          IconButton(onClick = {
            if (listening) { recognizer?.stopListening(); listening = false; processing = true; status = "Finishing speech…" }
            else permission.launch(android.Manifest.permission.RECORD_AUDIO)
          }, modifier = Modifier.fillMaxSize().testTag("live_mic_toggle_button")) {
            Icon(if (listening) Icons.Filled.Mic else Icons.Filled.MicOff, "Start or stop voice input", tint = if (listening) ObsidianBg else CyanNeon, modifier = Modifier.size(48.dp))
          }
        }
      }
      Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurface).border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp)).padding(14.dp)) {
        Text("YOU", color = AmberAccent, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        Text(transcript, color = SilverText, fontSize = 13.sp, lineHeight = 18.sp)
      }
      Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurfaceElevated).border(1.dp, CyanNeon.copy(alpha = .3f), RoundedCornerShape(18.dp)).padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("CORE", color = CyanNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          Spacer(Modifier.weight(1f))
          if (processing) androidx.compose.material3.CircularProgressIndicator(Modifier.size(14.dp), color = CyanNeon, strokeWidth = 2.dp)
          if (reply.isNotBlank()) IconButton(onClick = { if (ttsReady) speech?.speak(reply, TextToSpeech.QUEUE_FLUSH, Bundle(), "skeleton_voice_replay") }) {
            Icon(Icons.Filled.VolumeUp, "Speak reply", tint = CyanNeon)
          }
        }
        Text(reply.ifBlank { "CORE's spoken reply will appear here." }, color = SilverText, fontSize = 13.sp, lineHeight = 18.sp)
      }
      Text(status, color = SilverDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    }
  }
}
