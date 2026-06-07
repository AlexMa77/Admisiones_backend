package com.gestion.educativa.data.repository

import com.gestion.educativa.data.api.ApiService
import com.gestion.educativa.data.api.TokenManager
import com.gestion.educativa.data.model.*
import com.gestion.educativa.data.preferences.UserPreferences
import com.gestion.educativa.utils.JwtUtils
import com.gestion.educativa.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager,
    private val prefs: UserPreferences
) : BaseRepository() {

    suspend fun login(username: String, password: String): Resource<LoginResponse> {
        val result = safeCall { api.login(LoginRequest(username, password)) }
        if (result is Resource.Success) {
            val tokens = result.data
            tokenManager.accessToken = tokens.access
            tokenManager.refreshToken = tokens.refresh
            tokenManager.isAdmin = JwtUtils.isAdmin(tokens.access)
            tokenManager.username = JwtUtils.getUsername(tokens.access) ?: username
            prefs.saveTokens(tokens.access, tokens.refresh)
            prefs.saveUserInfo(tokenManager.username!!, tokenManager.isAdmin)
        }
        return result
    }

    suspend fun register(
        username: String, email: String, password: String,
        firstName: String = "", lastName: String = ""
    ): Resource<RegisterResponse> = safeCall {
        api.register(RegisterRequest(username, email, password, firstName, lastName))
    }

    suspend fun logout(): Resource<Unit> {
        val refresh = tokenManager.refreshToken
            ?: return Resource.Error("No hay sesión activa")
        val result = safeCall { api.logout(LogoutRequest(refresh)) }
        tokenManager.clear()
        prefs.clear()
        return result
    }

    fun restoreSession(accessToken: String, refreshToken: String, username: String, isAdmin: Boolean) {
        tokenManager.accessToken = accessToken
        tokenManager.refreshToken = refreshToken
        tokenManager.username = username
        tokenManager.isAdmin = isAdmin
    }
}
