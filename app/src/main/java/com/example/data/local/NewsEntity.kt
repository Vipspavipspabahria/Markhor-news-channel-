package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "news_articles")
data class NewsArticle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val headline: String,
    val summary: String,
    val content: String,
    val timestamp: Long,
    val formattedDate: String, // e.g., "Today", "Yesterday", "Jul 28"
    val formattedTime: String, // e.g., "10:42 AM"
    val category: String, // "Defense", "Geopolitics", "Intelligence", "Cyber", "National"
    val imageUrl: String? = null,
    val drawableResName: String? = null, // e.g., "img_news_defense", "img_news_cyber", "img_markhor_logo"
    val isRead: Boolean = false,
    val isBookmarked: Boolean = false,
    val channelName: String = "Markhor News Channel",
    val authorTitle: String = "Inter Services Intelligence Media Cell",
    val isVerified: Boolean = true,
    val priority: String = "NORMAL" // "URGENT", "HIGH", "NORMAL"
)

@Entity(tableName = "news_statuses")
data class NewsStatus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val snippet: String,
    val fullText: String,
    val timestamp: Long,
    val category: String,
    val drawableResName: String? = null,
    val isSeen: Boolean = false
)

@Entity(tableName = "news_channels")
data class NewsChannelInfo(
    @PrimaryKey val channelId: String,
    val name: String,
    val description: String,
    val followerCount: String,
    val isSubscribed: Boolean = false,
    val lastPost: String,
    val lastPostTime: String,
    val isVerified: Boolean = true
)

@Dao
interface NewsDao {
    @Query("""
        SELECT * FROM news_articles 
        WHERE (:query = '' OR headline LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%')
        AND (:category = 'All' OR category = :category)
        AND (:bookmarkedOnly = 0 OR isBookmarked = 1)
        ORDER BY timestamp DESC
    """)
    fun getFilteredArticles(query: String, category: String, bookmarkedOnly: Int): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles WHERE id = :id")
    fun getArticleById(id: Int): Flow<NewsArticle?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticle): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticle>)

    @Query("UPDATE news_articles SET isRead = :isRead WHERE id = :id")
    suspend fun updateReadStatus(id: Int, isRead: Boolean)

    @Query("UPDATE news_articles SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun toggleBookmark(id: Int)

    @Query("DELETE FROM news_articles WHERE id = :id")
    suspend fun deleteArticle(id: Int)

    @Query("SELECT COUNT(*) FROM news_articles")
    suspend fun getArticleCount(): Int

    // Statuses
    @Query("SELECT * FROM news_statuses ORDER BY timestamp DESC")
    fun getAllStatuses(): Flow<List<NewsStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatuses(statuses: List<NewsStatus>)

    @Query("UPDATE news_statuses SET isSeen = 1 WHERE id = :id")
    suspend fun markStatusSeen(id: Int)

    // Channels
    @Query("SELECT * FROM news_channels")
    fun getAllChannels(): Flow<List<NewsChannelInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channels: List<NewsChannelInfo>)

    @Query("UPDATE news_channels SET isSubscribed = CASE WHEN isSubscribed = 1 THEN 0 ELSE 1 END WHERE channelId = :id")
    suspend fun toggleSubscription(id: String)
}

@Database(entities = [NewsArticle::class, NewsStatus::class, NewsChannelInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
