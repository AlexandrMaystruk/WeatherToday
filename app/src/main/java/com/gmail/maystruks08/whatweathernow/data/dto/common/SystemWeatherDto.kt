package com.gmail.maystruks08.whatweathernow.data.dto.common

data class SystemWeatherDto (
    val type: Long,
    val id: Long,
    val message: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
