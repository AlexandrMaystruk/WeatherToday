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


class WeatherForecastFragment : BaseFragment(), WeatherForecastContract.View {

    @Inject
    lateinit var location: FindLocation

    @Inject
    lateinit var presenter: WeatherForecastContract.Presenter

    private var hourlyForecastRecyclerAdapter: HourlyForecastRecyclerAdapter? = null

    override fun provideLayoutRes(): Int {
        return R.layout.fragment_hourly_weather
    }

    override fun initPresenter() {
        WeatherApplication.component.provideHourlyWeatherComponent().inject(this)
        presenter.attach(this)
    }

    override fun initViews() {
        checkLocationPermission()

        activity()?.run {
            removeNavigationIcon()
            showOptionMenu(true)
            configOverlay(true)
        }
        hourlyForecastRecyclerAdapter = HourlyForecastRecyclerAdapter()
        recycler3hForecast.adapter = hourlyForecastRecyclerAdapter
        recycler3hForecast.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        presenter.loadContent()
    }

    override fun showCurrentWeather(forecast: CurrentWeatherUi) {
        tvWindSpeed.text = forecast.windSpeed
        tvTemperatureNow.text = forecast.temp
        tvCurrentTime.text = SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        tvCurrentDate.text = SimpleDateFormat(
            "EEE, d MMM yyyy",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        tvHumidity.text = forecast.humidity
        tvSunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(forecast.sunrise)
        tvSunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(forecast.sunset)
        customWeatherSunClock.setSunriseSunsetTime(forecast.sunrise, forecast.sunset)

    }

    override fun updateTime() {
        val date = Date(Calendar.getInstance().time.time)
        customWeatherSunClock?.setProgress(date.time)
    }

    override fun showHourlyWeatherForecast(forecast: HourlyForecastUi) {
        (recycler3hForecast?.adapter as? HourlyForecastRecyclerAdapter)
            ?.update(forecast.minTemp, forecast.maxTemp, forecast.hourItem)

        activity()?.setToolbarTitle(forecast.city)
    }

    override fun showFiveDayWeatherForecast(forecast: DailyForecastUi) {
        (recycler3hForecast?.adapter as? HourlyForecastRecyclerAdapter)
            ?.update(forecast.minTemp, forecast.maxTemp, forecast.items)
    }

    override fun setForecastSwitchModeState(enabled: Boolean) {
        switchWeatherMode.setOnCheckedChangeListener(null)
        switchWeatherMode.isChecked = enabled
        switchWeatherMode.setOnCheckedChangeListener { _, isEnabled ->
            presenter.onWeatherModeChanged(isEnabled)
        }
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
        location.stopLocationMonitoring()
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
        fun newInstance(): WeatherForecastFragment {
            val fragment = WeatherForecastFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
