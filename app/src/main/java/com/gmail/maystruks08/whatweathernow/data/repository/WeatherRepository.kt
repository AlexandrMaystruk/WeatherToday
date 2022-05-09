package com.gmail.maystruks08.whatweathernow.data.repository

import com.gmail.maystruks08.whatweathernow.data.API_KEY
import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.network.NetworkUtil
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import com.gmail.maystruks08.whatweathernow.ui.forecast.WeatherForecastContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

@FlowPreview
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val networkUtil: NetworkUtil,
    private val localeStorage: LocaleStorage
) : WeatherForecastContract.Repository {

    private val tag = "WeatherRepository"

    override suspend fun getForecastMode(): Flow<ForecastMode> {
        return localeStorage
            .getForecastMode()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getCurrentWeather(
    ): Flow<CurrentWeatherData> {
        return locationFlow()
            .flatMapConcat { location ->
                flow {
                    val cachedWeatherResult = localeStorage.getCurrentWeather()
                    val cachedWeatherData = cachedWeatherResult.getOrNull()
                    if (cachedWeatherResult.isSuccess && cachedWeatherData != null) {
                        emit(cachedWeatherData)
                    }

                    if (networkUtil.isNetworkTurnedOn()) {
                        val response = weatherApi.getWeatherByCoordinate(
                            latitude = location.lat,
                            longitude = location.lon,
                            apiKey = API_KEY
                        )
                        val currentWeather = response.body()
                        if (response.isSuccessful && currentWeather != null) {
                            emit(currentWeather)
                            localeStorage.saveCurrentWeather(currentWeather)
                        } else {
                            handleApiError(response)
                        }
                    }
                }
            }
    }


    override suspend fun getHourlyFiveDayForecast(
    ): Flow<HourlyForecast5Days> {
        return locationFlow()
            .flatMapConcat { location ->
                flow {
                    val cachedWeatherResult = localeStorage.getHourlyForecast5Days()
                    val cachedWeatherData = cachedWeatherResult.getOrNull()
                    if (cachedWeatherResult.isSuccess && cachedWeatherData != null) {
                        emit(cachedWeatherData)
                    }
                    if (networkUtil.isNetworkTurnedOn()) {
                        val response = weatherApi.getHourlyForecastByCoordinate(
                            latitude = location.lat,
                            longitude = location.lon,
                            apiKey = API_KEY
                        )
                        val currentWeather = response.body()
                        if (response.isSuccessful && currentWeather != null) {
                            emit(currentWeather)
                            localeStorage.saveHourlyForecast5Days(currentWeather)
                        } else {
                            handleApiError(response)
                        }
                    }
                }
            }
    }

    override suspend fun get7DaysForecast(): Flow<DailyForecast7Days> {
        return locationFlow()
            .flatMapConcat { location ->
                flow {
                    val cachedWeatherResult = localeStorage.getDailyForecast7Days()
                    val cachedWeatherData = cachedWeatherResult.getOrNull()
                    if (cachedWeatherResult.isSuccess && cachedWeatherData != null) {
                        emit(cachedWeatherData)
                    }

                    if (networkUtil.isNetworkTurnedOn()) {
                        val response = weatherApi.getDailyForecast7DaysByCoordinate(
                            latitude = location.lat,
                            longitude = location.lon,
                            apiKey = API_KEY
                        )
                        val currentWeather = response.body()
                        if (response.isSuccessful && currentWeather != null) {
                            emit(currentWeather)
                            localeStorage.saveDailyForecast7Days(currentWeather)
                        } else {
                            handleApiError(response)
                        }
                    }
                }
            }
    }

    override suspend fun updateLocation(newLocation: LocaleStorage.Location) {
        localeStorage.saveLocation(newLocation)
    }

    override suspend fun updateForecastMode(newForecastMode: ForecastMode) {
        localeStorage.saveForecastMode(newForecastMode)
    }

    private fun locationFlow(): Flow<LocaleStorage.Location> {
        return localeStorage
            .getLocation()
            .flowOn(Dispatchers.IO)
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
