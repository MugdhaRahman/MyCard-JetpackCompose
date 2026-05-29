package com.mrapps.mycard.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FuturisticDarkScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = OnPrimaryPurple,
    primaryContainer = PrimaryPurpleContainer,
    onPrimaryContainer = OnPrimaryPurpleContainer,
    secondary = SecondaryTeal,
    onSecondary = OnSecondaryTeal,
    secondaryContainer = SecondaryTealContainer,
    onSecondaryContainer = OnSecondaryTealContainer,
    tertiary = TertiaryMagenta,
    onTertiary = OnTertiaryMagenta,
    background = DeepSpaceBackground,
    onBackground = DeepSpaceOnSurface,
    surface = DeepSpaceSurface,
    onSurface = DeepSpaceOnSurface,
    surfaceVariant = DeepSpaceSurfaceVariant,
    onSurfaceVariant = DeepSpaceOnSurfaceVariant,
    outline = DeepSpaceOutline,
    error = FuturisticError,
    onError = FuturisticOnError
)

@Composable
fun MyCardTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FuturisticDarkScheme,
        typography = Typography,
        content = content
    )
}
