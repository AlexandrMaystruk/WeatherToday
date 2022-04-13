package com.gmail.maystruks08.whatweathernow.ui.editlocation

import android.os.Bundle
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_city.*
import javax.inject.Inject


class CityFragment : BaseFragment(), CityContract.View {

    @Inject
    lateinit var cityPresenter: CityPresenterImpl

    override fun provideLayoutRes(): Int {
        return R.layout.fragment_city
    }

    override fun initPresenter() {
        WeatherApplication.component.provideCityComponent().inject(this)
        cityPresenter.attach(this)
    }

    override fun initViews() {
        activity()?.showOptionMenu(false)
        activity()?.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        cityPresenter.initUi()

        switchGetByGeolocation.setOnCheckedChangeListener { _, isChecked ->
            cityPresenter.onUseGeoLocationSettingsChanged(isChecked)
        }

    }

    override fun showGridImage(list: ArrayList<Int>) {

    }

    override fun setSwitchState(shouldUseGeoLocation: Boolean) {
        switchGetByGeolocation.isChecked = shouldUseGeoLocation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cityPresenter.detach()
    }

    companion object {
        fun newInstance(): CityFragment {
            val fragment = CityFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
