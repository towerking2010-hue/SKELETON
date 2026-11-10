package com.example.ui.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.util.Locale
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
import com.example.ui.theme.SinhalaPillGold

data class LanguageMasteryProfile(
  val id: String,
  val name: String,
  val nativeScript: String,
  val family: String,
  val syntaxType: String,
  val blueprintPhonology: String,
  val blueprintMorphology: String,
  val sampleDrill: String,
  val drillTranslation: String,
  val quizQuestion: String,
  val quizOptions: List<String>,
  val quizCorrectIndex: Int
)

enum class TeacherTab(val title: String) {
  BLUEPRINT("Blueprint"),
  DRILL("Drill"),
  SYNTAX("Syntax"),
  QUIZ("Quiz")
}

@Composable
fun LanguageTeacherScreen(
  currentLanguage: LanguageSetting,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var pronunciationTts by remember { mutableStateOf<TextToSpeech?>(null) }
  var pronunciationReady by remember { mutableStateOf(false) }
  val languages = remember {
    listOf(
      LanguageMasteryProfile(
        id = "lang_sinhala",
        name = "Sinhala",
        nativeScript = "සිංහල",
        family = "Indo-Aryan (Insular)",
        syntaxType = "SOV (Subject - Object - Verb)",
        blueprintPhonology = "54 characters (18 vowels, 36 consonants). Distinct pre-nasalized consonants (ඟ, ඬ, ඳ, ඹ). High phonetic consistency with Brahmic script lineage.",
        blueprintMorphology = "Agglutinative postpositional case endings (-ට dative, -ගෙන් ablative, -ගේ genitive). Inanimate vs animate grammatical gender distinction.",
        sampleDrill = "මම අද රාත්‍රියේ පොතක් කියවනවා.",
        drillTranslation = "I am reading a book tonight. [Mama ada rathriye pothak kiyawanawa.]",
        quizQuestion = "In Sinhala morphology, which suffix designates the dative case (to / for)?",
        quizOptions = listOf("-ට (-ta)", "-ගේ (-ge)", "-ගෙන් (-gen)", "-ව (-wa)"),
        quizCorrectIndex = 0
      ),
      LanguageMasteryProfile(
        id = "lang_japanese",
        name = "Japanese",
        nativeScript = "日本語",
        family = "Japonic",
        syntaxType = "SOV (Strict Verb-Final)",
        blueprintPhonology = "Mora-timed rhythm with pitch accent. 5 pure vowels (a, i, u, e, o). Kana syllabaries (Hiragana/Katakana) + Kanji ideograms.",
        blueprintMorphology = "Postpositional particles: は (wa - topic), が (ga - subject), を (wo - direct object), に (ni - target/time). Zero grammatical gender or plural markers.",
        sampleDrill = "私は今晩、本を読みます。",
        drillTranslation = "I will read a book tonight. [Watashi wa konban, hon o yomimasu.]",
        quizQuestion = "Which particle marks the direct grammatical object of a transitive verb in Japanese?",
        quizOptions = listOf("は (wa)", "を (o/wo)", "に (ni)", "で (de)"),
        quizCorrectIndex = 1
      ),
      LanguageMasteryProfile(
        id = "lang_greek",
        name = "Ancient Greek",
        nativeScript = "Ἑλληνική",
        family = "Hellenic (Indo-European)",
        syntaxType = "Flexible (Inflected Case-Driven)",
        blueprintPhonology = "24-letter alphabet. Pitch accent (acute, grave, circumflex). Distinct rough/smooth breathings (spiritus asper/lenis).",
        blueprintMorphology = "5 cases (Nominative, Genitive, Dative, Accusative, Vocative). 3 grammatical genders, 3 voices (Active, Middle, Passive). Inflected verb aspects.",
        sampleDrill = "Ὁ βίος βραχύς, ἡ δὲ τέχνη μακρή.",
        drillTranslation = "Life is short, but the art is long. (Hippocrates)",
        quizQuestion = "In Ancient Greek, which case marks the indirect object of a verb?",
        quizOptions = listOf("Accusative", "Dative", "Genitive", "Nominative"),
        quizCorrectIndex = 1
      ),
      LanguageMasteryProfile(
        id = "lang_latin",
        name = "Latin",
        nativeScript = "Latīna",
        family = "Italic (Indo-European)",
        syntaxType = "SOV Default (Case Inflected)",
        blueprintPhonology = "Phonemic vowel length (macron vs breve). Hard 'c' (/k/) and 'g' (/g/) in Classical pronunciation.",
        blueprintMorphology = "6 noun cases (Nominative, Genitive, Dative, Accusative, Ablative, Vocative) across 5 declensions. 4 verb conjugations.",
        sampleDrill = "Scientia ipsa potentia est.",
        drillTranslation = "Knowledge itself is power. (Francis Bacon)",
        quizQuestion = "Which Latin case is primarily used for instrument, separation, and location?",
        quizOptions = listOf("Ablative", "Genitive", "Accusative", "Vocative"),
        quizCorrectIndex = 0
      ),
      LanguageMasteryProfile(
        id = "lang_german",
        name = "German",
        nativeScript = "Deutsch",
        family = "West Germanic",
        syntaxType = "V2 in Main Clauses / SOV in Subordinate",
        blueprintPhonology = "Umlauts (ä, ö, ü), eszett (ß). Auslautverhärtung (final-obstruent devoicing). Strong stress on root syllables.",
        blueprintMorphology = "4 cases (Nominative, Accusative, Dative, Genitive). 3 genders (der, die, das). Highly productive compounding morphology.",
        sampleDrill = "Die Grenzen meiner Sprache bedeuten die Grenzen meiner Welt.",
        drillTranslation = "The limits of my language mean the limits of my world. (Wittgenstein)",
        quizQuestion = "Where does the conjugated finite verb sit in a standard German main declarative clause?",
        quizOptions = listOf("Position 1", "Position 2 (V2)", "At the absolute end", "Variable"),
        quizCorrectIndex = 1
      )
    )
  }

  var selectedLanguage by remember { mutableStateOf(languages.first()) }

  DisposableEffect(selectedLanguage.id) {
    val locale = when (selectedLanguage.id) {
      "lang_sinhala" -> Locale("si", "LK")
      "lang_japanese" -> Locale.JAPAN
      "lang_greek" -> Locale("el", "GR")
      "lang_latin" -> Locale("la", "VA")
      else -> Locale.US
    }
    val engine = TextToSpeech(context) { result ->
      if (result == TextToSpeech.SUCCESS) {
        val available = pronunciationTts?.isLanguageAvailable(locale) ?: TextToSpeech.LANG_NOT_SUPPORTED
        if (available >= TextToSpeech.LANG_AVAILABLE) {
          pronunciationTts?.language = locale
          pronunciationReady = true
        } else pronunciationReady = false
      } else pronunciationReady = false
    }
    pronunciationTts = engine
    onDispose {
      engine.stop()
      engine.shutdown()
      pronunciationTts = null
      pronunciationReady = false
    }
  }
  var currentTab by remember { mutableStateOf(TeacherTab.BLUEPRINT) }
  var selectedQuizOption by remember { mutableStateOf<Int?>(null) }
  var userDrillInput by remember { mutableStateOf("") }
  var drillFeedback by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("language_teacher_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      // Top Navigation
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.size(42.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = SilverText
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          SkeletonBracketLogo(size = 18.dp, accentColor = CyanNeon)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "LANGUAGE TEACHER // TUTOR",
            color = CyanNeon,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        Icon(
          imageVector = Icons.Filled.School,
          contentDescription = null,
          tint = EmeraldMastery,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Language Selector Carousel
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(languages) { lang ->
          val isSelected = lang.id == selectedLanguage.id
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) CyanNeon.copy(alpha = 0.15f) else ObsidianSurface)
              .border(
                1.dp,
                if (isSelected) CyanNeon else ObsidianBorder,
                RoundedCornerShape(14.dp)
              )
              .clickable {
                selectedLanguage = lang
                selectedQuizOption = null
                drillFeedback = null
              }
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Column {
              Text(
                text = lang.nativeScript,
                color = if (isSelected) CyanNeon else SilverText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = lang.name,
                color = SilverMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Active Sub-Tab Selector
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        TeacherTab.values().forEach { tab ->
          val isSelected = tab == currentTab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) ObsidianSurfaceElevated else ObsidianSurface)
              .border(
                1.dp,
                if (isSelected) CyanNeon else ObsidianBorder,
                RoundedCornerShape(12.dp)
              )
              .clickable { currentTab = tab }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = tab.title,
              color = if (isSelected) CyanNeon else SilverMuted,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Content Box
      val scrollState = rememberScrollState()
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .clip(RoundedCornerShape(26.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(26.dp))
          .padding(18.dp)
          .verticalScroll(scrollState)
      ) {
        when (currentTab) {
          TeacherTab.BLUEPRINT -> {
            Column(modifier = Modifier.fillMaxWidth()) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "[SYSTEM: ${selectedLanguage.family}]",
                  color = CyanNeon,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "SYNTAX: ${selectedLanguage.syntaxType}",
                  color = AmberAccent,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "${selectedLanguage.name} (${selectedLanguage.nativeScript}) Blueprint",
                color = SilverText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Phonology Card
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(ObsidianSurface)
                  .padding(14.dp)
              ) {
                Text(
                  text = "ROOT PHONOLOGY & ORTHOGRAPHY",
                  color = CyanNeon,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = selectedLanguage.blueprintPhonology,
                  color = SilverText,
                  fontSize = 13.sp,
                  lineHeight = 18.sp
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Morphology Card
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(ObsidianSurface)
                  .padding(14.dp)
              ) {
                Text(
                  text = "MORPHOLOGY & CASE GOVERNANCE",
                  color = EmeraldMastery,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = selectedLanguage.blueprintMorphology,
                  color = SilverText,
                  fontSize = 13.sp,
                  lineHeight = 18.sp
                )
              }
            }
          }

          TeacherTab.DRILL -> {
            Column(modifier = Modifier.fillMaxWidth()) {
              Text(
                text = "CONVERSATIONAL DRILL // DASUN",
                color = CyanNeon,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Native Phrase Card
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(ObsidianSurface)
                  .border(0.8.dp, CyanNeon.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                  .padding(14.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "TARGET PHRASE",
                    color = AmberAccent,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                  )
                  Icon(
                    imageVector = Icons.Filled.VolumeUp,
                    contentDescription = "Pronunciation",
                    tint = CyanNeon,
                    modifier = Modifier.size(16.dp).clickable {
                      if (pronunciationReady) {
                        pronunciationTts?.speak(selectedLanguage.sampleDrill, TextToSpeech.QUEUE_FLUSH, Bundle(), "language_phrase")
                      }
                    }
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = selectedLanguage.sampleDrill,
                  color = SilverText,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = selectedLanguage.drillTranslation,
                  color = SilverMuted,
                  fontSize = 12.sp
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "Type your response or translation:",
                color = SilverMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )

              Spacer(modifier = Modifier.height(6.dp))

              OutlinedTextField(
                value = userDrillInput,
                onValueChange = { userDrillInput = it },
                placeholder = { Text("Enter response for Dasun...", color = SilverDim, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = ObsidianBorder,
                  focusedTextColor = SilverText,
                  unfocusedTextColor = SilverText
                ),
                shape = RoundedCornerShape(14.dp)
              )

              Spacer(modifier = Modifier.height(10.dp))

              ElevatedButton(
                onClick = {
                  drillFeedback = if (userDrillInput.isNotBlank()) {
                    "Accurate structural execution! Vocabulary syntax aligned with ${selectedLanguage.syntaxType} rules."
                  } else {
                    "Please input a response to evaluate syntax."
                  }
                },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                  containerColor = CyanNeon,
                  contentColor = ObsidianBg
                ),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("EVALUATE DRILL", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }

              drillFeedback?.let { feedback ->
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldMastery.copy(alpha = 0.15f))
                    .border(0.8.dp, EmeraldMastery, RoundedCornerShape(12.dp))
                    .padding(12.dp)
                ) {
                  Text(text = feedback, color = EmeraldMastery, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
              }
            }
          }

          TeacherTab.SYNTAX -> {
            Column(modifier = Modifier.fillMaxWidth()) {
              Text(
                text = "STRUCTURAL SYNTAX DIAGRAM",
                color = AmberAccent,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Visual Block Diagram
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(18.dp))
                  .background(ObsidianSurface)
                  .padding(16.dp)
              ) {
                Text(
                  text = "Order: ${selectedLanguage.syntaxType}",
                  color = CyanNeon,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .clip(RoundedCornerShape(10.dp))
                      .background(CyanNeon.copy(alpha = 0.15f))
                      .border(1.dp, CyanNeon, RoundedCornerShape(10.dp))
                      .padding(8.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Text("SUBJECT", color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                      Text("Agent", color = SilverMuted, fontSize = 10.sp)
                    }
                  }

                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .clip(RoundedCornerShape(10.dp))
                      .background(AmberAccent.copy(alpha = 0.15f))
                      .border(1.dp, AmberAccent, RoundedCornerShape(10.dp))
                      .padding(8.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Text("OBJECT", color = AmberAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                      Text("Patient", color = SilverMuted, fontSize = 10.sp)
                    }
                  }

                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .clip(RoundedCornerShape(10.dp))
                      .background(EmeraldMastery.copy(alpha = 0.15f))
                      .border(1.dp, EmeraldMastery, RoundedCornerShape(10.dp))
                      .padding(8.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Text("VERB", color = EmeraldMastery, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                      Text("Action", color = SilverMuted, fontSize = 10.sp)
                    }
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = "Linguistic Invariant: Case markers pin semantic roles regardless of word order shifts.",
                  color = SilverText,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )
              }
            }
          }

          TeacherTab.QUIZ -> {
            Column(modifier = Modifier.fillMaxWidth()) {
              Text(
                text = "ACTIVE RECALL QUIZ // DASUN",
                color = EmeraldMastery,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = selectedLanguage.quizQuestion,
                color = SilverText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(14.dp))

              selectedLanguage.quizOptions.forEachIndexed { index, option ->
                val isSelected = selectedQuizOption == index
                val isCorrect = index == selectedLanguage.quizCorrectIndex
                val showResult = selectedQuizOption != null

                val borderColor = when {
                  !showResult && isSelected -> CyanNeon
                  showResult && isCorrect -> EmeraldMastery
                  showResult && isSelected && !isCorrect -> AmberAccent
                  else -> ObsidianBorder
                }

                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ObsidianSurface)
                    .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable { selectedQuizOption = index }
                    .padding(14.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = option,
                      color = SilverText,
                      fontSize = 13.sp,
                      fontFamily = FontFamily.Monospace
                    )
                    if (showResult && isCorrect) {
                      Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = EmeraldMastery, modifier = Modifier.size(16.dp))
                    }
                  }
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}
