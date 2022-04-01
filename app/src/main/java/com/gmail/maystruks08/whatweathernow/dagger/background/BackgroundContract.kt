package com.gmail.maystruks08.whatweathernow.dagger.background

import com.gmail.maystruks08.whatweathernow.ui.base.BaseView
import com.gmail.maystruks08.whatweathernow.ui.base.BasePresenter
import com.gmail.maystruks08.whatweathernow.ui.settings.adapter.BackgroundData

interface BackgroundContract {

    interface View : BaseView {
        fun showGridImage(listImage: List<BackgroundData>)
        fun navigateBack()
    }

    interface Presenter : BasePresenter<View> {
        fun initUi()
        fun backgroundSelected()
    }

}