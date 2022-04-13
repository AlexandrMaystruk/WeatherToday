package com.gmail.maystruks08.whatweathernow.data.network

import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {


    @GET("weather?mode=json")
    suspend fun getWeatherByCoordinate(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String = "metric"
    ): Response<CurrentWeatherData>

    /**
     * Hourly forecast for 5 days.
     */
    @GET("forecast?mode=json")
    suspend fun getHourlyForecastByCoordinate(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String = "metric"
    ): Response<HourlyForecast5Days>

    /**
     * Daily Forecast 7 Days is available at any location on the globe.
     */
    @GET("onecall?mode=json")
    suspend fun getDailyForecast7DaysByCoordinate(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("exclude") exclude: String = "current,minutely,hourly,national,historical",
        @Query("appid") apiKey: String,
        @Query("units") unit: String = "metric"
    ): Response<DailyForecast7Days>


    @GET("weather?mode=json")
    suspend fun getWeatherByCityName(
        @Query("") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String = "metric"
    ): Response<CurrentWeatherData>

    @GET("forecast?mode=json")
    suspend fun getHourlyForecastByCityName(
        @Query("") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String = "metric"
    ): Response<HourlyForecast5Days>


}