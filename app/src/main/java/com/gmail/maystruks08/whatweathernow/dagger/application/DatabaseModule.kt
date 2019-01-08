package com.gmail.maystruks08.whatweathernow.dagger.application

import android.content.Context
import androidx.room.Room
import com.gmail.maystruks08.whatweathernow.data.database.WeatherDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {


    //TODO remove allowMainThreadQueries
    @Provides
    @Singleton
    fun appDatabase(context: Context): WeatherDataBase =
        Room.databaseBuilder(context, WeatherDataBase::class.java, "weather_db").allowMainThreadQueries().build()
}