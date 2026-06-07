package com.gestion.educativa.ui.navigation

sealed class Screen(val route: String) {
    // Auth
    object Login : Screen("login")
    object Register : Screen("register")
    object Verification : Screen("verification")

    // Main
    object Home : Screen("home")

    // Facultades
    object FacultadList   : Screen("facultad_list")
    object FacultadDetail : Screen("facultad_detail/{id}") {
        fun go(id: Int) = "facultad_detail/$id"
    }
    object FacultadForm   : Screen("facultad_form?id={id}") {
        fun go(id: Int? = null) = "facultad_form${id?.let { "?id=$it" } ?: ""}"
    }

    // Carreras
    object CarreraList   : Screen("carrera_list")
    object CarreraDetail : Screen("carrera_detail/{id}") {
        fun go(id: Int) = "carrera_detail/$id"
    }
    object CarreraForm   : Screen("carrera_form?id={id}") {
        fun go(id: Int? = null) = "carrera_form${id?.let { "?id=$it" } ?: ""}"
    }

    // Docentes
    object DocenteList   : Screen("docente_list")
    object DocenteDetail : Screen("docente_detail/{id}") {
        fun go(id: Int) = "docente_detail/$id"
    }
    object DocenteForm   : Screen("docente_form?id={id}") {
        fun go(id: Int? = null) = "docente_form${id?.let { "?id=$it" } ?: ""}"
    }

    // Estudiantes
    object EstudianteList   : Screen("estudiante_list")
    object EstudianteDetail : Screen("estudiante_detail/{id}") {
        fun go(id: Int) = "estudiante_detail/$id"
    }
    object EstudianteForm   : Screen("estudiante_form?id={id}") {
        fun go(id: Int? = null) = "estudiante_form${id?.let { "?id=$it" } ?: ""}"
    }

    // Materias
    object MateriaList   : Screen("materia_list")
    object MateriaDetail : Screen("materia_detail/{id}") {
        fun go(id: Int) = "materia_detail/$id"
    }
    object MateriaForm   : Screen("materia_form?id={id}") {
        fun go(id: Int? = null) = "materia_form${id?.let { "?id=$it" } ?: ""}"
    }

    // Matrículas
    object MatriculaList   : Screen("matricula_list")
    object MatriculaDetail : Screen("matricula_detail/{id}") {
        fun go(id: Int) = "matricula_detail/$id"
    }
    object MatriculaForm   : Screen("matricula_form?id={id}") {
        fun go(id: Int? = null) = "matricula_form${id?.let { "?id=$it" } ?: ""}"
    }

    // Notas
    object NotaList   : Screen("nota_list")
    object NotaDetail : Screen("nota_detail/{id}") {
        fun go(id: Int) = "nota_detail/$id"
    }
    object NotaForm   : Screen("nota_form?id={id}") {
        fun go(id: Int? = null) = "nota_form${id?.let { "?id=$it" } ?: ""}"
    }
}
