package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarreraRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, search: String? = null, facultad: Int? = null) =
        safeCall { api.getCarreras(page, search?.ifBlank { null }, facultad) }

    suspend fun get(id: Int): Resource<Carrera> = safeCall { api.getCarrera(id) }

    suspend fun create(r: CarreraRequest): Resource<Carrera> = safeCall { api.createCarrera(r) }

    suspend fun update(id: Int, r: CarreraRequest): Resource<Carrera> =
        safeCall { api.updateCarrera(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteCarrera(id) }
}
