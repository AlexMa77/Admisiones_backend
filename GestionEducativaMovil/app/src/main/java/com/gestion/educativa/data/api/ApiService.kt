package com.gestion.educativa.data.api

import com.gestion.educativa.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ── Auth ──────────────────────────────────────────────────────────────────

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/refresh/")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<RefreshResponse>

    @POST("auth/logout/")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>

    @GET("users/")
    suspend fun getUsers(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null
    ): Response<List<UserInfo>>

    // ── Facultades ────────────────────────────────────────────────────────────

    @GET("facultades/")
    suspend fun getFacultades(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("activo") activo: Boolean? = null
    ): Response<PaginatedResponse<Facultad>>

    @GET("facultades/{id}/")
    suspend fun getFacultad(@Path("id") id: Int): Response<Facultad>

    @POST("facultades/")
    suspend fun createFacultad(@Body request: FacultadRequest): Response<Facultad>

    @PUT("facultades/{id}/")
    suspend fun updateFacultad(@Path("id") id: Int, @Body request: FacultadRequest): Response<Facultad>

    @DELETE("facultades/{id}/")
    suspend fun deleteFacultad(@Path("id") id: Int): Response<Unit>

    // ── Carreras ──────────────────────────────────────────────────────────────

    @GET("carreras/")
    suspend fun getCarreras(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("facultad") facultad: Int? = null,
        @Query("activo") activo: Boolean? = null
    ): Response<PaginatedResponse<Carrera>>

    @GET("carreras/{id}/")
    suspend fun getCarrera(@Path("id") id: Int): Response<Carrera>

    @POST("carreras/")
    suspend fun createCarrera(@Body request: CarreraRequest): Response<Carrera>

    @PUT("carreras/{id}/")
    suspend fun updateCarrera(@Path("id") id: Int, @Body request: CarreraRequest): Response<Carrera>

    @DELETE("carreras/{id}/")
    suspend fun deleteCarrera(@Path("id") id: Int): Response<Unit>

    // ── Docentes ──────────────────────────────────────────────────────────────

    @GET("docentes/")
    suspend fun getDocentes(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("activo") activo: Boolean? = null
    ): Response<PaginatedResponse<Docente>>

    @GET("docentes/{id}/")
    suspend fun getDocente(@Path("id") id: Int): Response<Docente>

    @POST("docentes/")
    suspend fun createDocente(@Body request: DocenteRequest): Response<Docente>

    @PUT("docentes/{id}/")
    suspend fun updateDocente(@Path("id") id: Int, @Body request: DocenteRequest): Response<Docente>

    @DELETE("docentes/{id}/")
    suspend fun deleteDocente(@Path("id") id: Int): Response<Unit>

    // ── Estudiantes ───────────────────────────────────────────────────────────

    @GET("estudiantes/")
    suspend fun getEstudiantes(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("carrera") carrera: Int? = null,
        @Query("activo") activo: Boolean? = null
    ): Response<PaginatedResponse<Estudiante>>

    @GET("estudiantes/{id}/")
    suspend fun getEstudiante(@Path("id") id: Int): Response<Estudiante>

    @POST("estudiantes/")
    suspend fun createEstudiante(@Body request: EstudianteRequest): Response<Estudiante>

    @PUT("estudiantes/{id}/")
    suspend fun updateEstudiante(@Path("id") id: Int, @Body request: EstudianteRequest): Response<Estudiante>

    @DELETE("estudiantes/{id}/")
    suspend fun deleteEstudiante(@Path("id") id: Int): Response<Unit>

    // ── Materias ──────────────────────────────────────────────────────────────

    @GET("materias/")
    suspend fun getMaterias(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("carrera") carrera: Int? = null,
        @Query("semestre") semestre: Int? = null,
        @Query("activo") activo: Boolean? = null
    ): Response<PaginatedResponse<Materia>>

    @GET("materias/{id}/")
    suspend fun getMateria(@Path("id") id: Int): Response<Materia>

    @POST("materias/")
    suspend fun createMateria(@Body request: MateriaRequest): Response<Materia>

    @PUT("materias/{id}/")
    suspend fun updateMateria(@Path("id") id: Int, @Body request: MateriaRequest): Response<Materia>

    @DELETE("materias/{id}/")
    suspend fun deleteMateria(@Path("id") id: Int): Response<Unit>

    // ── Matrículas ────────────────────────────────────────────────────────────

    @GET("matriculas/")
    suspend fun getMatriculas(
        @Query("page") page: Int? = null,
        @Query("search") search: String? = null,
        @Query("estado") estado: String? = null,
        @Query("estudiante") estudiante: Int? = null,
        @Query("materia") materia: Int? = null
    ): Response<PaginatedResponse<Matricula>>

    @GET("matriculas/{id}/")
    suspend fun getMatricula(@Path("id") id: Int): Response<Matricula>

    @POST("matriculas/")
    suspend fun createMatricula(@Body request: MatriculaRequest): Response<Matricula>

    @PUT("matriculas/{id}/")
    suspend fun updateMatricula(@Path("id") id: Int, @Body request: MatriculaRequest): Response<Matricula>

    @DELETE("matriculas/{id}/")
    suspend fun deleteMatricula(@Path("id") id: Int): Response<Unit>

    // ── Notas ─────────────────────────────────────────────────────────────────

    @GET("notas/")
    suspend fun getNotas(
        @Query("page") page: Int? = null,
        @Query("matricula") matricula: Int? = null,
        @Query("aprobado") aprobado: Boolean? = null
    ): Response<PaginatedResponse<Nota>>

    @GET("notas/{id}/")
    suspend fun getNota(@Path("id") id: Int): Response<Nota>

    @POST("notas/")
    suspend fun createNota(@Body request: NotaRequest): Response<Nota>

    @PUT("notas/{id}/")
    suspend fun updateNota(@Path("id") id: Int, @Body request: NotaRequest): Response<Nota>

    @DELETE("notas/{id}/")
    suspend fun deleteNota(@Path("id") id: Int): Response<Unit>
}
