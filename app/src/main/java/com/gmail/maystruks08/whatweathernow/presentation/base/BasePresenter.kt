package com.gmail.maystruks08.whatweathernow.presentation.base


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BasePresenter {

    val compositeDisposable = CompositeDisposable()

    fun compositeDiposable(disposable: Disposable?) {
        if (disposable != null) {
            compositeDisposable.add(disposable)
        }
    }

    protected fun compositeDisposableRemove(disposable: Disposable?) {
        if (disposable != null) {
            compositeDisposable.delete(disposable)
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    fun clearRx() {
        compositeDisposable.clear()
    }

}
