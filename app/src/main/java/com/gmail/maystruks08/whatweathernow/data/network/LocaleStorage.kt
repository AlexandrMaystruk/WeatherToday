package com.gmail.maystruks08.whatweathernow.data.network

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gmail.maystruks08.whatweathernow.data.JsonUtil
import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    suspend fun saveForecastMode(forecastMode: ForecastMode) {
        context.prefsDataStore.edit { preferences ->
            preferences[FORECAST_MODE] = forecastMode.name
        }
    }

    fun getForecastMode(): Flow<ForecastMode> {
        return context.prefsDataStore.data
            .map { preferences ->
                preferences[FORECAST_MODE]?.let {
                    ForecastMode.valueOf(it)
                } ?: ForecastMode.HOURLY
            }
    }

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

    /**
     * Caching weather data in json format
     */

    suspend fun saveCurrentWeather(weatherData: CurrentWeatherData) {
        context.prefsDataStore.edit { preferences ->
            preferences[CURRENT_WEATHER_CACHE] = JsonUtil.toJson(weatherData)
        }
    }

    suspend fun getCurrentWeather(): Result<CurrentWeatherData> {
        return kotlin.runCatching {
            context.prefsDataStore.data
                .map { preferences ->
                    preferences[CURRENT_WEATHER_CACHE]
                        ?.let {
                            JsonUtil.fromJson<CurrentWeatherData>(it)
                        } ?: throw RuntimeException("Absent cached current weather")
                }.first()
        }
    }

    suspend fun saveHourlyForecast5Days(weather: HourlyForecast5Days) {
        context.prefsDataStore.edit { preferences ->
            preferences[HOURLY_5_DAYS_CACHE] = JsonUtil.toJson(weather)
        }
    }

    suspend fun getHourlyForecast5Days(): Result<HourlyForecast5Days> {
        return kotlin.runCatching {
            context.prefsDataStore.data
                .map { preferences ->
                    preferences[HOURLY_5_DAYS_CACHE]?.let {
                        JsonUtil.fromJson<HourlyForecast5Days>(it)
                    } ?: throw RuntimeException("Absent cached HourlyForecast5Days")
                }.first()
        }
    }

    suspend fun saveDailyForecast7Days(weather: DailyForecast7Days) {
        context.prefsDataStore.edit { preferences ->
            preferences[DAILY_7_DAYS_CACHE] = JsonUtil.toJson(weather)
        }
    }

    suspend fun getDailyForecast7Days(): Result<DailyForecast7Days> {
        return kotlin.runCatching {
            context.prefsDataStore.data
                .map { preferences ->
                    preferences[DAILY_7_DAYS_CACHE]?.let {
                        JsonUtil.fromJson<DailyForecast7Days>(it)
                    } ?: throw RuntimeException("Absent cached DailyForecast7Days")
                }.first()
        }
    }

    companion object {
        private val FORECAST_MODE = stringPreferencesKey("FORECAST_MODE")
        private val LOCATION = stringPreferencesKey("current_location")
        private val SHOULD_USE_LOCATION = booleanPreferencesKey("should_use_location")

        private val CURRENT_WEATHER_CACHE = stringPreferencesKey("CURRENT_WEATHER_CACHE")
        private val HOURLY_5_DAYS_CACHE = stringPreferencesKey("HOURLY_5_DAYS_CACHE")
        private val DAILY_7_DAYS_CACHE = stringPreferencesKey("DAILY_7_DAYS_CACHE")
    }

    data class Location(val lat: String, val lon: String)
}