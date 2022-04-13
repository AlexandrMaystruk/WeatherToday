package com.gmail.maystruks08.whatweathernow.dagger.weather

import android.content.Context
import com.gmail.maystruks08.whatweathernow.FindLocation
import com.gmail.maystruks08.whatweathernow.data.repository.WeatherRepository
import com.gmail.maystruks08.whatweathernow.ui.forecast.WeatherForecastContract
import com.gmail.maystruks08.whatweathernow.ui.forecast.WeatherForecastPresenterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class HourlyWeatherModule {

    @Binds
    @HourlyWeatherScope
    abstract fun repository(weatherRepository: WeatherRepository): WeatherForecastContract.Repository


    @Binds
    @HourlyWeatherScope
    abstract fun presenter(weatherForecastPresenter: WeatherForecastPresenterImpl): WeatherForecastContract.Presenter


    companion object {
        @Provides
        @HourlyWeatherScope
        fun provideLocationManager(context: Context): FindLocation {
            return FindLocation(context)
        }
    }

}