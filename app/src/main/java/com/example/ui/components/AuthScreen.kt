package com.example.ui.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val activity = context as? Activity
  val scope = rememberCoroutineScope()
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(ObsidianSurfaceElevated)
      .border(0.8.dp, ObsidianBorder, RoundedCornerShape(16.dp))
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    if (currentUser != null) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(EmeraldMastery.copy(alpha = 0.2f))
              .border(1.dp, EmeraldMastery, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.CloudDone,
              contentDescription = null,
              tint = EmeraldMastery,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = currentUser.displayName ?: currentUser.email ?: "Cognitive Operator",
              color = SilverText,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              maxLines = 1
            )
            Text(
              text = "Cloud Firestore Sync: ACTIVE",
              color = EmeraldMastery,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        IconButton(
          onClick = {
            AuthManager.signOut(context, scope) {
              onSignOut()
            }
          },
          modifier = Modifier
            .size(36.dp)
            .testTag("auth_sign_out_button")
        ) {
          Icon(
            imageVector = Icons.Filled.ExitToApp,
            contentDescription = "Sign Out",
            tint = SilverDim,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    } else {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = AmberAccent,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Cloud Sync & Persistence",
            color = SilverMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Button(
          onClick = {
            if (activity != null) {
              isLoading = true
              errorMessage = null
              AuthManager.signInWithGoogle(
                activity = activity,
                scope = scope,
                onSuccess = { user ->
                  isLoading = false
                  onSignInSuccess(user)
                },
                onError = { err ->
                  isLoading = false
                  errorMessage = err
                },
                onCancelled = {
                  isLoading = false
                }
              )
            }
          },
          enabled = !isLoading,
          colors = ButtonDefaults.buttonColors(
            containerColor = CyanNeon,
            contentColor = ObsidianBg
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .height(38.dp)
            .testTag("google_sign_in_button")
        ) {
          if (isLoading) {
            CircularProgressIndicator(
              modifier = Modifier.size(16.dp),
              color = ObsidianBg,
              strokeWidth = 2.dp
            )
          } else {
            Text(
              text = "Sign in with Google",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = errorMessage ?: "",
          color = AmberAccent,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}
