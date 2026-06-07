package com.gestion.educativa.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestion.educativa.data.api.TokenManager
import com.gestion.educativa.data.preferences.UserPreferences
import com.gestion.educativa.data.repository.AuthRepository
import com.gestion.educativa.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = false,
    val username: String? = null,
    val error: String? = null,
    val successMessage: String? = null,
    val isSessionChecked: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
    private val prefs: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        checkSavedSession()
    }

    private fun checkSavedSession() {
        viewModelScope.launch {
            combine(
                prefs.accessToken,
                prefs.refreshToken,
                prefs.username,
                prefs.isAdmin
            ) { access, refresh, username, admin ->
                listOf(access, refresh, username, admin.toString())
            }.first().let { values ->
                val access = values[0]
                val refresh = values[1]
                val username = values[2]
                val isAdmin = values[3].toBoolean()
                if (access != null && refresh != null) {
                    repository.restoreSession(access, refresh, username ?: "", isAdmin)
                    _state.update { it.copy(isLoggedIn = true, isAdmin = isAdmin, username = username, isSessionChecked = true) }
                } else {
                    _state.update { it.copy(isSessionChecked = true) }
                }
            }
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Usuario y contraseña son obligatorios") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.login(username, password)) {
                is Resource.Success -> _state.update {
                    it.copy(isLoading = false, isLoggedIn = true,
                        isAdmin = tokenManager.isAdmin, username = tokenManager.username)
                }
                is Resource.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
                else -> {}
            }
        }
    }

    fun register(username: String, email: String, password: String,
                 firstName: String, lastName: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Usuario, email y contraseña son obligatorios") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.register(username, email, password, firstName, lastName)) {
                is Resource.Success -> _state.update {
                    it.copy(isLoading = false, successMessage = "Cuenta creada. Inicia sesión")
                }
                is Resource.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
                else -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.logout()
            _state.update { AuthUiState(isSessionChecked = true) }
        }
    }

    fun clearMessages() = _state.update { it.copy(error = null, successMessage = null) }
}
