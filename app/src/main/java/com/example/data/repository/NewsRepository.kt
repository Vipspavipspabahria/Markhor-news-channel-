package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.local.NewsArticle
import com.example.data.local.NewsChannelInfo
import com.example.data.local.NewsStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NewsRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "markhor_news_database"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.newsDao()

    fun getArticles(query: String = "", category: String = "All", bookmarkedOnly: Boolean = false): Flow<List<NewsArticle>> {
        return dao.getFilteredArticles(
            query = query.trim(),
            category = category,
            bookmarkedOnly = if (bookmarkedOnly) 1 else 0
        )
    }

    fun getStatuses(): Flow<List<NewsStatus>> = dao.getAllStatuses()

    fun getChannels(): Flow<List<NewsChannelInfo>> = dao.getAllChannels()

    suspend fun toggleBookmark(id: Int) = dao.toggleBookmark(id)

    suspend fun toggleReadStatus(id: Int, currentReadStatus: Boolean) {
        dao.updateReadStatus(id, !currentReadStatus)
    }

    suspend fun markStatusSeen(id: Int) = dao.markStatusSeen(id)

    suspend fun toggleChannelSubscription(channelId: String) = dao.toggleSubscription(channelId)

    suspend fun postUserDispatch(headline: String, summary: String, category: String, content: String): Long {
        val now = System.currentTimeMillis()
        val article = NewsArticle(
            headline = headline,
            summary = summary,
            content = if (content.isBlank()) summary else content,
            timestamp = now,
            formattedDate = "Today",
            formattedTime = "Just now",
            category = category,
            drawableResName = "img_markhor_logo",
            isRead = true,
            isBookmarked = false,
            channelName = "Markhor Field Bureau",
            authorTitle = "Verified Markhor Dispatch",
            isVerified = true,
            priority = "HIGH"
        )
        return dao.insertArticle(article)
    }

    suspend fun refreshNews(): Int = withContext(Dispatchers.IO) {
        // Simulates fetching latest intelligence dispatches from Markhor News Server
        val now = System.currentTimeMillis()
        val newDispatches = listOf(
            NewsArticle(
                headline = "⚡ BREAKING: Markhor Intelligence Intercepts Critical Cyber Grid Threat",
                summary = "Special Operations Cyber Division neutralizes high-frequency external malware targeted at regional communication lines.",
                content = "ISLAMABAD - Inter Services Intelligence Special Cyber Wing today announced the successful mitigation of a sophisticated distributed cyber intrusion attempting to disrupt national communication infrastructure. Advanced quantum encryption protocols were deployed immediately to safeguard strategic assets.\n\nAll primary network nodes report 100% operational status with zero compromised telemetry data.",
                timestamp = now,
                formattedDate = "Today",
                formattedTime = "12:05 PM",
                category = "Cyber",
                drawableResName = "img_news_cyber",
                isRead = false,
                isBookmarked = false,
                channelName = "Markhor News Channel",
                authorTitle = "ISI Cyber Security Directorate",
                isVerified = true,
                priority = "URGENT"
            ),
            NewsArticle(
                headline = "Strategic Defense Briefing: Joint Tactical Readiness Exercise Completed",
                summary = "Northern Command completes high-altitude combat drill 'Markhor Shield 2026' with precision strike demonstrations.",
                content = "RAWALPINDI - The Inter Services Intelligence Media Cell reports the successful culmination of 'Markhor Shield 2026', a comprehensive multi-domain exercise featuring aerial reconnaissance, electronic warfare suppression, and rapid deployment forces in rugged terrain.\n\nChief of Staff highlighted the exceptional synergy between ground units and tactical intelligence surveillance.",
                timestamp = now - 3600000,
                formattedDate = "Today",
                formattedTime = "11:00 AM",
                category = "Defense",
                drawableResName = "img_news_defense",
                isRead = true,
                isBookmarked = true,
                channelName = "Markhor Defense Feed",
                authorTitle = "Inter Services Intelligence Media Cell",
                isVerified = true,
                priority = "HIGH"
            )
        )
        dao.insertArticles(newDispatches)
        return@withContext newDispatches.size
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.getArticleCount() == 0) {
            val now = System.currentTimeMillis()
            val initialArticles = listOf(
                NewsArticle(
                    headline = "⚡ BREAKING: Markhor Intelligence Intercepts Critical Cyber Grid Threat",
                    summary = "Special Operations Cyber Division neutralizes high-frequency external malware targeted at regional communication lines.",
                    content = "ISLAMABAD - Inter Services Intelligence Special Cyber Wing today announced the successful mitigation of a sophisticated distributed cyber intrusion attempting to disrupt national communication infrastructure. Advanced quantum encryption protocols were deployed immediately to safeguard strategic assets.\n\nAll primary network nodes report 100% operational status with zero compromised telemetry data.",
                    timestamp = now - 600000,
                    formattedDate = "Today",
                    formattedTime = "11:45 AM",
                    category = "Cyber",
                    drawableResName = "img_news_cyber",
                    isRead = true,
                    isBookmarked = true,
                    channelName = "Markhor Intelligence Center",
                    authorTitle = "ISI Cyber Security Directorate",
                    isVerified = true,
                    priority = "URGENT"
                ),
                NewsArticle(
                    headline = "Strategic Defense Briefing: Joint Tactical Readiness Exercise Completed",
                    summary = "Northern Command completes high-altitude combat drill 'Markhor Shield 2026' with precision strike demonstrations.",
                    content = "RAWALPINDI - The Inter Services Intelligence Media Cell reports the successful culmination of 'Markhor Shield 2026', a comprehensive multi-domain exercise featuring aerial reconnaissance, electronic warfare suppression, and rapid deployment forces in rugged terrain.\n\nChief of Staff highlighted the exceptional synergy between ground units and tactical intelligence surveillance.",
                    timestamp = now - 3600000 * 2,
                    formattedDate = "Today",
                    formattedTime = "09:30 AM",
                    category = "Defense",
                    drawableResName = "img_news_defense",
                    isRead = true,
                    isBookmarked = false,
                    channelName = "Markhor Defense Feed",
                    authorTitle = "Inter Services Intelligence Media Cell",
                    isVerified = true,
                    priority = "HIGH"
                ),
                NewsArticle(
                    headline = "Geopolitical Summit: Regional Trade Accord Signed in Islamabad",
                    summary = "Delegates conclude strategic economic corridor agreement focusing on renewable energy and maritime logistics.",
                    content = "ISLAMABAD - High-level diplomatic representatives concluded a landmark multi-nation trade pact today. The accord enhances economic cooperation, border security technology sharing, and regional energy stability.",
                    timestamp = now - 86400000,
                    formattedDate = "Yesterday",
                    formattedTime = "04:15 PM",
                    category = "Geopolitics",
                    drawableResName = "img_markhor_logo",
                    isRead = false,
                    isBookmarked = true,
                    channelName = "Markhor News Channel",
                    authorTitle = "Diplomatic Intelligence Wing",
                    isVerified = true,
                    priority = "NORMAL"
                ),
                NewsArticle(
                    headline = "National Security Bulletin: Border Reconnaissance Drone Fleet Deployed",
                    summary = "Automated high-endurance surveillance UAVs begin 24/7 patrol along key mountain passes.",
                    content = "PESHAWAR - Autonomous flight systems equipped with thermal imaging and AI target recognition have been activated for border monitoring. Live feeds are transmitted directly to the Central Command Room.",
                    timestamp = now - 86400000 * 2,
                    formattedDate = "Jul 28",
                    formattedTime = "02:20 PM",
                    category = "National",
                    drawableResName = "img_news_defense",
                    isRead = true,
                    isBookmarked = false,
                    channelName = "Markhor Border Watch",
                    authorTitle = "Frontier Corps Intelligence",
                    isVerified = true,
                    priority = "NORMAL"
                )
            )
            dao.insertArticles(initialArticles)

            val initialStatuses = listOf(
                NewsStatus(
                    title = "Operation Falcon Eye",
                    snippet = "Real-time aerial monitoring initiated across northern corridors.",
                    fullText = "Operation Falcon Eye: High-altitude drone sensors operational 24/7. Border security integrity maintained at peak readiness.",
                    timestamp = now - 1800000,
                    category = "Defense",
                    drawableResName = "img_news_defense",
                    isSeen = false
                ),
                NewsStatus(
                    title = "Cyber Advisory #409",
                    snippet = "Critical security patch issued for defense communications.",
                    fullText = "Cyber Security Directorate urges all units to update encryption keys to Protocol 7X immediately.",
                    timestamp = now - 5400000,
                    category = "Cyber",
                    drawableResName = "img_news_cyber",
                    isSeen = false
                ),
                NewsStatus(
                    title = "Diplomatic Dispatch",
                    snippet = "Delegation arrives in Tashkent for trilateral security summit.",
                    fullText = "High level diplomatic talks underway regarding regional anti-terrorism coordination.",
                    timestamp = now - 14400000,
                    category = "Geopolitics",
                    drawableResName = "img_markhor_logo",
                    isSeen = true
                )
            )
            dao.insertStatuses(initialStatuses)

            val initialChannels = listOf(
                NewsChannelInfo(
                    channelId = "isi_official",
                    name = "Inter Services Intelligence (Official)",
                    description = "Official updates, security press releases, and strategic announcements from ISI Pakistan.",
                    followerCount = "4.8M followers",
                    isSubscribed = true,
                    lastPost = "Markhor Shield 2026 drill completed successfully.",
                    lastPostTime = "09:30 AM",
                    isVerified = true
                ),
                NewsChannelInfo(
                    channelId = "markhor_breaking",
                    name = "Markhor Breaking News",
                    description = "24/7 rapid response news flash for national defense and geopolitical affairs.",
                    followerCount = "2.1M followers",
                    isSubscribed = true,
                    lastPost = "⚡ BREAKING: Cyber Grid Attack Neutralized.",
                    lastPostTime = "11:45 AM",
                    isVerified = true
                ),
                NewsChannelInfo(
                    channelId = "defense_watcher",
                    name = "Pakistan Defense Watch",
                    description = "In-depth military hardware analysis, strategic doctrines, and air power updates.",
                    followerCount = "950K followers",
                    isSubscribed = false,
                    lastPost = "New UAV squadron commissioned in Northern Sector.",
                    lastPostTime = "Yesterday",
                    isVerified = true
                )
            )
            dao.insertChannels(initialChannels)
        }
    }
}
