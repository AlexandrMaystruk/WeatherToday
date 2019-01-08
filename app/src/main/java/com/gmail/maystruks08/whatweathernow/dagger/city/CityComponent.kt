package com.gmail.maystruks08.whatweathernow.dagger.city

import com.gmail.maystruks08.whatweathernow.ui.fragments.CityFragment
import com.gmail.maystruks08.whatweathernow.ui.fragments.SelectBackgroundFragment
import dagger.Subcomponent

@Subcomponent(modules = [CityModule::class])
@CityScope
interface CityComponent {

    fun inject( cityFragment: CityFragment)

}