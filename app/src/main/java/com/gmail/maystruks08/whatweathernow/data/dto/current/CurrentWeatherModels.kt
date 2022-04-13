package com.gmail.maystruks08.whatweathernow.data.dto.current

import com.gmail.maystruks08.whatweathernow.data.dto.common.*

data class CurrentWeatherData(
    val coord: Coord,
    val weather: List<ShortWeatherInfo>, //always one element
    val base: String,
    val main: MainDto,
    val visibility: Long,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: SystemWeatherDto,
    val id: Long,
    val name: String,
    val cod: Long
) {

    fun getShortWeatherInfo() = weather.first()

    data class MainDto(
        val temp: Double,
        val pressure: Long,
        val humidity: Long,
        val tempMin: Double,
        val tempMax: Double
    )
}
