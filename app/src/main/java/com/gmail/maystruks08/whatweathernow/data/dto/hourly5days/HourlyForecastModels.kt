package com.gmail.maystruks08.whatweathernow.data.dto.hourly5days

import com.gmail.maystruks08.whatweathernow.data.dto.common.City
import com.gmail.maystruks08.whatweathernow.data.dto.common.Clouds
import com.gmail.maystruks08.whatweathernow.data.dto.common.SystemWeatherDto
import com.gmail.maystruks08.whatweathernow.data.dto.common.ShortWeatherInfo
import com.gmail.maystruks08.whatweathernow.data.dto.common.Wind

data class HourlyForecast5Days(
    val cod: String,
    val message: Double,
    val cnt: Long,
    val list: List<HourlySectionItemDto>,
    val city: City
)

data class HourlySectionItemDto(
    val dt: Long,
    val main: MainHourlyWeatherDto,
    val weather: List<ShortWeatherInfo>, //always one element
    val clouds: Clouds,
    val wind: Wind,
    val sys: SystemWeatherDto,
    val dt_txt: String,
    val rain: Rain? = null
){
    fun getShortWeatherInfo() = weather.first()

    data class MainHourlyWeatherDto(
        val temp: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Double,
        val sea_level: Double,
        val grnd_level: Double,
        val humidity: Long,
        val temp_kf: Double
    )

    data class Rain(
        val the3H: Double
    )
}




