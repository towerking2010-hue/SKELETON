package com.example.data.model

enum class IntelligenceMode(
  val label: String,
  val subtitle: String,
  val badge: String,
  val telemetry: String
) {
  CORE(
    label = "CORE",
    subtitle = "Omniscient Conversational Twin",
    badge = "GLOBAL STREAM",
    telemetry = "Meta-AI Tone • High-Impact Analogies • Feed Sync Ready"
  ),
  DEEPDIVE(
    label = "DEEPDIVE",
    subtitle = "Isolated Context Engine",
    badge = "SINGLE-TOPIC THREAD",
    telemetry = "Zero Slop • 2-Layer Breakdown • Architectural Syntax"
  )
}
