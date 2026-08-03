package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NewsArticle
import com.example.data.local.NewsChannelInfo
import com.example.data.local.NewsStatus
import com.example.data.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NewsTab(val title: String, val badgeCount: Int = 0) {
    CHAT_FEED("Dispatches"),
    CHANNELS("Channels"),
    BULLETINS("Bulletins"),
    FAVORITES("Saved")
}

class MarkhorNewsViewModel(application: Application) : AndroidViewModel(application) {

    val repository = NewsRepository(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedTab = MutableStateFlow(NewsTab.CHAT_FEED)
    val selectedTab: StateFlow<NewsTab> = _selectedTab.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _selectedArticleForDetail = MutableStateFlow<NewsArticle?>(null)
    val selectedArticleForDetail: StateFlow<NewsArticle?> = _selectedArticleForDetail.asStateFlow()

    private val _activeStatusForStory = MutableStateFlow<NewsStatus?>(null)
    val activeStatusForStory: StateFlow<NewsStatus?> = _activeStatusForStory.asStateFlow()

    private val _showNewDispatchDialog = MutableStateFlow(false)
    val showNewDispatchDialog: StateFlow<Boolean> = _showNewDispatchDialog.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: StateFlow<List<NewsArticle>> = combine(_searchQuery, _selectedCategory, _selectedTab) { query, cat, tab ->
        Triple(query, cat, tab)
    }.flatMapLatest { (query, cat, tab) ->
        val isBookmarkedOnly = (tab == NewsTab.FAVORITES)
        repository.getArticles(
            query = query,
            category = cat,
            bookmarkedOnly = isBookmarkedOnly
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val statuses: StateFlow<List<NewsStatus>> = repository.getStatuses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val channels: StateFlow<List<NewsChannelInfo>> = repository.getChannels().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun onTabSelect(tab: NewsTab) {
        _selectedTab.value = tab
    }

    fun refreshNewsFeed() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val count = repository.refreshNews()
            _isRefreshing.value = false
            _userFeedbackMessage.value = "Fetched $count new intelligence dispatches"
        }
    }

    fun toggleBookmark(articleId: Int) {
        viewModelScope.launch {
            repository.toggleBookmark(articleId)
        }
    }

    fun toggleReadReceipt(article: NewsArticle) {
        viewModelScope.launch {
            repository.toggleReadStatus(article.id, article.isRead)
            val msg = if (!article.isRead) "Marked as read (Blue ticks)" else "Marked as unread"
            _userFeedbackMessage.value = msg
        }
    }

    fun openArticleDetail(article: NewsArticle) {
        _selectedArticleForDetail.value = article
        if (!article.isRead) {
            viewModelScope.launch {
                repository.toggleReadStatus(article.id, false)
            }
        }
    }

    fun closeArticleDetail() {
        _selectedArticleForDetail.value = null
    }

    fun openStatusStory(status: NewsStatus) {
        _activeStatusForStory.value = status
        viewModelScope.launch {
            repository.markStatusSeen(status.id)
        }
    }

    fun closeStatusStory() {
        _activeStatusForStory.value = null
    }

    fun toggleChannelSubscription(channelId: String) {
        viewModelScope.launch {
            repository.toggleChannelSubscription(channelId)
        }
    }

    fun openNewDispatchDialog() {
        _showNewDispatchDialog.value = true
    }

    fun closeNewDispatchDialog() {
        _showNewDispatchDialog.value = false
    }

    fun publishDispatch(headline: String, summary: String, category: String, content: String) {
        if (headline.isBlank() || summary.isBlank()) return
        viewModelScope.launch {
            repository.postUserDispatch(headline, summary, category, content)
            _showNewDispatchDialog.value = false
            _userFeedbackMessage.value = "Published dispatch to Markhor stream"
        }
    }

    fun clearFeedbackMessage() {
        _userFeedbackMessage.value = null
    }
}
