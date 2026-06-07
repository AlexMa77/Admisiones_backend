package com.gestion.educativa.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import com.gestion.educativa.ui.components.GradientButton
import com.gestion.educativa.data.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.ESTUDIANTE) }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            viewModel.clearMessages()
            onRegistered()
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.background
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(Modifier.height(8.dp))

                // Register Form Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(24.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Registro de Datos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        state.error?.let { error ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = error,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = viewModel::clearMessages,
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Selecciona tu Rol",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val roles = listOf(UserRole.ESTUDIANTE, UserRole.DOCENTE)
                            roles.forEach { role ->
                                val isSelected = selectedRole == role
                                val borderColor = if (isSelected) role.gradientColors.first() else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                val bgColor = if (isSelected) role.gradientColors.first().copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                                val tintColor = if (isSelected) role.gradientColors.first() else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                        .clickable { selectedRole = role },
                                    colors = CardDefaults.cardColors(containerColor = bgColor),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = role.icon,
                                            contentDescription = null,
                                            tint = tintColor,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            text = when(role) {
                                                UserRole.ESTUDIANTE -> "Aspirante"
                                                UserRole.DOCENTE -> "Docente"
                                                else -> role.displayName
                                            },
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = tintColor,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            text = when(role) {
                                                UserRole.ESTUDIANTE -> "Consulta asignaturas y notas"
                                                UserRole.DOCENTE -> "Evalúa y califica alumnos"
                                                else -> role.description
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 10.sp,
                                            color = tintColor.copy(alpha = 0.8f),
                                            textAlign = TextAlign.Center,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        // Username field
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario *") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )

                        // Email field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email *") },
                            leadingIcon = { Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )

                        // Names row
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("Nombre") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                            
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Apellido") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                        }

                        // Password field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña *") },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = MaterialTheme.colorScheme.outline)
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )

                        Text(
                            text = "* Campos obligatorios",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(Modifier.height(4.dp))

                        // Register Button using GradientButton
                        GradientButton(
                            text = "Registrarse",
                            onClick = {
                                val finalUsername = when (selectedRole) {
                                    UserRole.DOCENTE -> if (username.endsWith("_docente")) username else "${username}_docente"
                                    UserRole.ESTUDIANTE -> if (username.endsWith("_estudiante")) username else "${username}_estudiante"
                                    else -> username
                                }
                                viewModel.register(finalUsername, email, password, firstName, lastName)
                            },
                            loading = state.isLoading,
                            icon = Icons.Default.PersonAdd
                        )
                    }
                }
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
