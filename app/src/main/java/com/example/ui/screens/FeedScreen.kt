package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.data.model.FeedCard
import com.example.data.model.GhostChallenge
import com.example.data.model.LanguageSetting
import com.example.data.model.LearningSubject
import com.example.ui.components.AuthStatusBar
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

@Composable
fun FeedScreen(
  cards: List<FeedCard>,
  ghostChallenges: List<GhostChallenge>,
  currentLanguage: LanguageSetting,
  cringeFilterEnabled: Boolean,
  currentUser: com.google.firebase.auth.FirebaseUser? = null,
  onSignInSuccess: (com.google.firebase.auth.FirebaseUser) -> Unit = {},
  onSignOut: () -> Unit = {},
  onOpenEmailSignIn: () -> Unit = {},
  onAskDeepDive: (String) -> Unit,
  onToggleSave: (FeedCard) -> Unit,
  onTriggerGhostChallenge: () -> Unit,
  onAnswerGhostChallenge: (GhostChallenge, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val pendingChallenge = ghostChallenges.firstOrNull { it.status == "PENDING" }
  var challengeAnswerInput by remember { mutableStateOf("") }
  var selectedSubject by remember { mutableStateOf(LearningSubject.ALL) }
  val visibleCards = cards.filter { selectedSubject.matches(it) }
  val pagerState = rememberPagerState(pageCount = { visibleCards.size })
  val pagerScope = rememberCoroutineScope()
  val masteredCount = cards.count { it.recallStage >= 4 }
  val mastery = if (cards.isEmpty()) 0f else masteredCount.toFloat() / cards.size.toFloat()

  Column(
    modifier = modifier.fillMaxSize().background(ObsidianBg).padding(horizontal = 10.dp, vertical = 4.dp),
    verticalArrangement = Arrangement.spacedBy(5.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("DISCOVER", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 1.5.sp)
        Text("Full lessons. Your pace.", color = SilverMuted, fontSize = 10.sp)
      }
      Text("${visibleCards.size} lessons", color = SilverDim, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
    }

    AuthStatusBar(currentUser = currentUser, onSignInSuccess = onSignInSuccess, onSignOut = onSignOut, onOpenEmailSignIn = onOpenEmailSignIn)

    LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
      items(LearningSubject.values().toList(), key = { it.name }) { subject ->
        val selected = subject == selectedSubject
        Box(
          modifier = Modifier.clip(RoundedCornerShape(18.dp))
            .background(if (selected) CyanNeon.copy(alpha = 0.15f) else ObsidianSurface)
            .border(1.dp, if (selected) CyanNeon else ObsidianBorder, RoundedCornerShape(18.dp))
            .clickable { selectedSubject = subject }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(subject.label, color = if (selected) CyanNeon else SilverMuted, fontSize = 10.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
        }
      }
    }

    // Mastery is based on completed recall stages, not views or swipes.
    Column(
      modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
        .background(ObsidianSurface).border(1.dp, ObsidianBorder, RoundedCornerShape(15.dp))
        .padding(horizontal = 10.dp, vertical = 6.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("MASTERY", color = EmeraldMastery, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text("$masteredCount / ${cards.size} mastered  •  ${(mastery * 100).toInt()}%", color = SilverMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
      }
      Box(Modifier.fillMaxWidth().height(3.dp).clip(CircleShape).background(ObsidianBorder)) {
        Box(Modifier.fillMaxWidth(mastery).height(3.dp).clip(CircleShape).background(EmeraldMastery))
      }
    }

    if (pendingChallenge != null) {
      Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
          .background(Color(0xFF191222)).border(1.dp, AmberAccent, RoundedCornerShape(15.dp)).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
      ) {
        Text("GHOST OF MASTERY", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(pendingChallenge.promptQuestion, color = SilverText, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically) {
          BasicTextField(
            value = challengeAnswerInput,
            onValueChange = { challengeAnswerInput = it },
            modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(ObsidianSurfaceElevated).padding(10.dp).testTag("ghost_answer_input"),
            textStyle = TextStyle(color = SilverText, fontSize = 12.sp),
            cursorBrush = SolidColor(AmberAccent),
            decorationBox = { inner -> Box { if (challengeAnswerInput.isBlank()) Text("Your explanation…", color = SilverDim, fontSize = 12.sp); inner() } }
          )
          Spacer(Modifier.width(8.dp))
          ElevatedButton(
            onClick = { onAnswerGhostChallenge(pendingChallenge, challengeAnswerInput.trim()); challengeAnswerInput = "" },
            enabled = challengeAnswerInput.isNotBlank(),
            colors = ButtonDefaults.elevatedButtonColors(containerColor = AmberAccent, contentColor = ObsidianBg),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp)
          ) { Text("CHECK", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        }
      }
    } else {
      Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(ObsidianSurface)
          .clickable { onTriggerGhostChallenge() }.padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("⚡ Quick mastery challenge", color = SilverMuted, fontSize = 11.sp)
        Text("START", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
      }
    }

    if (visibleCards.isEmpty()) {
      Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(if (cards.isEmpty()) "Your learning library is waiting. Ask CORE to explore a topic." else "No lessons in this subject yet.", color = SilverDim, fontSize = 13.sp)
      }
    } else {
      VerticalPager(
        state = pagerState,
        modifier = Modifier.weight(1f).fillMaxWidth().testTag("discovery_fullscreen_pager"),
        pageSpacing = 8.dp
      ) { page ->
        val card = visibleCards[page]
        DiscoveryLessonCard(
          card = card,
          page = page + 1,
          total = visibleCards.size,
          modifier = Modifier.fillMaxSize(),
          onAsk = { onAskDeepDive(card.id) },
          onSave = { onToggleSave(card) }
        )
      }
    }
  }
}

@Composable
private fun DiscoveryLessonCard(
  card: FeedCard,
  page: Int,
  total: Int,
  modifier: Modifier = Modifier,
  onAsk: () -> Unit,
  onSave: () -> Unit
) {
  Column(
    modifier = modifier.clip(RoundedCornerShape(24.dp))
      .background(ObsidianSurface).border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp))
      .padding(15.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(card.branchDomain.uppercase(), color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
        Text("$page / $total", color = SilverDim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
      }
      Text(card.title, color = SilverText, fontSize = 27.sp, lineHeight = 33.sp, fontWeight = FontWeight.Bold)
      Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurfaceElevated).padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
      ) {
        Text("THE QUESTION", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(card.itch, color = SilverText, fontSize = 15.sp, lineHeight = 23.sp)
      }
      Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text("THE IDEA", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(card.intuition, color = SilverMuted, fontSize = 14.sp, lineHeight = 22.sp)
      }
      Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text("WHY IT WORKS", color = SilverDim, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(card.skeleton, color = SilverText, fontSize = 13.sp, lineHeight = 20.sp)
      }
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      if (card.recallStage > 0) {
        Text(if (card.recallStage >= 4) "MASTERED" else "RECALL STAGE ${card.recallStage}/4", color = EmeraldMastery, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
      }
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onSave, modifier = Modifier.size(46.dp).clip(CircleShape).background(ObsidianSurfaceElevated).testTag("save_card_button_${card.id}")) {
          Icon(if (card.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, contentDescription = if (card.isSaved) "Unsave discovery" else "Save discovery", tint = if (card.isSaved) AmberAccent else SilverMuted)
        }
        ElevatedButton(
          onClick = onAsk,
          modifier = Modifier.weight(1f).height(46.dp).testTag("open_deep_dive_${card.id}"),
          colors = ButtonDefaults.elevatedButtonColors(containerColor = CyanNeon, contentColor = ObsidianBg),
          shape = RoundedCornerShape(15.dp)
        ) {
          Icon(Icons.Filled.Psychology, contentDescription = null, modifier = Modifier.size(17.dp))
          Spacer(Modifier.width(7.dp))
          Text("Talk this through", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }
  }
}
