package com.gmail.maystruks08.whatweathernow.dagger.city

import com.gmail.maystruks08.whatweathernow.presentation.base.BaseView

interface CityContract {

    interface View : BaseView {

        fun showGridImage(list: ArrayList<Int>)

    }

    interface Presenter {
        fun attach(view: CityContract.View)

        fun initUi()

        fun detach()
    }

}