package com.gmail.maystruks08.whatweathernow.dagger.weather

import com.gmail.maystruks08.whatweathernow.ui.forecast.HourlyWeatherForecastFragment
import dagger.Subcomponent

@Subcomponent(modules = [HourlyWeatherModule::class])
@HourlyWeatherScope
interface HourlyWeatherComponent {

    fun inject(hourlyWeatherForecastFragment: HourlyWeatherForecastFragment)

}