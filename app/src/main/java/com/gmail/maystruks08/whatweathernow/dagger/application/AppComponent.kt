package com.gmail.maystruks08.whatweathernow.dagger.application

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.gmail.maystruks08.whatweathernow.FindLocation
import com.gmail.maystruks08.whatweathernow.dagger.background.BackgroundComponent
import com.gmail.maystruks08.whatweathernow.dagger.city.CityComponent
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherComponent
import com.gmail.maystruks08.whatweathernow.data.database.WeatherDataBase
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import com.gmail.maystruks08.whatweathernow.ui.activity.MainActivity
import javax.inject.Singleton
import dagger.Component
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class, NavigationModule::class, DatabaseModule::class])
interface AppComponent {

    fun provideContext(): Context

    fun provideRouter(): Router

    fun provideNavigatorHolder(): NavigatorHolder

    fun provideWeatherApi(): WeatherApi

    fun provideAppDatabase(): WeatherDataBase

    fun provideConnectionManager(): ConnectivityManager


    fun provideHourlyWeatherComponent(): HourlyWeatherComponent
    fun provideBackgroundComponent(): BackgroundComponent
    fun provideCityComponent(): CityComponent


    fun inject(application: Application)

    fun inject(activity: MainActivity)

}
