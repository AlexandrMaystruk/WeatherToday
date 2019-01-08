package com.gmail.maystruks08.whatweathernow.dagger.weather

import android.net.ConnectivityManager
import com.gmail.maystruks08.whatweathernow.ui.fragments.HourlyWeatherFragment
import dagger.Subcomponent

@Subcomponent(modules = [HourlyWeatherModule::class])
@HourlyWeatherScope
interface HourlyWeatherComponent {

    fun inject(hourlyWeatherFragment: HourlyWeatherFragment)

}