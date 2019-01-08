package com.gmail.maystruks08.whatweathernow.dagger.application


import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigationModule {

    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun router (): Router = cicerone.router

    @Provides
    @Singleton
    fun navigatorHolder (): NavigatorHolder = cicerone.navigatorHolder

}
