package com.gmail.maystruks08.whatweathernow.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.presentation.presenter.BackgroundData
import com.gmail.maystruks08.whatweathernow.ui.base.BaseAdapter
import kotlinx.android.synthetic.main.background_item.view.*
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject


class BackgroundRecyclerAdapter(private var listId: ArrayList<BackgroundData>) : BaseAdapter<BackgroundRecyclerAdapter.MyViewHolder>() {

    private val mClickSubject = PublishSubject.create<Int>()

    fun subscribeItemClick(onNext: (Int) -> Unit): Disposable {
        return mClickSubject.toFlowable(BackpressureStrategy.LATEST).subscribe(onNext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.background_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.ivBack.setImageDrawable(listId[position].drawableSmallRes)
        compositeDisposable(RxView.clicks(holder.itemView)
            .subscribe {
                mClickSubject.onNext(listId[position].idHDRes)
            })

    }


    override fun getItemCount(): Int {
        return listId.size
    }


    inner class MyViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
        val ivBack: ImageView = v.ivBackgroundItem
    }

}