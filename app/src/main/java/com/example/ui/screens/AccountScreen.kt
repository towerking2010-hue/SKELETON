package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ObsidianBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.SilverDim
import com.example.ui.theme.SilverMuted
import com.example.ui.theme.SilverText

@Composable
fun AccountScreen(
  profileName: String,
  signedInEmail: String?,
  onSaveName: (String) -> Unit,
  onAuthenticate: (String, String, String, Boolean, () -> Unit, (String) -> Unit) -> Unit,
  onSignOut: () -> Unit,
  onBack: () -> Unit
) {
  var name by remember(profileName) { mutableStateOf(profileName) }
  var email by remember { mutableStateOf(signedInEmail.orEmpty()) }
  var password by remember { mutableStateOf("") }
  var createAccount by remember { mutableStateOf(false) }
  var status by remember { mutableStateOf<String?>(null) }
  var busy by remember { mutableStateOf(false) }

  Column(
    Modifier.fillMaxSize().background(ObsidianBg).padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(13.dp)
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("SKELETON IDENTITY", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace, letterSpacing = 1.8.sp)
        Text("Your name. Your progress. Your space.", color = SilverMuted, fontSize = 12.sp)
      }
      Text("BACK", color = SilverMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
    }

    Column(
      Modifier.fillMaxWidth().background(ObsidianSurface, RoundedCornerShape(18.dp))
        .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp)).padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text("YOUR PERSONAL PROFILE", color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
      Text("This is the name SKELETON uses in greetings, including THE ARENA. You can use the app without creating an account.", color = SilverDim, fontSize = 11.sp, lineHeight = 16.sp)
      OutlinedTextField(
        value = name, onValueChange = { name = it.take(40); status = null },
        modifier = Modifier.fillMaxWidth(), label = { Text("What should SKELETON call you?") },
        singleLine = true,
        colors = accountFieldColors()
      )
      Button(
        onClick = { onSaveName(name); status = "Profile name saved on this device." },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)
      ) { Text("SAVE MY NAME", fontWeight = FontWeight.Bold) }
    }

    Column(
      Modifier.fillMaxWidth().background(ObsidianSurface, RoundedCornerShape(18.dp))
        .border(1.dp, ObsidianBorder, RoundedCornerShape(18.dp)).padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text("EMAIL ACCOUNT", color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
      if (signedInEmail != null) {
        Text("SIGNED IN", color = SilverMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
        Text(signedInEmail, color = SilverText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text("Your account is connected. Local learning remains available if you sign out.", color = SilverDim, fontSize = 11.sp)
        Button(onClick = onSignOut, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = ObsidianBorder, contentColor = SilverText)) {
          Text("SIGN OUT")
        }
      } else {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(
            onClick = { createAccount = false; status = null },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = if (!createAccount) CyanNeon else ObsidianBorder, contentColor = if (!createAccount) ObsidianBg else SilverText)
          ) { Text("SIGN IN") }
          Button(
            onClick = { createAccount = true; status = null },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = if (createAccount) CyanNeon else ObsidianBorder, contentColor = if (createAccount) ObsidianBg else SilverText)
          ) { Text("CREATE ACCOUNT") }
        }
        if (createAccount) {
          OutlinedTextField(value = name, onValueChange = { name = it.take(40) }, modifier = Modifier.fillMaxWidth(), label = { Text("Display name") }, singleLine = true, colors = accountFieldColors())
        }
        OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Email") }, singleLine = true, colors = accountFieldColors())
        OutlinedTextField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Password · 6+ characters") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), colors = accountFieldColors())
        Button(
          onClick = {
            busy = true
            status = null
            onAuthenticate(email.trim(), password, name.trim(), createAccount, {
              busy = false
              password = ""
              status = "You're signed in. Welcome to SKELETON."
            }, { message ->
              busy = false
              status = message
            })
          },
          enabled = !busy && email.isNotBlank() && password.length >= 6 && (!createAccount || name.isNotBlank()),
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)
        ) { Text(if (busy) "CONNECTING…" else if (createAccount) "CREATE MY ACCOUNT" else "SIGN IN WITH EMAIL", fontWeight = FontWeight.Bold) }
        Text("Email/password sign-in requires the SKELETON Firebase project to be configured and Email/Password enabled. It does not unlock paid AI or change the zero-billing policy.", color = SilverDim, fontSize = 10.sp, lineHeight = 15.sp)
      }
      status?.let { Text(it, color = if (it.contains("failed", true) || it.contains("needs", true) || it.contains("enter", true)) Color(0xFFFFB36B) else CyanNeon, fontSize = 11.sp) }
    }

    Spacer(Modifier.height(4.dp))
    Text("WEEKLY REPORTS: account identity is the first step. Automatic email delivery needs a trusted sending service and explicit opt-in; SKELETON will not send your learning data without permission.", color = SilverDim, fontSize = 10.sp, lineHeight = 15.sp)
  }
}

@Composable
private fun accountFieldColors() = OutlinedTextFieldDefaults.colors(
  focusedTextColor = SilverText,
  unfocusedTextColor = SilverText,
  focusedBorderColor = CyanNeon,
  unfocusedBorderColor = ObsidianBorder,
  focusedLabelColor = CyanNeon,
  unfocusedLabelColor = SilverMuted,
  cursorColor = CyanNeon
)
