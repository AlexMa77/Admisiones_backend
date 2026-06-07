package com.gestion.educativa.ui.matricula

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestion.educativa.data.model.Matricula
import com.gestion.educativa.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatriculaListScreen(viewModel: MatriculaViewModel, onDetail: (Int) -> Unit, onCreate: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val search by viewModel.searchQuery.collectAsState()
    val estado by viewModel.estadoFilter.collectAsState()
    var deleteTarget by remember { mutableStateOf<Matricula?>(null) }
    val estados = listOf("" to "Todos", "activa" to "Activa", "retirada" to "Retirada", "finalizada" to "Finalizada")

    deleteTarget?.let { m ->
        ConfirmDeleteDialog("Matrícula #${m.id}", onConfirm = { viewModel.delete(m.id) { deleteTarget = null } }, onDismiss = { deleteTarget = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matrículas (${state.totalCount})", fontWeight = FontWeight.Bold) },
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
            SearchBar(search, viewModel::onSearch, "Buscar matrículas...")
            Spacer(Modifier.height(8.dp))
            
            // Filter chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                estados.forEach { (value, label) ->
                    FilterChip(
                        selected = estado == value, 
                        onClick = { viewModel.onEstadoFilter(value) }, 
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            when {
                state.isLoading && state.items.isEmpty() -> LoadingIndicator()
                state.error != null && state.items.isEmpty() -> ErrorMessage(state.error!!) { viewModel.load(true) }
                state.items.isEmpty() -> EmptyMessage("No se encontraron matrículas")
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items, key = { it.id }) { item ->
                        EducationalCard(
                            onClick = { onDetail(item.id) },
                            indicatorColor = when(item.estado.lowercase()) {
                                "activa" -> MaterialTheme.colorScheme.tertiary
                                "finalizada" -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.error
                            }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.AssignmentTurnedIn, null, 
                                            tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("Matrícula #${item.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    item.estudianteNombre?.let { Text("Estudiante: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface) }
                                    item.materiaNombre?.let { Text("Materia: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline) }
                                    Text("Período: ${item.periodo} | Estado: ${item.estado.replaceFirstChar { it.uppercase() }}",
                                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
fun MatriculaDetailScreen(id: Int, viewModel: MatriculaViewModel, onEdit: (Int) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(id) { viewModel.loadDetail(id) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Matrícula", fontWeight = FontWeight.Bold) },
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
                    val m = state.selected!!
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
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AssignmentTurnedIn, null, 
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Text(
                            "Matrícula #${m.id}", 
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
                                SectionHeader("Información de Matrícula")
                                Spacer(Modifier.height(8.dp))
                                InfoRow("Estudiante", m.estudianteNombre ?: "ID: ${m.estudiante}")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Materia", m.materiaNombre ?: "ID: ${m.materia}")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Período", m.periodo)
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Estado", m.estado.replaceFirstChar { it.uppercase() })
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Fecha Matrícula", m.fechaMatricula)
                                m.creadoEn?.let {
                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                    InfoRow("Fecha de Creación", it.take(10))
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
fun MatriculaFormScreen(id: Int?, viewModel: MatriculaViewModel, onSuccess: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var estudianteId by remember { mutableStateOf("") }
    var materiaId by remember { mutableStateOf("") }
    var periodo by remember { mutableStateOf("") }
    var estadoSel by remember { mutableStateOf("activa") }
    var fecha by remember { mutableStateOf("") }
    var initialized by remember { mutableStateOf(false) }
    val estados = listOf("activa", "retirada", "finalizada")
    var expandedEstado by remember { mutableStateOf(false) }

    LaunchedEffect(id) { if (id != null) viewModel.loadDetail(id) }
    LaunchedEffect(state.selected) {
        if (id != null && !initialized && state.selected?.id == id) {
            val m = state.selected!!
            estudianteId = m.estudiante.toString(); materiaId = m.materia.toString()
            periodo = m.periodo; estadoSel = m.estado; fecha = m.fechaMatricula; initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (id == null) "Nueva Matrícula" else "Editar Matrícula", fontWeight = FontWeight.Bold) },
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
                        Text("Detalles de la Matrícula", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        
                        FormField("ID Estudiante *", estudianteId, { estudianteId = it })
                        FormField("ID Materia *", materiaId, { materiaId = it })
                        FormField("Período * (ej: 2024-A)", periodo, { periodo = it })
                        FormField("Fecha Matrícula * (YYYY-MM-DD)", fecha, { fecha = it })

                        ExposedDropdownMenuBox(expanded = expandedEstado, onExpandedChange = { expandedEstado = it }) {
                            OutlinedTextField(
                                value = estadoSel.replaceFirstChar { it.uppercase() },
                                onValueChange = {}, 
                                readOnly = true, 
                                label = { Text("Estado") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEstado) },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                            ExposedDropdownMenu(expanded = expandedEstado, onDismissRequest = { expandedEstado = false }) {
                                estados.forEach { e ->
                                    DropdownMenuItem(
                                        text = { Text(e.replaceFirstChar { it.uppercase() }) },
                                        onClick = { estadoSel = e; expandedEstado = false }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                GradientButton(
                    text = if (id == null) "Crear Matrícula" else "Guardar Cambios",
                    onClick = {
                        viewModel.save(id, estudianteId.toIntOrNull() ?: 0, materiaId.toIntOrNull() ?: 0,
                            periodo.trim(), estadoSel, fecha.trim()) { onSuccess() }
                    },
                    enabled = estudianteId.isNotBlank() && materiaId.isNotBlank() && periodo.isNotBlank() && fecha.isNotBlank(),
                    loading = state.isLoading,
                    icon = Icons.Default.Save
                )
            }
        }
    }
}
