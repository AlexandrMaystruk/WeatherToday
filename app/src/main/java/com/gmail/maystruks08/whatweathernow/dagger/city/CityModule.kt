package com.gmail.maystruks08.whatweathernow.dagger.city


import com.gmail.maystruks08.whatweathernow.presentation.presenter.CityPresenter
import dagger.Module
import dagger.Provides

@Module
class CityModule {

    @Provides
    @CityScope
    fun presenter(cityPresenter: CityPresenter): CityContract.Presenter = cityPresenter

}