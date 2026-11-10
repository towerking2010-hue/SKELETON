package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deepdive_messages")
data class DeepDiveMessage(
  @PrimaryKey
  val id: String,
  val cardId: String,
  val isUser: Boolean,
  val text: String,
  val intuitionBridge: String? = null,
  val technicalBreakdown: String? = null,
  val scenarioChallenge: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)
