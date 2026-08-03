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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.MarkhorGold
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppTealHeader

@Composable
fun WhatsAppTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onRefresh: () -> Unit,
    onOpenNewDispatch: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val categories = listOf("All", "Defense", "Cyber", "Geopolitics", "Intelligence", "National")

    Surface(
        color = WhatsAppTealHeader,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            // Main Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSearchActive) {
                    IconButton(
                        onClick = {
                            isSearchActive = false
                            onSearchQueryChange("")
                        },
                        modifier = Modifier.testTag("close_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search intelligence dispatches...",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 15.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                            cursorBrush = SolidColor(Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_input_field")
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    // Profile/Logo Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(1.5.dp, MarkhorGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_markhor_logo),
                            contentDescription = "Markhor ISI Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Markhor News Channel",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Official ISI Verified",
                                tint = WhatsAppAccentGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Inter Services Intelligence • Official Feed",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }

                    // Top Bar Action Buttons
                    IconButton(
                        onClick = { isSearchActive = true },
                        modifier = Modifier.testTag("search_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier.testTag("refresh_news_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Feed",
                            tint = Color.White
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu Options",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("New Dispatch") },
                                onClick = {
                                    showMenu = false
                                    onOpenNewDispatch()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Refresh Feed") },
                                onClick = {
                                    showMenu = false
                                    onRefresh()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Official Portal: isi.gov.pk") },
                                onClick = { showMenu = false }
                            )
                        }
                    }
                }
            }

            // Horizontal Scrollable Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelect(category) },
                        label = {
                            Text(
                                text = category,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = WhatsAppAccentGreen,
                            containerColor = Color.White.copy(alpha = 0.15f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) WhatsAppAccentGreen else Color.White.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("category_chip_$category")
                    )
                }
            }
        }
    }
}
