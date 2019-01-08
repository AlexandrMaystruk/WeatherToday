package com.gmail.maystruks08.whatweathernow.dagger.application

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import com.gmail.maystruks08.whatweathernow.data.network.WeatherApi
import dagger.Module
import dagger.Provides

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import com.gmail.maystruks08.whatweathernow.data.BASE_URL
import com.gmail.maystruks08.whatweathernow.data.CONNECT_TIMEOUT
import com.gmail.maystruks08.whatweathernow.data.READ_TIMEOUT
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofitInterface(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).build()
    }


    @Provides
    @Singleton
    fun provideConnectionManager(mContext: Context): ConnectivityManager {
        return mContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
