package com.gmail.maystruks08.whatweathernow.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.maystruks08.whatweathernow.ui.activity.MainActivity


abstract class BaseFragment : Fragment(), BaseView {

    lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(provideLayoutRes(), container, false)
        initPresenter()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    protected fun activity(): MainActivity? {
        return activity as? MainActivity
    }

    abstract fun initPresenter()

    abstract fun initViews()

    protected abstract fun provideLayoutRes(): Int

}