package com.gmail.maystruks08.whatweathernow.ui.base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseRxFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()

    protected fun compositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun compositeDisposable(vararg ds: Disposable) {
        compositeDisposable.addAll(*ds)
    }

    protected fun compositeDisposableRemove(disposable: Disposable?) {
        if (disposable != null) {
            compositeDisposable.delete(disposable)
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}
