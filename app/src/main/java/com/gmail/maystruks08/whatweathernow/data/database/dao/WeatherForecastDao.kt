package com.gmail.maystruks08.whatweathernow.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData

@Dao
interface WeatherForecastDao {

    @Query("SELECT * from weatherForecastData")
    suspend fun getForecastWeather(): List<WeatherForecastData>

    @Insert(onConflict = REPLACE)
    suspend fun insertForecastWeather(weatherData: List<WeatherForecastData>)

    @Query("DELETE from weatherForecastData")
    suspend fun deleteAllFromForecastWeather()
}