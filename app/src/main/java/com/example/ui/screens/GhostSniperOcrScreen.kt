package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
import com.example.ui.theme.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun GhostSniperOcrScreen(
  currentLanguage: LanguageSetting,
  onExtract: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current
  var rawScanText by remember { mutableStateOf("") }
  var status by remember { mutableStateOf("Capture a page or choose an image to extract text.") }
  var working by remember { mutableStateOf(false) }
  var successFeedback by remember { mutableStateOf(false) }

  fun recognize(image: InputImage) {
    working = true
    successFeedback = false
    status = "Reading image on this device…"
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    recognizer.process(image)
      .addOnSuccessListener { result ->
        rawScanText = result.text
        status = if (result.text.isBlank()) "No readable Latin text found. Try a sharper, well-lit image." else "Text extracted · review it before creating a card."
        working = false
        recognizer.close()
      }
      .addOnFailureListener { error ->
        status = "OCR failed: ${error.localizedMessage ?: "unknown error"}"
        working = false
        recognizer.close()
      }
  }

  val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
    if (bitmap != null) recognize(InputImage.fromBitmap(bitmap, 0))
    else status = "Camera capture cancelled."
  }
  val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
    if (uri != null) {
      try { recognize(InputImage.fromFilePath(context, uri)) }
      catch (error: Exception) { status = "Could not open image: ${error.localizedMessage ?: "unknown error"}" }
    }
  }

  Column(modifier.fillMaxSize().background(ObsidianBg).statusBarsPadding().padding(top = 8.dp).navigationBarsPadding()) {
    Row(Modifier.fillMaxWidth().height(60.dp).background(ObsidianSurface).border(.8.dp, ObsidianBorder).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = EmeraldMastery) }
      Spacer(Modifier.width(8.dp))
      Column {
        Text("MODE [GHOST_SNIPER_OCR]", color = EmeraldMastery, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
        Text("On-device image text extraction", color = SilverMuted, fontSize = 10.sp)
      }
    }
    Column(Modifier.weight(1f).fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(ObsidianSurface).border(1.2.dp, EmeraldMastery.copy(alpha = .5f), RoundedCornerShape(20.dp)).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CameraAlt, null, tint = EmeraldMastery, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("OPTICAL TEXT CAPTURE", color = EmeraldMastery, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          }
          Text("${rawScanText.length} CHARS", color = SilverDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          ElevatedButton(onClick = { cameraLauncher.launch(null) }, enabled = !working, colors = ButtonDefaults.elevatedButtonColors(containerColor = EmeraldMastery, contentColor = ObsidianBg), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
            Icon(Icons.Filled.CameraAlt, null, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(5.dp)); Text("CAMERA", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          ElevatedButton(onClick = { imageLauncher.launch("image/*") }, enabled = !working, colors = ButtonDefaults.elevatedButtonColors(containerColor = ObsidianSurfaceElevated, contentColor = CyanNeon), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
            Text("CHOOSE IMAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
        Spacer(Modifier.height(10.dp))
        Text("OCR supports Latin-script text in this build. Sinhala OCR is not included yet.", color = AmberAccent, fontSize = 10.sp)
        Spacer(Modifier.height(12.dp))
        BasicTextField(
          value = rawScanText, onValueChange = { rawScanText = it; successFeedback = false },
          modifier = Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(12.dp)).background(ObsidianSurfaceElevated).border(.8.dp, ObsidianBorder, RoundedCornerShape(12.dp)).padding(12.dp).testTag("ocr_text_input"),
          textStyle = TextStyle(color = SilverText, fontSize = 12.sp, fontFamily = FontFamily.Monospace, lineHeight = 17.sp),
          cursorBrush = SolidColor(EmeraldMastery),
          decorationBox = { inner ->
            Box {
              if (rawScanText.isBlank()) Text("Extracted text appears here. You can also paste or edit text manually.", color = SilverMuted, fontSize = 12.sp)
              inner()
            }
          }
        )
        Spacer(Modifier.height(10.dp))
        Text(status, color = if (status.contains("failed") || status.contains("No readable")) AmberAccent else SilverMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.height(14.dp))
        ElevatedButton(
          onClick = { onExtract(rawScanText.trim()); successFeedback = true },
          enabled = rawScanText.isNotBlank() && !working,
          colors = ButtonDefaults.elevatedButtonColors(containerColor = EmeraldMastery, contentColor = ObsidianBg, disabledContainerColor = ObsidianBorder),
          shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(48.dp).testTag("execute_ocr_sniper_button")
        ) {
          Icon(Icons.Filled.FlashOn, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp))
          Text("SNIPE ITCH + SKELETON INTO FEED", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
        if (successFeedback) {
          Spacer(Modifier.height(8.dp))
          Text("Text sent to the Feed extraction pipeline.", color = EmeraldMastery, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }
      }
    }
  }
}
