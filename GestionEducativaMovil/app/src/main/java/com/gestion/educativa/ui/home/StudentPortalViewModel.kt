package com.gestion.educativa.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestion.educativa.data.api.TokenManager
import com.gestion.educativa.data.model.*
import com.gestion.educativa.data.repository.*
import com.gestion.educativa.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentPortalUiState(
    val student: Estudiante? = null,
    val matriculas: List<Matricula> = emptyList(),
    val notas: Map<Int, Nota> = emptyMap(), // matriculaId -> Nota
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StudentPortalViewModel @Inject constructor(
    private val estudianteRepository: EstudianteRepository,
    private val matriculaRepository: MatriculaRepository,
    private val notaRepository: NotaRepository,
    val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(StudentPortalUiState())
    val state: StateFlow<StudentPortalUiState> = _state.asStateFlow()

    init {
        loadStudentData()
    }

    fun loadStudentData() {
        val username = tokenManager.username ?: return
        val searchKeyword = username.substringBefore("_")
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            var foundStudent: Estudiante? = null
            var currentPage = 1
            var hasMorePages = true
            
            while (hasMorePages && foundStudent == null) {
                // Search with the prefix keyword to optimize DB search on first page, or query next pages if not found
                val searchParam = if (currentPage == 1) searchKeyword else null
                when (val estRes = estudianteRepository.list(page = currentPage, search = searchParam)) {
                    is Resource.Success -> {
                        val results = estRes.data.results
                        foundStudent = results.firstOrNull { 
                            it.userInfo?.username?.lowercase() == username.lowercase() 
                        }
                        if (foundStudent != null) {
                            break
                        }
                        hasMorePages = estRes.data.next != null
                        currentPage++
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = estRes.message) }
                        return@launch
                    }
                    else -> {
                        hasMorePages = false
                    }
                }
            }
            
            if (foundStudent != null) {
                _state.update { it.copy(student = foundStudent) }
                // 2. Fetch matriculas for this student
                fetchMatriculas(foundStudent.id)
            } else {
                _state.update { it.copy(isLoading = false, error = "No se encontró registro de estudiante para $username") }
            }
        }
    }

    private suspend fun fetchMatriculas(studentId: Int) {
        when (val matRes = matriculaRepository.list(estudiante = studentId)) {
            is Resource.Success -> {
                val matriculas = matRes.data.results
                _state.update { it.copy(matriculas = matriculas) }
                
                // 3. Fetch notas for each matricula
                val notasMap = mutableMapOf<Int, Nota>()
                for (matricula in matriculas) {
                    when (val notaRes = notaRepository.list(matricula = matricula.id)) {
                        is Resource.Success -> {
                            val nota = notaRes.data.results.firstOrNull()
                            if (nota != null) {
                                notasMap[matricula.id] = nota
                            }
                        }
                        else -> {}
                    }
                }
                _state.update { it.copy(notas = notasMap, isLoading = false) }
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, error = matRes.message) }
            }
            else -> {}
        }
    }
}
