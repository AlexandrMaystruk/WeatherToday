package com.gmail.maystruks08.whatweathernow.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * from weatherCurrentData")
    suspend fun getCurrentWeather(): WeatherCurrentData

    @Insert(onConflict = REPLACE)
    suspend fun insertCurrentWeather(weatherData: WeatherCurrentData)

    @Query("DELETE from weatherCurrentData")
    suspend fun deleteAllFromCurrentWeather()

}