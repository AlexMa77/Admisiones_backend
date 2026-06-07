package com.gestion.educativa.utils

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

object ErrorHandler {

    fun <T> parseError(response: Response<T>): String {
        return try {
            val body = response.errorBody()?.string().orEmpty()
            val json = JSONObject(body)
            when {
                json.has("detail") -> json.getString("detail")
                json.has("non_field_errors") -> {
                    val arr = json.getJSONArray("non_field_errors")
                    if (arr.length() > 0) arr.getString(0) else "Error de validación"
                }
                else -> {
                    val msgs = mutableListOf<String>()
                    json.keys().forEach { key ->
                        val v = json.get(key)
                        val text = if (v is JSONArray && v.length() > 0) v.getString(0) else v.toString()
                        if (text.isNotBlank()) msgs.add("$key: $text")
                    }
                    msgs.joinToString("\n").ifBlank { httpMessage(response.code()) }
                }
            }
        } catch (_: Exception) {
            httpMessage(response.code())
        }
    }

    fun networkError(message: String?): String = when {
        message?.contains("Unable to resolve host") == true -> "Sin conexión. Verifica tu red"
        message?.contains("timeout") == true -> "Tiempo de espera agotado"
        message?.contains("ECONNREFUSED") == true -> "No se puede conectar al servidor"
        else -> "Error de conexión"
    }

    private fun httpMessage(code: Int) = when (code) {
        400 -> "Datos inválidos. Verifica los campos"
        401 -> "No autorizado. Inicia sesión nuevamente"
        403 -> "No tienes permisos para esta acción"
        404 -> "Registro no encontrado"
        500 -> "Error interno del servidor"
        else -> "Error desconocido ($code)"
    }
}
