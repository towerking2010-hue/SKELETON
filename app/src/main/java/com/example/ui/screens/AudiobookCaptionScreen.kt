package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
import com.example.engine.ocr.SinhalaScannedPdfOcr
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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.util.zip.ZipInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private data class AudiobookDocument(
  val title: String,
  val author: String,
  val chapter: String,
  val text: String
)

private fun splitIntoSentences(text: String): List<String> =
  Regex("(?<=[.!?।])\\s+|\\n+")
    .split(text.replace("\r", " "))
    .map { it.trim() }
    .filter { it.isNotBlank() }

private fun readImportedBook(context: android.content.Context, uri: Uri, currentLanguage: LanguageSetting, onProgress: (String) -> Unit = {}): AudiobookDocument {
  val name = context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
    ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
    ?: "Imported book"
  val mime = context.contentResolver.getType(uri).orEmpty()
  val raw = context.contentResolver.openInputStream(uri)?.use { input ->
    if (mime.contains("pdf") || name.endsWith(".pdf", true)) {
      PDFBoxResourceLoader.init(context)
      PDDocument.load(input).use { pdf ->
        val selectableText = PDFTextStripper().getText(pdf).trim()
        if (selectableText.isNotBlank()) {
          selectableText
        } else if (currentLanguage == LanguageSetting.SINHALA) {
          onProgress("සිංහල OCR මඟින් ස්කෑන් කළ පිටු කියවමින්…")
          SinhalaScannedPdfOcr.extract(
            context = context,
            pdf = pdf,
            onModelDownload = { onProgress("සිංහල OCR ආකෘතිය බාගත කරමින් (1.7 MB)…") },
            onPageProgress = { completed, total ->
              onProgress("ස්කෑන් කළ පිටු කියවමින්: $completed / $total")
            }
          )
        } else {
          ""
        }
      }
    } else if (mime.contains("epub") || name.endsWith(".epub", true)) {
      ZipInputStream(input).use { zip ->
        val chapters = StringBuilder()
        var entry = zip.nextEntry
        while (entry != null) {
          if (!entry.isDirectory && (entry.name.endsWith(".xhtml", true) ||
                entry.name.endsWith(".html", true) || entry.name.endsWith(".htm", true))) {
            val html = BufferedReader(InputStreamReader(zip, Charsets.UTF_8)).readText()
            val visible = html.replace(Regex("(?is)<(script|style)[^>]*>.*?</\\1>"), " ")
              .replace(Regex("(?i)<br\\s*/?>|</p>|</div>|</h[1-6]>"), "\n")
              .replace(Regex("<[^>]+>"), " ")
              .replace("&nbsp;", " ").replace("&amp;", "&")
              .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
            chapters.append(visible).append("\n\n")
          }
          zip.closeEntry()
          entry = zip.nextEntry
        }
        chapters.toString()
      }
    } else {
      BufferedReader(InputStreamReader(input, Charsets.UTF_8)).readText()
    }
  }.orEmpty().replace(Regex("[ \\t]+"), " ").trim()

  require(raw.isNotBlank()) {
    if (currentLanguage == LanguageSetting.SINHALA) {
      "මෙම ගොනුවෙන් කියවිය හැකි සිංහල අකුරු හමු නොවීය. PDF පිටු පැහැදිලිද සහ අකුරු මුද්‍රිතදැයි පරීක්ෂා කරන්න."
    } else {
      "No selectable text was found. This may be a scanned PDF; image-based OCR is required before it can be read."
    }
  }
  return AudiobookDocument(name.substringBeforeLast('.', name), "Imported from device", "Imported text", raw)
}

@Composable
fun AudiobookCaptionScreen(
  currentLanguage: LanguageSetting,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val si = currentLanguage == LanguageSetting.SINHALA
  val samples = remember {
    listOf(
      AudiobookDocument(
        "The Kybalion", "Three Initiates", "Chapter II: The Seven Hermetic Principles",
        "The principles of truth are seven. The first principle is Mentalism: The All is Mind; the Universe is Mental. The second is Correspondence: As above, so below; as below, so above. The third is Vibration: Nothing rests; everything moves; everything vibrates."
      ),
      AudiobookDocument(
        "මහාවංශය", "මහානාම හිමි", "ධාතුසේන රජු සහ කලා වැව",
        "ධාතුසේන රජතුමා මහා සයුරක් බඳු කලා වැව කරවා ජල කළමනාකරණ විස්මයක් ලොවට දායාද කළේය. කළාවැවේ ජලයම තමාගේ සදාකාලික ධන නිධානය බව එතුමා ප්‍රකාශ කළේය."
      ),
      AudiobookDocument(
        "The Trivium", "Sister Miriam Joseph", "Chapter I: Nature of the Liberal Arts",
        "The liberal arts are the arts of the mind. Grammar is the art of inventing and combining symbols. Logic is the art of thinking. Rhetoric is the art of communicating thought from one mind to another."
      )
    )
  }
  var document by remember(currentLanguage) { mutableStateOf(if (si) samples[1] else samples.first()) }
  var sentences by remember(document) { mutableStateOf(splitIntoSentences(document.text)) }
  var currentSentence by remember { mutableIntStateOf(0) }
  var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
  var isPlaying by remember { mutableStateOf(false) }
  var ttsReady by remember { mutableStateOf(false) }
  var selectedVoiceAvailable by remember { mutableStateOf(false) }
  val latestSentences by rememberUpdatedState(sentences)
  var voiceStatus by remember(currentLanguage) { mutableStateOf(if (si) "දුරකථනයේ හඬ සේවාව සූදානම් කරමින්…" else "Initializing device speech engine…") }
  var tts by remember { mutableStateOf<TextToSpeech?>(null) }
  var importError by remember { mutableStateOf<String?>(null) }
  var importStatus by remember { mutableStateOf("") }
  val mainHandler = remember { Handler(Looper.getMainLooper()) }

  val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
    if (uri != null) {
      scope.launch {
        try {
          try {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
          } catch (_: Exception) { }
          // PDF/EPUB extraction and scanned-page OCR run off the UI thread.
          importStatus = if (currentLanguage == LanguageSetting.SINHALA) "ගොනුව කියවමින්…" else "Reading document…"
          val imported = withContext(Dispatchers.IO) {
            readImportedBook(context, uri, currentLanguage) { message ->
              mainHandler.post { importStatus = message }
            }
          }
          document = imported
          sentences = splitIntoSentences(imported.text)
          currentSentence = 0
          isPlaying = false
          tts?.stop()
          importError = null
          importStatus = ""
        } catch (error: Exception) {
          importStatus = ""
          importError = if (si) {
            when {
              error.message.orEmpty().contains("download", true) || error.message.orEmpty().contains("internet", true) ->
                "සිංහල OCR ආකෘතිය බාගත කළ නොහැකි වුණා. අන්තර්ජාල සම්බන්ධතාව පරීක්ෂා කර නැවත උත්සාහ කරන්න."
              else -> "මෙම ලේඛනය කියවීමට නොහැකි වුණා: ${error.message.orEmpty()}"
            }
          } else error.message ?: "Could not read this document."
        }
      }
    }
  }

  DisposableEffect(currentLanguage) {
    var engine: TextToSpeech? = null
    val speech = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        val instance = engine
        if (instance != null) {
          val locale = if (currentLanguage == LanguageSetting.SINHALA) Locale("si", "LK") else Locale.US
          val availability = instance.isLanguageAvailable(locale)
          if (availability >= TextToSpeech.LANG_AVAILABLE) {
            instance.language = locale
            selectedVoiceAvailable = true
            voiceStatus = if (si) "මෙම දුරකථනයේ සිංහල හඬ තිබේ." else "English voice ready"
          } else {
            selectedVoiceAvailable = false
            voiceStatus = if (currentLanguage == LanguageSetting.SINHALA) {
              "සිංහල හඬක් ස්ථාපනය කර නැහැ. Android Text-to-Speech සැකසුම්වලින් හඬ දත්ත ස්ථාපනය කරන්න."
            } else "English voice unavailable on this device"
          }
          ttsReady = true
        }
      } else {
        voiceStatus = if (si) "දුරකථනයේ හඬ සේවාව ආරම්භ කළ නොහැකි වුණා." else "Android Text-to-Speech initialization failed"
      }
    }
    engine = speech
    tts = speech
    speech.setSpeechRate(playbackSpeed)
    speech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
      override fun onStart(utteranceId: String?) {
        val index = utteranceId?.removePrefix("skeleton_sentence_")?.toIntOrNull() ?: return
        android.os.Handler(android.os.Looper.getMainLooper()).post { currentSentence = index }
      }
      override fun onDone(utteranceId: String?) {
        val index = utteranceId?.removePrefix("skeleton_sentence_")?.toIntOrNull() ?: return
        if (index == latestSentences.lastIndex) {
          android.os.Handler(android.os.Looper.getMainLooper()).post { isPlaying = false }
        }
      }
      @Deprecated("Deprecated in Java")
      override fun onError(utteranceId: String?) {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
          isPlaying = false
          voiceStatus = if (si) "හඬ වාදනය අසාර්ථකයි. ස්ථාපිත හඬ පරීක්ෂා කරන්න." else "Speech failed. Check the installed device voice."
        }
      }
    })
    onDispose {
      speech.stop()
      speech.shutdown()
      if (tts === speech) tts = null
      ttsReady = false
      isPlaying = false
    }
  }

  fun playFrom(index: Int) {
    val engine = tts
    if (!ttsReady || engine == null || sentences.isEmpty()) {
      voiceStatus = if (si) "හඬ සේවාව තවම සූදානම් නැහැ." else "Speech engine is not ready yet."
      return
    }
    if (!selectedVoiceAvailable) {
      voiceStatus = if (si) "තෝරාගත් භාෂාවට ගැළපෙන හඬක් ස්ථාපනය කර නැහැ." else "No installed voice supports the selected language."
      return
    }
    engine.stop()
    engine.setSpeechRate(playbackSpeed)
    isPlaying = true
    val start = index.coerceIn(0, sentences.lastIndex)
    for (i in start..sentences.lastIndex) {
      engine.speak(sentences[i], if (i == start) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD,
        Bundle(), "skeleton_sentence_$i")
    }
  }

  val sentence = sentences.getOrNull(currentSentence).orEmpty()
  val scrollState = rememberScrollState()

  Box(
    modifier = modifier.fillMaxSize().background(ObsidianBg)
      .testTag("audiobook_caption_screen")
  ) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
      Row(
        Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = { tts?.stop(); isPlaying = false; onBack() }, modifier = Modifier.size(42.dp)) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = SilverText)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          SkeletonBracketLogo(size = 18.dp, accentColor = CyanNeon)
          Spacer(Modifier.width(8.dp))
          Text(if (si) "ශ්‍රව්‍ය පොත සහ උපසිරැසි" else "AUDIOBOOK & CAPTIONS", color = CyanNeon, fontSize = 12.sp,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        Icon(Icons.Filled.VolumeUp, "Audio engine", tint = if (isPlaying) EmeraldMastery else SilverDim)
      }

      Spacer(Modifier.height(12.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
          Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(ObsidianSurface)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp))
            .clickable { importLauncher.launch(arrayOf("text/plain", "application/epub+zip", "application/epub", "application/pdf")) }
            .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
          Text(if (si) "＋ TXT / EPUB / PDF ආයාත කරන්න" else "＋ IMPORT TXT / EPUB / PDF", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
      if (importStatus.isNotBlank()) {
        Text(importStatus, color = AmberAccent, fontSize = 11.sp, modifier = Modifier.padding(top = 6.dp))
      }
      importError?.let {
        Text(it, color = Color(0xFFFF8A80), fontSize = 11.sp, modifier = Modifier.padding(top = 6.dp))
      }

      Spacer(Modifier.height(10.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        samples.forEach { sample ->
          val selected = document.title == sample.title
          Box(
            Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
              .background(if (selected) CyanNeon.copy(alpha = 0.14f) else ObsidianSurface)
              .border(1.dp, if (selected) CyanNeon else ObsidianBorder, RoundedCornerShape(12.dp))
              .clickable {
                tts?.stop(); isPlaying = false; document = sample
                sentences = splitIntoSentences(sample.text); currentSentence = 0
              }.padding(8.dp)
          ) {
            Text(sample.title, color = if (selected) CyanNeon else SilverText, fontSize = 10.sp,
              fontWeight = FontWeight.Bold, maxLines = 2)
          }
        }
      }

      Spacer(Modifier.height(12.dp))
      Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp)).padding(14.dp)
      ) {
        Text(document.chapter, color = AmberAccent, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(document.title, color = SilverText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(document.author, color = SilverMuted, fontSize = 11.sp)
      }

      Spacer(Modifier.height(12.dp))
      Column(
        Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(24.dp))
          .background(Color.Black).border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp))
          .verticalScroll(scrollState).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        sentences.forEachIndexed { index, item ->
          Text(
            text = item,
            color = when {
              index == currentSentence -> Color(0xFF000000)
              index < currentSentence -> SilverMuted
              else -> SilverText
            },
            fontSize = 17.sp, lineHeight = 27.sp,
            fontWeight = if (index == currentSentence) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.fillMaxWidth()
              .then(if (index == currentSentence) Modifier.background(CyanNeon, RoundedCornerShape(8.dp)).padding(8.dp)
                else Modifier.clickable { currentSentence = index; if (isPlaying) playFrom(index) }.padding(8.dp))
          )
        }
      }

      Spacer(Modifier.height(12.dp))
      Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp)).padding(14.dp)
      ) {
        Slider(
          value = if (sentences.isEmpty()) 0f else currentSentence.toFloat() / sentences.lastIndex.coerceAtLeast(1),
          onValueChange = { fraction ->
            currentSentence = (fraction * sentences.lastIndex.coerceAtLeast(0)).toInt()
          },
          colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon, inactiveTrackColor = ObsidianBorderLight)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(if (si) "වාක්‍යය ${if (sentences.isEmpty()) 0 else currentSentence + 1} / ${sentences.size}"
            else "SENTENCE ${if (sentences.isEmpty()) 0 else currentSentence + 1} / ${sentences.size}",
            color = SilverMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
          Text("${playbackSpeed}x", color = CyanNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
        Text(voiceStatus, color = if (selectedVoiceAvailable) SilverMuted else AmberAccent,
          fontSize = 10.sp, modifier = Modifier.padding(top = 5.dp))
        if (currentLanguage == LanguageSetting.SINHALA && !selectedVoiceAvailable) {
          TextButton(onClick = {
            runCatching {
              context.startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA))
            }.onFailure {
              voiceStatus = "Android TTS settings could not be opened."
            }
          }) {
            Text("සිංහල හඬ දත්ත ස්ථාපනය කරන්න", color = CyanNeon, fontSize = 11.sp)
          }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = { val target = (currentSentence - 1).coerceAtLeast(0); currentSentence = target; if (isPlaying) playFrom(target) }) {
            Icon(Icons.Filled.FastRewind, if (si) "පෙර වාක්‍යය" else "Previous sentence", tint = SilverMuted)
          }
          Box(
            Modifier.size(54.dp).clip(CircleShape).background(CyanNeon)
              .clickable {
                if (isPlaying) {
                  tts?.stop()
                  isPlaying = false
                } else playFrom(currentSentence)
              }.testTag("audiobook_play_pause"),
            contentAlignment = Alignment.Center
          ) {
            Icon(if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
              if (isPlaying) (if (si) "විරාමය" else "Pause") else (if (si) "වාදනය" else "Play"),
              tint = ObsidianBg, modifier = Modifier.size(28.dp))
          }
          IconButton(onClick = { val target = (currentSentence + 1).coerceAtMost(sentences.lastIndex.coerceAtLeast(0)); currentSentence = target; if (isPlaying) playFrom(target) }) {
            Icon(Icons.Filled.FastForward, if (si) "ඊළඟ වාක්‍යය" else "Next sentence", tint = SilverMuted)
          }
          Box(
            Modifier.clip(RoundedCornerShape(10.dp)).background(ObsidianSurface)
              .border(1.dp, ObsidianBorder, RoundedCornerShape(10.dp))
              .clickable {
                playbackSpeed = when (playbackSpeed) {
                  0.75f -> 1.0f
                  1.0f -> 1.25f
                  1.25f -> 1.5f
                  1.5f -> 2.0f
                  else -> 0.75f
                }
                tts?.setSpeechRate(playbackSpeed)
              }.padding(horizontal = 9.dp, vertical = 8.dp)
          ) {
            Text(if (si) "වේගය" else "SPEED", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
      Spacer(Modifier.height(8.dp))
    }
  }
}
