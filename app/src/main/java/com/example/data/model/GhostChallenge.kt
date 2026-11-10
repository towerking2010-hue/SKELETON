package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ghost_challenges")
data class GhostChallenge(
  @PrimaryKey
  val id: String,
  val cardId: String,
  val cardTitle: String,
  val promptQuestion: String,
  val expectedKeywords: String, // comma-separated
  val status: String = "PENDING", // PENDING, CLEARED, FAILED
  val userResponse: String? = null,
  val language: String = "en",
  val timestamp: Long = System.currentTimeMillis()
)
