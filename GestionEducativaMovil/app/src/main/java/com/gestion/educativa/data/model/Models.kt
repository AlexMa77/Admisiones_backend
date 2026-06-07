package com.gestion.educativa.data.model

import com.google.gson.annotations.SerializedName

// ── Auth ──────────────────────────────────────────────────────────────────────

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val access: String, val refresh: String)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("first_name") val firstName: String = "",
    @SerializedName("last_name") val lastName: String = ""
)

data class RegisterResponse(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?
)

data class RefreshRequest(val refresh: String)
data class RefreshResponse(val access: String)
data class LogoutRequest(val refresh: String)

// ── Pagination ────────────────────────────────────────────────────────────────

data class PaginatedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)

// ── Shared ────────────────────────────────────────────────────────────────────

data class UserInfo(
    val id: Int,
    val username: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    val email: String?
) {
    val fullName: String
        get() = "${firstName.orEmpty()} ${lastName.orEmpty()}".trim().ifBlank { username }
}

// ── Facultad ──────────────────────────────────────────────────────────────────

data class Facultad(
    val id: Int = 0,
    val nombre: String = "",
    val codigo: String = "",
    val descripcion: String = "",
    val activo: Boolean = true,
    @SerializedName("creado_en") val creadoEn: String? = null
)

data class FacultadRequest(
    val nombre: String,
    val codigo: String,
    val descripcion: String = "",
    val activo: Boolean = true
)

// ── Carrera ───────────────────────────────────────────────────────────────────

data class Carrera(
    val id: Int = 0,
    val facultad: Int = 0,
    @SerializedName("facultad_nombre") val facultadNombre: String? = null,
    val nombre: String = "",
    val codigo: String = "",
    @SerializedName("duracion_semestres") val duracionSemestres: Int = 8,
    val activo: Boolean = true,
    @SerializedName("creado_en") val creadoEn: String? = null
)

data class CarreraRequest(
    val facultad: Int,
    val nombre: String,
    val codigo: String,
    @SerializedName("duracion_semestres") val duracionSemestres: Int = 8,
    val activo: Boolean = true
)

// ── Docente ───────────────────────────────────────────────────────────────────

data class Docente(
    val id: Int = 0,
    val user: Int = 0,
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    val cedula: String = "",
    val telefono: String = "",
    val especialidad: String = "",
    val activo: Boolean = true,
    @SerializedName("creado_en") val creadoEn: String? = null
) {
    val nombreCompleto: String get() = userInfo?.fullName ?: "Docente #$id"
}

data class DocenteRequest(
    val user: Int,
    val cedula: String,
    val telefono: String,
    val especialidad: String,
    val activo: Boolean = true
)

// ── Estudiante ────────────────────────────────────────────────────────────────

data class Estudiante(
    val id: Int = 0,
    val user: Int = 0,
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    val carrera: Int = 0,
    @SerializedName("carrera_nombre") val carreraNombre: String? = null,
    val cedula: String = "",
    val telefono: String = "",
    @SerializedName("semestre_actual") val semestreActual: Int = 1,
    val activo: Boolean = true,
    @SerializedName("creado_en") val creadoEn: String? = null
) {
    val nombreCompleto: String get() = userInfo?.fullName ?: "Estudiante #$id"
}

data class EstudianteRequest(
    val user: Int,
    val carrera: Int,
    val cedula: String,
    val telefono: String,
    @SerializedName("semestre_actual") val semestreActual: Int = 1,
    val activo: Boolean = true
)

// ── Materia ───────────────────────────────────────────────────────────────────

data class Materia(
    val id: Int = 0,
    val carrera: Int = 0,
    @SerializedName("carrera_nombre") val carreraNombre: String? = null,
    val docente: Int = 0,
    @SerializedName("docente_nombre") val docenteNombre: String? = null,
    val nombre: String = "",
    val codigo: String = "",
    val creditos: Int = 3,
    val semestre: Int = 1,
    val activo: Boolean = true,
    @SerializedName("creado_en") val creadoEn: String? = null
)

data class MateriaRequest(
    val carrera: Int,
    val docente: Int,
    val nombre: String,
    val codigo: String,
    val creditos: Int = 3,
    val semestre: Int = 1,
    val activo: Boolean = true
)

// ── Matricula ─────────────────────────────────────────────────────────────────

data class Matricula(
    val id: Int = 0,
    val estudiante: Int = 0,
    @SerializedName("estudiante_nombre") val estudianteNombre: String? = null,
    val materia: Int = 0,
    @SerializedName("materia_nombre") val materiaNombre: String? = null,
    val periodo: String = "",
    val estado: String = "activa",
    @SerializedName("fecha_matricula") val fechaMatricula: String = "",
    @SerializedName("creado_en") val creadoEn: String? = null
)

data class MatriculaRequest(
    val estudiante: Int,
    val materia: Int,
    val periodo: String,
    val estado: String = "activa",
    @SerializedName("fecha_matricula") val fechaMatricula: String
)

// ── Nota ──────────────────────────────────────────────────────────────────────

data class Nota(
    val id: Int = 0,
    val matricula: Int = 0,
    val parcial1: Double? = null,
    val parcial2: Double? = null,
    @SerializedName("examen_final") val examenFinal: Double? = null,
    @SerializedName("nota_final") val notaFinal: Double? = null,
    val aprobado: Boolean? = null,
    @SerializedName("actualizado_en") val actualizadoEn: String? = null
)

data class NotaRequest(
    val matricula: Int,
    val parcial1: Double? = null,
    val parcial2: Double? = null,
    @SerializedName("examen_final") val examenFinal: Double? = null
)
