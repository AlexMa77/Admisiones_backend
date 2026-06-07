package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotaRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, matricula: Int? = null, aprobado: Boolean? = null) =
        safeCall { api.getNotas(page, matricula, aprobado) }

    suspend fun get(id: Int): Resource<Nota> = safeCall { api.getNota(id) }

    suspend fun create(r: NotaRequest): Resource<Nota> = safeCall { api.createNota(r) }

    suspend fun update(id: Int, r: NotaRequest): Resource<Nota> =
        safeCall { api.updateNota(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteNota(id) }
}
