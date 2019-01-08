package com.gmail.maystruks08.whatweathernow.ui.fragments

import android.os.Bundle
import android.widget.Toast
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.WeatherApplication
import com.gmail.maystruks08.whatweathernow.dagger.city.CityContract
import com.gmail.maystruks08.whatweathernow.presentation.presenter.CityPresenter
import com.gmail.maystruks08.whatweathernow.ui.base.BaseFragment
import javax.inject.Inject


class CityFragment : BaseFragment(), CityContract.View {

    @Inject
    lateinit var cityPresenter: CityPresenter

    override fun provideLayoutRes(): Int {
        return R.layout.fragment_city
    }

    companion object {
        fun newInstance(): CityFragment {
            val fragment = CityFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun initPresenter() {
        WeatherApplication.component.provideCityComponent().inject(this)
        cityPresenter.attach(this)
    }

    override fun initViews() {
        activity()?.showOptionMenu(false)
        activity()?.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        cityPresenter.initUi()

    }

    override fun showGridImage(list: ArrayList<Int>) {

    }


    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        super.onDestroy()


    }
}
