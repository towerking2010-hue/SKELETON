package com.example.engine.ai

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val TAG = "GeminiService"

enum class ModelTier(val modelName: String, val displayName: String) {
  FAST("gemini-3.5-flash-lite", "Flash-Lite (Free tier)"),
  GENERAL("gemini-3.8-flash", "Gemini 3.8 Flash (General)"),
  REASONING("gemini-3.8-flash", "Gemini 3.8 Flash (Reasoning prompt)")
}

enum class VideoAspectRatio(val ratioString: String, val label: String) {
  LANDSCAPE_16_9("16:9", "16:9 (Landscape)"),
  PORTRAIT_9_16("9:16", "9:16 (Portrait)")
}

enum class AiRole(val roleName: String, val systemInstruction: String) {
  SKELETON_CORE(
    "CORE Omniscient Twin",
    "You are SKELETON's CORE engine. Act like a brilliant, casual friend (Meta AI style)—relaxed, direct, zero text walls, zero corporate fluff, using high-impact analogies. Conclude novel concepts with [FEED SYNC]."
  ),
  DEEPDIVE_SCIENTIST(
    "DEEPDIVE Deconstructor",
    "You are SKELETON's DEEPDIVE engine. Provide a two-layer breakdown: 1) Brief intuition bridge, followed immediately by 2) Raw mathematical, technical, or code syntax invariants. Conclude with a crisp active-recall scenario challenge."
  ),
  BIOSCIENCE_EXPERT(
    "Bioscience & Neuro Specialist",
    "You are a specialized Bioscience & Cognitive Neuroscientist. Analyze molecular mechanisms (CRISPR, epigenetics, synaptic plasticity, optogenetics, ion channels) with absolute precision and biochemical rigor."
  )
}

object GeminiCognitiveService {

  /**
   * Multi-turn chat generation with system instruction role and selectable model tier
   */
  suspend fun chatGenerate(
    userPrompt: String,
    role: AiRole,
    tier: ModelTier = ModelTier.GENERAL
  ): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(
          modelName = tier.modelName,
          systemInstruction = content { text(role.systemInstruction) }
        )
        val response = model.generateContent(userPrompt)
        response.text ?: "Synthesis complete without output."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Chat generation with ${tier.modelName} encountered: ${e.message}", e)
      "Inference error [${tier.modelName}]: ${e.localizedMessage ?: "attestation error"}"
    }
  }

  /** External Search grounding is intentionally disabled in zero-billing mode. */
  suspend fun searchGroundedQuery(query: String): String =
    "Google Search grounding is disabled in zero-billing mode. No search request was sent."

  /** External Maps grounding is intentionally disabled in zero-billing mode. */
  suspend fun mapsGroundedQuery(query: String): String =
    "Google Maps grounding is disabled in zero-billing mode. No Maps request was sent."

  /** Legacy text-only transcription entry point; real microphone capture uses Android SpeechRecognizer. */
  suspend fun transcribeAudio(audioDescriptionOrBytes: String): String =
    "Audio transcription now uses Android SpeechRecognizer. This legacy text-only API makes no network request."

  /** Legacy live-audio entry point; the current voice screen uses device speech services. */
  suspend fun liveVoiceSession(speechInput: String): String =
    "Live audio streaming is not implemented. Use the app's Android speech input and CORE response flow."

  /** Image generation is disabled so the app cannot make a paid media request. */
  suspend fun generateOrEditImage(prompt: String, existingImage: Bitmap? = null): String =
    "Image generation is disabled in zero-billing mode. No paid model request was sent."

  /** Video rendering is not available in the current zero-billing build. */
  suspend fun generateVideo(prompt: String, aspectRatio: VideoAspectRatio = VideoAspectRatio.LANDSCAPE_16_9): String =
    "Video rendering is not implemented. No external API request was sent."

  /** Photo animation is not available in the current zero-billing build. */
  suspend fun animateImageToVideo(image: Bitmap, prompt: String, aspectRatio: VideoAspectRatio = VideoAspectRatio.LANDSCAPE_16_9): String =
    "Photo animation is not implemented. No external API request was sent."
}
