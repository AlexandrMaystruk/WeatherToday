package com.gmail.maystruks08.whatweathernow.ui.forecast

import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenter
import com.gmail.maystruks08.whatweathernow.ui.base.BaseView
import kotlinx.coroutines.flow.Flow
import java.util.*

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
    val hourItem: List<HourWeatherItemUi>
)

interface WeatherItemUi

data class HourWeatherItemUi(
    val time: String,
    val humidity: String,
    val temperature: Int,
    val weatherIconUrl: String
): WeatherItemUi


data class DailyForecastUi(
    val minTemp: Float,
    val maxTemp: Float,
    val items: List<DayWeatherItemUi>
)

data class DayWeatherItemUi(
    val sunrise: Long,
    val sunset: Long,
    val pressure: String,
    val humidity: String,
    val clouds: Int,
    val dateTime: String,
    val windSpeed: Float,
    val tempMin: String,
    val tempMax: String,
    val tempMinFeelsLike: Float?,
    val tempMaxFeelsLike: Float?,
    val icon: String,
    val description: String,
    val ultraviolet: String,
    val rain: String,
    val isSelected: Boolean
): WeatherItemUi

interface WeatherForecastContract {

    interface View : BaseView {
        fun updateTime()
        fun showCurrentWeather(forecast: CurrentWeatherUi)
        fun showHourlyWeatherForecast(forecast: HourlyForecastUi)
        fun showFiveDayWeatherForecast(forecast: DailyForecastUi)
        fun setForecastSwitchModeState(enabled: Boolean)
        fun showLoading()
        fun hideLoading()
        fun showError(error: String)
    }

    interface Presenter : BasePresenter<View> {
        fun loadContent()
        fun updateLocation(location: LocaleStorage.Location)
        fun onWeatherModeChanged(enabled: Boolean)
    }


    interface Repository {
        suspend fun getForecastMode(): Flow<ForecastMode>
        suspend fun getCurrentWeather(): Flow<CurrentWeatherData>
        suspend fun getHourlyFiveDayForecast(): Flow<HourlyForecast5Days>
        suspend fun get7DaysForecast(): Flow<DailyForecast7Days>
        suspend fun updateLocation(newLocation: LocaleStorage.Location)
        suspend fun updateForecastMode(newForecastMode: ForecastMode)

    }

}