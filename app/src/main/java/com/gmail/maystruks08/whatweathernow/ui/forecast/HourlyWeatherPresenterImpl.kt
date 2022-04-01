package com.gmail.maystruks08.whatweathernow.ui.forecast

import android.util.Log
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherContract
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.data.repository.HourlyWeatherRepository
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HourlyWeatherPresenterImpl @Inject constructor(
    private var repository: HourlyWeatherRepository
) : BasePresenterImpl<HourlyWeatherContract.View>(), HourlyWeatherContract.Presenter {


    override fun loadContent() {
        getForecastByLatLng()
        getFiveDayForecastByLatLng()
    }

    override fun updateLocation(location: LocaleStorage.Location) {
        presenterCoroutineScope.launch {
            repository.updateLocation(location)
        }
    }

    private fun getForecastByLatLng() {
        view.showLoading()
        presenterCoroutineScope.launch {
            kotlin.runCatching {
                repository
                    .getForecastByLatLng()
                    .collect {
                        withContext(Dispatchers.Main) {
                            view.hideLoading()
                            view.showWeatherForecastByLatLnd(it)
                        }
                        Log.d("RETROFIT", "RESULT -> $it")
                    }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showError(it.localizedMessage.orEmpty())
                }
                Log.e("RETROFIT", "Error -> ${it.localizedMessage}")
            }
        }
    }

    private fun getFiveDayForecastByLatLng() {
        view.showLoading()
        presenterCoroutineScope.launch {
            kotlin.runCatching {
                repository
                    .getFiveDayForecastByLatLng()
                    .collect { weatherCurrentData ->
                        withContext(Dispatchers.Main) {
                            view.hideLoading()
                            view.showFiveDayWeatherForecast(weatherCurrentData)
                        }
                        Log.d("RETROFIT", "RESULT -> $weatherCurrentData")
                    }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showError(it.localizedMessage.orEmpty())
                }
                Log.e("RETROFIT", "Error -> ${it.localizedMessage}")
            }
        }
    }

}
