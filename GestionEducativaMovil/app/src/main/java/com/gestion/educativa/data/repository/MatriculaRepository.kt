package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatriculaRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, search: String? = null, estado: String? = null, estudiante: Int? = null) =
        safeCall { api.getMatriculas(page, search?.ifBlank { null }, estado?.ifBlank { null }, estudiante) }

    suspend fun get(id: Int): Resource<Matricula> = safeCall { api.getMatricula(id) }

    suspend fun create(r: MatriculaRequest): Resource<Matricula> = safeCall { api.createMatricula(r) }

    suspend fun update(id: Int, r: MatriculaRequest): Resource<Matricula> =
        safeCall { api.updateMatricula(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteMatricula(id) }
}
