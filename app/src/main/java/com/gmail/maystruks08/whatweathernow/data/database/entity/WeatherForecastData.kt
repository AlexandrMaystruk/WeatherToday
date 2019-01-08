package com.gmail.maystruks08.whatweathernow.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherForecastData")
data class WeatherForecastData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var temp: Int,
    var humidity: String,
    var dt_txt: String,
    var icon: String,
    var city: String,
    var latitude: Double,
    var longitude: Double
)

