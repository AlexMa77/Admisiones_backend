package com.gestion.educativa.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gestion.educativa.VerificationScreen
import com.gestion.educativa.ui.auth.AuthViewModel
import com.gestion.educativa.ui.auth.LoginScreen
import com.gestion.educativa.ui.auth.RegisterScreen
import com.gestion.educativa.ui.carrera.*
import com.gestion.educativa.ui.docente.*
import com.gestion.educativa.ui.estudiante.*
import com.gestion.educativa.ui.facultad.*
import com.gestion.educativa.ui.home.HomeScreen
import com.gestion.educativa.ui.materia.*
import com.gestion.educativa.ui.matricula.*
import com.gestion.educativa.ui.nota.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.state.collectAsState()

    if (!authState.isSessionChecked) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDest = if (authState.isLoggedIn) Screen.Home.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDest) {

        // ── Auth ──────────────────────────────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToVerification = { navController.navigate(Screen.Verification.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegistered = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Verification.route) {
            VerificationScreen(onBack = { navController.popBackStack() })
        }

        // ── Home ──────────────────────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                authViewModel = authViewModel,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Facultades ────────────────────────────────────────────────────────
        composable(Screen.FacultadList.route) {
            val vm: FacultadViewModel = hiltViewModel()
            FacultadListScreen(vm,
                onNavigateToDetail = { navController.navigate(Screen.FacultadDetail.go(it)) },
                onNavigateToCreate = { navController.navigate(Screen.FacultadForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.FacultadDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: FacultadViewModel = hiltViewModel()
            FacultadDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.FacultadForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.FacultadForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: FacultadViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            FacultadFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }

        // ── Carreras ──────────────────────────────────────────────────────────
        composable(Screen.CarreraList.route) {
            val vm: CarreraViewModel = hiltViewModel()
            CarreraListScreen(vm,
                onDetail = { navController.navigate(Screen.CarreraDetail.go(it)) },
                onCreate = { navController.navigate(Screen.CarreraForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.CarreraDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: CarreraViewModel = hiltViewModel()
            CarreraDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.CarreraForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.CarreraForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: CarreraViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            CarreraFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }

        // ── Docentes ──────────────────────────────────────────────────────────
        composable(Screen.DocenteList.route) {
            val vm: DocenteViewModel = hiltViewModel()
            DocenteListScreen(vm,
                onDetail = { navController.navigate(Screen.DocenteDetail.go(it)) },
                onCreate = { navController.navigate(Screen.DocenteForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.DocenteDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: DocenteViewModel = hiltViewModel()
            DocenteDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.DocenteForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.DocenteForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: DocenteViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            DocenteFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }

        // ── Estudiantes ───────────────────────────────────────────────────────
        composable(Screen.EstudianteList.route) {
            val vm: EstudianteViewModel = hiltViewModel()
            EstudianteListScreen(vm,
                onDetail = { navController.navigate(Screen.EstudianteDetail.go(it)) },
                onCreate = { navController.navigate(Screen.EstudianteForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.EstudianteDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: EstudianteViewModel = hiltViewModel()
            EstudianteDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.EstudianteForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.EstudianteForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: EstudianteViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            EstudianteFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }

        // ── Materias ──────────────────────────────────────────────────────────
        composable(Screen.MateriaList.route) {
            val vm: MateriaViewModel = hiltViewModel()
            MateriaListScreen(vm,
                onDetail = { navController.navigate(Screen.MateriaDetail.go(it)) },
                onCreate = { navController.navigate(Screen.MateriaForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.MateriaDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: MateriaViewModel = hiltViewModel()
            MateriaDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.MateriaForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.MateriaForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: MateriaViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            MateriaFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }

        // ── Matrículas ────────────────────────────────────────────────────────
        composable(Screen.MatriculaList.route) {
            val vm: MatriculaViewModel = hiltViewModel()
            MatriculaListScreen(vm,
                onDetail = { navController.navigate(Screen.MatriculaDetail.go(it)) },
                onCreate = { navController.navigate(Screen.MatriculaForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.MatriculaDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: MatriculaViewModel = hiltViewModel()
            MatriculaDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.MatriculaForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.MatriculaForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: MatriculaViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            MatriculaFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }

        // ── Notas ─────────────────────────────────────────────────────────────
        composable(Screen.NotaList.route) {
            val vm: NotaViewModel = hiltViewModel()
            NotaListScreen(vm,
                onDetail = { navController.navigate(Screen.NotaDetail.go(it)) },
                onCreate = { navController.navigate(Screen.NotaForm.go()) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.NotaDetail.route, arguments = listOf(navArgument("id") { type = NavType.IntType })) {
            val vm: NotaViewModel = hiltViewModel()
            NotaDetailScreen(it.arguments!!.getInt("id"), vm,
                onEdit = { id -> navController.navigate(Screen.NotaForm.go(id)) },
                onBack = { navController.popBackStack() })
        }
        composable(Screen.NotaForm.route, arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })) {
            val vm: NotaViewModel = hiltViewModel()
            val id = it.arguments!!.getInt("id").takeIf { v -> v != -1 }
            NotaFormScreen(id, vm,
                onSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() })
        }
    }
}
