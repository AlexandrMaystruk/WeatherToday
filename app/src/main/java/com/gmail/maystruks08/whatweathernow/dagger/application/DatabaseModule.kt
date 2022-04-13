package com.gmail.maystruks08.whatweathernow.dagger.application

import android.content.Context
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun localeStorage(
        context: Context
    ) = LocaleStorage(context)

}