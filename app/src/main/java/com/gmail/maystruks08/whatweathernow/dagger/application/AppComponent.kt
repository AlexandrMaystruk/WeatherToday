package com.gmail.maystruks08.whatweathernow.dagger.application

import android.app.Application
import android.content.Context
import com.gmail.maystruks08.whatweathernow.dagger.background.BackgroundComponent
import com.gmail.maystruks08.whatweathernow.dagger.city.CityComponent
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherComponent
import com.gmail.maystruks08.whatweathernow.data.database.WeatherDataBase
import com.gmail.maystruks08.whatweathernow.data.network.NetworkUtil
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import com.gmail.maystruks08.whatweathernow.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class, DatabaseModule::class])
interface AppComponent {

    fun provideContext(): Context
    fun provideWeatherApi(): WeatherApi
    fun provideAppDatabase(): WeatherDataBase
    fun provideNetworkUtil(): NetworkUtil

    fun provideHourlyWeatherComponent(): HourlyWeatherComponent
    fun provideBackgroundComponent(): BackgroundComponent
    fun provideCityComponent(): CityComponent

    fun inject(application: Application)
    fun inject(activity: MainActivity)

}
