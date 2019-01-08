package com.gmail.maystruks08.whatweathernow.presentation.presenter

import android.util.Log
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherContract
import com.gmail.maystruks08.whatweathernow.presentation.base.BasePresenter
import com.gmail.maystruks08.whatweathernow.data.model.HourlyWeatherRepository
import ru.terrakok.cicerone.Router

import javax.inject.Inject


class HourlyWeatherPresenter @Inject constructor(
    private var repository: HourlyWeatherRepository,
    private var router: Router?
) :
    BasePresenter(), HourlyWeatherContract.Presenter {


    var mView: HourlyWeatherContract.View? = null


    override fun attach(view: HourlyWeatherContract.View) {
        this.mView = view
//        static Odessa coordinate
//        getForecastByLatLng("46.484579", "30.732597")
//        getFiveDayForecastByLatLng("46.484579", "30.732597")
    }


    override fun getForecastByLatLng(lat: String, lon: String) {

        mView?.showLoading()
        compositeDiposable(repository.getForecastByLatLng(lat, lon)
            ?.subscribe(
                { result ->
                    mView?.hideLoading()
                    mView?.showWeatherForecastByLatLnd(result)
                    Log.d("RETROFIT", "RESULT -> " + result.toString())
                },
                { error ->
                    mView?.hideLoading()
                    mView?.showError(error.toString())
                    Log.e("RETROFIT", "ERROR -> " + error.message)
                }
            ))
    }


    override fun getFiveDayForecastByLatLng(lat: String, lon: String) {

        mView?.showLoading()

        compositeDiposable(repository.getFiveDayForecastByLatLng(lat, lon)
            ?.subscribe(
                { result ->
                    mView?.hideLoading()
                    mView?.showFiveDayWeatherForecast(result)
                    Log.d("RETROFIT", "RESULT -> " + result.toString())
                },
                { error ->
                    mView?.hideLoading()
                    mView?.showError(error.toString())
                    Log.e("RETROFIT", "ERROR -> " + error.message)
                }
            ))
    }


    override fun detach() {
        compositeDisposable.clear()
    }


}

