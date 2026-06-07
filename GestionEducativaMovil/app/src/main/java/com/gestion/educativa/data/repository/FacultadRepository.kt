package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacultadRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, search: String? = null) =
        safeCall { api.getFacultades(page, search?.ifBlank { null }) }

    suspend fun get(id: Int): Resource<Facultad> = safeCall { api.getFacultad(id) }

    suspend fun create(r: FacultadRequest): Resource<Facultad> = safeCall { api.createFacultad(r) }

    suspend fun update(id: Int, r: FacultadRequest): Resource<Facultad> =
        safeCall { api.updateFacultad(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteFacultad(id) }
}
