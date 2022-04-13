package com.gmail.maystruks08.whatweathernow.data.dto.common

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int
)