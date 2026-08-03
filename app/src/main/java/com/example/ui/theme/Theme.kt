package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WhatsAppAccentGreen,
    onPrimary = WhatsAppDarkTextPrimary,
    primaryContainer = WhatsAppTealDark,
    onPrimaryContainer = WhatsAppDarkTextPrimary,
    secondary = WhatsAppTealHeader,
    onSecondary = WhatsAppDarkTextPrimary,
    background = WhatsAppDarkBackground,
    onBackground = WhatsAppDarkTextPrimary,
    surface = WhatsAppDarkSurface,
    onSurface = WhatsAppDarkTextPrimary,
    surfaceVariant = WhatsAppDarkBubbleReceived,
    onSurfaceVariant = WhatsAppDarkTextSecondary,
    tertiary = MarkhorGold
)

private val LightColorScheme = lightColorScheme(
    primary = WhatsAppTealDark,
    onPrimary = WhatsAppLightSurface,
    primaryContainer = WhatsAppTealHeader,
    onPrimaryContainer = WhatsAppLightSurface,
    secondary = WhatsAppAccentGreen,
    onSecondary = WhatsAppLightSurface,
    background = WhatsAppLightBackground,
    onBackground = WhatsAppLightTextPrimary,
    surface = WhatsAppLightSurface,
    onSurface = WhatsAppLightTextPrimary,
    surfaceVariant = WhatsAppLightBubbleSent,
    onSurfaceVariant = WhatsAppLightTextSecondary,
    tertiary = MarkhorGold
)

@Composable
fun MarkhorNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent WhatsApp branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
