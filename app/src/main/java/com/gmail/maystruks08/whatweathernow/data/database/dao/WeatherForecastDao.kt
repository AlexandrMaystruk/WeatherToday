package com.gmail.maystruks08.whatweathernow.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import io.reactivex.Single

@Dao
interface WeatherForecastDao {

    @Query("SELECT * from weatherForecastData")
    fun getForecastWeather(): Single<List<WeatherForecastData>>

    @Insert(onConflict = REPLACE)
    fun insertForecastWeather(weatherData: List<WeatherForecastData>)

    @Query("DELETE from weatherForecastData")
    fun deleteAllFromForecastWeather()
}