package com.gmail.maystruks08.whatweathernow.dagger.background

import android.content.Context
import com.gmail.maystruks08.whatweathernow.presentation.base.BaseView
import com.gmail.maystruks08.whatweathernow.presentation.presenter.BackgroundData

interface BackgroundContract {

    interface View : BaseView {

        fun showGridImage(listImage: ArrayList<BackgroundData>)
    }

    interface Presenter {
        fun attach(view: BackgroundContract.View)
        fun initUi(context: Context?)


        fun detach()
        fun backgroundSelected()
    }

}