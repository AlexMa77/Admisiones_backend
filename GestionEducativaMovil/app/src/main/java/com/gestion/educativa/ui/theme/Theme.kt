package com.gestion.educativa.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = White,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = TextDark,
    secondary = SkySecondary,
    onSecondary = White,
    secondaryContainer = SkyContainer,
    tertiary = EmeraldTertiary,
    error = RoseError,
    errorContainer = RoseErrorContainer,
    background = SlateBackground,
    surface = White,
    surfaceVariant = SlateSurfaceVariant,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextMuted
)

@Composable
fun GestionEducativaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
