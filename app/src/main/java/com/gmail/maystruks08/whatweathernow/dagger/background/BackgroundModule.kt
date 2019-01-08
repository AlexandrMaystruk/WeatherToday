package com.gmail.maystruks08.whatweathernow.dagger.background


import com.gmail.maystruks08.whatweathernow.presentation.presenter.SelectBackgroundPresenter
import dagger.Module
import dagger.Provides

@Module
class BackgroundModule {


    @Provides
    @BackgroundScope
    fun presenter(backgroundPresenter: SelectBackgroundPresenter): BackgroundContract.Presenter =
        backgroundPresenter


}