package com.gmail.maystruks08.whatweathernow.presentation.presenter

import com.gmail.maystruks08.whatweathernow.dagger.city.CityContract
import com.gmail.maystruks08.whatweathernow.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CityPresenter @Inject constructor(private var router: Router?) :
    BasePresenter(), CityContract.Presenter {


    private var mView: CityContract.View? = null

    override fun attach(view: CityContract.View) {
        this.mView = view
    }

    override fun initUi() {
    }

    override fun detach() {
    }

}
