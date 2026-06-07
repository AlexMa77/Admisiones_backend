package com.gestion.educativa.data.api

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor() {
    var accessToken: String? = null
    var refreshToken: String? = null
    var isAdmin: Boolean = false
    var username: String? = null

    val isLoggedIn: Boolean get() = accessToken != null

    fun clear() {
        accessToken = null
        refreshToken = null
        isAdmin = false
        username = null
    }
}
