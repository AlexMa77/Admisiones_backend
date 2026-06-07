package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocenteRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, search: String? = null) =
        safeCall { api.getDocentes(page, search?.ifBlank { null }) }

    suspend fun get(id: Int): Resource<Docente> = safeCall { api.getDocente(id) }

    suspend fun create(r: DocenteRequest): Resource<Docente> = safeCall { api.createDocente(r) }

    suspend fun update(id: Int, r: DocenteRequest): Resource<Docente> =
        safeCall { api.updateDocente(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteDocente(id) }
}
