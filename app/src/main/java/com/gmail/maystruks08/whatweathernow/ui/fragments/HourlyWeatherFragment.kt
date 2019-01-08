package com.gmail.maystruks08.whatweathernow.ui.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.weather.HourlyWeatherContract
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.ui.adapter.HourlyForecastRecyclerAdapter
import com.gmail.maystruks08.whatweathernow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_hourly_weather.*
import kotlinx.android.synthetic.main.fragment_hourly_weather.view.*

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.content.Intent
import android.net.ConnectivityManager
import android.content.IntentFilter
import android.location.Location
import com.gmail.maystruks08.whatweathernow.FindLocation
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener


class HourlyWeatherFragment : BaseFragment(), HourlyWeatherContract.View {

    @Inject
    lateinit var location: FindLocation

    @Inject
    lateinit var mHourlyWeatherPresenter: HourlyWeatherContract.Presenter

    private lateinit var mAdapter: HourlyForecastRecyclerAdapter


    override fun provideLayoutRes(): Int {
        return R.layout.fragment_hourly_weather
    }

    override fun initPresenter() {
        WeatherApplication.component.provideHourlyWeatherComponent().inject(this)
        mHourlyWeatherPresenter.attach(this)
    }

    override fun initViews() {
        checkLocationPermission()

        activity()?.removeNavigationIcon()
        activity()?.showOptionMenu(true)
        activity()?.configOverlay(true)

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context?.registerReceiver(networkChangeReceiver, intentFilter)

        recycler3hForecast?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    override fun showWeatherForecastByLatLnd(forecast: WeatherCurrentData) {

        //set progress each minutes
        val handler = Handler(Looper.myLooper())
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post {

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
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHourlyWeatherPresenter.detach()

    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(networkChangeReceiver)

    }

    private fun checkLocationPermission() {
        Dexter.withActivity(activity())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    location.startLocationMonitoring()

                    location.addLocationListener(object : FindLocation.MyLocationChange {
                        override fun callbackLocationChange(loc: Location) {


                            mHourlyWeatherPresenter.getForecastByLatLng(
                                loc.latitude.toString(),
                                loc.longitude.toString()
                            )

                            mHourlyWeatherPresenter.getFiveDayForecastByLatLng(
                                loc.latitude.toString(),
                                loc.longitude.toString()
                            )

                            //Save location to SharedPreferences
                            val pref = activity()?.getSharedPreferences("CURRENT_LOCATION", Context.MODE_PRIVATE)
                            val ed = pref?.edit()
                            ed?.putString("LOCATION_LATITUDE", loc.latitude.toString())
                            ed?.putString("LOCATION_LONGITUDE", loc.longitude.toString())
                            ed?.apply()
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

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            if (activeNetwork != null && activeNetwork.isConnected) {
                activity()?.configToolbarOfflineMessage(false)

                //get coordinate from SharedPreferences when Internet Active
                val pref = activity()?.getSharedPreferences("CURRENT_LOCATION", Context.MODE_PRIVATE)
                val lat = pref?.getString("LOCATION_LATITUDE", "46.484579")
                val lon = pref?.getString("LOCATION_LONGITUDE", "30.732597")

                if (lat != null  && lon != null && lat != "-1" && lon != "-1") {
                    mHourlyWeatherPresenter.getForecastByLatLng(lat, lon)
                    mHourlyWeatherPresenter.getFiveDayForecastByLatLng(lat, lon)
                }

            } else
                activity()?.configToolbarOfflineMessage(true)
        }
    }

    companion object {
        fun newInstance(): HourlyWeatherFragment {
            val fragment = HourlyWeatherFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
