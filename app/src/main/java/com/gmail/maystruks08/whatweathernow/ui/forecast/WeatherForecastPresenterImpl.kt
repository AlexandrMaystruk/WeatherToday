package com.gmail.maystruks08.whatweathernow.ui.forecast

import android.util.Log
import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.getShortTime
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.parseDate
import com.gmail.maystruks08.whatweathernow.data.repository.WeatherRepository
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import com.gmail.maystruks08.whatweathernow.data.toHumanDayOfWeek
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenterImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject


@FlowPreview
class WeatherForecastPresenterImpl @Inject constructor(
    private var repository: WeatherRepository
) : BasePresenterImpl<WeatherForecastContract.View>(), WeatherForecastContract.Presenter {

    private var timerTickerJob: Job? = null
    private var currentWeatherJob: Job? = null
    private var hourlyFiveDayForecastJob: Job? = null
    private var forecast7DaysJob: Job? = null

    override fun loadContent() {
        presenterCoroutineScope.launch {
            getCurrentWeather()
            repository
                .getForecastMode()
                .collect {
                    val isEnabled = when (it) {
                        ForecastMode.HOURLY -> true
                        ForecastMode.TEN_DAYS -> false
                    }
                    updateUI { setForecastSwitchModeState(isEnabled) }
                    when (it) {
                        ForecastMode.HOURLY -> getHourlyFiveDayForecast()
                        ForecastMode.TEN_DAYS -> get7DaysForecast()
                    }
                }
        }
    }

    override fun updateLocation(location: LocaleStorage.Location) {
        presenterCoroutineScope.launch {
            repository.updateLocation(location)
        }
    }

    override fun onWeatherModeChanged(enabled: Boolean) {
        presenterCoroutineScope.launch {
            val forecastMode = if (enabled) ForecastMode.HOURLY else ForecastMode.TEN_DAYS
            repository.updateForecastMode(forecastMode)
        }
    }

    private fun getCurrentWeather() {
        currentWeatherJob?.cancel()
        currentWeatherJob = presenterCoroutineScope.launch {
            updateUI { view.showLoading() }
            kotlin.runCatching {
                Log.d("RETROFIT", "start call current")
                repository
                    .getCurrentWeather()
                    .map { it.toUI() }
                    .collect {
                        Log.d("RETROFIT", "RESULT current -> ${it.temp}")
                        updateUI {
                            hideLoading()
                            showCurrentWeather(it)
                            timerTickerJob?.cancel()
                            timerTickerJob = presenterCoroutineScope.launch(Dispatchers.IO) {
                                tickerFlow(3 * 60 * 1000).collectLatest {
                                    updateTime()
                                }
                            }
                        }
                    }
            }.onFailure {
                Log.e("RETROFIT", "Error -> ${it.localizedMessage}")
                updateUI {
                    hideLoading()
                    showError(it.localizedMessage.orEmpty())
                }
            }
        }
    }

    private fun getHourlyFiveDayForecast() {
        cancelForecastsJobs()
        hourlyFiveDayForecastJob = presenterCoroutineScope.launch {
            updateUI { view.showLoading() }
            kotlin.runCatching {
                Log.d("RETROFIT", "start call hourly")
                repository
                    .getHourlyFiveDayForecast()
                    .map { it.toUI() }
                    .collect { weatherCurrentData ->
                        Log.d("RETROFIT", "RESULT hourly -> ${weatherCurrentData.city}")
                        updateUI {
                            hideLoading()
                            showHourlyWeatherForecast(weatherCurrentData)
                        }
                    }
            }.onFailure {
                updateUI {
                    hideLoading()
                    showError(it.localizedMessage.orEmpty())
                }
                Log.e("RETROFIT", "Error -> ${it.localizedMessage}")
            }
        }
    }

    private fun get7DaysForecast() {
        cancelForecastsJobs()
        forecast7DaysJob = presenterCoroutineScope.launch {
            updateUI { view.showLoading() }
            kotlin.runCatching {
                Log.d("RETROFIT", "start call 7 days")
                repository
                    .get7DaysForecast()
                    .map { it.toUi() }
                    .collect { forecastItems ->
                        Log.d("RETROFIT", "RESULT 7 days -> ${forecastItems.maxTemp}")
                        updateUI {
                            hideLoading()
                            showFiveDayWeatherForecast(forecastItems)
                        }
                    }
            }.onFailure {
                Log.e("RETROFIT", "Error -> ${it.localizedMessage}")
                updateUI {
                    hideLoading()
                    showError(it.localizedMessage.orEmpty())
                }
            }
        }
    }

    private fun cancelForecastsJobs() {
        hourlyFiveDayForecastJob?.cancel("hourlyFiveDayForecastJob CANCELLED")
        forecast7DaysJob?.cancel("forecast7DaysJob CANCELLED")
    }

    private fun CurrentWeatherData.toUI() = CurrentWeatherUi(
        temp = main.temp.toInt().toString().plus("°C"),
        windSpeed = wind.speed.toString().plus("km/h"),
        humidity = main.humidity.toString().plus("%"),
        sunrise = sys.sunrise * 1000L,
        sunset = sys.sunset * 1000L,
    )

    private fun HourlyForecast5Days.toUI(): HourlyForecastUi {
        //2022-04-12 21:00:00 yyyy-MM-dd HH:mm:ss
        var minTemp = list.firstOrNull()?.main?.temp_min?.toFloat() ?: 0f
        var maxTemp = list.firstOrNull()?.main?.temp_max?.toFloat() ?: 0f

        val hourItem = mutableListOf<HourWeatherItemUi>(
        ).apply {
            list.forEach {
                val shortWeatherInfo = it.getShortWeatherInfo()
                val maxTemperature = it.main.temp_max.toFloat()
                val minTemperature = it.main.temp_min.toFloat()
                if (maxTemperature > maxTemp) maxTemp = maxTemperature
                if (minTemperature < minTemp) minTemp = minTemperature
                val dateTime = it.dt_txt.parseDate()
                add(
                    HourWeatherItemUi(
                        time = "${dateTime.toHumanDayOfWeek()} ${dateTime.getShortTime()}",
                        humidity = it.main.humidity.toString().plus("%"),
                        temperature = it.main.temp.toInt(),
                        weatherIconUrl = "http://openweathermap.org/img/w/${shortWeatherInfo.icon}.png"
                    )
                )
            }
        }
        return HourlyForecastUi(
            hourItem = hourItem,
            city = city.name,
            minTemp = minTemp,
            maxTemp = maxTemp
        )
    }

    private fun DailyForecast7Days.toUi(): DailyForecastUi {
        var minTemp = daily.firstOrNull()?.temp?.min?.toFloat() ?: 0f
        var maxTemp = daily.firstOrNull()?.temp?.max?.toFloat() ?: 0f
        val items = daily.mapIndexed { index, daily ->
            val shortWeatherInfo = daily.getShortWeatherInfo()
            val maxTemperature = daily.temp.max.toFloat()
            val minTemperature = daily.temp.min.toFloat()
            if (maxTemperature > maxTemp) maxTemp = maxTemperature
            if (minTemperature < minTemp) minTemp = minTemperature
            DayWeatherItemUi(
                sunrise = daily.sunrise.toLong() * 1000L,
                sunset = daily.sunset.toLong() * 1000L,
                pressure = daily.pressure.toString(),
                humidity = "${daily.humidity}%",
                clouds = daily.clouds,
                dateTime = Date(daily.dt.toLong() * 1000L).toHumanDayOfWeek(),
                windSpeed = daily.windSpeed.toFloat(),
                tempMin = "${daily.temp.min.toInt()}°C",
                tempMax = "${daily.temp.max.toInt()}°C",
                tempMinFeelsLike = daily.feelsLike?.night?.toFloat(),
                tempMaxFeelsLike = daily.feelsLike?.day?.toFloat(),
                icon = "http://openweathermap.org/img/w/${shortWeatherInfo.icon}.png",
                description = shortWeatherInfo.description,
                ultraviolet = "уф ${daily.uvi.toFloat()}",
                rain = "${daily.rain.toFloat()} mm/ч",
                isSelected = index == 0
            )
        }
        return DailyForecastUi(minTemp, maxTemp, items)
    }

    private fun tickerFlow(period: Long, initialDelay: Long = 0L) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }
}