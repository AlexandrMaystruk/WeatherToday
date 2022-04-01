package com.gmail.maystruks08.whatweathernow.dagger.application

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ContextModule(private val mContext: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return mContext
    }
}
