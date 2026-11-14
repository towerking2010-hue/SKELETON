package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.arena.ArenaChallenge
import com.example.engine.arena.ArenaEngine
import com.example.engine.arena.ArenaProfile
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
fun ArenaScreen(
  profile: ArenaProfile,
  onCompleteChallenge: (String, Int) -> Unit,
  onRecordAttempt: (String, Boolean) -> Unit,
  onBack: () -> Unit
) {
  var selectedMode by remember { mutableStateOf("ALL") }
  var activeChallengeId by remember { mutableStateOf<String?>(null) }
  var challengeCycle by remember { mutableIntStateOf(0) }
  val available = ArenaEngine.challengesFor(selectedMode, profile)
  val challenge = available.firstOrNull { it.id == activeChallengeId } ?: available.firstOrNull()
  var selectedAnswer by remember { mutableIntStateOf(-1) }
  var submitted by remember { mutableStateOf(false) }
  var lastWasCorrect by remember { mutableStateOf(false) }

  LaunchedEffect(challenge?.id, challengeCycle) {
    activeChallengeId = challenge?.id
    selectedAnswer = -1
    submitted = false
    lastWasCorrect = false
  }

  Column(
    modifier = Modifier.fillMaxSize().background(ObsidianBg).padding(horizontal = 18.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text("THE ARENA", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
        Text("Train your thinking. Earn your rank.", color = SilverMuted, fontSize = 12.sp)
      }
      Box(
        Modifier.clip(RoundedCornerShape(12.dp)).background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(12.dp)).clickable(onClick = onBack).padding(horizontal = 13.dp, vertical = 9.dp)
      ) { Text("EXIT", color = SilverMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.fillMaxWidth()) {
      StatPanel("LEVEL", profile.level.toString(), Modifier.weight(1f))
      StatPanel("TOTAL XP", profile.xp.toString(), Modifier.weight(1f))
      StatPanel("RANK", profile.rank, Modifier.weight(1f))
    }

    Column(
      Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(ObsidianSurface)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp)).padding(13.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("NEXT LEVEL", color = SilverMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text("${profile.levelProgress} / 100 XP", color = CyanNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
      }
      Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(ObsidianBorder)) {
        Box(Modifier.fillMaxWidth(profile.levelProgress / 100f).height(6.dp).clip(CircleShape).background(CyanNeon))
      }
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("TERRITORIES", color = SilverMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
        Text("${ArenaEngine.unlockedTerritories(profile.xp)} / 5 unlocked", color = AmberAccent, fontSize = 10.sp)
      }
    }

    Column(
      Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).background(ObsidianSurface)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(15.dp)).padding(11.dp),
      verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
      Text("YOUR COGNITIVE PROFILE", color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
      Text("Private skill signals from your answers · not an IQ or clinical test", color = SilverDim, fontSize = 8.sp)
      Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ArenaEngine.skillsFor(profile).forEach { skill ->
          Column(Modifier.width(112.dp).clip(RoundedCornerShape(9.dp)).background(ObsidianSurfaceElevated).padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(skill.label, color = SilverText, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(if (skill.evidence.attempts == 0) "NOT TESTED" else skill.evidence.accuracy.toString() + "% · " + skill.evidence.attempts.toString() + " tries", color = if (skill.evidence.isEstablished) EmeraldMastery else AmberAccent, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
          }
        }
      }
    }
    Text("CHOOSE YOUR TRIAL", color = SilverMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
      ArenaEngine.modes.forEach { mode ->
        val selected = selectedMode == mode
        Box(
          Modifier.clip(RoundedCornerShape(18.dp)).background(if (selected) CyanNeon.copy(alpha = 0.14f) else ObsidianSurface)
            .border(1.dp, if (selected) CyanNeon else ObsidianBorder, RoundedCornerShape(18.dp))
            .clickable { selectedMode = mode }.padding(horizontal = 12.dp, vertical = 9.dp)
        ) { Text(mode, color = if (selected) CyanNeon else SilverMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
      }
    }

    if (challenge == null) {
      Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
          Text("TRIALS CLEARED", color = EmeraldMastery, fontSize = 17.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
          Text("You've completed every available challenge in this category.", color = SilverMuted, fontSize = 12.sp)
          Text("Choose another trial to keep exploring.", color = SilverDim, fontSize = 11.sp)
        }
      }
    } else {
      ChallengePanel(
        challenge = challenge,
        selectedAnswer = selectedAnswer,
        submitted = submitted,
        lastWasCorrect = lastWasCorrect,
        onSelect = { if (!submitted) selectedAnswer = it },
        onRetry = { selectedAnswer = -1; submitted = false; lastWasCorrect = false },
        onSubmit = {
          val isCorrect = selectedAnswer == challenge.correctIndex
          onRecordAttempt(challenge.skillId, isCorrect)
          if (isCorrect) {
            activeChallengeId = null
            challengeCycle++
            lastWasCorrect = true
            onCompleteChallenge(challenge.id, challenge.rewardXp)
          } else {
            lastWasCorrect = false
          }
          submitted = true
        },
        modifier = Modifier.weight(1f)
      )
    }
    Text("OFFLINE • NO ADS • NO PURCHASES • XP ONLY FOR CORRECT FIRST-TIME SOLVES", color = SilverDim, fontSize = 8.sp, fontFamily = FontFamily.Monospace, letterSpacing = 0.3.sp)
  }
}

@Composable
private fun StatPanel(label: String, value: String, modifier: Modifier = Modifier) {
  Column(modifier.clip(RoundedCornerShape(13.dp)).background(ObsidianSurface)
    .border(1.dp, ObsidianBorder, RoundedCornerShape(13.dp)).padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Text(label, color = SilverDim, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    Text(value, color = SilverText, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
  }
}

@Composable
private fun ChallengePanel(
  challenge: ArenaChallenge,
  selectedAnswer: Int,
  submitted: Boolean,
  lastWasCorrect: Boolean,
  onSelect: (Int) -> Unit,
  onRetry: () -> Unit,
  onSubmit: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(ObsidianSurface)
      .border(1.dp, ObsidianBorder, RoundedCornerShape(22.dp)).padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(challenge.mode, color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
      Text(challenge.difficulty, color = if (challenge.difficulty == "HARD") AmberAccent else SilverMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
    Text(challenge.title, color = SilverText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    Text(challenge.domain.uppercase(), color = SilverDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
    Text(challenge.prompt, color = SilverText, fontSize = 14.sp, lineHeight = 21.sp)
    challenge.options.forEachIndexed { index, option ->
      val isSelected = selectedAnswer == index
      val isCorrect = submitted && index == challenge.correctIndex
      val outline = if (isCorrect) EmeraldMastery else if (isSelected) CyanNeon else ObsidianBorder
      Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
          .background(if (isCorrect) EmeraldMastery.copy(alpha = 0.10f) else if (isSelected) CyanNeon.copy(alpha = 0.09f) else ObsidianSurfaceElevated)
          .border(1.dp, outline, RoundedCornerShape(12.dp))
          .clickable(enabled = !submitted) { onSelect(index) }
          .padding(11.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(Modifier.size(23.dp).clip(CircleShape).border(1.dp, outline, CircleShape), contentAlignment = Alignment.Center) {
          Text(('A' + index).toString(), color = if (isCorrect) EmeraldMastery else SilverMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(9.dp))
        Text(option, color = SilverText, fontSize = 12.sp, lineHeight = 17.sp, modifier = Modifier.weight(1f))
      }
    }
    if (submitted) {
      Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
        .background(if (lastWasCorrect) EmeraldMastery.copy(alpha = 0.10f) else AmberAccent.copy(alpha = 0.08f))
        .padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(if (lastWasCorrect) "TRIAL CLEARED  +${challenge.rewardXp} XP" else "NOT QUITE — STUDY THE MECHANISM", color = if (lastWasCorrect) EmeraldMastery else AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(challenge.explanation, color = SilverMuted, fontSize = 11.sp, lineHeight = 16.sp)
        if (!lastWasCorrect) Text("Try again after reviewing the explanation.", color = SilverDim, fontSize = 10.sp)
      }
    }
    if (!submitted) {
      Button(onClick = onSubmit, enabled = selectedAnswer >= 0, modifier = Modifier.fillMaxWidth().testTag("arena_submit"),
        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)) {
        Text("SUBMIT ANSWER", fontWeight = FontWeight.Black, fontSize = 11.sp, letterSpacing = 1.sp)
      }
    } else if (lastWasCorrect) {
      Text("NEXT TRIAL LOADS AUTOMATICALLY", color = SilverDim, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
    } else {
      Button(onClick = onRetry, modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = ObsidianSurfaceElevated, contentColor = SilverText)) {
        Text("TRY AGAIN", fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
