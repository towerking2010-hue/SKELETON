package com.example.ui.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageSetting
import com.example.ui.theme.*
import java.util.Locale

private data class BhQuestion(val en: String, val si: String, val choices: List<String>, val answer: Int, val whyEn: String, val whySi: String)
private data class BhLesson(val id: String, val en: String, val si: String, val phrase: String, val meaningEn: String, val meaningSi: String, val ideaEn: String, val ideaSi: String, val pattern: String, val questions: List<BhQuestion>)
private data class BhCourse(val id: String, val name: String, val native: String, val locale: Locale, val lessons: List<BhLesson>)

private fun bhCourses() = listOf(
  BhCourse("ja","Japanese","日本語",Locale.JAPAN,listOf(
    BhLesson("ja1","Build your first sentence","ඔබේ පළමු වාක්‍යය ගොඩනඟමු","私は水を飲みます。","I drink water.","මම වතුර බොනවා.","Japanese commonly puts the verb last. を marks the thing receiving the action: I / water / drink.","ජපන් භාෂාවේ ක්‍රියාව බොහෝවිට වාක්‍යයේ අගට යෙදෙනවා. を යන්නෙන් ක්‍රියාවට ලක්වන දෙය පෙන්වනවා: මම / වතුර / බොනවා.","Subject → Object → Verb",listOf(
      BhQuestion("Which particle marks the direct object?","ක්‍රියාවට සෘජුවම ලක්වන දෙය පෙන්වන්නේ කුමන නිපාතයෙන්ද?",listOf("は","を","に","で"),1,"を marks the direct object.","を යන්න සෘජු කර්මය පෙන්වනවා."),
      BhQuestion("Where does the verb usually appear?","ජපන් වාක්‍යයක ක්‍රියාව සාමාන්‍යයෙන් යෙදෙන්නේ කොතැනද?",listOf("Beginning","Middle","End","Omitted"),2,"The verb normally comes last.","ක්‍රියාව සාමාන්‍යයෙන් වාක්‍යයේ අගට එනවා."))),
    BhLesson("ja2","Introduce your topic","කතා කරන මාතෘකාව හඳුන්වමු","私は学生です。","I am a student.","මම ශිෂ්‍යයෙක් / ශිෂ්‍යාවක්.","は marks the topic. It is written は but pronounced 'wa' when used as the topic particle.","は යන්නෙන් වාක්‍යයේ මාතෘකාව හඳුන්වනවා. මේ භාවිතයේදී ලියන්නේ は වුණත් උච්චාරණය 'වා' ලෙසයි.","Topic + は + information",listOf(
      BhQuestion("How is は pronounced as the topic particle?","මාතෘකා නිපාතය ලෙස යෙදෙන は උච්චාරණය කරන්නේ කෙසේද?",listOf("ha","wa","o","ne"),1,"In this use, は is pronounced wa.","මෙම භාවිතයේදී は උච්චාරණය කරන්නේ 'වා' ලෙසයි."),
      BhQuestion("What does は introduce?","は මඟින් හඳුන්වා දෙන්නේ කුමක්ද?",listOf("Topic","Past tense","Question","Location"),0,"It marks the topic of the sentence.","එය වාක්‍යයේ මාතෘකාව හඳුන්වනවා.")))
  )),
  BhCourse("de","German","Deutsch",Locale.GERMANY,listOf(
    BhLesson("de1","The verb takes position two","ක්‍රියාව දෙවන ස්ථානයට","Ich lerne heute Deutsch.","I am learning German today.","මම අද ජර්මන් භාෂාව ඉගෙන ගන්නවා.","In a normal German main statement, the conjugated verb usually occupies position two. The first position can change; the verb stays second.","ජර්මන් සාමාන්‍ය ප්‍රකාශ වාක්‍යයක ප්‍රධාන ක්‍රියාපදය බොහෝවිට දෙවන ස්ථානයේ තියෙනවා. මුල් ස්ථානය වෙනස් කළත් ක්‍රියාපදය දෙවන තැන රැකගන්නවා.","Position 1 + verb + rest",listOf(
      BhQuestion("Where is the finite verb in a main statement?","සාමාන්‍ය ප්‍රකාශ වාක්‍යයක ප්‍රධාන ක්‍රියාපදය තිබෙන්නේ කොතැනද?",listOf("Position 1","Position 2","Always last","Position 4"),1,"German main clauses commonly use V2 order.","ජර්මන් ප්‍රධාන වාක්‍යවල ක්‍රියාපදය සාමාන්‍යයෙන් දෙවන තැනයි."),
      BhQuestion("Can a time phrase take position one?","කාලය දක්වන පදයක් මුල් තැනට ගත හැකිද?",listOf("Yes","Never","Only questions","Only past"),0,"Example: Heute lerne ich Deutsch.","උදාහරණයක්: Heute lerne ich Deutsch.")))
  )),
  BhCourse("el","Ancient Greek","Ἑλληνική",Locale("el","GR"),listOf(
    BhLesson("el1","Recognise the subject","කර්තෘ හඳුනාගමු","ὁ ἄνθρωπος","the person","පුද්ගලයා","Greek noun endings carry grammatical information. The nominative form commonly identifies the subject.","ග්‍රීක නාම පදවල අවසානයෙන් ව්‍යාකරණ තොරතුරු ලැබෙනවා. නාමකාරක රූපය සාමාන්‍යයෙන් කර්තෘ හඳුන්වනවා.","Nominative = subject",listOf(
      BhQuestion("Which case commonly marks the subject?","වාක්‍යයක කර්තෘ සාමාන්‍යයෙන් හඳුන්වන්නේ කුමන විභක්තියෙන්ද?",listOf("Genitive","Accusative","Nominative","Vocative"),2,"The nominative commonly marks the subject.","නාමකාරක විභක්තිය සාමාන්‍යයෙන් කර්තෘ හඳුන්වනවා."),
      BhQuestion("What carries grammatical role information?","ව්‍යාකරණ භූමිකාව පෙන්වන්නේ බොහෝවිට කුමක්ද?",listOf("Word endings","Word colour","Punctuation","Word length"),0,"Inflectional endings carry grammatical roles.","වචනවල විභක්ති අවසාන කොටස් ව්‍යාකරණ භූමිකා පෙන්වනවා.")))
  )),
  BhCourse("la","Latin","Latina",Locale("la","VA"),listOf(
    BhLesson("la1","Find the subject by its ending","වචන අවසානයෙන් කර්තෘ සොයමු","Puella aquam portat.","The girl carries water.","ගැහැණු ළමයා වතුර රැගෙන යනවා.","Latin endings identify grammatical roles. Puella is nominative singular here, so it is the subject even though word order can vary.","ලතින් භාෂාවේ වචන අවසාන කොටස් ව්‍යාකරණ භූමිකා පෙන්වනවා. මෙහි Puella නාමකාරක ඒකවචනය නිසා කර්තෘයි. වචන පිළිවෙළ වෙනස් වුණත් එහි භූමිකාව එලෙසම පවතිනවා.","Nominative = subject · Accusative = object",listOf(
      BhQuestion("Which word is the subject?","මෙම වාක්‍යයේ කර්තෘ වන්නේ කුමන වචනයද?",listOf("Puella","Aquam","Portat","None"),0,"Puella is nominative and is the subject.","Puella නාමකාරක නිසා එය කර්තෘයි."),
      BhQuestion("Which case commonly marks the direct object?","සෘජු කර්මය හඳුන්වන්නේ කුමන විභක්තියෙන්ද?",listOf("Nominative","Accusative","Vocative","Genitive"),1,"The accusative commonly marks the direct object.","කර්මකාරක විභක්තියෙන් සාමාන්‍යයෙන් සෘජු කර්මය හඳුන්වනවා.")))
  )),
  BhCourse("si","Sinhala","සිංහල",Locale("si","LK"),listOf(
    BhLesson("si1","Build a clear sentence","පැහැදිලි වාක්‍යයක් ගොඩනඟමු","මම පොත කියවනවා.","I am reading a book.","මම පොත කියවනවා.","Sinhala commonly places the verb at the end. A sentence tells us who acts, what is affected and what action happens.","සිංහල වාක්‍යයක ක්‍රියාව බොහෝවිට අගට යෙදෙනවා. කවුද ක්‍රියාව කරන්නේ, කුමකටද එය බලපාන්නේ, කුමක්ද සිදුවන්නේ යන්න වාක්‍යයෙන් කියවෙනවා.","කර්තෘ + කර්මය + ක්‍රියාව",listOf(
      BhQuestion("Which part tells us the action?","ක්‍රියාව සිදුවන බව කියන්නේ කුමන කොටසද?",listOf("කර්තෘ","කර්මය","ක්‍රියාව","විශේෂණය"),2,"The verb expresses the action.","ක්‍රියා පදය ක්‍රියාව හෝ සිදුවීම පෙන්වනවා."),
      BhQuestion("Where does the verb commonly appear?","සිංහල වාක්‍යයක ක්‍රියාව බොහෝවිට යෙදෙන්නේ කොතැනද?",listOf("මුල","මැද","අග","සෑම විටම නැත"),2,"Sinhala commonly uses verb-final order.","සිංහලයේ බොහෝවිට ක්‍රියාව වාක්‍යයේ අගට යෙදෙනවා.")))
  ))
)

@Composable
fun LanguageTeacherScreen(currentLanguage: LanguageSetting, onBack: () -> Unit, modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val si = currentLanguage == LanguageSetting.SINHALA
  val prefs = remember { context.getSharedPreferences("fluentpath_progress", android.content.Context.MODE_PRIVATE) }
  val courses = remember { bhCourses() }
  var courseId by remember { mutableStateOf(prefs.getString("course", "ja") ?: "ja") }
  var lessonId by remember { mutableStateOf<String?>(null) }
  var qIndex by remember { mutableIntStateOf(0) }
  var answer by remember { mutableIntStateOf(-1) }
  var checked by remember { mutableStateOf(false) }
  var lessonHadMistake by remember { mutableStateOf(false) }
  var xp by remember { mutableIntStateOf(prefs.getInt("xp", 0)) }
  var done by remember { mutableStateOf(prefs.getStringSet("done", emptySet())?.toSet() ?: emptySet()) }
  var review by remember { mutableStateOf(prefs.getStringSet("review", emptySet())?.toSet() ?: emptySet()) }
  var tts by remember { mutableStateOf<TextToSpeech?>(null) }
  var voiceReady by remember { mutableStateOf(false) }
  var voiceStatus by remember { mutableStateOf("") }
  val course = courses.firstOrNull { it.id == courseId } ?: courses.first()
  val lesson = course.lessons.firstOrNull { it.id == lessonId }
  val q = lesson?.questions?.getOrNull(qIndex) ?: BhQuestion("", "", emptyList(), 0, "", "")

  DisposableEffect(course.id) {
    var engine: TextToSpeech? = null
    val created = TextToSpeech(context) { status ->
      val e = engine
      if (status == TextToSpeech.SUCCESS && e != null) {
        voiceReady = e.setLanguage(course.locale) >= TextToSpeech.LANG_AVAILABLE
        voiceStatus = if (voiceReady) (if (si) "හඬින් අසන්න සූදානම්." else "Pronunciation ready.")
          else (if (si) "මෙම භාෂාව සඳහා හඬක් දුරකථනයේ නැහැ." else "No installed voice supports this language.")
      } else {
        voiceReady = false
        voiceStatus = if (si) "හඬ සේවාව ආරම්භ කළ නොහැක." else "Speech engine could not start."
      }
    }
    engine = created
    tts = created
    onDispose { created.stop(); created.shutdown(); if (tts === created) tts = null; voiceReady = false }
  }

  val learn = if (si) "පාඩම් මාර්ගය" else "LEARNING PATH"
  val start = if (si) "පාඩම අරඹන්න" else "START LESSON"
  val check = if (si) "පිළිතුර පරීක්ෂා කරන්න" else "CHECK ANSWER"
  val next = if (si) "ඊළඟ ප්‍රශ්නය" else "NEXT QUESTION"
  val finish = if (si) "පාඩම අවසන් කරන්න" else "COMPLETE LESSON"

  Box(modifier.fillMaxSize().background(ObsidianBg).statusBarsPadding().navigationBarsPadding().testTag("language_teacher_screen")) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
      Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (lesson != null) lessonId = null else onBack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = SilverText) }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
          Text(if (si) "FluentPath" else "FLUENTPATH", color = CyanNeon, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
          Text(if (si) "භාෂා ප්‍රගුණතා මධ්‍යස්ථානය" else "THE LANGUAGE MASTERY LAB", color = SilverMuted, fontSize = 9.sp, letterSpacing = 1.sp)
        }
        Icon(Icons.Filled.School, null, tint = EmeraldMastery, modifier = Modifier.size(23.dp))
      }
      if (lesson == null) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(learn, color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("$xp ${if (si) "ලකුණු" else "XP"}", color = AmberAccent, fontWeight = FontWeight.Bold)
          }
          Text(if (si) "ඉගෙන ගන්නා භාෂාව තෝරන්න. සෑම පාඩමකම පැහැදිලි කිරීමක්, උදාහරණයක් සහ ප්‍රශ්න කිහිපයක් ඇත." else "Choose a language. Each lesson combines explanation, examples, active recall and a mastery check.", color = SilverMuted, fontSize = 12.sp, lineHeight = 19.sp)
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            courses.forEach { c ->
              val active = c.id == course.id
              Column(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(if (active) CyanNeon.copy(alpha = .14f) else ObsidianSurface).border(1.dp, if (active) CyanNeon else ObsidianBorder, RoundedCornerShape(12.dp)).clickable { courseId = c.id; prefs.edit().putString("course", c.id).apply() }.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(c.native, color = if (active) CyanNeon else SilverText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(c.name, color = SilverMuted, fontSize = 8.sp)
              }
            }
          }
          Text("${if (si) "ප්‍රගතිය" else "PROGRESS"} · ${course.lessons.count { it.id in done }} / ${course.lessons.size}", color = SilverText, fontSize = 12.sp)
          LinearProgressIndicator(progress = { course.lessons.count { it.id in done }.toFloat() / course.lessons.size.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(8.dp)), color = EmeraldMastery, trackColor = ObsidianBorder)
          course.lessons.forEachIndexed { i, item ->
            val mastered = item.id in done
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurfaceElevated).border(1.dp, if (mastered) EmeraldMastery else ObsidianBorder, RoundedCornerShape(18.dp)).clickable { lessonId = item.id; qIndex = 0; answer = -1; checked = false; lessonHadMistake = false }.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
              Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${if (si) "පාඩම" else "LESSON"} ${i + 1}", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(if (mastered) (if (si) "✓ සම්පූර්ණයි" else "✓ MASTERED") else if (item.id in review) (if (si) "නැවත පුහුණුව" else "REVIEW") else "＋", color = if (mastered) EmeraldMastery else AmberAccent, fontSize = 10.sp)
              }
              Text(if (si) item.si else item.en, color = SilverText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
              Text(item.phrase, color = CyanNeon, fontSize = 19.sp, fontWeight = FontWeight.Bold)
              Text(if (si) item.meaningSi else item.meaningEn, color = SilverMuted, fontSize = 12.sp)
              OutlinedButton(onClick = { lessonId = item.id; qIndex = 0; answer = -1; checked = false }, modifier = Modifier.fillMaxWidth()) { Text(if (mastered) (if (si) "නැවත පුහුණු වන්න" else "PRACTISE AGAIN") else start) }
            }
          }
          if (review.isNotEmpty()) Text(if (si) "නැවත පුහුණු විය යුතු පාඩම්: ${review.size}" else "Lessons to review: ${review.size}", color = AmberAccent, fontSize = 12.sp)
        }
      } else {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("${if (si) "පාඩම" else "LESSON"} · ${course.name}", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          Text(if (si) lesson.si else lesson.en, color = SilverText, fontSize = 22.sp, fontWeight = FontWeight.Black)
          Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurface).padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text(if (si) "අද ඉගෙන ගන්නේ" else "UNDERSTAND THE IDEA", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(if (si) lesson.ideaSi else lesson.ideaEn, color = SilverText, fontSize = 14.sp, lineHeight = 22.sp)
            Text(if (si) "වාක්‍ය රටාව" else "SENTENCE PATTERN", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(lesson.pattern, color = SilverText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
          Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurfaceElevated).border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
              Text(if (si) "උදාහරණය" else "EXAMPLE", color = EmeraldMastery, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              IconButton(onClick = { if (voiceReady) tts?.speak(lesson.phrase, TextToSpeech.QUEUE_FLUSH, Bundle(), "fluentpath") else voiceStatus = if (si) "මෙම භාෂාව සඳහා හඬක් ස්ථාපනය කර නැහැ." else "No supported voice is installed." }) { Icon(Icons.Filled.VolumeUp, "Listen", tint = CyanNeon) }
            }
            Text(lesson.phrase, color = SilverText, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text(if (si) lesson.meaningSi else lesson.meaningEn, color = SilverMuted, fontSize = 13.sp)
            if (voiceStatus.isNotBlank()) Text(voiceStatus, color = AmberAccent, fontSize = 10.sp)
          }
          Text(if (si) "දැන් ඔබේ දැනුම පරීක්ෂා කරමු" else "ACTIVE RECALL · PROVE WHAT YOU LEARNED", color = EmeraldMastery, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          Text("${if (si) "ප්‍රශ්නය" else "QUESTION"} ${qIndex + 1} / ${lesson.questions.size}", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          Text(if (si) q!!.si else q!!.en, color = SilverText, fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 23.sp)
          q.choices.forEachIndexed { index, choice ->
            val correct = checked && index == q.answer
            val wrong = checked && index == answer && index != q.answer
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(if (correct) EmeraldMastery.copy(alpha = .12f) else ObsidianSurface).border(1.dp, if (correct) EmeraldMastery else if (wrong) AmberAccent else ObsidianBorder, RoundedCornerShape(13.dp)).clickable(enabled = !checked) { answer = index }.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(choice, color = SilverText, fontSize = 15.sp)
              if (correct) Icon(Icons.Filled.Check, null, tint = EmeraldMastery)
            }
          }
          if (checked) {
            Text((if (answer == q.answer) (if (si) "නිවැරදියි. " else "Correct. ") else (if (si) "තවම නිවැරදි නැහැ. " else "Not quite. ")) + (if (si) q.whySi else q.whyEn), color = if (answer == q.answer) EmeraldMastery else AmberAccent, fontSize = 12.sp, lineHeight = 19.sp)
            Button(onClick = {
              if (qIndex < lesson.questions.lastIndex) { qIndex++; answer = -1; checked = false }
              else {
                if (answer == q.answer && !lessonHadMistake) { done = done + lesson.id; review = review - lesson.id; xp += 20 }
                else review = review + lesson.id
                prefs.edit().putStringSet("done", done).putStringSet("review", review).putInt("xp", xp).apply()
                lessonId = null; qIndex = 0; answer = -1; checked = false
              }
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)) { Text(if (qIndex < lesson.questions.lastIndex) next else finish, fontWeight = FontWeight.Bold) }
          } else {
            Button(onClick = { checked = true; if (answer != q.answer) lessonHadMistake = true }, enabled = answer >= 0, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)) { Text(check, fontWeight = FontWeight.Bold) }
          }
        }
      }
    }
  }
}
