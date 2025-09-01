package com.example.videoapp.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(private val dataStore: DataStore<Preferences>) {
    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    val userToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[USER_TOKEN]
    }

    suspend fun storeToken(token: String) {
        dataStore.edit { prefs -> prefs[USER_TOKEN] = token }
    }

    suspend fun clearToken() {
        dataStore.edit { prefs -> prefs.remove(USER_TOKEN) }
    }
}