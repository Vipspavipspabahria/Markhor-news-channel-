package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.NewsChannelInfo
import com.example.ui.theme.MarkhorBadgeGreen
import com.example.ui.theme.MarkhorGold
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppTealDark

@Composable
fun ChannelsScreen(
    channels: List<NewsChannelInfo>,
    onToggleSubscribe: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = WhatsAppTealDark),
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
                            imageVector = Icons.Default.RssFeed,
                            contentDescription = "Channels",
                            tint = MarkhorGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "WhatsApp Channels Directory",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Stay updated with official security broadcasts from Inter Services Intelligence.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "FEATURED INTELLIGENCE CHANNELS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = WhatsAppAccentGreen,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        items(channels, key = { it.channelId }) { channel ->
            ChannelCardItem(
                channel = channel,
                onToggleSubscribe = { onToggleSubscribe(channel.channelId) }
            )
        }
    }
}

@Composable
fun ChannelCardItem(
    channel: NewsChannelInfo,
    onToggleSubscribe: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("channel_item_${channel.channelId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(WhatsAppTealDark)
                    .border(1.dp, MarkhorGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_markhor_logo),
                    contentDescription = channel.name,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = channel.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (channel.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = MarkhorBadgeGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = channel.followerCount,
                    fontSize = 12.sp,
                    color = WhatsAppAccentGreen,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = channel.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (channel.isSubscribed) {
                OutlinedButton(
                    onClick = onToggleSubscribe,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WhatsAppAccentGreen),
                    modifier = Modifier.testTag("follow_button_${channel.channelId}")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Following", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Following", fontSize = 12.sp)
                }
            } else {
                Button(
                    onClick = onToggleSubscribe,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppAccentGreen),
                    modifier = Modifier.testTag("follow_button_${channel.channelId}")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Follow", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Follow", fontSize = 12.sp)
                }
            }
        }
    }
}
