package com.gmail.maystruks08.whatweathernow.ui.base


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel


abstract class BasePresenterImpl<View : BaseView> : BasePresenter<View> {

    val presenterCoroutineScope = CoroutineScope(Dispatchers.IO)

    private var _view: View? = null
    override val view: View
        get() = _view ?: throw RuntimeException("Presenter view not initialised")

    override fun attach(view: View) {
        this._view = view
    }

    override fun detach() {
        this._view = null
        presenterCoroutineScope.coroutineContext.cancel()
    }

}
