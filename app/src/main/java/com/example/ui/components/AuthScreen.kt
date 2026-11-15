package com.example.ui.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.auth.AuthManager
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
import com.google.firebase.auth.FirebaseUser

@Composable
fun AuthStatusBar(
  currentUser: FirebaseUser?,
  onSignInSuccess: (FirebaseUser) -> Unit,
  onSignOut: () -> Unit,
  onOpenEmailSignIn: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val activity = context as? Activity
  val scope = rememberCoroutineScope()
  var showSignIn by remember { mutableStateOf(false) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  if (currentUser != null) {
    Row(
      modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
        .background(ObsidianSurfaceElevated).border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(Modifier.weight(1f)) {
        Text(currentUser.displayName?.takeIf { it.isNotBlank() } ?: currentUser.email ?: "SKELETON account",
          color = SilverText, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(currentUser.email ?: "Account connected", color = EmeraldMastery, fontSize = 10.sp, maxLines = 1)
      }
      IconButton(onClick = { AuthManager.signOut(context, scope, onSignOut) }, modifier = Modifier.size(34.dp).testTag("auth_sign_out_button")) {
        Icon(Icons.Filled.ExitToApp, contentDescription = "Sign out", tint = SilverDim)
      }
    }
  } else {
    Row(
      modifier = modifier.clip(RoundedCornerShape(14.dp)).background(ObsidianSurfaceElevated)
        .border(1.dp, ObsidianBorder, RoundedCornerShape(14.dp))
        .clickable { showSignIn = true }
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
      Icon(Icons.Filled.Lock, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(16.dp))
      Text("Sign in", color = SilverText, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
      Spacer(Modifier.weight(1f))
      Text("Sync your progress", color = SilverMuted, fontSize = 10.sp)
    }
    if (showSignIn) {
      androidx.compose.material3.AlertDialog(
        onDismissRequest = { if (!isLoading) showSignIn = false },
        containerColor = ObsidianSurface,
        title = { Text("Sign in to SKELETON", color = SilverText, fontWeight = FontWeight.Bold) },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Save your learning progress and access your account across devices.", color = SilverMuted, fontSize = 12.sp)
            Button(
              onClick = {
                if (activity == null) {
                  errorMessage = "Google sign-in needs an Android activity."
                } else {
                  isLoading = true
                  errorMessage = null
                  AuthManager.signInWithGoogle(activity, scope,
                    onSuccess = { user -> isLoading = false; showSignIn = false; onSignInSuccess(user) },
                    onError = { message -> isLoading = false; errorMessage = message },
                    onCancelled = { isLoading = false })
                }
              },
              enabled = !isLoading,
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = ObsidianBg)
            ) {
              if (isLoading) CircularProgressIndicator(Modifier.size(18.dp), color = ObsidianBg, strokeWidth = 2.dp)
              else Text("Continue with Google", fontWeight = FontWeight.Bold)
            }
            Button(
              onClick = { showSignIn = false; onOpenEmailSignIn() },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = ObsidianBorder, contentColor = SilverText)
            ) { Text("Continue with email") }
            Button(
              onClick = { errorMessage = "Apple sign-in is not configured for this Android app yet." },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = ObsidianBorder, contentColor = SilverText)
            ) { Text("Continue with Apple") }
            errorMessage?.let { Text(it, color = AmberAccent, fontSize = 11.sp) }
          }
        },
        confirmButton = {
          androidx.compose.material3.TextButton(onClick = { showSignIn = false }, enabled = !isLoading) {
            Text("Not now", color = SilverMuted)
          }
        }
      )
    }
  }
}
