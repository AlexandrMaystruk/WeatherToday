package com.gmail.maystruks08.whatweathernow.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmail.maystruks08.whatweathernow.data.database.dao.CurrentWeatherDao
import com.gmail.maystruks08.whatweathernow.data.database.dao.WeatherForecastDao
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData

@Database(entities = [WeatherCurrentData::class, WeatherForecastData::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun weatherCurrentWeatherDao(): CurrentWeatherDao
    abstract fun weatherForecastDao(): WeatherForecastDao

}