package com.gmail.maystruks08.whatweathernow.ui.forecast

import android.Manifest
import android.app.AlertDialog
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.maystruks08.whatweathernow.FindLocation
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherContract
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.data.network.LocaleStorage
import com.gmail.maystruks08.whatweathernow.ui.base.BaseFragment
import com.gmail.maystruks08.whatweathernow.ui.forecast.adapter.HourlyForecastRecyclerAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_hourly_weather.*
import kotlinx.android.synthetic.main.fragment_hourly_weather.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class HourlyWeatherForecastFragment : BaseFragment(), HourlyWeatherContract.View {

    @Inject
    lateinit var location: FindLocation

    @Inject
    lateinit var presenter: HourlyWeatherContract.Presenter

    private var mAdapter: HourlyForecastRecyclerAdapter? = null


    override fun provideLayoutRes(): Int {
        return R.layout.fragment_hourly_weather
    }

    override fun initPresenter() {
        WeatherApplication.component.provideHourlyWeatherComponent().inject(this)
        presenter.attach(this)
    }

    override fun initViews() {
        checkLocationPermission()

        activity()?.removeNavigationIcon()
        activity()?.showOptionMenu(true)
        activity()?.configOverlay(true)

        recycler3hForecast?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        presenter.loadContent()
    }

    override fun showWeatherForecastByLatLnd(forecast: WeatherCurrentData) {
        //set progress each minutes
        Timer().schedule(object : TimerTask() {
            override fun run() {
                view?.post {
                    //get current time and set progress, sunrise and sunset
                    val date = Date(Calendar.getInstance().time.time)
                    customWeatherSunClock?.setProgress(date.time)
                }
            }
        }, 0, 6000)


        tvWindSpeed?.text = forecast.windSpeed.toString().plus("km/h")
        tvTemperatureNow?.text = forecast.temp.plus("°C")

        tvCurrentTime?.text = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        tvCurrentDate?.text = SimpleDateFormat(
            "EEE, d MMM yyyy",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        tvHumidity?.text = forecast.humidity.plus("%")

        customWeatherSunClock?.setSunriseSunsetTime(forecast.sunrise * 1000L, forecast.sunset * 1000L)

        tvSunrise?.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(forecast.sunrise * 1000L)
        tvSunset?.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(forecast.sunset * 1000L)

    }

    override fun showFiveDayWeatherForecast(forecast: List<WeatherForecastData>) {
        mAdapter = HourlyForecastRecyclerAdapter(forecast, context)
        recycler3hForecast?.adapter = mAdapter

        activity()?.setToolbarTitle(forecast[0].city)
    }

    override fun showLoading() {
        mView.progress.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mView.progress.visibility = View.GONE
    }

    override fun showError(error: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("Error description")
            .setPositiveButton("Reload") { di, _ ->
                presenter.loadContent()
                di.dismiss()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
        recycler3hForecast?.adapter = null
    }

    private fun checkLocationPermission() {
        Dexter.withActivity(activity())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    location.addLocationListener(object : FindLocation.MyLocationChange {
                        override fun callbackLocationChange(loc: Location) {
                            presenter.updateLocation(
                                LocaleStorage.Location(
                                    loc.latitude.toString(),
                                    loc.longitude.toString()
                                )
                            )
                        }
                    })
                    location.startLocationMonitoring()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    DialogOnDeniedPermissionListener.Builder
                        .withContext(activity())
                        .withTitle("Location permission")
                        .withMessage("Find location permission is needed to take coordinate you city")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                }
            }).check()
    }

    companion object {
        fun newInstance(): HourlyWeatherForecastFragment {
            val fragment = HourlyWeatherForecastFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
