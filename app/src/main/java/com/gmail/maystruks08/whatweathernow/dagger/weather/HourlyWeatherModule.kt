package com.gmail.maystruks08.whatweathernow.dagger.weather

import android.content.Context
import com.gmail.maystruks08.whatweathernow.FindLocation
import com.gmail.maystruks08.whatweathernow.data.model.HourlyWeatherRepository
import com.gmail.maystruks08.whatweathernow.presentation.presenter.HourlyWeatherPresenter
import dagger.Module
import dagger.Provides

@Module
class HourlyWeatherModule {

    @Provides
    @HourlyWeatherScope
    fun repository(hourlyWeatherRepository: HourlyWeatherRepository): HourlyWeatherContract.Repository =
        hourlyWeatherRepository


    @Provides
    @HourlyWeatherScope
    fun presenter(hourlyWeatherPresenter: HourlyWeatherPresenter): HourlyWeatherContract.Presenter =
        hourlyWeatherPresenter


    @Provides
    @HourlyWeatherScope
    fun provideLocationManager(context: Context): FindLocation {
        return FindLocation(context)
    }

}