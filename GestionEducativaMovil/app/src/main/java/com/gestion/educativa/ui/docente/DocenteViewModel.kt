package com.gestion.educativa.ui.docente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestion.educativa.data.api.TokenManager
import com.gestion.educativa.data.model.Docente
import com.gestion.educativa.data.model.DocenteRequest
import com.gestion.educativa.data.repository.DocenteRepository
import com.gestion.educativa.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocenteUiState(
    val items: List<Docente> = emptyList(),
    val selected: Docente? = null,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val totalCount: Int = 0,
    val hasMore: Boolean = false,
    val currentPage: Int = 1
)

@HiltViewModel
class DocenteViewModel @Inject constructor(
    private val repository: DocenteRepository,
    val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(DocenteUiState())
    val state: StateFlow<DocenteUiState> = _state.asStateFlow()
    var searchQuery = MutableStateFlow(""); private set

    init { load(reset = true) }

    fun onSearch(q: String) { searchQuery.value = q; load(reset = true) }

    fun load(reset: Boolean = false) {
        viewModelScope.launch {
            if (reset) _state.update { it.copy(items = emptyList(), currentPage = 1) }
            val page = if (reset) 1 else _state.value.currentPage
            _state.update { if (reset) it.copy(isLoading = true) else it.copy(isLoadingMore = true) }
            when (val r = repository.list(page, searchQuery.value)) {
                is Resource.Success -> {
                    val all = if (reset) r.data.results else _state.value.items + r.data.results
                    _state.update { it.copy(items = all, isLoading = false, isLoadingMore = false,
                        totalCount = r.data.count, hasMore = r.data.next != null, currentPage = page + 1, error = null) }
                }
                is Resource.Error -> _state.update { it.copy(isLoading = false, isLoadingMore = false, error = r.message) }
                else -> {}
            }
        }
    }

    fun loadDetail(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val r = repository.get(id)) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, selected = r.data) }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = r.message) }
                else -> {}
            }
        }
    }

    fun save(id: Int?, userId: Int, cedula: String, telefono: String,
             especialidad: String, activo: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val req = DocenteRequest(userId, cedula, telefono, especialidad, activo)
            val result = if (id == null) repository.create(req) else repository.update(id, req)
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, successMessage = if (id == null) "Docente creado" else "Docente actualizado") }
                    onSuccess()
                }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
                else -> {}
            }
        }
    }

    fun delete(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val r = repository.delete(id)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, items = _state.value.items.filter { it.id != id },
                        totalCount = _state.value.totalCount - 1, successMessage = "Docente eliminado") }
                    onSuccess()
                }
                is Resource.Error -> _state.update { it.copy(isLoading = false, error = r.message) }
                else -> {}
            }
        }
    }

    fun clearMessages() = _state.update { it.copy(error = null, successMessage = null) }
}
