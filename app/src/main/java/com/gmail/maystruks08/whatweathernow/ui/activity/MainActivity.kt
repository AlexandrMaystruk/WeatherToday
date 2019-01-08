package com.gmail.maystruks08.whatweathernow.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.navigation.Screens

import com.gmail.maystruks08.whatweathernow.navigation.AppNavigator
import com.gmail.maystruks08.whatweathernow.ui.base.BaseRxActivity
import kotlinx.android.synthetic.main.activity_content.*

import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject


class MainActivity : BaseRxActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router


    internal var onBackHandler: Runnable? = null
    private val pressTwiceInterval: Long = 2000
    private var lastBackPressTime: Long = 0

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(main_toolbar)

        WeatherApplication.component.inject(this)
        router.newRootScreen(Screens.HourlyFragment)

        setupBackgroundImage()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        main_toolbar.overflowIcon = resources.getDrawable(R.drawable.ic_settings_24dp)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                router.navigateTo(Screens.MyCityFragment)
                true
            }
            R.id.action_change_background -> {
                router.navigateTo(Screens.BackgroundsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private val navigator: Navigator = object : AppNavigator(this, supportFragmentManager, R.id.main_content) {

        override fun setupFragmentTransaction(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction
        ) {
            fragmentTransaction.setReorderingAllowed(true)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }


    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }


    override fun onBackPressed() {
        this.hideSoftKeyboard()
        this.navigateBack()
    }

    private fun navigateBack() {
        when {
            onBackHandler != null -> onBackHandler!!.run()
            supportFragmentManager.backStackEntryCount > 0 -> router.exit()
            this.lastBackPressTime < System.currentTimeMillis() - pressTwiceInterval -> {
                Toast.makeText(this, R.string.toast_exit_app_warning_text, Toast.LENGTH_SHORT).show()
                this.lastBackPressTime = System.currentTimeMillis()
            }
            else -> router.exit()
        }
    }

    fun setBackground(icon: Int) {
        mainBackground.background = getDrawable(icon)
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
        if (overlay) {
            viewFrameController.visibility = View.GONE
        } else viewFrameController.visibility = View.VISIBLE

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

}
