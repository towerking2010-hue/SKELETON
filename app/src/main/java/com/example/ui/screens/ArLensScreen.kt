package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.FeedCard
import com.example.data.model.LanguageSetting
import com.example.ui.components.SkeletonBracketLogo
import com.example.ui.theme.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.UUID
import java.util.concurrent.Executors

@Composable
fun ArLensScreen(
  currentLanguage: LanguageSetting,
  onSaveToVault: (FeedCard) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER } }
  var cameraGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
  var camera by remember { mutableStateOf<Camera?>(null) }
  var torchEnabled by remember { mutableStateOf(false) }
  var detectedText by remember { mutableStateOf("") }
  var cameraStatus by remember { mutableStateOf("Camera permission is needed to start the live lens.") }
  var isSaved by remember { mutableStateOf(false) }
  val latestText by rememberUpdatedState(detectedText)
  val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
    cameraGranted = granted
    cameraStatus = if (granted) "Starting live camera…" else "Camera permission denied."
  }

  DisposableEffect(cameraGranted, lifecycleOwner, previewView) {
    if (!cameraGranted) {
      onDispose { }
    } else {
      val executor = Executors.newSingleThreadExecutor()
      val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
      val providerFuture = ProcessCameraProvider.getInstance(context)
      var provider: ProcessCameraProvider? = null
      providerFuture.addListener({
        try {
          provider = providerFuture.get()
          val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
          val analysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
          analysis.setAnalyzer(executor) { proxy ->
            val media = proxy.image
            if (media == null) {
              proxy.close()
            } else {
              val image = InputImage.fromMediaImage(media, proxy.imageInfo.rotationDegrees)
              recognizer.process(image)
                .addOnSuccessListener { result ->
                  val text = result.text.trim()
                  if (text.isNotBlank()) {
                    detectedText = text
                    cameraStatus = "Live text detected · Latin script"
                    isSaved = false
                  }
                }
                .addOnFailureListener { cameraStatus = "Live text analysis failed." }
                .addOnCompleteListener { proxy.close() }
            }
          }
          provider?.unbindAll()
          camera = provider?.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
          cameraStatus = "LIVE OPTICAL FEED · scanning for text"
        } catch (error: Exception) {
          cameraStatus = "Camera could not start: ${error.localizedMessage ?: "unknown error"}"
        }
      }, ContextCompat.getMainExecutor(context))
      onDispose {
        try { provider?.unbindAll() } catch (_: Exception) { }
        recognizer.close()
        executor.shutdown()
        camera = null
      }
    }
  }

  Column(modifier.fillMaxSize().background(ObsidianBg).statusBarsPadding().navigationBarsPadding().testTag("ar_lens_screen")) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = onBack, modifier = Modifier.size(42.dp).testTag("ar_back_button")) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = SilverText)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        SkeletonBracketLogo(size = 18.dp, accentColor = CyanNeon)
        Spacer(Modifier.width(8.dp))
        Text("AR LENS // LIVE OPTICAL", color = CyanNeon, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
      }
      IconButton(onClick = {
        val next = !torchEnabled
        camera?.cameraControl?.enableTorch(next)
        torchEnabled = next
      }, enabled = camera != null, modifier = Modifier.size(42.dp)) {
        Icon(Icons.Filled.FlashOn, "Toggle torch", tint = if (torchEnabled) AmberAccent else SilverDim)
      }
    }

    Box(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 14.dp).clip(RoundedCornerShape(24.dp)).border(1.dp, CyanNeon.copy(alpha = .5f), RoundedCornerShape(24.dp))) {
      if (cameraGranted) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        // HUD brackets frame the actual camera preview; the scan itself comes from ML Kit.
        Column(Modifier.align(Alignment.TopStart).padding(12.dp)) {
          Text("[ LIVE CAMERA ]", color = CyanNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
      } else {
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("CAMERA ACCESS REQUIRED", color = CyanNeon, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }, colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)) {
            Text("ENABLE CAMERA")
          }
        }
      }
    }

    Column(Modifier.fillMaxWidth().padding(14.dp).clip(RoundedCornerShape(22.dp)).background(ObsidianSurfaceElevated).border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp)).padding(14.dp)) {
      Text("OPTICAL TEXT DETECTION", color = CyanNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
      Spacer(Modifier.height(5.dp))
      Text(cameraStatus, color = SilverMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
      Spacer(Modifier.height(8.dp))
      Text(detectedText.ifBlank { "Point the camera at printed text. Detected text will appear here." }, color = SilverText, fontSize = 12.sp, lineHeight = 17.sp, maxLines = 5)
      Text("Latin-script OCR only in this build; no object identity or semantic understanding is claimed.", color = AmberAccent, fontSize = 9.sp)
      Spacer(Modifier.height(10.dp))
      Button(
        onClick = {
          val raw = latestText.trim()
          if (raw.isNotBlank()) {
            onSaveToVault(FeedCard(
              id = "lens_" + UUID.randomUUID().toString().take(8),
              title = raw.lineSequence().first().take(72).ifBlank { "Camera text capture" },
              itch = "Live camera text capture",
              skeleton = raw,
              intuition = "Captured from the device camera. Ask CORE or DEEPDIVE to explain this text.",
              branchDomain = "Optical OCR Capture",
              crossLinkTarget = "Ghost Sniper OCR",
              isSaved = true
            ))
            isSaved = true
          }
        },
        enabled = detectedText.isNotBlank() && !isSaved,
        modifier = Modifier.fillMaxWidth().height(44.dp).testTag("ar_save_vault_button"),
        colors = ButtonDefaults.buttonColors(containerColor = if (isSaved) EmeraldMastery else CyanNeon, contentColor = ObsidianBg)
      ) {
        Icon(Icons.Filled.Save, null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(if (isSaved) "SAVED TO VAULT" else "SAVE CAPTURE TO VAULT", fontWeight = FontWeight.Bold, fontSize = 11.sp)
      }
    }
  }
}
