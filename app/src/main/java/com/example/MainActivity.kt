package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.SkeletonApp
import com.example.ui.SkeletonViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBg

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    com.example.util.configureAppCheck(this, intent)
    enableEdgeToEdge()
    setContent {
      val viewModel: SkeletonViewModel = viewModel()
      val adrenalMode by viewModel.adrenalMode.collectAsState()

      MyApplicationTheme(adrenalMode = adrenalMode) {
        Surface(
          modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
          color = ObsidianBg
        ) {
          SkeletonApp(viewModel = viewModel)
        }
      }
    }
  }
}
