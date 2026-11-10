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
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
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
import kotlinx.coroutines.delay

data class EbookTreatise(
  val id: String,
  val title: String,
  val author: String,
  val chapter: String,
  val text: String
)

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun AudiobookCaptionScreen(
  currentLanguage: LanguageSetting,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val treatises = remember {
    listOf(
      EbookTreatise(
        id = "treatise_kybalion",
        title = "The Kybalion",
        author = "Three Initiates",
        chapter = "Chapter II: The Seven Hermetic Principles",
        text = "The principles of truth are seven: he who knows these, understandingly, possesses the magic key before whose touch all the doors of the temple fly open. The first principle is Mentalism: The All is Mind; the Universe is Mental. The second is Correspondence: As above, so below; as below, so above. The third is Vibration: Nothing rests; everything moves; everything vibrates."
      ),
      EbookTreatise(
        id = "treatise_mahavamsa",
        title = "මහාවංශය (Mahavamsa)",
        author = "මහානාම හිමි",
        chapter = "38 වන පරිච්ඡේදය: ධාතුසේන රජු සහ කලා වැව",
        text = "ධාතුසේන රජතුමා මහා සයුරක් බඳු කලා වැව කරවා ජල කළමනාකරණ විස්මයක් ලොවට දායාද කළේය. කළාවැවේ ජලයම තමාගේ සදාකාලික ධන නිධානය බව එතුමා ප්‍රකාශ කළේය. මිනිස් ශ්‍රමය සහ ගුරුත්වාකර්ෂණ ජ්‍යාමිතිය එක්තැන් වූ මෙම වාරි ශිෂ්ටාචාරය සදා අමරණීය වේ."
      ),
      EbookTreatise(
        id = "treatise_sidat_sangarawa",
        title = "සිදත් සඟරාව (Sidat Sangarawa)",
        author = "පතිරාජ දේව",
        chapter = "පළමු පරිච්ඡේදය: අක්ෂර සහ ව්‍යාකරණ න්‍යාය",
        text = "වදන් පෙරළි ව්‍යාකරණ නීති අනුව පද සන්ධි වන ආකාරය සහ නාම පද ආඛ්‍යාත පද සමඟ එක්වන ආකාරය මනාව විග්‍රහ කෙරේ. භාෂාවක අභ්‍යන්තර ආත්මය රඳා පවතින්නේ එහි ව්‍යාකරණමය නීති සහ ශබ්ද විද්‍යාත්මක ශක්තිය මතය."
      ),
      EbookTreatise(
        id = "treatise_trivium",
        title = "The Trivium: The Liberal Arts",
        author = "Sister Miriam Joseph",
        chapter = "Chapter I: Nature of the Liberal Arts",
        text = "The liberal arts are the arts of the mind. By them man learns to think clearly, to formulate ideas, and to express them precisely. Grammar is the art of inventing and combining symbols. Logic is the art of thinking. Rhetoric is the art of communicating thought from one mind to another. These three form the foundational Trivium."
      ),
      EbookTreatise(
        id = "treatise_amawatura",
        title = "අමාවතුර (Amawatura)",
        author = "ගුරුළුගෝමී",
        chapter = "පුරිසදම්මසාරථී විග්‍රහය: විඥාන සංස්ලේෂණය",
        text = "හික්මවිය නොහැකි දරුණු මනස ප්‍රඥාවේ සහ දයාවේ ආලෝකයෙන් ශාන්ත භාවයට පත් කිරීමේ අපූර්ව මනෝවිද්‍යාත්මක ක්‍රමවේදය මෙහි විවරණය වේ. ආවේග පාලනය කර සිත සමාහිත කිරීම ශ්‍රේෂ්ඨතම ජයග්‍රහණයයි."
      ),
      EbookTreatise(
        id = "treatise_meditations",
        title = "Meditations",
        author = "Marcus Aurelius",
        chapter = "Book IV: The Inner Citadel",
        text = "Nowhere can man find a quieter or more untroubled retreat than in his own soul, especially he who has within himself such principles that on turning to look into them he is immediately in perfect tranquility. Keep this retreat always in mind, and refresh yourself continuously."
      )
    )
  }

  var selectedTreatise by remember { mutableStateOf(treatises.first()) }
  var isPlaying by remember { mutableStateOf(false) }
  var currentWordIndex by remember { mutableIntStateOf(0) }
  var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

  val words = remember(selectedTreatise) {
    selectedTreatise.text.split(Regex("\\s+")).filter { it.isNotBlank() }
  }

  // Active word playback simulation
  LaunchedEffect(isPlaying, words, playbackSpeed) {
    if (isPlaying) {
      while (currentWordIndex < words.size - 1) {
        val delayMillis = (320L / playbackSpeed).toLong()
        delay(delayMillis)
        currentWordIndex++
      }
      isPlaying = false
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("audiobook_caption_screen")
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
            text = "AUDIOBOOK CAPTION ENGINE",
            color = CyanNeon,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        Icon(
          imageVector = Icons.Filled.VolumeUp,
          contentDescription = "Audio Engine",
          tint = if (isPlaying) EmeraldMastery else SilverDim,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // E-book Selector Carousel
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(treatises) { treatise ->
          val isSelected = treatise.id == selectedTreatise.id
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
                selectedTreatise = treatise
                currentWordIndex = 0
                isPlaying = false
              }
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Column {
              Text(
                text = treatise.title,
                color = if (isSelected) CyanNeon else SilverText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = treatise.author,
                color = SilverMuted,
                fontSize = 10.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Active Treatise Details Header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = selectedTreatise.chapter,
            color = AmberAccent,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = if (currentLanguage == LanguageSetting.SINHALA) "TTS: NATURAL SINHALA AUDIO // DASUN" else "TTS: NATURAL SPEECH // DASUN",
            color = if (currentLanguage == LanguageSetting.SINHALA) AmberAccent else CyanNeon,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = selectedTreatise.title,
          color = SilverText,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 3. High-Contrast Word-by-Word Caption Highlighting Teleprompter Box
      val scrollState = rememberScrollState()
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(Color(0xFF000000)) // Pure deep black background for contrast
          .border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp))
          .padding(20.dp)
          .verticalScroll(scrollState)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Render flow of words with active highlighting
          androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            words.forEachIndexed { index, word ->
              val isSpoken = index < currentWordIndex
              val isActive = index == currentWordIndex
              val isPending = index > currentWordIndex

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(
                    when {
                      isActive -> CyanNeon
                      isSpoken -> CyanNeon.copy(alpha = 0.12f)
                      else -> Color.Transparent
                    }
                  )
                  .clickable { currentWordIndex = index }
                  .padding(horizontal = 4.dp, vertical = 2.dp)
              ) {
                Text(
                  text = word,
                  color = when {
                    isActive -> ObsidianBg
                    isSpoken -> SilverText
                    else -> SilverDim
                  },
                  fontSize = 16.sp,
                  fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 4. Playback Scrubber & Speed Controls
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(24.dp))
          .padding(16.dp)
      ) {
        // Scrubber Slider
        Slider(
          value = if (words.isNotEmpty()) currentWordIndex.toFloat() / words.size.toFloat() else 0f,
          onValueChange = { frac ->
            currentWordIndex = (frac * words.size).toInt().coerceIn(0, words.size - 1)
          },
          colors = SliderDefaults.colors(
            thumbColor = CyanNeon,
            activeTrackColor = CyanNeon,
            inactiveTrackColor = ObsidianBorderLight
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "WORD: ${currentWordIndex + 1} / ${words.size}",
            color = SilverMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
          Text(
            text = "SPEED: ${playbackSpeed}x",
            color = CyanNeon,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Transport Controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Rewind 5 words
          IconButton(
            onClick = {
              currentWordIndex = (currentWordIndex - 5).coerceAtLeast(0)
            }
          ) {
            Icon(
              imageVector = Icons.Filled.FastRewind,
              contentDescription = "Rewind",
              tint = SilverMuted
            )
          }

          // Play / Pause Primary Button
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(CyanNeon)
              .clickable { isPlaying = !isPlaying }
              .testTag("audiobook_play_pause"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Play",
              tint = ObsidianBg,
              modifier = Modifier.size(28.dp)
            )
          }

          // Forward 5 words
          IconButton(
            onClick = {
              currentWordIndex = (currentWordIndex + 5).coerceAtMost(words.size - 1)
            }
          ) {
            Icon(
              imageVector = Icons.Filled.FastForward,
              contentDescription = "Forward",
              tint = SilverMuted
            )
          }

          // Speed Multiplier Cycle
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(ObsidianSurface)
              .border(0.8.dp, ObsidianBorder, RoundedCornerShape(10.dp))
              .clickable {
                playbackSpeed = when (playbackSpeed) {
                  0.75f -> 1.0f
                  1.0f -> 1.25f
                  1.25f -> 1.5f
                  1.5f -> 2.0f
                  else -> 0.75f
                }
              }
              .padding(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Text(
              text = "${playbackSpeed}x",
              color = AmberAccent,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}
