package com.gmail.maystruks08.whatweathernow.data.model

import android.util.Log
import com.gmail.maystruks08.whatweathernow.WeatherMapper
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherContract
import com.gmail.maystruks08.whatweathernow.data.API_KEY
import com.gmail.maystruks08.whatweathernow.data.database.WeatherDataBase
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.data.model.current.CurrentWeather
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HourlyWeatherRepository @Inject constructor(
    private val weatherMapper: WeatherMapper,
    private val weatherApi: WeatherApi?,
    private val dataBase: WeatherDataBase
) : HourlyWeatherContract.Repository {

    override fun getForecastByLatLng(lat: String, lon: String): Single<WeatherCurrentData>? {

        return if (isConnected()) {
            Log.d("database_ins", "Connected")

            weatherApi?.getCurrentWeatherByLatLng(lat, lon, API_KEY, "metric")
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.flatMap { weatherMapper.mapCurrentWeather(it) }
                ?.flatMap { saveToDb(it) }
        } else {
            Log.d("database_ins", "Disconnect")
            return dataBase.weatherCurrentWeatherDao().getCurrentWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }


    override fun getFiveDayForecastByLatLng(lat: String, lon: String): Single<List<WeatherForecastData>>? {
        return if (isConnected()) {
            Log.d("database_ins", "Connected")

            weatherApi?.getFiveDayForecastByLatLng(API_KEY, lat.toDouble(), lon.toDouble())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.flatMap { weatherMapper.mapForecastWeather(it) }
                ?.flatMap { saveForecastToDb(it) }


        } else {
            Log.d("database_ins", "Disconnect")
            return dataBase.weatherForecastDao().getForecastWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }


    }


    private fun saveToDb(weather: WeatherCurrentData): Single<WeatherCurrentData>? {

        Log.d("database_ins", "save ->" + weather.toString())

        return Single.just(dataBase.weatherCurrentWeatherDao().insertCurrentWeather(weather))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { Single.just(weather) }

    }


    private fun saveForecastToDb(weather: List<WeatherForecastData>): Single<List<WeatherForecastData>>? {
        dataBase.weatherForecastDao().deleteAllFromForecastWeather()
        dataBase.weatherForecastDao().insertForecastWeather(weather)
        return Single.just(weather)
    }


    private fun isConnected(): Boolean =
        WeatherApplication.component.provideConnectionManager().activeNetworkInfo != null
                && WeatherApplication.component.provideConnectionManager().activeNetworkInfo.isConnected


}