package com.gmail.maystruks08.whatweathernow.dagger.weather

import com.gmail.maystruks08.whatweathernow.ui.forecast.WeatherForecastFragment
import dagger.Subcomponent

@Subcomponent(modules = [HourlyWeatherModule::class])
@HourlyWeatherScope
interface HourlyWeatherComponent {

    fun inject(weatherForecastFragment: WeatherForecastFragment)

}