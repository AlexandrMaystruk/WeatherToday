package com.gmail.maystruks08.whatweathernow.ui.forecast

import android.util.Log
import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.repository.WeatherRepository
import com.gmail.maystruks08.whatweathernow.data.repository.entity.ForecastMode
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenterImpl
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@FlowPreview
class WeatherForecastPresenterImpl @Inject constructor(
    private var repository: WeatherRepository
) : BasePresenterImpl<WeatherForecastContract.View>(), WeatherForecastContract.Presenter {

    val calendar = Calendar.getInstance()

    override fun loadContent() {
        presenterCoroutineScope.launch {
            repository
                .getForecastMode()
                .collect {
                    when (it) {
                        ForecastMode.HOURLY -> getHourlyForecast()
                        ForecastMode.TEN_DAYS -> {

                        }
                    }
                }
        }
    }

    override fun updateLocation(location: LocaleStorage.Location) {
        presenterCoroutineScope.launch {
            repository.updateLocation(location)
        }
    }

    private fun getHourlyForecast() {
        getForecastByLatLng()
        getHourlyFiveDayForecastByLatLng()
//        get7DaysForecastByLatLng()
    }

    private fun getForecastByLatLng() {
        view.showLoading()
        presenterCoroutineScope.launch {
            kotlin.runCatching {
                Log.d("RETROFIT", "start call current")
                repository
                    .getCurrentWeather()
                    .map { it.toUI() }
                    .collect {
                        Log.d("RETROFIT", "RESULT current -> $it")
                        updateUI {
                            hideLoading()
                            showCurrentWeather(it)
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

    private fun getHourlyFiveDayForecastByLatLng() {
        view.showLoading()
        presenterCoroutineScope.launch {
            kotlin.runCatching {
                Log.d("RETROFIT", "start call hourly")
                repository
                    .getHourlyFiveDayForecast()
                    .map { it.toUI() }
                    .collect { weatherCurrentData ->
                        Log.d("RETROFIT", "RESULT hourly -> $weatherCurrentData")
                        updateUI {
                            hideLoading()
                            showFiveDayWeatherForecast(weatherCurrentData)
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

    private fun get7DaysForecastByLatLng() {
        view.showLoading()
        presenterCoroutineScope.launch {
            kotlin.runCatching {
                Log.d("RETROFIT", "start call 7 days")
                repository
                    .get7DaysForecast()
                    .map {
                        //map to ui
                    }
                    .collect { weatherCurrentData ->
                        Log.d("RETROFIT", "RESULT 7 days -> $weatherCurrentData")
                        updateUI {
                            hideLoading()
                            //TODO show on UI
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

    private fun CurrentWeatherData.toUI() = CurrentWeatherUi(
        temp = main.temp.toString().plus("°C"),
        windSpeed = wind.speed.toString().plus("km/h"),
        humidity = main.humidity.toString().plus("%"),
        sunrise = sys.sunrise * 1000L,
        sunset = sys.sunset * 1000L,
    )

    private fun HourlyForecast5Days.toUI(): HourlyForecastUi {
        //2022-04-12 21:00:00 yyyy-MM-dd HH:mm:ss
        var minTemp = list.firstOrNull()?.main?.temp_min?.toFloat() ?: 0f
        var maxTemp = list.firstOrNull()?.main?.temp_max?.toFloat() ?: 0f

        val hourItem = mutableListOf<HourItemUi>(
        ).apply {
            list.forEach {
                val shortWeatherInfo = it.getShortWeatherInfo()
                val maxTemperature = it.main.temp_max.toFloat()
                val minTemperature = it.main.temp_min.toFloat()
                if (maxTemperature > maxTemp) maxTemp = maxTemperature
                if (minTemperature < minTemp) minTemp = minTemperature


                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                calendar.time = formatter.parse(it.dt_txt) ?: Date()

                add(
                    HourItemUi(
                        time = "${calendar.get(Calendar.DAY_OF_WEEK)} ${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)} ",
                        humidity = it.main.humidity.toString().plus(" %"),
                        temperature = it.main.temp.toFloat(),
                        maxTemperature = it.main.temp_max.toFloat(),
                        minTemperature = it.main.temp_min.toFloat(),
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
}
