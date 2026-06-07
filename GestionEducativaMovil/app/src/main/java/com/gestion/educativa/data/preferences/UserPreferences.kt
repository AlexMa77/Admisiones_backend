package com.gestion.educativa.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val KEY_USERNAME = stringPreferencesKey("username")
        val KEY_IS_ADMIN = booleanPreferencesKey("is_admin")
    }

    private val store get() = context.dataStore

    val accessToken: Flow<String?> = store.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_ACCESS_TOKEN] }

    val refreshToken: Flow<String?> = store.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_REFRESH_TOKEN] }

    val username: Flow<String?> = store.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_USERNAME] }

    val isAdmin: Flow<Boolean> = store.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_IS_ADMIN] ?: false }

    suspend fun saveTokens(access: String, refresh: String) {
        store.edit {
            it[KEY_ACCESS_TOKEN] = access
            it[KEY_REFRESH_TOKEN] = refresh
        }
    }

    suspend fun saveUserInfo(username: String, isAdmin: Boolean) {
        store.edit {
            it[KEY_USERNAME] = username
            it[KEY_IS_ADMIN] = isAdmin
        }
    }

    suspend fun clear() = store.edit { it.clear() }
}
