package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "core_messages")
data class CoreChatMessage(
  @PrimaryKey
  val id: String,
  val isUser: Boolean,
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val feedSyncTopic: String? = null,
  val feedSyncAdded: Boolean = false
)
