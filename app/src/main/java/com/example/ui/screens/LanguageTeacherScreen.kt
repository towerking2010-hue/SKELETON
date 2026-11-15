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
import com.example.engine.learning.FluentPathProgressState
import com.example.engine.learning.FluentPathProgression
import com.example.engine.learning.FluentPathLocalization
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
    BhLesson("ja3","Ask a yes-or-no question","ඔව් හෝ නැහැ යනුවෙන් පිළිතුරු දිය හැකි ප්‍රශ්නයක් අසමු","これは本ですか。","Is this a book?","මේක පොතක්ද?","か is placed at the end of a polite statement to turn it into a yes-or-no question. The sentence structure stays familiar; the ending signals the question.","です යන ආචාරශීලී ප්‍රකාශයක අගට か යෙදීමෙන් ඔව් හෝ නැහැ යනුවෙන් පිළිතුරු දිය හැකි ප්‍රශ්නයක් සෑදේ. වාක්‍යයේ රටාව බොහෝ දුරට එලෙසම පවතින අතර අග කොටස ප්‍රශ්නයක් බව පෙන්වයි.","Statement + か",listOf(
      BhQuestion("Which particle turns this polite statement into a yes-or-no question?","මෙම ආචාරශීලී ප්‍රකාශය ඔව්/නැහැ ප්‍රශ්නයක් කරන්නේ කුමන නිපාතයෙන්ද?",listOf("か","は","を","に"),0,"か marks a yes-or-no question at the end of the sentence.","වාක්‍යයේ අගට යෙදෙන か ඔව් හෝ නැහැ ප්‍රශ්නයක් බව පෙන්වයි."),
      BhQuestion("Where is か usually placed in this pattern?","මෙම රටාවේ か සාමාන්‍යයෙන් යෙදෙන්නේ කොතැනද?",listOf("At the beginning","After the noun only","At the end","Before the verb"),2,"In this polite pattern, か comes at the end.","මෙම ආචාරශීලී රටාවේ か වාක්‍යයේ අගට යෙදේ."))),
    BhLesson("ja4","Talk about yesterday","ඊයේ සිදු වූ දෙයක් කියමු","昨日、映画を見ました。","I watched a film yesterday.","මම ඊයේ චිත්‍රපටයක් බැලුවා.","The polite past form 見ました expresses a completed action. 昨日 gives the time, を marks the object, and the verb comes last.","見ました යන ආචාරශීලී අතීත රූපයෙන් අවසන් වූ ක්‍රියාවක් පෙන්වයි. 昨日 කාලය දක්වයි, を කර්මය පෙන්වයි, ක්‍රියාව වාක්‍යයේ අගට යෙදේ.","Time + object + verb (past)",listOf(
      BhQuestion("Which part means yesterday?","ඊයේ යන අර්ථය දෙන්නේ කුමන කොටසද?",listOf("昨日","映画","を","見ました"),0,"昨日 means yesterday.","昨日 යන්නෙහි අර්ථය ඊයේ යන්නයි."),
      BhQuestion("Which verb form expresses the polite past here?","මෙහි ආචාරශීලී අතීත ක්‍රියාව පෙන්වන්නේ කුමන කොටසද?",listOf("見ます","見ました","見ません","見ますか"),1,"見ました is the polite past form of 見ます.","見ました යනු 見ます ක්‍රියාපදයේ ආචාරශීලී අතීත රූපයයි.")))
  )),
  BhCourse("de","German","Deutsch",Locale.GERMANY,listOf(
    BhLesson("de1","The verb takes position two","ක්‍රියාව දෙවන ස්ථානයට","Ich lerne heute Deutsch.","I am learning German today.","මම අද ජර්මන් භාෂාව ඉගෙන ගන්නවා.","In a normal German main statement, the conjugated verb usually occupies position two. The first position can change; the verb stays second.","ජර්මන් සාමාන්‍ය ප්‍රකාශ වාක්‍යයක ප්‍රධාන ක්‍රියාපදය බොහෝවිට දෙවන ස්ථානයේ තියෙනවා. මුල් ස්ථානය වෙනස් කළත් ක්‍රියාපදය දෙවන තැන රැකගන්නවා.","Position 1 + verb + rest",listOf(
      BhQuestion("Where is the finite verb in a main statement?","සාමාන්‍ය ප්‍රකාශ වාක්‍යයක ප්‍රධාන ක්‍රියාපදය තිබෙන්නේ කොතැනද?",listOf("Position 1","Position 2","Always last","Position 4"),1,"German main clauses commonly use V2 order.","ජර්මන් ප්‍රධාන වාක්‍යවල ක්‍රියාපදය සාමාන්‍යයෙන් දෙවන තැනයි."),
      BhQuestion("Can a time phrase take position one?","කාලය දක්වන පදයක් මුල් තැනට ගත හැකිද?",listOf("Yes","Never","Only questions","Only past"),0,"Example: Heute lerne ich Deutsch.","උදාහරණයක්: Heute lerne ich Deutsch.")))
    BhLesson("de2","Mark the direct object","සෘජු කර්මය හඳුනාගමු","Ich sehe den Hund.","I see the dog.","මම බල්ලා දකිනවා.","In German, a masculine singular definite article changes from der to den when it marks a direct object in the accusative case.","ජර්මන් භාෂාවේ පුරුෂ ලිංග ඒකවචන නියත උපපදය සෘජු කර්මය ලෙස යෙදෙන විට der සිට den ලෙස වෙනස් වේ. මෙය කර්මකාරක විභක්තියයි.","Subject + verb + accusative object",listOf(
      BhQuestion("Which form marks the masculine direct object here?","මෙහි පුරුෂ ලිංග සෘජු කර්මය පෙන්වන්නේ කුමන රූපයෙන්ද?",listOf("der","den","die","das"),1,"den is the masculine accusative form in this sentence.","මෙම වාක්‍යයේ den යනු පුරුෂ ලිංග කර්මකාරක රූපයයි."),
      BhQuestion("What is the role of den Hund?","den Hund යන කොටසෙහි ව්‍යාකරණ භූමිකාව කුමක්ද?",listOf("Subject","Direct object","Verb","Time phrase"),1,"den Hund is the direct object of sehe.","den Hund යනු sehe ක්‍රියාවට ලක්වන සෘජු කර්මයයි."))),
    BhLesson("de3","Say that you do not have something","යමක් නොමැති බව කියමු","Ich habe kein Buch.","I do not have a book.","මට පොතක් නැහැ.","kein is used before a noun to negate an indefinite noun phrase. Its ending changes to agree with the noun's grammatical gender, number and case.","kein යන්න නාම පදයකට පෙර යෙදී එම නාම පදය නොමැති බව පෙන්වයි. නාම පදයේ ලිංගය, වචන සංඛ්‍යාව සහ විභක්තිය අනුව එහි අවසානය වෙනස් වේ.","Subject + verb + kein(e) + noun",listOf(
      BhQuestion("Which word negates Buch in this sentence?","මෙම වාක්‍යයේ Buch යන්න නැතැයි පෙන්වන්නේ කුමන වචනයද?",listOf("kein","nicht","den","der"),0,"kein negates the noun Buch in this example.","මෙම උදාහරණයේ kein යන්න Buch නාම පදය නැතැයි පෙන්වයි."),
      BhQuestion("What does kein change to match?","kein යන්න වෙනස් වන්නේ කුමකට ගැළපීමටද?",listOf("Only the verb tense","The noun's gender, number and case","Only the sentence length","The speaker's age"),1,"The form of kein agrees with the noun it accompanies.","kein හි රූපය එය සමඟ යෙදෙන නාම පදයේ ව්‍යාකරණ ලක්ෂණවලට ගැළපේ.")))
  )),
  BhCourse("el","Ancient Greek","Ἑλληνική",Locale("el","GR"),listOf(
    BhLesson("el1","Recognise the subject","කර්තෘ හඳුනාගමු","ὁ ἄνθρωπος","the person","පුද්ගලයා","Greek noun endings carry grammatical information. The nominative form commonly identifies the subject.","ග්‍රීක නාම පදවල අවසානයෙන් ව්‍යාකරණ තොරතුරු ලැබෙනවා. නාමකාරක රූපය සාමාන්‍යයෙන් කර්තෘ හඳුන්වනවා.","Nominative = subject",listOf(
      BhQuestion("Which case commonly marks the subject?","වාක්‍යයක කර්තෘ සාමාන්‍යයෙන් හඳුන්වන්නේ කුමන විභක්තියෙන්ද?",listOf("Genitive","Accusative","Nominative","Vocative"),2,"The nominative commonly marks the subject.","නාමකාරක විභක්තිය සාමාන්‍යයෙන් කර්තෘ හඳුන්වනවා."),
      BhQuestion("What carries grammatical role information?","ව්‍යාකරණ භූමිකාව පෙන්වන්නේ බොහෝවිට කුමක්ද?",listOf("Word endings","Word colour","Punctuation","Word length"),0,"Inflectional endings carry grammatical roles.","වචනවල විභක්ති අවසාන කොටස් ව්‍යාකරණ භූමිකා පෙන්වනවා.")))
    BhLesson("el2","Recognise possession","අයිතිය හඳුනාගමු","τοῦ ἀνθρώπου","of the person","පුද්ගලයාගේ","The genitive case commonly expresses possession or a relationship. The article and noun ending together help identify the grammatical role.","සම්බන්ධක විභක්තියෙන් බොහෝවිට අයිතියක් හෝ සම්බන්ධතාවයක් පෙන්වයි. උපපදයේ සහ නාම පදයේ අවසාන කොටස් එකට සලකා ව්‍යාකරණ භූමිකාව හඳුනාගත හැක.","Genitive = relationship / possession",listOf(
      BhQuestion("Which case commonly expresses possession?","අයිතියක් පෙන්වීමට සාමාන්‍යයෙන් යෙදෙන්නේ කුමන විභක්තියද?",listOf("Genitive","Accusative","Nominative","Vocative"),0,"The genitive commonly marks possession or relationship.","සම්බන්ධක විභක්තියෙන් සාමාන්‍යයෙන් අයිතිය හෝ සම්බන්ධතාවය පෙන්වයි."),
      BhQuestion("What helps identify the case in an inflected language?","විභක්ති සහිත භාෂාවක විභක්තිය හඳුනාගැනීමට උපකාරී වන්නේ කුමක්ද?",listOf("The ending of the word","The word's colour","The number of letters only","The font size"),0,"Word endings and their agreement patterns carry grammatical information.","වචන අවසාන කොටස් සහ ඒවායේ ගැළපීම් ව්‍යාකරණ තොරතුරු දරයි."))),
    BhLesson("el3","Read a present-tense verb","වර්තමාන ක්‍රියාපදයක් කියවමු","λύει","he / she / it loosens","ඔහු / ඇය / එය ලිහිල් කරයි","λύει is a present active indicative third-person singular form. The verb ending helps identify who performs the action.","λύει යනු වර්තමාන කාලයේ සක්‍රිය ප්‍රකාශක තුන්වන පුද්ගල ඒකවචන රූපයකි. ක්‍රියාපදයේ අවසානයෙන් ක්‍රියාව කරන්නේ කවුද යන්න හඳුනාගැනීමට උපකාරී වේ.","Stem + personal ending",listOf(
      BhQuestion("What person does λύει represent in this basic paradigm?","මෙම මූලික ක්‍රියා රටාවේ λύει නිරූපණය කරන්නේ කුමන පුද්ගලයාද?",listOf("First person","Second person","Third person","Plural"),2,"λύει is third-person singular in the present active indicative.","λύει යනු වර්තමාන සක්‍රිය ප්‍රකාශක රූපයේ තුන්වන පුද්ගල ඒකවචනයයි."),
      BhQuestion("What can a verb ending help identify?","ක්‍රියාපද අවසානයෙන් හඳුනාගත හැක්කේ කුමක්ද?",listOf("Person and number","Word colour","Page number","Only noun gender"),0,"Verb endings can encode person and number, among other grammatical features.","ක්‍රියාපද අවසානයෙන් පුද්ගලයා සහ වචන සංඛ්‍යාව වැනි ව්‍යාකරණ ලක්ෂණ පෙන්විය හැක.")))
  )),
  BhCourse("la","Latin","Latina",Locale("la","VA"),listOf(
    BhLesson("la1","Find the subject by its ending","වචන අවසානයෙන් කර්තෘ සොයමු","Puella aquam portat.","The girl carries water.","ගැහැණු ළමයා වතුර රැගෙන යනවා.","Latin endings identify grammatical roles. Puella is nominative singular here, so it is the subject even though word order can vary.","ලතින් භාෂාවේ වචන අවසාන කොටස් ව්‍යාකරණ භූමිකා පෙන්වනවා. මෙහි Puella නාමකාරක ඒකවචනය නිසා කර්තෘයි. වචන පිළිවෙළ වෙනස් වුණත් එහි භූමිකාව එලෙසම පවතිනවා.","Nominative = subject · Accusative = object",listOf(
      BhQuestion("Which word is the subject?","මෙම වාක්‍යයේ කර්තෘ වන්නේ කුමන වචනයද?",listOf("Puella","Aquam","Portat","None"),0,"Puella is nominative and is the subject.","Puella නාමකාරක නිසා එය කර්තෘයි."),
      BhQuestion("Which case commonly marks the direct object?","සෘජු කර්මය හඳුන්වන්නේ කුමන විභක්තියෙන්ද?",listOf("Nominative","Accusative","Vocative","Genitive"),1,"The accusative commonly marks the direct object.","කර්මකාරක විභක්තියෙන් සාමාන්‍යයෙන් සෘජු කර්මය හඳුන්වනවා.")))
    BhLesson("la2","Read the verb ending","ක්‍රියාපද අවසානය කියවමු","Puella cantat.","The girl sings.","ගැහැණු ළමයා ගී ගයනවා.","The ending -t marks a third-person singular verb in this common present-tense pattern. The verb form gives information about its subject.","මෙම සාමාන්‍ය වර්තමාන රටාවේ -t අවසානයෙන් තුන්වන පුද්ගල ඒකවචන ක්‍රියාපදයක් පෙන්වයි. ක්‍රියාපදයේ රූපයෙන් එහි කර්තෘ පිළිබඳ තොරතුරු ලැබේ.","Subject + verb (3rd person singular)",listOf(
      BhQuestion("Which part means 'sings'?","ගී ගයයි යන අර්ථය දෙන්නේ කුමන කොටසද?",listOf("Puella","cantat","aqua","bonus"),1,"cantat is the verb meaning sings.","cantat යනු ගී ගයයි යන අර්ථය ඇති ක්‍රියාපදයයි."),
      BhQuestion("What does -t commonly mark in this present pattern?","මෙම වර්තමාන රටාවේ -t අවසානයෙන් සාමාන්‍යයෙන් පෙන්වන්නේ කුමක්ද?",listOf("First-person singular","Second-person singular","Third-person singular","Plural"),2,"-t marks the third-person singular form in this example.","මෙම උදාහරණයේ -t අවසානයෙන් තුන්වන පුද්ගල ඒකවචනය පෙන්වයි."))),
    BhLesson("la3","Make adjectives agree","විශේෂණය ගැළපෙන ලෙස යොදමු","bona puella","a good girl","හොඳ ගැහැණු ළමයෙක්","In Latin, an adjective agrees with the noun it describes in gender, number and case. The forms must fit together grammatically.","ලතින් භාෂාවේ විශේෂණ පදය එය විස්තර කරන නාම පදයේ ලිංගය, වචන සංඛ්‍යාව සහ විභක්තියට ගැළපේ. වචන දෙකේ රූප ව්‍යාකරණානුකූලව එකිනෙකට ගැළපිය යුතුය.","Adjective agrees with noun",listOf(
      BhQuestion("What does a Latin adjective agree with?","ලතින් විශේෂණ පදය ගැළපෙන්නේ කුමක් සමඟද?",listOf("The noun it describes","Only the verb","The sentence length","The punctuation"),0,"The adjective agrees with its noun in gender, number and case.","විශේෂණය එය විස්තර කරන නාම පදයේ ලිංගය, වචන සංඛ්‍යාව සහ විභක්තියට ගැළපේ."),
      BhQuestion("Which features are involved in this agreement?","මෙම ගැළපීමට අදාළ ව්‍යාකරණ ලක්ෂණ මොනවාද?",listOf("Gender, number and case","Only word order","Only the first letter","Nothing"),0,"Gender, number and case determine the matching form.","ලිංගය, වචන සංඛ්‍යාව සහ විභක්තිය අනුව ගැළපෙන රූපය තීරණය වේ.")))
  )),
  BhCourse("si","Sinhala","සිංහල",Locale("si","LK"),listOf(
    BhLesson("si1","Build a clear sentence","පැහැදිලි වාක්‍යයක් ගොඩනඟමු","මම පොත කියවනවා.","I am reading a book.","මම පොත කියවනවා.","Sinhala commonly places the verb at the end. A sentence tells us who acts, what is affected and what action happens.","සිංහල වාක්‍යයක ක්‍රියාව බොහෝවිට අගට යෙදෙනවා. කවුද ක්‍රියාව කරන්නේ, කුමකටද එය බලපාන්නේ, කුමක්ද සිදුවන්නේ යන්න වාක්‍යයෙන් කියවෙනවා.","කර්තෘ + කර්මය + ක්‍රියාව",listOf(
      BhQuestion("Which part tells us the action?","ක්‍රියාව සිදුවන බව කියන්නේ කුමන කොටසද?",listOf("කර්තෘ","කර්මය","ක්‍රියාව","විශේෂණය"),2,"The verb expresses the action.","ක්‍රියා පදය ක්‍රියාව හෝ සිදුවීම පෙන්වනවා."),
      BhQuestion("Where does the verb commonly appear?","සිංහල වාක්‍යයක ක්‍රියාව බොහෝවිට යෙදෙන්නේ කොතැනද?",listOf("මුල","මැද","අග","සෑම විටම නැත"),2,"Sinhala commonly uses verb-final order.","සිංහලයේ බොහෝවිට ක්‍රියාව වාක්‍යයේ අගට යෙදෙනවා.")))
    BhLesson("si2","Talk about yesterday","ඊයේ සිදු වූ දෙයක් කියමු","මම ඊයේ පොත කියෙව්වා.","I read a book yesterday.","මම ඊයේ පොත කියෙව්වා.","The verb කියෙව්වා expresses a completed past action. The time word ඊයේ places the event in the past.","කියෙව්වා යන ක්‍රියා රූපයෙන් අවසන් වූ අතීත ක්‍රියාවක් පෙන්වයි. ඊයේ යන කාල පදය සිදුවීම අතීතයට අයත් බව පැහැදිලි කරයි.","කාලය + කර්මය + ක්‍රියාව",listOf(
      BhQuestion("Which word places the event in the past?","සිදුවීම අතීතයේ බව පෙන්වන්නේ කුමන වචනයද?",listOf("මම","ඊයේ","පොත","කියෙව්වා"),1,"ඊයේ means yesterday.","ඊයේ යන්නෙන් yesterday යන අර්ථය ලැබේ."),
      BhQuestion("Which word expresses the completed action?","අවසන් වූ ක්‍රියාව පෙන්වන්නේ කුමන වචනයද?",listOf("මම","ඊයේ","පොත","කියෙව්වා"),3,"කියෙව්වා is the past-tense verb in this sentence.","කියෙව්වා යනු මෙම වාක්‍යයේ අතීත කාල ක්‍රියාපදයයි."))),
    BhLesson("si3","Ask where someone is going","කෙනෙකු යන්නේ කොහේදැයි අසමු","ඔයා කොහෙද යන්නේ?","Where are you going?","ඔයා කොහෙද යන්නේ?","කොහෙද asks about a place. The question word appears before the verb phrase in this natural spoken Sinhala question.","කොහෙද යන්නෙන් ස්ථානයක් ගැන විමසයි. ස්වභාවික කථන සිංහලයේ මෙම ප්‍රශ්න පදය ක්‍රියා කොටසට පෙර යෙදේ.","කර්තෘ + ස්ථානය ගැන ප්‍රශ්නය + ක්‍රියාව",listOf(
      BhQuestion("Which word asks about a place?","ස්ථානයක් ගැන විමසන්නේ කුමන වචනයෙන්ද?",listOf("ඔයා","කොහෙද","යන්නේ","ද"),1,"කොහෙද asks where.","කොහෙද යන්නෙන් කොතැනදැයි අසයි."),
      BhQuestion("Which punctuation mark ends this written question?","මෙම ලිඛිත ප්‍රශ්නයේ අගට යෙදෙන්නේ කුමන විරාම ලකුණද?",listOf("ප්‍රශ්නාර්ථ ලකුණ","කොමාව","තිත","අර්ධ විරාමය"),0,"A question mark marks the written question.","ලිඛිත ප්‍රශ්නයක අගට ප්‍රශ්නාර්ථ ලකුණ යෙදේ.")))
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
                Text(FluentPathLocalization.courseName(c.id, c.name, currentLanguage), color = SilverMuted, fontSize = 8.sp)
              }
            }
          }
          Text("${if (si) "ප්‍රගතිය" else "PROGRESS"} · ${course.lessons.count { it.id in done && it.id !in review }} / ${course.lessons.size}", color = SilverText, fontSize = 12.sp)
          LinearProgressIndicator(progress = { course.lessons.count { it.id in done }.toFloat() / course.lessons.size.coerceAtLeast(1) }, modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(8.dp)), color = EmeraldMastery, trackColor = ObsidianBorder)
          course.lessons.forEachIndexed { i, item ->
            val needsReview = item.id in review
            val mastered = item.id in done && !needsReview
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurfaceElevated).border(1.dp, if (mastered) EmeraldMastery else if (needsReview) AmberAccent else ObsidianBorder, RoundedCornerShape(18.dp)).clickable { lessonId = item.id; qIndex = 0; answer = -1; checked = false; lessonHadMistake = false }.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
              Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${if (si) "පාඩම" else "LESSON"} ${i + 1}", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(if (needsReview) (if (si) "නැවත පුහුණුව" else "REVIEW") else if (mastered) (if (si) "✓ සම්පූර්ණයි" else "✓ MASTERED") else "＋", color = if (mastered) EmeraldMastery else AmberAccent, fontSize = 10.sp)
              }
              Text(if (si) item.si else item.en, color = SilverText, fontSize = 17.sp, fontWeight = FontWeight.Bold)
              Text(item.phrase, color = CyanNeon, fontSize = 19.sp, fontWeight = FontWeight.Bold)
              Text(if (si) item.meaningSi else item.meaningEn, color = SilverMuted, fontSize = 12.sp)
              OutlinedButton(onClick = { lessonId = item.id; qIndex = 0; answer = -1; checked = false; lessonHadMistake = false }, modifier = Modifier.fillMaxWidth()) { Text(if (mastered || needsReview) (if (si) "නැවත පුහුණු වන්න" else "PRACTISE AGAIN") else start) }
            }
          }
          if (review.isNotEmpty()) Text(if (si) "නැවත පුහුණු විය යුතු පාඩම්: ${review.size}" else "Lessons to review: ${review.size}", color = AmberAccent, fontSize = 12.sp)
        }
      } else {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("${if (si) "පාඩම" else "LESSON"} · ${FluentPathLocalization.courseName(course.id, course.name, currentLanguage)}", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          Text(if (si) lesson.si else lesson.en, color = SilverText, fontSize = 22.sp, fontWeight = FontWeight.Black)
          Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(ObsidianSurface).padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text(if (si) "අද ඉගෙන ගන්නේ" else "UNDERSTAND THE IDEA", color = AmberAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(if (si) lesson.ideaSi else lesson.ideaEn, color = SilverText, fontSize = 14.sp, lineHeight = 22.sp)
            Text(if (si) "වාක්‍ය රටාව" else "SENTENCE PATTERN", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(FluentPathLocalization.pattern(lesson.pattern, currentLanguage), color = SilverText, fontSize = 15.sp, fontWeight = FontWeight.Bold)
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
              Text(FluentPathLocalization.choice(choice, currentLanguage), color = SilverText, fontSize = 15.sp)
              if (correct) Icon(Icons.Filled.Check, null, tint = EmeraldMastery)
            }
          }
          if (checked) {
            Text((if (answer == q.answer) (if (si) "නිවැරදියි. " else "Correct. ") else (if (si) "තවම නිවැරදි නැහැ. " else "Not quite. ")) + (if (si) q.whySi else q.whyEn), color = if (answer == q.answer) EmeraldMastery else AmberAccent, fontSize = 12.sp, lineHeight = 19.sp)
            Button(onClick = {
              if (qIndex < lesson.questions.lastIndex) { qIndex++; answer = -1; checked = false }
              else {
                val completion = FluentPathProgression.completeLesson(
                  current = FluentPathProgressState(xp = xp, masteredLessonIds = done, reviewLessonIds = review),
                  lessonId = lesson.id,
                  allAnswersCorrect = answer == q.answer && !lessonHadMistake
                )
                xp = completion.state.xp
                done = completion.state.masteredLessonIds
                review = completion.state.reviewLessonIds
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
