package com.gmail.maystruks08.whatweathernow.dagger.weather

import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.data.model.forecast.ForecastWeather
import com.gmail.maystruks08.whatweathernow.presentation.base.BaseView
import io.reactivex.Single

interface HourlyWeatherContract {

    interface View : BaseView {

        fun showWeatherForecastByLatLnd(forecast: WeatherCurrentData)

        fun showFiveDayWeatherForecast(forecast: List<WeatherForecastData>)

    }

    interface Presenter {
        fun attach(view: HourlyWeatherContract.View)
        fun getForecastByLatLng(lat: String, lon: String)
        fun getFiveDayForecastByLatLng(lat: String, lon: String)
        fun detach()
    }


    interface Repository {

        fun getForecastByLatLng(lat: String, lon: String): Single<WeatherCurrentData>?
        fun getFiveDayForecastByLatLng(lat: String, lon: String): Single<List<WeatherForecastData>>?


    }

}