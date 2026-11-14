package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.FeedCard
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MailTransmissionLog(
  val id: String,
  val timestamp: String,
  val subject: String,
  val recipient: String,
  val status: String
)

enum class ReportType(val title: String, val desc: String) {
  DAILY_SYNTHESIS("Daily Pinnacle Synthesis", "Full summary of the 19 Master Domains and active feed nodes"),
  SPACED_RECALL("Spaced-Repetition Deck", "Active recall cards due for 3, 7, 14, and 30-day reviews"),
  CONCEPT_COLLIDER("Cross-Domain Collider Report", "High-impact synthesis between 2 disparate knowledge vault nodes")
}

@Composable
fun VaultMailerScreen(
  vaultCards: List<FeedCard>,
  currentLanguage: LanguageSetting,
  userEmail: String = "",
  userName: String = "Dasun",
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedReport by remember { mutableStateOf(ReportType.DAILY_SYNTHESIS) }
  var isAutoScheduleActive by remember { mutableStateOf(false) }
  var lastSentMessage by remember { mutableStateOf<String?>(null) }

  val transmissionLogs = remember { mutableStateListOf<MailTransmissionLog>() }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBg)
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("vault_mailer_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      // Top Bar
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
            text = "GMAIL // VAULT MAILER",
            color = CyanNeon,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        Icon(
          imageVector = Icons.Filled.MarkEmailRead,
          contentDescription = null,
          tint = EmeraldMastery,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // User Identity Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(ObsidianSurfaceElevated)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(20.dp))
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CyanNeon.copy(alpha = 0.15f))
                .border(1.dp, CyanNeon, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = null,
                tint = CyanNeon,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = userName,
                color = SilverText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = userEmail,
                color = SilverMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(EmeraldMastery.copy(alpha = 0.15f))
              .border(0.8.dp, EmeraldMastery, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = if (userEmail.isBlank()) "SIGN IN REQUIRED" else "SIGNED IN",
              color = EmeraldMastery,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Report Selector
      Text(
        text = "DISPATCH REPORT TYPE",
        color = SilverMuted,
        fontSize = 10.sp,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ReportType.values().forEach { report ->
          val isSelected = report == selectedReport
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(if (isSelected) ObsidianSurfaceElevated else ObsidianSurface)
              .border(
                1.dp,
                if (isSelected) CyanNeon else ObsidianBorder,
                RoundedCornerShape(16.dp)
              )
              .clickable { selectedReport = report }
              .padding(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = report.title,
                  color = if (isSelected) CyanNeon else SilverText,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace
                )
                Text(
                  text = report.desc,
                  color = SilverMuted,
                  fontSize = 10.sp,
                  lineHeight = 14.sp
                )
              }
              if (isSelected) {
                Icon(
                  imageVector = Icons.Filled.CheckCircle,
                  contentDescription = null,
                  tint = CyanNeon,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Automated Schedule Toggle
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianBorder, RoundedCornerShape(16.dp))
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Schedule,
              contentDescription = null,
              tint = AmberAccent,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "AUTOMATIC SCHEDULE UNAVAILABLE",
              color = SilverText,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
          Text(
            text = "Manual email drafts only. This build does not schedule or send emails automatically.",
            color = SilverDim,
            fontSize = 10.sp
          )
        }

        Text("MANUAL", color = AmberAccent, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Open a user-reviewed email draft; the app does not claim to send it.
      ElevatedButton(
        onClick = {
          val opened = dispatchEmailDigest(
            context = context,
            userEmail = userEmail,
            userName = userName,
            reportType = selectedReport,
            cards = vaultCards
          )
          if (opened) {
            val newLog = MailTransmissionLog(
              id = "log_${System.currentTimeMillis()}",
              timestamp = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date()),
              subject = "[SKELETON VAULT]: ${selectedReport.title}",
              recipient = userEmail,
              status = "COMPOSER OPENED"
            )
            transmissionLogs.add(0, newLog)
            lastSentMessage = "Opened an email draft addressed to $userEmail. Review and send it in your email app."
          } else {
            lastSentMessage = "No email app could open the draft. Nothing was sent."
          }
        },
        enabled = userEmail.isNotBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("dispatch_mailer_button"),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = CyanNeon,
          contentColor = ObsidianBg
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Send,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (userEmail.isBlank()) "SIGN IN TO USE VAULT MAILER" else "OPEN EMAIL DRAFT",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          letterSpacing = 0.5.sp
        )
      }

      lastSentMessage?.let { msg ->
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = msg,
          color = EmeraldMastery,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Transmission Audit Log
      Text(
        text = "TRANSMISSION AUDIT LOG",
        color = SilverMuted,
        fontSize = 10.sp,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.weight(1f)
      ) {
        items(transmissionLogs) { log ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(ObsidianSurfaceElevated)
              .border(0.8.dp, ObsidianBorder, RoundedCornerShape(12.dp))
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = log.subject,
                color = SilverText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${log.timestamp} -> ${log.recipient}",
                color = SilverDim,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
              )
            }
            Text(
              text = log.status,
              color = EmeraldMastery,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

private fun dispatchEmailDigest(
  context: Context,
  userEmail: String,
  userName: String,
  reportType: ReportType,
  cards: List<FeedCard>
): Boolean {
  val subject = "[SKELETON VAULT]: ${reportType.title} for $userName"
  val bodyBuilder = StringBuilder()
  bodyBuilder.append("SKELETON // OMNISCIENT COGNITIVE VAULT REPORT\n")
  bodyBuilder.append("Recipient: $userName ($userEmail)\n")
  bodyBuilder.append("Report: ${reportType.title}\n")
  bodyBuilder.append("Generated: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n\n")
  bodyBuilder.append("=========================================\n\n")

  cards.take(6).forEachIndexed { i, card ->
    bodyBuilder.append("NODE ${i + 1}: ${card.title} [${card.branchDomain}]\n")
    bodyBuilder.append("THE ITCH:\n${card.itch}\n\n")
    bodyBuilder.append("SKELETON:\n${card.skeleton}\n\n")
    bodyBuilder.append("INTUITION:\n${card.intuition}\n\n")
    if (card.challengeScenario.isNotBlank()) {
      bodyBuilder.append("ACTIVE RECALL CHALLENGE FOR $userName:\n${card.challengeScenario}\n")
      bodyBuilder.append("EXPECTED KEY: ${card.challengeExpectedKey}\n\n")
    }
    bodyBuilder.append("-----------------------------------------\n\n")
  }

  bodyBuilder.append("End of Synthesis. Master the constraints.\n— SKELETON Cognitive Engine\n")

  val intent = Intent(Intent.ACTION_SENDTO).apply {
    data = Uri.parse("mailto:$userEmail")
    putExtra(Intent.EXTRA_SUBJECT, subject)
    putExtra(Intent.EXTRA_TEXT, bodyBuilder.toString())
  }

  try {
    context.startActivity(Intent.createChooser(intent, "Review email draft"))
    return true
  } catch (e: Exception) {
    Toast.makeText(context, "Email client not found. Nothing was sent.", Toast.LENGTH_SHORT).show()
    return false
  }
}
