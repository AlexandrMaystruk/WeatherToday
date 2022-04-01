package com.gmail.maystruks08.whatweathernow.dagger.city


import com.gmail.maystruks08.whatweathernow.ui.editlocation.CityPresenterImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CityModule {

    @Binds
    @CityScope
    abstract fun presenter(cityPresenter: CityPresenterImpl): CityContract.Presenter

}