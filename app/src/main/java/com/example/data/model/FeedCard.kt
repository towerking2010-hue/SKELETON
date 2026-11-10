package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_cards")
data class FeedCard(
  @PrimaryKey
  val id: String,
  val title: String,
  val itch: String,
  val skeleton: String,
  val intuition: String,
  val branchDomain: String,
  val crossLinkTarget: String? = null,
  val isSaved: Boolean = false,
  val timestamp: Long = System.currentTimeMillis(),
  val recallStage: Int = 0, // 0: fresh, 1: 3-day, 2: 7-day, 3: 14-day, 4: mastered
  val lastReviewedTimestamp: Long = 0L,
  val weakRecallCount: Int = 0,
  val challengeScenario: String = "",
  val challengeExpectedKey: String = "",
  val language: String = "en"
)
