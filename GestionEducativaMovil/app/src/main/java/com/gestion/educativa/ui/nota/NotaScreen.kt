package com.gestion.educativa.ui.nota

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
import com.gestion.educativa.data.model.Nota
import com.gestion.educativa.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotaListScreen(viewModel: NotaViewModel, onDetail: (Int) -> Unit, onCreate: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var deleteTarget by remember { mutableStateOf<Nota?>(null) }

    deleteTarget?.let { n ->
        ConfirmDeleteDialog("Nota #${n.id}", onConfirm = { viewModel.delete(n.id) { deleteTarget = null } }, onDismiss = { deleteTarget = null })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notas (${state.totalCount})", fontWeight = FontWeight.Bold) },
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
            when {
                state.isLoading && state.items.isEmpty() -> LoadingIndicator()
                state.error != null && state.items.isEmpty() -> ErrorMessage(state.error!!) { viewModel.load(true) }
                state.items.isEmpty() -> EmptyMessage("No se encontraron notas")
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items, key = { it.id }) { item ->
                        val aprobado = item.aprobado ?: ((item.notaFinal ?: 0.0) >= 7.0)
                        EducationalCard(
                            onClick = { onDetail(item.id) },
                            indicatorColor = if (aprobado) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = CircleShape,
                                    color = (if (aprobado) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error).copy(alpha = 0.08f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.Grade, null, 
                                            tint = if (aprobado) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error, 
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("Nota #${item.id} — Matrícula #${item.matricula}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("P1: ${item.parcial1 ?: "—"} | P2: ${item.parcial2 ?: "—"} | Final: ${item.examenFinal ?: "—"}",
                                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    item.notaFinal?.let { nf ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 6.dp)
                                        ) {
                                            Text(
                                                "Nota Final: ${String.format("%.2f", nf)}  ",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            StatusChip(aprobado)
                                        }
                                    }
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
fun NotaDetailScreen(id: Int, viewModel: NotaViewModel, onEdit: (Int) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(id) { viewModel.loadDetail(id) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Nota", fontWeight = FontWeight.Bold) },
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
                    val n = state.selected!!
                    val aprobado = n.aprobado ?: ((n.notaFinal ?: 0.0) >= 7.0)
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
                            color = (if (aprobado) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error).copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Grade, null, 
                                    tint = if (aprobado) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Text(
                            "Nota #${n.id}", 
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
                                SectionHeader("Calificaciones")
                                Spacer(Modifier.height(8.dp))
                                InfoRow("Matrícula ID", n.matricula.toString())
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Parcial 1", n.parcial1?.let { String.format("%.2f", it) } ?: "—")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Parcial 2", n.parcial2?.let { String.format("%.2f", it) } ?: "—")
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                InfoRow("Examen Final", n.examenFinal?.let { String.format("%.2f", it) } ?: "—")
                                
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 8.dp))
                                SectionHeader("Resultado")
                                Spacer(Modifier.height(8.dp))
                                n.notaFinal?.let {
                                    InfoRow("Nota Final", String.format("%.2f", it))
                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                    ) {
                                        Text("Estado de Aprobación", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium, modifier = Modifier.weight(0.4f))
                                        Box(modifier = Modifier.weight(0.6f)) {
                                            StatusChip(aprobado)
                                        }
                                    }
                                }
                                n.actualizadoEn?.let {
                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f), modifier = Modifier.padding(vertical = 4.dp))
                                    InfoRow("Última Actualización", it.take(10))
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
fun NotaFormScreen(id: Int?, viewModel: NotaViewModel, onSuccess: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var matriculaId by remember { mutableStateOf("") }
    var parcial1 by remember { mutableStateOf("") }
    var parcial2 by remember { mutableStateOf("") }
    var examenFinal by remember { mutableStateOf("") }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(id) { if (id != null) viewModel.loadDetail(id) }
    LaunchedEffect(state.selected) {
        if (id != null && !initialized && state.selected?.id == id) {
            val n = state.selected!!
            matriculaId = n.matricula.toString()
            parcial1 = n.parcial1?.toString() ?: ""; parcial2 = n.parcial2?.toString() ?: ""
            examenFinal = n.examenFinal?.toString() ?: ""; initialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (id == null) "Nueva Nota" else "Editar Nota", fontWeight = FontWeight.Bold) },
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
                        Text("Detalles de Calificación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        
                        FormField("ID Matrícula *", matriculaId, { matriculaId = it })
                        
                        Text("Calificaciones (0-10)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        
                        OutlinedTextField(
                            value = parcial1, 
                            onValueChange = { parcial1 = it },
                            label = { Text("Parcial 1") }, 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true, 
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                        OutlinedTextField(
                            value = parcial2, 
                            onValueChange = { parcial2 = it },
                            label = { Text("Parcial 2") }, 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true, 
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                        OutlinedTextField(
                            value = examenFinal, 
                            onValueChange = { examenFinal = it },
                            label = { Text("Examen Final") }, 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true, 
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                        
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Nota: La nota final y el estado de aprobación son calculados de forma automática por el servidor.",
                                modifier = Modifier.padding(12.dp), 
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                GradientButton(
                    text = if (id == null) "Crear Nota" else "Guardar Cambios",
                    onClick = {
                        viewModel.save(id, matriculaId.toIntOrNull() ?: 0,
                            parcial1.toDoubleOrNull(), parcial2.toDoubleOrNull(), examenFinal.toDoubleOrNull()) { onSuccess() }
                    },
                    enabled = matriculaId.isNotBlank(),
                    loading = state.isLoading,
                    icon = Icons.Default.Save
                )
            }
        }
    }
}
