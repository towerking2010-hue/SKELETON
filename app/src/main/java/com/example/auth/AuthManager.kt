package com.example.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object AuthManager {
  private const val TAG = "AuthManager"

  private fun getGoogleWebClientId(context: Context): String {
    val resourceId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
    require(resourceId != 0) { "Google Sign-In configuration missing: default_web_client_id" }
    return context.getString(resourceId)
  }

  private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
  val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

  // Firebase is optional: local SKELETON features remain usable without project config.
  private fun firebaseAuthOrNull(): FirebaseAuth? = runCatching { Firebase.auth }.getOrNull()

  fun attemptAutoSignIn(
    context: Context,
    scope: CoroutineScope,
    onSuccess: (FirebaseUser) -> Unit = {},
    onUnauthenticated: () -> Unit = {}
  ) {
    val auth = firebaseAuthOrNull()
    if (auth == null) {
      Log.i(TAG, "Firebase is not configured; continuing in local-only mode.")
      onUnauthenticated()
      return
    }
    auth.addAuthStateListener { firebaseAuth -> _currentUser.value = firebaseAuth.currentUser }
    val user = auth.currentUser
    if (user != null) {
      _currentUser.value = user
      onSuccess(user)
      return
    }

    val clientId = try {
      getGoogleWebClientId(context)
    } catch (e: Exception) {
      Log.w(TAG, "default_web_client_id not found")
      onUnauthenticated()
      return
    }

    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetGoogleIdOption.Builder()
      .setFilterByAuthorizedAccounts(true)
      .setServerClientId(clientId)
      .setAutoSelectEnabled(true)
      .build()

    val request = GetCredentialRequest.Builder()
      .addCredentialOption(googleIdOption)
      .build()

    scope.launch {
      try {
        val result = credentialManager.getCredential(context, request)
        val credential = result.credential
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
          val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
          val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
          val authResult = auth.signInWithCredential(authCredential).await()
          authResult.user?.let {
            _currentUser.value = it
            onSuccess(it)
          } ?: onUnauthenticated()
        } else {
          onUnauthenticated()
        }
      } catch (e: Exception) {
        onUnauthenticated()
      }
    }
  }

  fun signInWithGoogle(
    activity: Activity,
    scope: CoroutineScope,
    onSuccess: (FirebaseUser) -> Unit,
    onError: (String) -> Unit,
    onCancelled: () -> Unit = {}
  ) {
    val clientId = try {
      getGoogleWebClientId(activity)
    } catch (e: Exception) {
      onError("Google Sign-In configuration missing: default_web_client_id")
      return
    }

    val auth = firebaseAuthOrNull()
    if (auth == null) {
      onError("Cloud sign-in is unavailable because Firebase is not configured. Local learning still works.")
      return
    }

    val credentialManager = CredentialManager.create(activity)
    val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
    val request = GetCredentialRequest.Builder()
      .addCredentialOption(signInOption)
      .build()

    scope.launch {
      try {
        val result = credentialManager.getCredential(activity, request)
        val credential = result.credential
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
          val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
          val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
          val authResult = auth.signInWithCredential(authCredential).await()
          val signedInUser = authResult.user
          if (signedInUser != null) {
            _currentUser.value = signedInUser
            onSuccess(signedInUser)
          } else {
            onError("Authentication failed: User empty")
          }
        } else {
          onError("Unexpected credential type returned")
        }
      } catch (e: GetCredentialCancellationException) {
        Log.w(TAG, "Google Sign-In flow cancelled: ${e.message}")
        onCancelled()
      } catch (e: Exception) {
        Log.e(TAG, "Google Sign-In failed", e)
        onError(e.localizedMessage ?: "Sign-in error")
      }
    }
  }


  fun signInWithEmail(
    email: String,
    password: String,
    displayName: String,
    createAccount: Boolean,
    scope: CoroutineScope,
    onSuccess: (FirebaseUser) -> Unit,
    onError: (String) -> Unit
  ) {
    val auth = firebaseAuthOrNull()
    if (auth == null) {
      onError("Email sign-in needs SKELETON's Firebase project configuration. Local profile mode is still available.")
      return
    }
    if (email.isBlank() || password.length < 6) {
      onError("Enter a valid email and a password with at least 6 characters.")
      return
    }
    scope.launch {
      try {
        val result = if (createAccount) {
          auth.createUserWithEmailAndPassword(email.trim(), password).await()
        } else {
          auth.signInWithEmailAndPassword(email.trim(), password).await()
        }
        val user = result.user ?: throw IllegalStateException("Authentication returned no user.")
        if (createAccount && displayName.isNotBlank()) {
          user.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(displayName.trim()).build()
          ).await()
        }
        _currentUser.value = user
        onSuccess(user)
      } catch (error: Exception) {
        onError(error.localizedMessage ?: "Email sign-in failed. Check your details and try again.")
      }
    }
  }

  fun signOut(context: Context, scope: CoroutineScope, onComplete: () -> Unit = {}) {
    firebaseAuthOrNull()?.signOut()
    _currentUser.value = null
    val credentialManager = CredentialManager.create(context)
    scope.launch {
      try {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
      } catch (e: Exception) {
        Log.e(TAG, "Error clearing credential state", e)
      } finally {
        onComplete()
      }
    }
  }
}
