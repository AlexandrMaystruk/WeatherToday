package com.gmail.maystruks08.whatweathernow.ui.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseAdapter<T : androidx.recyclerview.widget.RecyclerView.ViewHolder> : androidx.recyclerview.widget.RecyclerView.Adapter<T>() {


    var mCompositeDisposable = CompositeDisposable()

    fun compositeDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onDetachedFromRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        mCompositeDisposable.dispose()
    }

    fun clearRx() {
        mCompositeDisposable.clear()
    }


}
