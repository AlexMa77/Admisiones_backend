package com.gestion.educativa.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestion.educativa.ui.auth.AuthViewModel
import com.gestion.educativa.ui.navigation.Screen

data class MenuModule(val title: String, val icon: ImageVector, val route: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val state by authViewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val modules = listOf(
        MenuModule("Facultades", Icons.Default.AccountBalance, Screen.FacultadList.route,
            MaterialTheme.colorScheme.primary),
        MenuModule("Carreras", Icons.Default.School, Screen.CarreraList.route,
            MaterialTheme.colorScheme.secondary),
        MenuModule("Docentes", Icons.Default.Person, Screen.DocenteList.route,
            MaterialTheme.colorScheme.tertiary),
        MenuModule("Estudiantes", Icons.Default.Groups, Screen.EstudianteList.route,
            MaterialTheme.colorScheme.primary),
        MenuModule("Materias", Icons.Default.MenuBook, Screen.MateriaList.route,
            MaterialTheme.colorScheme.secondary),
        MenuModule("Matrículas", Icons.Default.AssignmentTurnedIn, Screen.MatriculaList.route,
            MaterialTheme.colorScheme.tertiary),
        MenuModule("Notas", Icons.Default.Grade, Screen.NotaList.route,
            MaterialTheme.colorScheme.primary),
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Cerrar Sesión", fontWeight = FontWeight.Bold) },
            text = { Text("¿Deseas salir del sistema de gestión académica?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    authViewModel.logout()
                    onLogout()
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                    Text("Salir", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Admisión de Instituto Educativo", 
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            // User Welcome Header Banner with Custom Gradient and Rounded Bottom Corners
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(headerGradient)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "¡Hola, ${state.username ?: "Usuario"}!",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (state.isAdmin) "Coordinador de Admisiones (Admin)" else "Asistente de Admisiones",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    if (state.isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    // Admissions Stats Dashboard Widget
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Admisiones 2026-I",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = "Activo",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("6", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                    Text("Postulantes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("9", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                    Text("Matrículas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("6", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                    Text("Cursos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                if (state.isAdmin) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Panel de control administrativo activo.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    text = "Módulos del Sistema",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(modules) { module ->
                        ModuleCard(module = module, onClick = { onNavigate(module.route) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(module: MenuModule, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Bubble
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = module.color.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        module.icon,
                        contentDescription = module.title,
                        modifier = Modifier.size(28.dp),
                        tint = module.color
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}
