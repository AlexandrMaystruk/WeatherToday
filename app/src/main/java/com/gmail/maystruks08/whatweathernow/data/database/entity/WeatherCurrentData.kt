package com.gmail.maystruks08.whatweathernow.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherCurrentData")
data class WeatherCurrentData(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var temp: String,
    var windSpeed: Double,
    var humidity: String,
    var sunrise: Long,
    var sunset: Long
)
