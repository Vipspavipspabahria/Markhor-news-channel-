package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.local.NewsStatus
import com.example.ui.theme.MarkhorGold
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppTealDark
import kotlinx.coroutines.delay

@Composable
fun StatusStoriesBar(
    statuses: List<NewsStatus>,
    onStatusClick: (NewsStatus) -> Unit,
    onOpenNewDispatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                text = "INTELLIGENCE BULLETINS & STATUS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsAppAccentGreen,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "My Status / Post Dispatch" Circle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onOpenNewDispatch() }
                        .testTag("add_status_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(WhatsAppTealDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_markhor_logo),
                            contentDescription = "Post Bulletin",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(WhatsAppAccentGreen)
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Post Alert", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }

                // List of Intelligence Status Bulletins
                statuses.forEach { status ->
                    val ringColor = if (status.isSeen) Color.Gray.copy(alpha = 0.5f) else WhatsAppAccentGreen
                    val localImage = when (status.drawableResName) {
                        "img_news_cyber" -> R.drawable.img_news_cyber
                        "img_news_defense" -> R.drawable.img_news_defense
                        else -> R.drawable.img_markhor_logo
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onStatusClick(status) }
                            .testTag("status_item_${status.id}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.5.dp, ringColor, CircleShape)
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = localImage),
                                contentDescription = status.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = status.title,
                            fontSize = 11.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusStoryViewerModal(
    status: NewsStatus,
    onClose: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(status.id) {
        progress = 0f
        while (progress < 1f) {
            delay(50)
            progress += 0.01f
        }
        onClose()
    }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            val localImage = when (status.drawableResName) {
                "img_news_cyber" -> R.drawable.img_news_cyber
                "img_news_defense" -> R.drawable.img_news_defense
                else -> R.drawable.img_markhor_logo
            }

            // Fullscreen Background Image
            Image(
                painter = painterResource(id = localImage),
                contentDescription = status.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Progress Timer Bar + Close Button
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { progress },
                        color = WhatsAppAccentGreen,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(WhatsAppTealDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_markhor_logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Markhor Special Bulletin",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Official ISI Intelligence Stream",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close story",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Story Content Card at Bottom
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WhatsAppTealDark.copy(alpha = 0.95f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Category",
                                tint = MarkhorGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = status.category.uppercase(),
                                color = MarkhorGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = status.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = status.fullText,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
