package com.gmail.maystruks08.whatweathernow.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import io.reactivex.Single

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * from weatherCurrentData")
    fun getCurrentWeather(): Single<WeatherCurrentData>

    @Insert(onConflict = REPLACE)
    fun insertCurrentWeather(weatherData: WeatherCurrentData)

    @Query("DELETE from weatherCurrentData")
    fun deleteAllFromCurrentWeather()

}