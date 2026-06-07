package com.gestion.educativa.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun decode(token: String): JSONObject? = try {
        val parts = token.split(".")
        if (parts.size != 3) null
        else {
            val bytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            JSONObject(String(bytes))
        }
    } catch (_: Exception) { null }

    fun isAdmin(token: String): Boolean = decode(token)?.optBoolean("is_staff", false) ?: false

    fun getUsername(token: String): String? =
        decode(token)?.optString("username")?.takeIf { it.isNotBlank() }
}
