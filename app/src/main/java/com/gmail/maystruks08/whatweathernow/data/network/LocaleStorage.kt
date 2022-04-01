package com.gmail.maystruks08.whatweathernow.data.network

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleStorage @Inject constructor(
    private val context: Context
) {

    private val Context.prefsDataStore by preferencesDataStore(
        name = "com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage"
    )

    suspend fun saveLocation(location: Location) {
        context.prefsDataStore.edit { preferences ->
            preferences[LOCATION] = "${location.lat}:${location.lon}"
        }
    }

    fun getLocation(): Flow<Location> {
        return context.prefsDataStore.data
            .map { preferences ->
                preferences[LOCATION]?.split(":")?.let {
                    Location(it[0], it[1])
                } ?: Location("0", "0")
            }
    }

    suspend fun saveLocationSettingsConfig(shouldUseLocation: Boolean) {
        context.prefsDataStore.edit { preferences ->
            preferences[SHOULD_USE_LOCATION] = shouldUseLocation
        }
    }

    fun getLocationSettingsConfig(): Flow<Boolean> {
        return context.prefsDataStore.data
            .map { preferences ->
                preferences[SHOULD_USE_LOCATION] ?: false
            }
    }

    companion object {
        private val LOCATION = stringPreferencesKey("current_location")
        private val SHOULD_USE_LOCATION = booleanPreferencesKey("should_use_location")

    }

    data class Location(val lat: String, val lon: String)
}