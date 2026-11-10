package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.FeedCard
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreKnowledgeRepository(private val db: FirebaseFirestore) {

  companion object {
    private const val TAG = "FirestoreRepo"

    fun create(context: Context): FirestoreKnowledgeRepository {
      val dbId = context.getString(R.string.firestore_database_id)
      return FirestoreKnowledgeRepository(FirebaseFirestore.getInstance(dbId))
    }
  }

  private fun requireUserId(): String {
    return Firebase.auth.currentUser?.uid
      ?: throw IllegalStateException("User must be authenticated with Google to access Firestore.")
  }

  suspend fun saveCardToCloud(card: FeedCard) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid).collection("cards").document(card.id)
    val data = mapOf(
      "id" to card.id,
      "userId" to uid,
      "title" to card.title,
      "itch" to card.itch,
      "skeleton" to card.skeleton,
      "intuition" to card.intuition,
      "branchDomain" to card.branchDomain,
      "crossLinkTarget" to (card.crossLinkTarget ?: ""),
      "isSaved" to card.isSaved,
      "recallStage" to card.recallStage,
      "createdAt" to FieldValue.serverTimestamp()
    )
    docRef.set(data).await()
    Log.d(TAG, "Card saved to cloud: ${card.id}")
  }

  fun observeCloudCards(): Flow<List<FeedCard>> = flow {
    val uid = requireUserId()
    db.collection("users").document(uid).collection("cards")
      .snapshots()
      .map { snapshot ->
        snapshot.documents.mapNotNull { doc ->
          val id = doc.getString("id") ?: doc.id
          val title = doc.getString("title") ?: return@mapNotNull null
          val itch = doc.getString("itch") ?: ""
          val skeleton = doc.getString("skeleton") ?: ""
          val intuition = doc.getString("intuition") ?: ""
          val domain = doc.getString("branchDomain") ?: "Knowledge"
          val crossLink = doc.getString("crossLinkTarget")
          val isSaved = doc.getBoolean("isSaved") ?: true
          val recallStage = doc.getLong("recallStage")?.toInt() ?: 0
          FeedCard(
            id = id,
            title = title,
            itch = itch,
            skeleton = skeleton,
            intuition = intuition,
            branchDomain = domain,
            crossLinkTarget = if (crossLink.isNullOrBlank()) null else crossLink,
            isSaved = isSaved,
            recallStage = recallStage
          )
        }
      }
      .catch { e ->
        Log.e(TAG, "Error observing cloud cards", e)
        emit(emptyList())
      }
      .collect { emit(it) }
  }
}
