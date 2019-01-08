package com.gmail.maystruks08.whatweathernow.dagger.application

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides


@Module
class ContextModule(private val mContext: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return mContext
    }
}
