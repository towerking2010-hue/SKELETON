package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CoreChatMessage
import com.example.data.model.DeepDiveMessage
import com.example.data.model.FeedCard
import com.example.data.model.GhostChallenge
import kotlinx.coroutines.flow.Flow

@Dao
interface SkeletonDao {

  // Feed Cards
  @Query("SELECT * FROM feed_cards ORDER BY timestamp DESC")
  fun getAllFeedCards(): Flow<List<FeedCard>>

  @Query("SELECT * FROM feed_cards WHERE isSaved = 1 ORDER BY timestamp DESC")
  fun getSavedCards(): Flow<List<FeedCard>>

  @Query("SELECT * FROM feed_cards WHERE lastReviewedTimestamp = 0 OR lastReviewedTimestamp + CASE WHEN recallStage <= 0 THEN 600000 WHEN recallStage = 1 THEN 259200000 WHEN recallStage = 2 THEN 604800000 WHEN recallStage = 3 THEN 1209600000 ELSE 2592000000 END <= :now ORDER BY lastReviewedTimestamp ASC, timestamp DESC")
  fun getDueReviewCards(now: Long): Flow<List<FeedCard>>

  @Query("SELECT * FROM feed_cards WHERE id = :id LIMIT 1")
  fun getFeedCardById(id: String): Flow<FeedCard?>

  @Query("SELECT * FROM feed_cards WHERE id = :id LIMIT 1")
  suspend fun getFeedCardByIdDirect(id: String): FeedCard?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFeedCard(card: FeedCard)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFeedCards(cards: List<FeedCard>)

  @Update
  suspend fun updateFeedCard(card: FeedCard)

  @Delete
  suspend fun deleteFeedCard(card: FeedCard)

  @Query("UPDATE feed_cards SET isSaved = :isSaved WHERE id = :id")
  suspend fun updateSavedState(id: String, isSaved: Boolean)

  @Query("UPDATE feed_cards SET recallStage = :stage, weakRecallCount = :weakCount, lastReviewedTimestamp = :timestamp WHERE id = :id")
  suspend fun updateSpacedRepetition(id: String, stage: Int, weakCount: Int, timestamp: Long)

  // DeepDive Messages (Strict Context Isolation by cardId)
  @Query("SELECT * FROM deepdive_messages WHERE cardId = :cardId ORDER BY timestamp ASC")
  fun getDeepDiveMessages(cardId: String): Flow<List<DeepDiveMessage>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDeepDiveMessage(message: DeepDiveMessage)

  // CORE Messages
  @Query("SELECT * FROM core_messages ORDER BY timestamp ASC")
  fun getAllCoreMessages(): Flow<List<CoreChatMessage>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCoreMessage(message: CoreChatMessage)

  @Query("UPDATE core_messages SET feedSyncAdded = 1 WHERE id = :messageId")
  suspend fun markFeedSyncAdded(messageId: String)

  // Ghost Challenges
  @Query("SELECT * FROM ghost_challenges ORDER BY timestamp DESC")
  fun getAllGhostChallenges(): Flow<List<GhostChallenge>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGhostChallenge(challenge: GhostChallenge)

  @Update
  suspend fun updateGhostChallenge(challenge: GhostChallenge)
}
