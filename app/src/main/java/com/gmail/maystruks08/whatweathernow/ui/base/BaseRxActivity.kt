package com.gmail.maystruks08.whatweathernow.ui.base


import android.os.Bundle

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseRxActivity : BaseRootActivity() {
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCompositeDisposable = CompositeDisposable()

    }

    protected fun compositeDisposable(disposable: Disposable) {
        mCompositeDisposable!!.add(disposable)
    }

    protected fun compositeDisposable(vararg ds: Disposable) {
        mCompositeDisposable!!.addAll(*ds)
    }

    protected fun compositeDisposableRemove(disposable: Disposable?) {
        if (disposable != null) {
            mCompositeDisposable!!.delete(disposable)
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    override fun onDestroy() {
        if (isFinishing) {
            this.mCompositeDisposable!!.clear()
        }

        super.onDestroy()
    }
}

