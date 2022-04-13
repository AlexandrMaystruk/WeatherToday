package com.gmail.maystruks08.whatweathernow.data.dto.daily7days

import com.gmail.maystruks08.whatweathernow.data.dto.common.ShortWeatherInfo


data class DailyForecast7Days(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val daily: List<Daily>
)

data class Daily(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val windGust: Double,
    val weather: List<ShortWeatherInfo>, //always one element
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
){
    fun getShortWeatherInfo() = weather.first()

    data class FeelsLike(
        val day: Double,
        val night: Double,
        val eve: Double,
        val morn: Double
    )

    data class Temp(
        val day: Double,
        val min: Double,
        val max: Double,
        val night: Double,
        val eve: Double,
        val morn: Double
    )
}




