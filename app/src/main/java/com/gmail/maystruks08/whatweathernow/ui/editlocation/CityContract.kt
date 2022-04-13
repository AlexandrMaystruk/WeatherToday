package com.gmail.maystruks08.whatweathernow.ui.editlocation

import com.gmail.maystruks08.whatweathernow.ui.base.BaseView

interface CityContract {

    interface View : BaseView {

        fun showGridImage(list: ArrayList<Int>)

        fun setSwitchState(shouldUseGeoLocation: Boolean)

    }

    interface Presenter {
        fun initUi()
        fun onUseGeoLocationSettingsChanged(shouldUseGeoLocation: Boolean)
    }

}