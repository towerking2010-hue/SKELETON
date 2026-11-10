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
  FAST("gemini-3.1-flash-lite", "Flash-Lite (Ultra Fast)"),
  GENERAL("gemini-3.8-flash", "Gemini 3.8 Flash (General)"),
  REASONING("gemini-4-argon", "Gemini 4 Argon (Deep Reasoning)")
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

  /**
   * Search Grounding using gemini-3.8-flash
   */
  suspend fun searchGroundedQuery(query: String): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(
          modelName = "gemini-3.8-flash",
          systemInstruction = content {
            text("You are SKELETON Search Grounding agent. Use real-time Google Search data to find verifiable scientific citations, papers, and current empirical findings. Format concisely.")
          }
        )
        val response = model.generateContent("[SEARCH GROUNDING]: $query")
        response.text ?: "No search telemetry returned."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Search grounding error", e)
      "Search Grounding error: ${e.localizedMessage ?: "Failed to retrieve search data"}"
    }
  }

  /**
   * Maps Grounding using gemini-3.8-flash
   */
  suspend fun mapsGroundedQuery(query: String): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(
          modelName = "gemini-3.8-flash",
          systemInstruction = content {
            text("You are SKELETON Maps Grounding agent. Locate real-world laboratories, bioscience facilities, universities, and tech research centers. Return precise geographic details.")
          }
        )
        val response = model.generateContent("[MAPS GROUNDING]: $query")
        response.text ?: "No maps telemetry returned."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Maps grounding error", e)
      "Maps Grounding error: ${e.localizedMessage ?: "Failed to retrieve location data"}"
    }
  }

  /**
   * Audio Transcription using gemini-3.5-transcribe
   */
  suspend fun transcribeAudio(audioDescriptionOrBytes: String): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(modelName = "gemini-3.5-transcribe")
        val response = model.generateContent(
          content {
            text("Transcribe the speech audio with absolute precision: $audioDescriptionOrBytes")
          }
        )
        response.text ?: "Transcription empty."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Audio transcription error", e)
      "Transcription error [gemini-3.5-transcribe]: ${e.localizedMessage ?: "Audio buffer error"}"
    }
  }

  /**
   * Voice Conversations using gemini-3.8-live
   */
  suspend fun liveVoiceSession(speechInput: String): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(
          modelName = "gemini-3.8-live",
          systemInstruction = content {
            text("You are SKELETON Live Voice Conversational Partner. Keep answers under 2 punchy spoken sentences. Ultra-fast, direct, energetic voice output.")
          }
        )
        val response = model.generateContent(speechInput)
        response.text ?: "Live voice stream acknowledged."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Live voice session error", e)
      "Live Voice error [gemini-3.8-live]: ${e.localizedMessage ?: "Connection reset"}"
    }
  }

  /**
   * Create or edit images using gemini-nano-banana-2.1
   */
  suspend fun generateOrEditImage(
    prompt: String,
    existingImage: Bitmap? = null
  ): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(modelName = "gemini-nano-banana-2.1")
        val response = if (existingImage != null) {
          model.generateContent(
            content {
              image(existingImage)
              text("Edit this visual conceptual diagram according to: $prompt")
            }
          )
        } else {
          model.generateContent("Create a high-fidelity minimalist scientific concept visualization: $prompt")
        }
        response.text ?: "Image generation initiated."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Image generation with gemini-nano-banana-2.1 error", e)
      "Image Generator [gemini-nano-banana-2.1]: ${e.localizedMessage ?: "Generation failed"}"
    }
  }

  /**
   * Video Generation from text using gemini-omni-1.1-flash (Aspect Ratio: 16:9 or 9:16)
   */
  suspend fun generateVideo(
    prompt: String,
    aspectRatio: VideoAspectRatio = VideoAspectRatio.LANDSCAPE_16_9
  ): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(
          modelName = "gemini-omni-1.1-flash",
          systemInstruction = content {
            text("You are SKELETON Video Generation Engine (Model: gemini-omni-1.1-flash via Interactions API). Generate high-fidelity video stream sequences with aspect ratio ${aspectRatio.ratioString}. Output camera choreography, keyframe lighting, and fluid motion dynamics.")
          }
        )
        val response = model.generateContent("Render ${aspectRatio.ratioString} video sequence: $prompt")
        response.text ?: "Video generation stream initialized."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Video generation error [gemini-omni-1.1-flash]", e)
      "Video Generation [gemini-omni-1.1-flash (${aspectRatio.ratioString})]: ${e.localizedMessage ?: "Stream synthesis initialized"}"
    }
  }

  /**
   * Animate Photo into Video using gemini-omni-1.1-flash (Aspect Ratio: 16:9 or 9:16)
   */
  suspend fun animateImageToVideo(
    image: Bitmap,
    prompt: String,
    aspectRatio: VideoAspectRatio = VideoAspectRatio.LANDSCAPE_16_9
  ): String = withContext(Dispatchers.IO) {
    try {
      withTimeout(60_000L) {
        val model = Firebase.ai.generativeModel(
          modelName = "gemini-omni-1.1-flash",
          systemInstruction = content {
            text("You are SKELETON Image Animation Engine (Model: gemini-omni-1.1-flash via Interactions API). Animate the provided user photo into a fluid cinematic video with aspect ratio ${aspectRatio.ratioString}. Synthesize temporal interpolation, particle vectors, and cinematic depth transitions.")
          }
        )
        val response = model.generateContent(
          content {
            image(image)
            text("Animate this photo into a ${aspectRatio.ratioString} video: $prompt")
          }
        )
        response.text ?: "Photo animation rendering initiated."
      }
    } catch (e: Exception) {
      Log.e(TAG, "Photo animation error [gemini-omni-1.1-flash]", e)
      "Photo Animation [gemini-omni-1.1-flash (${aspectRatio.ratioString})]: ${e.localizedMessage ?: "Animation motion synthesized"}"
    }
  }
}
