package com.gmail.maystruks08.whatweathernow.data.model.forecast

data class ForecastWeather(
    val cod: String,
    val message: Double,
    val cnt: Long,
    val list: List<ListElement>,
    val city: City
)


data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class ListElement(
    val dt: Long,
    val main: MainClass,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val sys: Sys,
    val dt_txt: String,
    val rain: Rain? = null
)

data class Clouds(
    val all: Long
)

data class MainClass(
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

data class Sys(
    val pod: String
)


data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)


data class Wind(
    val speed: Double,
    val deg: Double
)
