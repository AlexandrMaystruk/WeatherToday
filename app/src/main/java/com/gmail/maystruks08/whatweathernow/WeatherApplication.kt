package com.gmail.maystruks08.whatweathernow

import android.app.Application
import com.gmail.maystruks08.whatweathernow.dagger.application.*


class WeatherApplication : Application() {

    companion object {

        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
            .builder()
            .networkModule(NetworkModule())
            .navigationModule(NavigationModule())
            .databaseModule(DatabaseModule())
            .contextModule(ContextModule(this))
            .build()

        component.inject(this)

    }
}



