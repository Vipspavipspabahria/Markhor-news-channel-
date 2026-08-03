package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.NewsArticle
import com.example.data.local.NewsChannelInfo
import com.example.data.local.NewsStatus
import com.example.ui.components.ArticleDetailBottomSheet
import com.example.ui.components.ChannelsScreen
import com.example.ui.components.DateSeparatorHeader
import com.example.ui.components.NewDispatchDialog
import com.example.ui.components.NewsBubbleItem
import com.example.ui.components.StatusStoriesBar
import com.example.ui.components.StatusStoryViewerModal
import com.example.ui.components.WhatsAppTopBar
import com.example.ui.theme.MarkhorGold
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppDarkBackground
import com.example.ui.theme.WhatsAppLightBackground
import com.example.ui.theme.WhatsAppTealDark
import com.example.ui.theme.WhatsAppTealHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkhorMainScreen(
    viewModel: MarkhorNewsViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    val statuses by viewModel.statuses.collectAsStateWithLifecycle()
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val selectedArticleForDetail by viewModel.selectedArticleForDetail.collectAsStateWithLifecycle()
    val activeStatusForStory by viewModel.activeStatusForStory.collectAsStateWithLifecycle()
    val showNewDispatchDialog by viewModel.showNewDispatchDialog.collectAsStateWithLifecycle()
    val userFeedbackMessage by viewModel.userFeedbackMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val isDark = isSystemInDarkTheme()
    val chatBg = if (isDark) WhatsAppDarkBackground else WhatsAppLightBackground

    LaunchedEffect(userFeedbackMessage) {
        userFeedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedbackMessage()
        }
    }

    Scaffold(
        topBar = {
            WhatsAppTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                selectedCategory = selectedCategory,
                onCategorySelect = viewModel::onCategorySelect,
                onRefresh = viewModel::refreshNewsFeed,
                onOpenNewDispatch = viewModel::openNewDispatchDialog
            )
        },
        bottomBar = {
            WhatsAppNavigationBar(
                selectedTab = selectedTab,
                onTabSelect = viewModel::onTabSelect,
                unreadCount = articles.count { !it.isRead }
            )
        },
        floatingActionButton = {
            if (selectedTab == NewsTab.CHAT_FEED) {
                FloatingActionButton(
                    onClick = viewModel::openNewDispatchDialog,
                    containerColor = WhatsAppAccentGreen,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("post_dispatch_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Post Intelligence Dispatch"
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = chatBg,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                NewsTab.CHAT_FEED, NewsTab.FAVORITES -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = viewModel::refreshNewsFeed,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NewsFeedContent(
                            articles = articles,
                            statuses = statuses,
                            isFavoritesTab = selectedTab == NewsTab.FAVORITES,
                            onArticleClick = viewModel::openArticleDetail,
                            onToggleBookmark = viewModel::toggleBookmark,
                            onToggleReadReceipt = viewModel::toggleReadReceipt,
                            onStatusClick = viewModel::openStatusStory,
                            onOpenNewDispatch = viewModel::openNewDispatchDialog,
                            onRefreshFeed = viewModel::refreshNewsFeed
                        )
                    }
                }

                NewsTab.CHANNELS -> {
                    ChannelsScreen(
                        channels = channels,
                        onToggleSubscribe = viewModel::toggleChannelSubscription
                    )
                }

                NewsTab.BULLETINS -> {
                    BulletinsStatusTabScreen(
                        statuses = statuses,
                        onStatusClick = viewModel::openStatusStory,
                        onOpenNewDispatch = viewModel::openNewDispatchDialog
                    )
                }
            }
        }
    }

    // Article Reader Bottom Sheet
    selectedArticleForDetail?.let { article ->
        ArticleDetailBottomSheet(
            article = article,
            onDismiss = viewModel::closeArticleDetail,
            onToggleBookmark = viewModel::toggleBookmark
        )
    }

    // Status Story Viewer Modal
    activeStatusForStory?.let { status ->
        StatusStoryViewerModal(
            status = status,
            onClose = viewModel::closeStatusStory
        )
    }

    // New Dispatch Publisher Dialog
    if (showNewDispatchDialog) {
        NewDispatchDialog(
            onDismiss = viewModel::closeNewDispatchDialog,
            onPublish = viewModel::publishDispatch
        )
    }
}

@Composable
fun NewsFeedContent(
    articles: List<NewsArticle>,
    statuses: List<NewsStatus>,
    isFavoritesTab: Boolean,
    onArticleClick: (NewsArticle) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onToggleReadReceipt: (NewsArticle) -> Unit,
    onStatusClick: (NewsStatus) -> Unit,
    onOpenNewDispatch: () -> Unit,
    onRefreshFeed: () -> Unit
) {
    if (articles.isEmpty()) {
        EmptyFeedState(
            isFavoritesTab = isFavoritesTab,
            onRefreshFeed = onRefreshFeed,
            onOpenNewDispatch = onOpenNewDispatch
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Include Status Stories Bar at the top of main feed
            if (!isFavoritesTab && statuses.isNotEmpty()) {
                item(key = "status_bar_header") {
                    StatusStoriesBar(
                        statuses = statuses,
                        onStatusClick = onStatusClick,
                        onOpenNewDispatch = onOpenNewDispatch
                    )
                }
            }

            // Group articles by date for date separator headers (e.g., "Today", "Yesterday", "Jul 28")
            val groupedArticles = articles.groupBy { it.formattedDate }

            groupedArticles.forEach { (dateHeader, dateArticles) ->
                item(key = "header_$dateHeader") {
                    DateSeparatorHeader(dateText = dateHeader)
                }

                items(dateArticles, key = { it.id }) { article ->
                    NewsBubbleItem(
                        article = article,
                        onArticleClick = onArticleClick,
                        onToggleBookmark = onToggleBookmark,
                        onToggleReadReceipt = onToggleReadReceipt
                    )
                }
            }
        }
    }
}

@Composable
fun WhatsAppNavigationBar(
    selectedTab: NewsTab,
    onTabSelect: (NewsTab) -> Unit,
    unreadCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NewsTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab
                val icon = when (tab) {
                    NewsTab.CHAT_FEED -> Icons.Default.Chat
                    NewsTab.CHANNELS -> Icons.Default.RssFeed
                    NewsTab.BULLETINS -> Icons.Default.Shield
                    NewsTab.FAVORITES -> Icons.Default.Bookmark
                }

                Surface(
                    onClick = { onTabSelect(tab) },
                    color = if (isSelected) WhatsAppAccentGreen.copy(alpha = 0.15f) else Color.Transparent,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .testTag("nav_tab_${tab.name}")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (tab == NewsTab.CHAT_FEED && unreadCount > 0) {
                                    Badge(containerColor = WhatsAppAccentGreen, contentColor = Color.White) {
                                        Text(text = "$unreadCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) WhatsAppAccentGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = tab.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) WhatsAppAccentGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BulletinsStatusTabScreen(
    statuses: List<NewsStatus>,
    onStatusClick: (NewsStatus) -> Unit,
    onOpenNewDispatch: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = WhatsAppTealHeader),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Bulletins",
                            tint = MarkhorGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Special Operations Bulletins",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Tap to view real-time intelligence status stories with high-priority briefs.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "RECENT INTELLIGENCE UPDATES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsAppAccentGreen,
                letterSpacing = 0.8.sp
            )
        }

        items(statuses, key = { it.id }) { status ->
            Card(
                onClick = { onStatusClick(status) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("status_bulletin_${status.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(WhatsAppTealDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Status",
                            tint = MarkhorGold,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = status.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = status.snippet,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFeedState(
    isFavoritesTab: Boolean,
    onRefreshFeed: () -> Unit,
    onOpenNewDispatch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(WhatsAppAccentGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavoritesTab) Icons.Default.Star else Icons.Default.Newspaper,
                    contentDescription = "Empty Feed",
                    tint = WhatsAppAccentGreen,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isFavoritesTab) "No Saved Articles Yet" else "No Intelligence Dispatches Found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isFavoritesTab)
                    "Tap the bookmark icon on any message bubble to save articles for offline reading."
                else
                    "Try adjusting your search query, selecting a different category, or refreshing the feed.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onRefreshFeed,
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppTealDark),
                    modifier = Modifier.testTag("empty_refresh_button")
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Refresh")
                }

                Button(
                    onClick = onOpenNewDispatch,
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppAccentGreen),
                    modifier = Modifier.testTag("empty_post_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Dispatch", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Dispatch")
                }
            }
        }
    }
}
