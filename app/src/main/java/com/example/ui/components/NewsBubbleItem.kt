package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.local.NewsArticle
import com.example.ui.theme.MarkhorBadgeGreen
import com.example.ui.theme.MarkhorGold
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppBlueCheck
import com.example.ui.theme.WhatsAppDarkBubbleReceived
import com.example.ui.theme.WhatsAppDarkBubbleSent
import com.example.ui.theme.WhatsAppDarkTextPrimary
import com.example.ui.theme.WhatsAppDarkTextSecondary
import com.example.ui.theme.WhatsAppGreyCheck
import com.example.ui.theme.WhatsAppLightBubbleReceived
import com.example.ui.theme.WhatsAppLightBubbleSent
import com.example.ui.theme.WhatsAppLightTextPrimary
import com.example.ui.theme.WhatsAppLightTextSecondary
import com.example.ui.theme.WhatsAppTealDark

@Composable
fun NewsBubbleItem(
    article: NewsArticle,
    onArticleClick: (NewsArticle) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onToggleReadReceipt: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    // Determine bubble styling
    val bubbleBg = if (isDark) WhatsAppDarkBubbleSent else WhatsAppLightBubbleSent
    val textColor = if (isDark) WhatsAppDarkTextPrimary else WhatsAppLightTextPrimary
    val subTextColor = if (isDark) WhatsAppDarkTextSecondary else WhatsAppLightTextSecondary

    val localDrawableRes = when (article.drawableResName) {
        "img_news_defense" -> R.drawable.img_news_defense
        "img_news_cyber" -> R.drawable.img_news_cyber
        "img_markhor_logo" -> R.drawable.img_markhor_logo
        else -> R.drawable.img_markhor_logo
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        // Channel / ISI Sender Avatar
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(WhatsAppTealDark)
                .border(1.dp, MarkhorGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_markhor_logo),
                contentDescription = "Markhor Avatar",
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // WhatsApp Message Bubble Card
        Card(
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
                .clickable { onArticleClick(article) }
                .testTag("news_bubble_${article.id}")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Header inside bubble: Channel name, verified badge, priority indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.channelName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = WhatsAppAccentGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (article.isVerified) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Channel",
                            tint = MarkhorBadgeGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Surface(
                        color = when (article.priority) {
                            "URGENT" -> Color(0xFFD32F2F)
                            "HIGH" -> Color(0xFFF57C00)
                            else -> WhatsAppTealDark
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = article.category.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Optional Thumbnail Image (Coil or drawable fallback)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black.copy(alpha = 0.2f))
                ) {
                    if (article.imageUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(article.imageUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = localDrawableRes),
                            error = painterResource(id = localDrawableRes),
                            contentDescription = article.headline,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = localDrawableRes),
                            contentDescription = article.headline,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Headline
                Text(
                    text = article.headline,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Summary (Expandable)
                Text(
                    text = article.summary,
                    fontSize = 14.sp,
                    color = subTextColor,
                    lineHeight = 19.sp,
                    maxLines = if (isExpanded) 100 else 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (article.content.isNotBlank() && isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.content,
                        fontSize = 13.sp,
                        color = textColor.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Bar & Metadata Row (Read receipt, share, bookmark, timestamp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left action buttons: Expand, Bookmark, Share
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("expand_toggle_${article.id}")
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand details",
                                tint = subTextColor
                            )
                        }

                        IconButton(
                            onClick = { onToggleBookmark(article.id) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("bookmark_button_${article.id}")
                        ) {
                            Icon(
                                imageVector = if (article.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save article",
                                tint = if (article.isBookmarked) MarkhorGold else subTextColor
                            )
                        }

                        IconButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, article.headline)
                                    putExtra(Intent.EXTRA_TEXT, "📰 [Markhor News Channel]\n\n${article.headline}\n\n${article.summary}\n\nOfficial ISI Intelligence Feed")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share News Article"))
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("share_button_${article.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share news",
                                tint = subTextColor
                            )
                        }
                    }

                    // Right side: Timestamp + Double Checkmark Read Receipts
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onToggleReadReceipt(article) }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .testTag("read_receipt_${article.id}")
                    ) {
                        Text(
                            text = article.formattedTime,
                            fontSize = 11.sp,
                            color = subTextColor
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        // Double checkmark (Blue tick if read, Grey tick if unread)
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = if (article.isRead) "Read receipt (Blue ticks)" else "Delivered receipt",
                            tint = if (article.isRead) WhatsAppBlueCheck else WhatsAppGreyCheck,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
