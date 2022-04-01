package com.gmail.maystruks08.whatweathernow.data.network

import com.gmail.maystruks08.whatweathernow.data.models.current.CurrentWeather
import com.gmail.maystruks08.whatweathernow.data.models.forecast.ForecastWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

    @GET("weather?mode=json")
    suspend fun getCurrentWeatherByLatLng(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ): Response<CurrentWeather>

    @GET("forecast?mode=json")
    suspend fun getFiveDayForecastByLatLng(
        @Query("apiKey") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<ForecastWeather>
}