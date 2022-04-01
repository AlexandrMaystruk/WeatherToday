package com.gmail.maystruks08.whatweathernow.dagger.application

import android.content.Context
import androidx.room.Room
import com.gmail.maystruks08.whatweathernow.data.database.WeatherDataBase
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(
        context: Context
    ) = Room.databaseBuilder(context, WeatherDataBase::class.java, "weather_db").build()

    @Provides
    @Singleton
    fun localeStorage(
        context: Context
    ) = LocaleStorage(context)

}