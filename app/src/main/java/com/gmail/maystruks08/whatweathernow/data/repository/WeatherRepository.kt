package com.gmail.maystruks08.whatweathernow.data.repository

import android.util.Log
import com.gmail.maystruks08.whatweathernow.ui.forecast.WeatherForecastContract
import com.gmail.maystruks08.whatweathernow.data.API_KEY
import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.network.NetworkUtil
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

@FlowPreview
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val networkUtil: NetworkUtil,
    private val localeStorage: LocaleStorage
) : WeatherForecastContract.Repository {

    override suspend fun getForecastMode(): Flow<ForecastMode> {
        return localeStorage
            .getForecastMode()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getCurrentWeather(
    ): Flow<CurrentWeatherData> {
        return localeStorage
            .getLocation()
            .flowOn(Dispatchers.IO)
            .flatMapMerge { location ->
                if (!networkUtil.isNetworkTurnedOn()) {
                    Log.d("database_ins", "Disconnect")
                    return@flatMapMerge localeStorage.getCurrentWeather()
                }

                Log.d("database_ins", "Connected")
                val response = weatherApi.getWeatherByCoordinate(
                    latitude = location.lat,
                    longitude = location.lon,
                    apiKey = API_KEY
                )

                val currentWeather = response.body()
                if (response.isSuccessful && currentWeather != null) {
                    localeStorage.saveCurrentWeather(currentWeather)
                    return@flatMapMerge flow {
                        emit(currentWeather)
                    }
                }
                handleApiError(response)
            }
    }


    override suspend fun getHourlyFiveDayForecast(
    ): Flow<HourlyForecast5Days> {
        return localeStorage
            .getLocation()
            .flowOn(Dispatchers.IO)
            .flatMapMerge { location ->
                if (!networkUtil.isNetworkTurnedOn()) {
                    Log.d("database_ins", "Disconnect")
                    return@flatMapMerge localeStorage
                        .getHourlyForecast5Days()
                }

                Log.d("database_ins", "Connected")
                val response = weatherApi.getHourlyForecastByCoordinate(
                    latitude = location.lat,
                    longitude = location.lon,
                    apiKey = API_KEY
                )
                val forecast = response.body()
                if (response.isSuccessful && forecast != null) {
                    localeStorage.saveHourlyForecast5Days(forecast)
                    return@flatMapMerge flow {
                        emit(forecast)
                    }
                }
                handleApiError(response)
            }
    }

    override suspend fun get7DaysForecast(): Flow<DailyForecast7Days> {
        return localeStorage
            .getLocation()
            .flowOn(Dispatchers.IO)
            .flatMapMerge { location ->
                if (!networkUtil.isNetworkTurnedOn()) {
                    Log.d("database_ins", "Disconnect")
                    return@flatMapMerge localeStorage.getDailyForecast7Days()
                }

                Log.d("database_ins", "Connected")
                val response = weatherApi.getDailyForecast7DaysByCoordinate(
                    latitude = location.lat,
                    longitude = location.lon,
                    apiKey = API_KEY
                )
                val forecast = response.body()
                if (response.isSuccessful && forecast != null) {
                    localeStorage.saveDailyForecast7Days(forecast)
                    return@flatMapMerge flow { emit(forecast) }
                }

                handleApiError(response)
            }
    }

    override suspend fun updateLocation(newLocation: LocaleStorage.Location) {
        localeStorage.saveLocation(newLocation)
    }

    override suspend fun updateForecastMode(newForecastMode: ForecastMode) {
        localeStorage.saveForecastMode(newForecastMode)
    }

    private fun <T> handleApiError(response: Response<T>): Nothing {
        if (!response.isSuccessful) {
            val errorMessage = when (response.code()) {
                201, 202, 203 -> "Authorization error"
                404 -> "Data not found"
                504 -> "Internal server error"
                else -> "Internal server error"
            }
            throw Exception("$errorMessage: ${response.errorBody()?.string()}")
        }
        throw Exception("Unhandled exception")
    }
}
