package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.model.*
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MateriaRepository @Inject constructor(private val api: ApiService) : BaseRepository() {
    suspend fun list(page: Int? = null, search: String? = null, carrera: Int? = null) =
        safeCall { api.getMaterias(page, search?.ifBlank { null }, carrera) }

    suspend fun get(id: Int): Resource<Materia> = safeCall { api.getMateria(id) }

    suspend fun create(r: MateriaRequest): Resource<Materia> = safeCall { api.createMateria(r) }

    suspend fun update(id: Int, r: MateriaRequest): Resource<Materia> =
        safeCall { api.updateMateria(id, r) }

    suspend fun delete(id: Int): Resource<Unit> = safeCall { api.deleteMateria(id) }
}
