package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
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
fun GroundingScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val scope = rememberCoroutineScope()
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Search Grounding, 1: Maps Grounding
  var searchQuery by remember { mutableStateOf("Latest CRISPR base editing clinical trials 2026") }
  var mapsQuery by remember { mutableStateOf("CERN Large Hadron Collider, Geneva, Switzerland") }
  var isGrounding by remember { mutableStateOf(false) }
  var groundingResult by remember { mutableStateOf<String?>(null) }

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
          text = "GROUNDING ENGINE [GEMINI-3.8-FLASH]",
          color = CyanNeon,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "Search & Maps Grounding Integration",
          color = SilverMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // Tabs: Search vs Maps
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = ObsidianSurfaceElevated,
      contentColor = CyanNeon,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = if (selectedTab == 0) CyanNeon else AmberAccent
        )
      }
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = {
          selectedTab = 0
          groundingResult = null
        },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Google Search Data", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          }
        }
      )

      Tab(
        selected = selectedTab == 1,
        onClick = {
          selectedTab = 1
          groundingResult = null
        },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Place, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Google Maps Data", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          }
        }
      )
    }

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Query Input Box
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
        ) {
          Text(
            text = if (selectedTab == 0) "REAL-TIME SEARCH GROUNDING PROMPT" else "GEOGRAPHIC MAPS GROUNDING TARGET",
            color = if (selectedTab == 0) CyanNeon else AmberAccent,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(8.dp))

          BasicTextField(
            value = if (selectedTab == 0) searchQuery else mapsQuery,
            onValueChange = {
              if (selectedTab == 0) searchQuery = it else mapsQuery = it
            },
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(ObsidianSurfaceElevated)
              .padding(12.dp)
              .testTag("grounding_query_input"),
            textStyle = TextStyle(
              color = SilverText,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(if (selectedTab == 0) CyanNeon else AmberAccent)
          )

          Spacer(modifier = Modifier.height(14.dp))

          ElevatedButton(
            onClick = {
              isGrounding = true
              scope.launch {
                val res = if (selectedTab == 0) {
                  GeminiCognitiveService.searchGroundedQuery(searchQuery)
                } else {
                  GeminiCognitiveService.mapsGroundedQuery(mapsQuery)
                }
                groundingResult = res
                isGrounding = false
              }
            },
            enabled = !isGrounding,
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = if (selectedTab == 0) CyanNeon else AmberAccent,
              contentColor = ObsidianBg
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("execute_grounding_button")
          ) {
            if (isGrounding) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = ObsidianBg,
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = if (selectedTab == 0) Icons.Filled.Search else Icons.Filled.Place,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (selectedTab == 0) "EXECUTE SEARCH GROUNDING" else "LOCATE WITH MAPS GROUNDING",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }

      // Grounding Result Display Card
      if (groundingResult != null) {
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(ObsidianSurface)
              .border(1.dp, EmeraldMastery.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
              .padding(16.dp)
          ) {
            Text(
              text = if (selectedTab == 0) "GROUNDED SEARCH FINDINGS" else "GEOGRAPHIC GROUNDED TELEMETRY",
              color = EmeraldMastery,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = groundingResult ?: "",
              color = SilverText,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              lineHeight = 19.sp
            )
          }
        }
      }
    }
  }
}
