package com.gmail.maystruks08.whatweathernow.ui.forecast

import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenter
import com.gmail.maystruks08.whatweathernow.ui.base.BaseView
import kotlinx.coroutines.flow.Flow

data class CurrentWeatherUi(
    val temp: String,
    val windSpeed: String,
    val humidity: String,
    val sunrise: Long,
    val sunset: Long
)

data class HourlyForecastUi(
    val city: String,
    val minTemp: Float,
    val maxTemp: Float,
    val hourItem: List<HourItemUi>
)

data class HourItemUi(
    val time: String,
    val humidity: String,
    val maxTemperature: Float,
    val minTemperature: Float,
    val temperature: Float,
    val weatherIconUrl: String
)

interface WeatherForecastContract {

    interface View : BaseView {
        fun showCurrentWeather(forecast: CurrentWeatherUi)
        fun showFiveDayWeatherForecast(forecast: HourlyForecastUi)
        fun showLoading()
        fun hideLoading()
        fun showError(error: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadContent()
        fun updateLocation(location: LocaleStorage.Location)
    }


    interface Repository {
        suspend fun getForecastMode(): Flow<ForecastMode>
        suspend fun getCurrentWeather(): Flow<CurrentWeatherData>
        suspend fun getHourlyFiveDayForecast(): Flow<HourlyForecast5Days>
        suspend fun get7DaysForecast(): Flow<DailyForecast7Days>
        suspend fun updateLocation(newLocation: LocaleStorage.Location)
    }

}