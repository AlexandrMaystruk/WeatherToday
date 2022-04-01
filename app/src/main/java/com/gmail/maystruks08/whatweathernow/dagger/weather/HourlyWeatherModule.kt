package com.gmail.maystruks08.whatweathernow.dagger.weather

import android.content.Context
import com.gmail.maystruks08.whatweathernow.FindLocation
import com.gmail.maystruks08.whatweathernow.data.repository.HourlyWeatherRepository
import com.gmail.maystruks08.whatweathernow.ui.forecast.HourlyWeatherPresenterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class HourlyWeatherModule {

    @Binds
    @HourlyWeatherScope
    abstract fun repository(hourlyWeatherRepository: HourlyWeatherRepository): HourlyWeatherContract.Repository


    @Binds
    @HourlyWeatherScope
    abstract fun presenter(hourlyWeatherPresenter: HourlyWeatherPresenterImpl): HourlyWeatherContract.Presenter


    companion object {
        @Provides
        @HourlyWeatherScope
        fun provideLocationManager(context: Context): FindLocation {
            return FindLocation(context)
        }
    }

}