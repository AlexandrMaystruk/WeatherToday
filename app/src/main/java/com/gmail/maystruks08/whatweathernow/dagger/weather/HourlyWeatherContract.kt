package com.gmail.maystruks08.whatweathernow.dagger.weather

import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.ui.base.BaseView
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenter
import kotlinx.coroutines.flow.Flow

interface HourlyWeatherContract {

    interface View : BaseView {
        fun showWeatherForecastByLatLnd(forecast: WeatherCurrentData)
        fun showFiveDayWeatherForecast(forecast: List<WeatherForecastData>)
        fun showLoading()
        fun hideLoading()
        fun showError(error: String)
    }

    interface Presenter: BasePresenter<View> {
        fun loadContent()
        fun updateLocation(location: LocaleStorage.Location)
    }


    interface Repository {
        suspend fun getForecastByLatLng(): Flow<WeatherCurrentData>
        suspend fun getFiveDayForecastByLatLng(): Flow<List<WeatherForecastData>>
        suspend fun updateLocation(newLocation: LocaleStorage.Location)
    }

}