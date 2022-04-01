package com.gmail.maystruks08.whatweathernow.ui.base

interface BasePresenter<BaseView> {

    val view: BaseView

    fun attach(view: BaseView)

    fun detach()

}