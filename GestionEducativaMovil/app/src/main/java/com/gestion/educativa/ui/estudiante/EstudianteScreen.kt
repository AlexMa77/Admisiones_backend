package com.gestion.educativa.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestion.educativa.data.model.Estudiante
import com.gestion.educativa.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteListScreen(viewModel: EstudianteViewModel, onDetail: (Int) -> Unit, onCreate: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val search by viewModel.searchQuery.collectAsState()
    var deleteTarget by remember { mutableStateOf<Estudiante?>(null) }

    deleteTarget?.let { e ->
        ConfirmDeleteDialog(e.nombreCompleto, onConfirm = { viewModel.delete(e.id) { deleteTarget = null } }, onDismiss = { deleteTarget = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes (${state.totalCount})", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        floatingActionButton = {
            if (viewModel.tokenManager.isAdmin) {
                FloatingActionButton(
                    onClick = onCreate,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            SearchBar(search, viewModel::onSearch, "Buscar estudiantes...")
            Spacer(Modifier.height(12.dp))
            when {
                state.isLoading && state.items.isEmpty() -> LoadingIndicator()
                state.error != null && state.items.isEmpty() -> ErrorMessage(state.error!!) { viewModel.load(true) }
                state.items.isEmpty() -> EmptyMessage("No se encontraron estudiantes")
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items, key = { it.id }) { item ->
                        EducationalCard(
                            onClick = { onDetail(item.id) },
                            indicatorColor = MaterialTheme.colorScheme.primary
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.Groups, null, 
                                            tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(item.nombreCompleto, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Cédula: ${item.cedula} | Sem. ${item.semestreActual}", 
                                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    item.carreraNombre?.let { Text("Carrera: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline) }
                                    StatusChip(item.activo, Modifier.padding(top = 6.dp))
                                }
                                if (viewModel.tokenManager.isAdmin) IconButton(onClick = { deleteTarget = item }) {
                                    Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) }
                            }
                        }
                    }
                    if (state.hasMore) item { LoadMoreButton({ viewModel.load() }, state.isLoadingMore) }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteDetailScreen(id: Int, viewModel: EstudianteViewModel, onEdit: (Int) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(id) { viewModel.loadDetail(id) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Estudiante", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                actions = {
                    if (viewModel.tokenManager.isAdmin) IconButton(onClick = { onEdit(id) }) { Icon(Icons.Default.Edit, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            when {
                state.isLoading -> LoadingIndicator()
                state.selected == null -> EmptyMessage()
                else -> {
                    val e = state.selected!!
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Groups, null, 
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Text(
                            e.nombreCompleto, 
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                SectionHeader("Información Personal")
                                Spacer(Modifier.height(8.dp))
                                InfoRow("Cédula", e.cedula)
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Teléfono", e.telefono)
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Semestre Actual", e.semestreActual.toString())
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Carrera", e.carreraNombre ?: "ID: ${e.carrera}")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Estado", if (e.activo) "Activo" else "Inactivo")
                                
                                e.userInfo?.let {
                                    Spacer(Modifier.height(16.dp))
                                    SectionHeader("Cuenta de Usuario")
                                    Spacer(Modifier.height(8.dp))
                                    InfoRow("Usuario", it.username)
                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                    InfoRow("Email", it.email ?: "—")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteFormScreen(id: Int?, viewModel: EstudianteViewModel, onSuccess: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var userId by remember { mutableStateOf("") }
    var carreraId by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var semestre by remember { mutableStateOf("1") }
    var activo by remember { mutableStateOf(true) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(id) { if (id != null) viewModel.loadDetail(id) }
    LaunchedEffect(state.selected) {
        if (id != null && !initialized && state.selected?.id == id) {
            val e = state.selected!!
            userId = e.user.toString(); carreraId = e.carrera.toString(); cedula = e.cedula
            telefono = e.telefono; semestre = e.semestreActual.toString(); activo = e.activo; initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (id == null) "Nuevo Estudiante" else "Editar Estudiante", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.error?.let { 
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(it, Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall) 
                    } 
                }
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Detalles del Estudiante", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        
                        FormField("ID de Usuario *", userId, { userId = it })
                        FormField("ID de Carrera *", carreraId, { carreraId = it })
                        FormField("Cédula *", cedula, { cedula = it })
                        FormField("Teléfono", telefono, { telefono = it })
                        
                        OutlinedTextField(
                            value = semestre, 
                            onValueChange = { semestre = it },
                            label = { Text("Semestre Actual") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true, 
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Switch(
                                checked = activo, 
                                onCheckedChange = { activo = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(if (activo) "Estado: Activo" else "Estado: Inactivo", fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                GradientButton(
                    text = if (id == null) "Crear Estudiante" else "Guardar Cambios",
                    onClick = {
                        viewModel.save(id, userId.toIntOrNull() ?: 0, carreraId.toIntOrNull() ?: 0,
                            cedula.trim(), telefono.trim(), semestre.toIntOrNull() ?: 1, activo) { onSuccess() }
                    },
                    enabled = userId.isNotBlank() && carreraId.isNotBlank() && cedula.isNotBlank(),
                    loading = state.isLoading,
                    icon = Icons.Default.Save
                )
            }
        }
    }
}
