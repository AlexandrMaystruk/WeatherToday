package com.gmail.maystruks08.whatweathernow.ui.base

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.maystruks08.whatweathernow.presentation.base.BaseView
import com.gmail.maystruks08.whatweathernow.ui.activity.MainActivity


abstract class BaseFragment : BaseRxFragment(), BaseView {

    private val viewHandler = Handler()

    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
         mView = inflater.inflate(provideLayoutRes(), container, false)

        initPresenter()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        activity()?.onBackHandler = provideBackHandler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewHandler.removeCallbacksAndMessages(null)
    }

    protected fun activity(): MainActivity? {
        return if (activity is MainActivity) activity as MainActivity? else null
    }

    abstract fun initPresenter()

    abstract fun initViews()

    protected abstract fun provideLayoutRes(): Int


    protected fun provideBackHandler(): Runnable? {
        return null
    }

    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post(action)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(error: String) {

    }
}