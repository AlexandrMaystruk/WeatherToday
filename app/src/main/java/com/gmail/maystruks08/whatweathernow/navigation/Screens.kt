package com.gmail.maystruks08.whatweathernow.navigation

import com.gmail.maystruks08.whatweathernow.ui.fragments.CityFragment
import com.gmail.maystruks08.whatweathernow.ui.fragments.HourlyWeatherFragment
import com.gmail.maystruks08.whatweathernow.ui.fragments.SelectBackgroundFragment

object Screens {

    object HourlyFragment : AppScreen() {
        override fun getFragment() = HourlyWeatherFragment.newInstance()
    }

    object BackgroundsFragment : AppScreen() {
        override fun getFragment() = SelectBackgroundFragment.newInstance()
    }

    object MyCityFragment : AppScreen() {
        override fun getFragment() = CityFragment.newInstance()
    }

}