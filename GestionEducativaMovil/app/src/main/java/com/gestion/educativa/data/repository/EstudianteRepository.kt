package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EstudianteRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, search: String? = null, carrera: Int? = null) =
        safeCall { api.getEstudiantes(page, search?.ifBlank { null }, carrera) }

    suspend fun get(id: Int): Resource<Estudiante> = safeCall { api.getEstudiante(id) }

    suspend fun create(r: EstudianteRequest): Resource<Estudiante> = safeCall { api.createEstudiante(r) }

    suspend fun update(id: Int, r: EstudianteRequest): Resource<Estudiante> =
        safeCall { api.updateEstudiante(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteEstudiante(id) }
}
