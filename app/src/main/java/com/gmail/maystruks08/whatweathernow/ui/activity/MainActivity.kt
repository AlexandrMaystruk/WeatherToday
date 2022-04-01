package com.gmail.maystruks08.whatweathernow.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.ui.editlocation.CityFragment
import com.gmail.maystruks08.whatweathernow.ui.forecast.HourlyWeatherForecastFragment
import com.gmail.maystruks08.whatweathernow.ui.settings.SelectBackgroundFragment
import kotlinx.android.synthetic.main.activity_content.*


class MainActivity : AppCompatActivity() {

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        setSupportActionBar(main_toolbar)
        WeatherApplication.component.inject(this)
        replaceFragment(HourlyWeatherForecastFragment.newInstance())
        setupBackgroundImage()

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

//            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetwork = cm.activeNetworkInfo
//
//            if (activeNetwork != null && activeNetwork.isConnected) {
//                configToolbarOfflineMessage(false)
//
//                //get coordinate from SharedPreferences when Internet Active
//                val pref = getSharedPreferences("CURRENT_LOCATION", Context.MODE_PRIVATE)
//                val lat = pref?.getString("LOCATION_LATITUDE", "46.484579")
//                val lon = pref?.getString("LOCATION_LONGITUDE", "30.732597")
//
//                if (lat != null  && lon != null && lat != "-1" && lon != "-1") {
////                    mHourlyWeatherPresenter.getForecastByLatLng(lat, lon)
////                    mHourlyWeatherPresenter.getFiveDayForecastByLatLng(lat, lon)
//                }
//
//            } else {
//                configToolbarOfflineMessage(true)
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        main_toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_settings_24dp)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                navigateTo(CityFragment.newInstance())
                true
            }
            R.id.action_change_background -> {
                navigateTo(SelectBackgroundFragment.newInstance())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setBackground(@DrawableRes icon: Int) {
        mainBackground.background = ContextCompat.getDrawable(this, icon)
    }


    fun setNavigationIcon(@DrawableRes icon: Int) {
        main_toolbar.setNavigationIcon(icon)
        main_toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    fun removeNavigationIcon() {
        main_toolbar.navigationIcon = null
    }

    fun showOptionMenu(showMenu: Boolean) {
        if (menu == null) return
        this.menu?.setGroupVisible(R.id.main_menu_group, showMenu)
    }


    fun configToolbarOfflineMessage(isOffline: Boolean) {
        if (isOffline) {
            main_toolbar.title = null
            tvTitleToolbar.visibility = View.VISIBLE
            progressToolbar.visibility = View.VISIBLE
        } else {
            main_toolbar.title = getString(R.string.app_name)
            tvTitleToolbar.visibility = View.GONE
            progressToolbar.visibility = View.GONE
        }
    }

    fun configOverlay(overlay: Boolean) {
        viewFrameController.visibility = if (overlay) View.GONE else View.VISIBLE
    }

    fun setToolbarTitle(title: String) {
        main_toolbar.title = title
    }

    private fun setupBackgroundImage() {
        val idImageBackground = getSharedPreferences("BACKGROUND_IMAGE", Context.MODE_PRIVATE)
            ?.getString("BACKGROUND_IMAGE", R.drawable.default_back.toString())

        if (idImageBackground != null) {
            setBackground(idImageBackground.toInt())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .commit()
    }

    fun navigateBack() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
            return
        }
        super.onBackPressed()
    }

}
