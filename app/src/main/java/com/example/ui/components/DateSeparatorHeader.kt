package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppDarkBubbleReceived
import com.example.ui.theme.WhatsAppDarkTextSecondary
import com.example.ui.theme.WhatsAppLightTextSecondary

@Composable
fun DateSeparatorHeader(
    dateText: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val pillBg = if (isDark) WhatsAppDarkBubbleReceived else Color(0xFFE1F5FE)
    val textColor = if (isDark) WhatsAppDarkTextSecondary else Color(0xFF546E7A)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = pillBg,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 1.dp
        ) {
            Text(
                text = dateText.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                letterSpacing = 0.5.sp
            )
        }
    }
}
