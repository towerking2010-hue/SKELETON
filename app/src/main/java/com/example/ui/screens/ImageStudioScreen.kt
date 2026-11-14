package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun ImageStudioScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val scope = rememberCoroutineScope()
  var promptInput by remember {
    mutableStateOf("Minimalist cybernetic brain cortex node diagram with glowing synaptic vectors, obsidian dark theme")
  }
  var isGenerating by remember { mutableStateOf(false) }
  var resultOutput by remember { mutableStateOf<String?>(null) }
  var hasGeneratedVisual by remember { mutableStateOf(false) }

  val presets = listOf(
    "Synaptic vesicle exocytosis in neon amber",
    "Quantum qubit superposition sphere",
    "CRISPR-Cas9 base editing complex",
    "Black hole event horizon entropy boundary"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
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
          tint = AmberAccent
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = "IMAGE STUDIO // PROMPT LAB",
          color = AmberAccent,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Image rendering disabled · zero-billing mode",
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
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Visual Output Frame
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .clip(RoundedCornerShape(22.dp))
          .background(ObsidianSurface)
          .border(1.2.dp, AmberAccent.copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center
      ) {
        if (hasGeneratedVisual) {
          Image(
            painter = painterResource(id = R.drawable.ic_skeleton_logo_1793634876269),
            contentDescription = "Generated Visual",
            modifier = Modifier
              .fillMaxSize()
              .clip(RoundedCornerShape(22.dp))
          )
        } else {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Filled.Image,
              contentDescription = null,
              tint = SilverDim,
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Prompt lab ready · no image has been generated",
              color = SilverDim,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        if (isGenerating) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(ObsidianBg.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(color = AmberAccent, strokeWidth = 3.dp)
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "No paid image request will be sent",
                color = AmberAccent,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }

      // Presets
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(presets) { preset ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(ObsidianSurfaceElevated)
              .border(0.8.dp, ObsidianBorder, RoundedCornerShape(12.dp))
              .clickable { promptInput = preset }
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

      // Prompt Box
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp))
          .padding(14.dp)
      ) {
        Text(
          text = "CONCEPTUAL RENDERING PROMPT",
          color = AmberAccent,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
          value = promptInput,
          onValueChange = { promptInput = it },
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurfaceElevated)
            .padding(10.dp)
            .testTag("image_prompt_input"),
          textStyle = TextStyle(
            color = SilverText,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
          ),
          cursorBrush = SolidColor(AmberAccent)
        )

        Spacer(modifier = Modifier.height(12.dp))

        ElevatedButton(
          onClick = { resultOutput = "Image generation is disabled to prevent paid model usage. Your prompt remains local; no API request was made." },
          enabled = false,
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = AmberAccent,
            contentColor = ObsidianBg
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("generate_image_button")
        ) {
          Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "IMAGE GENERATION DISABLED · ZERO BILLING",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        if (resultOutput != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = resultOutput ?: "",
            color = CyanNeon,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}
