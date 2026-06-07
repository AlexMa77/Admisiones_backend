package com.gestion.educativa.ui.materia

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestion.educativa.data.model.Materia
import com.gestion.educativa.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaListScreen(viewModel: MateriaViewModel, onDetail: (Int) -> Unit, onCreate: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val search by viewModel.searchQuery.collectAsState()
    var deleteTarget by remember { mutableStateOf<Materia?>(null) }

    deleteTarget?.let { m ->
        ConfirmDeleteDialog(m.nombre, onConfirm = { viewModel.delete(m.id) { deleteTarget = null } }, onDismiss = { deleteTarget = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Materias (${state.totalCount})", fontWeight = FontWeight.Bold) },
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
            SearchBar(search, viewModel::onSearch, "Buscar materias...")
            Spacer(Modifier.height(12.dp))
            when {
                state.isLoading && state.items.isEmpty() -> LoadingIndicator()
                state.error != null && state.items.isEmpty() -> ErrorMessage(state.error!!) { viewModel.load(true) }
                state.items.isEmpty() -> EmptyMessage("No se encontraron materias")
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items, key = { it.id }) { item ->
                        EducationalCard(
                            onClick = { onDetail(item.id) },
                            indicatorColor = MaterialTheme.colorScheme.secondary
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.MenuBook, null, 
                                            tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("${item.codigo} | Créditos: ${item.creditos} | Semestre: ${item.semestre}",
                                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    item.docenteNombre?.let { Text("Docente: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline) }
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
fun MateriaDetailScreen(id: Int, viewModel: MateriaViewModel, onEdit: (Int) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(id) { viewModel.loadDetail(id) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Materia", fontWeight = FontWeight.Bold) },
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
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.MenuBook, null, 
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Text(
                            m.nombre, 
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
                                SectionHeader("Información Académica")
                                Spacer(Modifier.height(8.dp))
                                InfoRow("Código", m.codigo)
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Carrera", m.carreraNombre ?: "ID: ${m.carrera}")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Docente", m.docenteNombre ?: "ID: ${m.docente}")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Créditos", m.creditos.toString())
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Semestre", m.semestre.toString())
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Estado", if (m.activo) "Activo" else "Inactivo")
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
fun MateriaFormScreen(id: Int?, viewModel: MateriaViewModel, onSuccess: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var carreraId by remember { mutableStateOf("") }
    var docenteId by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var creditos by remember { mutableStateOf("3") }
    var semestre by remember { mutableStateOf("1") }
    var activo by remember { mutableStateOf(true) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(id) { if (id != null) viewModel.loadDetail(id) }
    LaunchedEffect(state.selected) {
        if (id != null && !initialized && state.selected?.id == id) {
            val m = state.selected!!
            carreraId = m.carrera.toString(); docenteId = m.docente.toString(); nombre = m.nombre
            codigo = m.codigo; creditos = m.creditos.toString(); semestre = m.semestre.toString()
            activo = m.activo; initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (id == null) "Nueva Materia" else "Editar Materia", fontWeight = FontWeight.Bold) },
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
                        Text("Detalles de la Materia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        
                        FormField("ID Carrera *", carreraId, { carreraId = it })
                        FormField("ID Docente *", docenteId, { docenteId = it })
                        FormField("Nombre *", nombre, { nombre = it })
                        FormField("Código *", codigo, { codigo = it })
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = creditos, 
                                onValueChange = { creditos = it },
                                label = { Text("Créditos") }, 
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true, 
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                            OutlinedTextField(
                                value = semestre, 
                                onValueChange = { semestre = it },
                                label = { Text("Semestre") }, 
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true, 
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                        }
                        
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
                    text = if (id == null) "Crear Materia" else "Guardar Cambios",
                    onClick = {
                        viewModel.save(id, carreraId.toIntOrNull() ?: 0, docenteId.toIntOrNull() ?: 0,
                            nombre.trim(), codigo.trim(), creditos.toIntOrNull() ?: 3, semestre.toIntOrNull() ?: 1, activo) { onSuccess() }
                    },
                    enabled = carreraId.isNotBlank() && docenteId.isNotBlank() && nombre.isNotBlank() && codigo.isNotBlank(),
                    loading = state.isLoading,
                    icon = Icons.Default.Save
                )
            }
        }
    }
}
