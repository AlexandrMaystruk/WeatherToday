package com.gmail.maystruks08.whatweathernow.dagger.background


import com.gmail.maystruks08.whatweathernow.ui.settings.SelectBackgroundPresenterImpl
import dagger.Binds
import dagger.Module

@Module
abstract class BackgroundModule {

    @Binds
    @BackgroundScope
    abstract fun presenter(
        backgroundPresenter: SelectBackgroundPresenterImpl
    ): BackgroundContract.Presenter

}