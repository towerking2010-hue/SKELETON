package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CoreChatMessage
import com.example.data.model.DeepDiveMessage
import com.example.data.model.FeedCard
import com.example.data.model.GhostChallenge

@Database(
  entities = [
    FeedCard::class,
    DeepDiveMessage::class,
    CoreChatMessage::class,
    GhostChallenge::class
  ],
  version = 1,
  exportSchema = false
)
abstract class SkeletonDatabase : RoomDatabase() {
  abstract fun skeletonDao(): SkeletonDao

  companion object {
    @Volatile
    private var INSTANCE: SkeletonDatabase? = null

    fun getDatabase(context: Context): SkeletonDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          SkeletonDatabase::class.java,
          "skeleton_cognitive.db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
