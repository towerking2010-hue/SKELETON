package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
import com.example.engine.research.ResearchEngine
import com.example.engine.research.ResearchSearchResult
import com.example.engine.research.ResearchSource
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText
import kotlinx.coroutines.launch

@Composable
fun ResearchScreen(
  currentLanguage: LanguageSetting,
  onBack: () -> Unit
) {
  var query by remember { mutableStateOf("") }
  var sources by remember { mutableStateOf<List<ResearchSource>>(emptyList()) }
  var status by remember { mutableStateOf<String?>(null) }
  var searching by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()
  val uriHandler = LocalUriHandler.current
  val sinhala = currentLanguage == LanguageSetting.SINHALA

  Column(
    modifier = Modifier.fillMaxSize().background(ObsidianBg).padding(horizontal = 18.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(if (sinhala) "මූලාශ්‍ර සොයන්න" else "RESEARCH DESK", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.8.sp)
        Text(if (sinhala) "විවෘත ශාස්ත්‍රීය දත්ත සමුදාය" else "Open scholarly metadata · no AI calls", color = SilverMuted, fontSize = 11.sp)
      }
      Text(if (sinhala) "ආපසු" else "BACK", color = SilverText, fontSize = 11.sp, modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onBack).padding(10.dp))
    }

    OutlinedTextField(
      value = query,
      onValueChange = { query = it },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      label = { Text(if (sinhala) "මාතෘකාවක් සොයන්න" else "Search a topic") },
      placeholder = { Text("e.g. habit formation, cognitive bias, alchemy") },
      shape = RoundedCornerShape(16.dp)
    )

    Button(
      onClick = {
        if (!searching && query.isNotBlank()) {
          searching = true
          status = null
          scope.launch {
            when (val result = ResearchEngine.search(query)) {
              is ResearchSearchResult.Success -> {
                sources = result.sources
                status = if (result.sources.isEmpty()) "No records found. Try broader keywords." else "${result.sources.size} records retrieved from Crossref."
              }
              is ResearchSearchResult.Unavailable -> status = result.message
            }
            searching = false
          }
        }
      },
      enabled = query.isNotBlank() && !searching,
      modifier = Modifier.fillMaxWidth(),
      colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg),
      shape = RoundedCornerShape(14.dp)
    ) {
      if (searching) CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.height(18.dp))
      else Text(if (sinhala) "සොයන්න" else "SEARCH OPEN SOURCES", fontWeight = FontWeight.Bold)
    }

    Surface(
      color = ObsidianSurface,
      shape = RoundedCornerShape(14.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
    ) {
      Text(
        "Free bibliographic search. Results are source leads, not verified conclusions. Open the original record and evaluate its methods, evidence and limitations.",
        color = SilverMuted,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        modifier = Modifier.padding(12.dp)
      )
    }

    status?.let { Text(it, color = SilverText, fontSize = 12.sp) }

    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(sources, key = { it.doi ?: it.url }) { source ->
        ResearchSourceCard(source = source, onOpen = {
          runCatching { uriHandler.openUri(source.url) }
        })
      }
    }
  }
}

@Composable
private fun ResearchSourceCard(source: ResearchSource, onOpen: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(ObsidianSurface)
      .clickable(onClick = onOpen)
      .padding(15.dp),
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    Text(source.sourceType.uppercase(), color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.1.sp)
    Text(source.title, color = SilverText, fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
    val metadata = buildList {
      source.authors.take(3).takeIf { it.isNotEmpty() }?.let { add(it.joinToString(", ")) }
      source.publishedYear?.let { add(it.toString()) }
      source.publisher?.let { add(it) }
    }.joinToString(" · ")
    if (metadata.isNotBlank()) Text(metadata, color = SilverMuted, fontSize = 10.sp, lineHeight = 15.sp)
    Text(source.doi?.let { "DOI: $it" } ?: source.url, color = CyanNeon, fontSize = 10.sp)
    Text(source.evidenceNote, color = SilverMuted, fontSize = 10.sp, lineHeight = 14.sp)
    Spacer(Modifier.height(1.dp))
    Text("OPEN ORIGINAL RECORD ↗", color = MaterialTheme.colorScheme.tertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
  }
}
