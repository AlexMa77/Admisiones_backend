package com.gestion.educativa.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.ui.graphics.vector.ImageVector

enum class UserRole(
    val displayName: String,
    val description: String,
    val icon: ImageVector,
    val gradientColors: List<Color>
) {
    ADMIN(
        displayName = "Coordinador de Admisión (Admin)",
        description = "Control total del proceso de admisión y nivelación académica",
        icon = Icons.Default.AdminPanelSettings,
        gradientColors = listOf(Color(0xFF4F46E5), Color(0xFF3730A3)) // Indigo
    ),
    DOCENTE(
        displayName = "Docente Evaluador",
        description = "Evaluación y registro de calificaciones de nivelación",
        icon = Icons.Default.SupervisorAccount,
        gradientColors = listOf(Color(0xFF059669), Color(0xFF047857)) // Emerald Green
    ),
    ESTUDIANTE(
        displayName = "Aspirante a Ingreso",
        description = "Consulta de asignaturas de nivelación y calificaciones",
        icon = Icons.Default.School,
        gradientColors = listOf(Color(0xFF0EA5E9), Color(0xFF0284C7)) // Sky Blue
    )
}

object RoleResolver {
    fun resolveRole(username: String?, isAdmin: Boolean): UserRole {
        if (isAdmin || username?.lowercase() == "admin") return UserRole.ADMIN
        val user = username.orEmpty().lowercase()
        return when {
            user.endsWith("_docente") -> UserRole.DOCENTE
            user.endsWith("_estudiante") -> UserRole.ESTUDIANTE
            else -> UserRole.ESTUDIANTE // Fallback for other registered users
        }
    }
}
