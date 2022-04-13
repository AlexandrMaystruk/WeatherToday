package com.gmail.maystruks08.whatweathernow.ui.base


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext


abstract class BasePresenterImpl<View : BaseView> : BasePresenter<View> {

    val presenterCoroutineScope = CoroutineScope(Dispatchers.Main)

    private var _view: View? = null
    override val view: View
        get() = _view ?: throw RuntimeException("Presenter view not initialised")

    override fun attach(view: View) {
        this._view = view
    }

    suspend fun updateUI(block: View.() -> Unit){
        withContext(Dispatchers.Main){
            block.invoke(view)
        }
    }

    override fun detach() {
        this._view = null
        presenterCoroutineScope.coroutineContext.cancel()
    }

}
