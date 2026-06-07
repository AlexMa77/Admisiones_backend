package com.gestion.educativa.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.gestion.educativa.ui.auth.AuthViewModel
import com.gestion.educativa.ui.navigation.Screen
import com.gestion.educativa.data.model.UserRole
import com.gestion.educativa.data.model.Estudiante
import com.gestion.educativa.data.model.Matricula
import com.gestion.educativa.data.model.Nota

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

    val userRole = state.role
    
    // Resolve dynamic modules list based on user role
    val modules = remember(userRole) {
        when (userRole) {
            UserRole.ADMIN -> listOf(
                MenuModule("Facultades", Icons.Default.AccountBalance, Screen.FacultadList.route, Color(0xFF6366F1)),
                MenuModule("Carreras", Icons.Default.School, Screen.CarreraList.route, Color(0xFF0EA5E9)),
                MenuModule("Docentes", Icons.Default.SupervisorAccount, Screen.DocenteList.route, Color(0xFF8B5CF6)),
                MenuModule("Estudiantes", Icons.Default.Groups, Screen.EstudianteList.route, Color(0xFFEC4899)),
                MenuModule("Materias", Icons.Default.MenuBook, Screen.MateriaList.route, Color(0xFFF59E0B)),
                MenuModule("Matrículas", Icons.Default.AssignmentTurnedIn, Screen.MatriculaList.route, Color(0xFF10B981)),
                MenuModule("Notas", Icons.Default.Grade, Screen.NotaList.route, Color(0xFFEF4444))
            )
            UserRole.DOCENTE -> listOf(
                MenuModule("Estudiantes", Icons.Default.Groups, Screen.EstudianteList.route, Color(0xFFEC4899)),
                MenuModule("Materias", Icons.Default.MenuBook, Screen.MateriaList.route, Color(0xFFF59E0B)),
                MenuModule("Notas", Icons.Default.Grade, Screen.NotaList.route, Color(0xFFEF4444))
            )
            UserRole.ESTUDIANTE -> emptyList() // The student has their own dashboard layout
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Cerrar Sesión", fontWeight = FontWeight.Bold) },
            text = { Text("¿Deseas salir del sistema de admisión y nivelación?") },
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
        colors = userRole.gradientColors
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (userRole == UserRole.ESTUDIANTE) "Portal del Aspirante" else "Portal de Admisiones", 
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = userRole.gradientColors.first(),
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
        if (userRole == UserRole.ESTUDIANTE) {
            val studentPortalViewModel: StudentPortalViewModel = hiltViewModel()
            StudentPortalContent(
                viewModel = studentPortalViewModel,
                paddingValues = padding,
                headerGradient = headerGradient,
                displayName = state.username?.replaceFirstChar { it.uppercase() } ?: "Usuario",
                userRole = userRole
            )
        } else {
            // Classical modules layout for Admin and Docente
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
                                val displayName = state.username?.replaceFirstChar { it.uppercase() } ?: "Usuario"
                                Text(
                                    text = "¡Hola, $displayName!",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = userRole.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                modifier = Modifier.size(52.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        userRole.icon,
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
                                when (userRole) {
                                    UserRole.ADMIN -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Control de Admisiones 2026-I",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = Color.White.copy(alpha = 0.2f)
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
                                    UserRole.DOCENTE -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Evaluador Académico",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = Color.White.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = "Fase 1",
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
                                                Text("3", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                                Text("Grupos Asignados", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("8", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                                Text("Notas Cargadas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("95%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                                Text("Avance Registro", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                                            }
                                        }
                                    }
                                    else -> {}
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
                    // Info Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, userRole.gradientColors.first().copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = userRole.gradientColors.first().copy(alpha = 0.08f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                userRole.icon,
                                contentDescription = null,
                                tint = userRole.gradientColors.first(),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = when(userRole) {
                                    UserRole.ADMIN -> "Panel de control administrativo activo."
                                    UserRole.DOCENTE -> "Módulos de calificación y asignaturas habilitados."
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = userRole.gradientColors.first(),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))

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
}

@Composable
fun StudentPortalContent(
    viewModel: StudentPortalViewModel,
    paddingValues: PaddingValues,
    headerGradient: Brush,
    displayName: String,
    userRole: UserRole
) {
    val state by viewModel.state.collectAsState()
    
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    if (state.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.ErrorOutline, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(56.dp))
                Spacer(Modifier.height(12.dp))
                Text(state.error ?: "Error al cargar la información", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.loadStudentData() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Reintentar")
                }
            }
        }
        return
    }

    val student = state.student
    if (student == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("No se encontró información del aspirante.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        // Welcoming header specifically for student
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(headerGradient)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${student.userInfo?.firstName ?: displayName}!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${userRole.displayName} — ${student.carreraNombre ?: "Nivelación"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            userRole.icon,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- 1. ESTADO DE ADMISIÓN CARD ---
            val totalMatriculas = state.matriculas.size
            val totalNotas = state.notas.size
            val hasReprobada = state.notas.values.any { it.aprobado == false }
            
            val status = when {
                totalMatriculas == 0 -> "Pendiente"
                hasReprobada -> "Reprobado"
                totalNotas < totalMatriculas -> "En Evaluación"
                else -> "Aprobado / Admitido"
            }

            val statusColor = when (status) {
                "Aprobado / Admitido" -> Color(0xFF10B981) // Emerald Green
                "Reprobado" -> Color(0xFFEF4444) // Red
                else -> Color(0xFFF59E0B) // Amber
            }

            val statusBgColor = statusColor.copy(alpha = 0.08f)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.2.dp, statusColor.copy(alpha = 0.25f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = statusBgColor)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Ficha de Admisión",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = statusColor
                        ) {
                            Text(
                                text = status,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    val average = if (totalNotas > 0) {
                        state.notas.values.mapNotNull { it.notaFinal }.average()
                    } else 0.0

                    if (totalNotas > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Promedio Final obtenido:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%.2f / 10.00", average),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = statusColor
                            )
                        }
                    }

                    HorizontalDivider(color = statusColor.copy(alpha = 0.15f), thickness = 1.dp)

                    Text(
                        text = when (status) {
                            "Aprobado / Admitido" -> "¡Felicidades! Has aprobado el examen de admisión y fuiste formalmente admitido en la carrera de ${student.carreraNombre}. Se han registrado tus asignaturas del ${student.semestreActual}° Semestre."
                            "Reprobado" -> "Estimado aspirante, tu promedio de admisión no alcanza el puntaje mínimo de aprobación (7.00). Por favor, contacta con secretaría académica para solicitar el examen remedial."
                            "En Evaluación" -> "Tus exámenes están siendo evaluados por los docentes responsables. Los resultados se publicarán aquí en tiempo real una vez finalizados."
                            else -> "No registras asignaturas de nivelación activas. Comunícate con el Coordinador de Admisiones."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // --- 2. DATOS PERSONALES CARD ---
            Text(
                "Mis Datos Personales",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRowItem("Nombres", student.nombreCompleto)
                    InfoRowItem("Cédula", student.cedula)
                    InfoRowItem("Teléfono", student.telefono)
                    InfoRowItem("Carrera", student.carreraNombre ?: "No asignada")
                    InfoRowItem("Semestre", "${student.semestreActual}° Semestre")
                    InfoRowItem("Email", student.userInfo?.email ?: "No registrado")
                }
            }

            // --- 3. EXAMENES Y MATERIAS CARD ---
            Text(
                "Mis Exámenes de Admisión",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (totalMatriculas == 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No tienes materias de nivelación asignadas.", textAlign = TextAlign.Center)
                    }
                }
            } else {
                state.matriculas.forEach { matricula ->
                    val nota = state.notas[matricula.id]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = matricula.materiaNombre ?: "Materia de Admisión",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                                if (nota != null) {
                                    val matAprobado = nota.aprobado ?: ((nota.notaFinal ?: 0.0) >= 7.0)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = (if (matAprobado) Color(0xFF10B981) else Color(0xFFEF4444)).copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = if (matAprobado) "Aprobada" else "Reprobada",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (matAprobado) Color(0xFF10B981) else Color(0xFFEF4444)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Periodo Académico: ${matricula.periodo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                            if (nota != null) {
                                Text(
                                    text = "Calificaciones del Examen",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(Modifier.height(4.dp))
                                
                                // Exam Scores Grid
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = nota.parcial1?.let { String.format("%.2f", it) } ?: "—",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Raz. Lógico",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = nota.parcial2?.let { String.format("%.2f", it) } ?: "—",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Comp. Lectora",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = nota.examenFinal?.let { String.format("%.2f", it) } ?: "—",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Conoc. Especialidad",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Promedio Final:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = nota.notaFinal?.let { String.format("%.2f", it) } ?: "—",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.HourglassEmpty, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Calificación del examen pendiente de revisión.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun InfoRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ModuleCard(module: MenuModule, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(1.dp, module.color.copy(alpha = 0.12f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Bubble
            Surface(
                modifier = Modifier.size(52.dp),
                shape = CircleShape,
                color = module.color.copy(alpha = 0.08f),
                border = androidx.compose.foundation.BorderStroke(1.dp, module.color.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        module.icon,
                        contentDescription = module.title,
                        modifier = Modifier.size(24.dp),
                        tint = module.color
                    )
                }
            }
            
            Spacer(Modifier.height(10.dp))
            
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}


