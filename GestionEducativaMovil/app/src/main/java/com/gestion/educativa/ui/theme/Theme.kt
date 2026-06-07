package com.gestion.educativa.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.gestion.educativa.data.model.UserRole

@Composable
fun GestionEducativaTheme(
    userRole: UserRole = UserRole.ESTUDIANTE,
    content: @Composable () -> Unit
) {
    val roleColor = userRole.gradientColors.first()
    val roleContainerColor = when (userRole) {
        UserRole.ADMIN -> IndigoContainer
        UserRole.DOCENTE -> EmeraldContainer
        UserRole.ESTUDIANTE -> SkyContainer
    }

    val dynamicColors = lightColorScheme(
        primary = roleColor,
        onPrimary = White,
        primaryContainer = roleContainerColor,
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

    MaterialTheme(
        colorScheme = dynamicColors,
        typography = Typography,
        content = content
    )
}
