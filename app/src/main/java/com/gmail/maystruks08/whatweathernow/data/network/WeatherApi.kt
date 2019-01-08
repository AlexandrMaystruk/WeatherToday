package com.gmail.maystruks08.whatweathernow.data.network

import com.gmail.maystruks08.whatweathernow.data.model.current.CurrentWeather
import com.gmail.maystruks08.whatweathernow.data.model.forecast.ForecastWeather
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("weather?mode=json")
    fun getCurrentWeatherByLatLng(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
        , @Query("units") unit: String
    ): Single<CurrentWeather>




    @GET("forecast?mode=json")
    fun getFiveDayForecastByLatLng(
        @Query("apiKey") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double

    ): Single<ForecastWeather>
}