package com.gmail.maystruks08.whatweathernow.data.repository

import android.util.Log
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherContract
import com.gmail.maystruks08.whatweathernow.data.API_KEY
import com.gmail.maystruks08.whatweathernow.data.WeatherMapper
import com.gmail.maystruks08.whatweathernow.data.database.WeatherDataBase
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.network.NetworkUtil
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HourlyWeatherRepository @Inject constructor(
    private val weatherMapper: WeatherMapper,
    private val weatherApi: WeatherApi,
    private val dataBase: WeatherDataBase,
    private val networkUtil: NetworkUtil,
    private val localeStorage: LocaleStorage
) : HourlyWeatherContract.Repository {

    override suspend fun getForecastByLatLng(
    ): Flow<WeatherCurrentData> {
        return localeStorage
            .getLocation()
            .map {
                if (networkUtil.isNetworkTurnedOn()) {
                    Log.d("database_ins", "Connected")
                    val response =
                        weatherApi.getCurrentWeatherByLatLng(it.lat, it.lon, API_KEY, "metric")
                    val currentWeather = response.body()
                    if (response.isSuccessful && currentWeather != null) {
                        return@map weatherMapper
                            .mapCurrentWeather(currentWeather)
                            .also { saveOneDayForecastToDb(it) }

                    }
                    //TODO map exception
                    throw Exception(response.errorBody()?.string().orEmpty())
                }
                Log.d("database_ins", "Disconnect")
                dataBase.weatherCurrentWeatherDao().getCurrentWeather()
            }

    }


    override suspend fun getFiveDayForecastByLatLng(
    ): Flow<List<WeatherForecastData>> {
        return localeStorage
            .getLocation()
            .map {
                if (networkUtil.isNetworkTurnedOn()) {
                    Log.d("database_ins", "Connected")
                    val response =
                        weatherApi.getFiveDayForecastByLatLng(
                            API_KEY,
                            it.lat.toDouble(),
                            it.lon.toDouble()
                        )
                    val forecast = response.body()
                    if (response.isSuccessful && forecast != null) {
                        return@map weatherMapper
                            .mapForecastWeather(forecast)
                            .also { saveForecastToDb(it) }
                    }
                    //TODO map exception
                    throw Exception(response.errorBody()?.string().orEmpty())
                }
                Log.d("database_ins", "Disconnect")
                dataBase.weatherForecastDao().getForecastWeather()
            }
    }

    override suspend fun updateLocation(newLocation: LocaleStorage.Location) {
        localeStorage.saveLocation(newLocation)
    }

    private suspend fun saveOneDayForecastToDb(weather: WeatherCurrentData) {
        Log.d("database_ins", "save ->$weather")
        withContext(Dispatchers.IO) {
            dataBase.weatherCurrentWeatherDao().insertCurrentWeather(weather)
        }
    }

    private suspend fun saveForecastToDb(weather: List<WeatherForecastData>) {
        withContext(Dispatchers.IO) {
            dataBase.weatherForecastDao().deleteAllFromForecastWeather()
            dataBase.weatherForecastDao().insertForecastWeather(weather)
        }
    }
}
