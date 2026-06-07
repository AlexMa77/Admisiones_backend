package com.gestion.educativa.data.repository

import com.gestion.educativa.utils.ErrorHandler
import com.gestion.educativa.utils.Resource
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    @Suppress("UNCHECKED_CAST")
    protected suspend fun <T> safeCall(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Resource.Success(body)
                else Resource.Success(Unit as T)
            } else {
                Resource.Error(ErrorHandler.parseError(response), response.code())
            }
        } catch (e: IOException) {
            Resource.Error(ErrorHandler.networkError(e.message))
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }
}
