package com.gmail.maystruks08.whatweathernow.ui.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.maystruks08.whatweathernow.R
import kotlinx.android.synthetic.main.background_item.view.*


class BackgroundRecyclerAdapter(
    private var listId: List<BackgroundData>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<BackgroundRecyclerAdapter.BackgroundCarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundCarViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.background_item, parent, false)
        return BackgroundCarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BackgroundCarViewHolder, position: Int) {
        holder.ivBack.setImageDrawable(listId[position].drawableSmallRes)
        holder.itemView.setOnClickListener {
            onItemClicked.invoke(listId[position].idHDRes)
        }
    }

    override fun getItemCount() = listId.size

    inner class BackgroundCarViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivBack: ImageView = v.ivBackgroundItem
    }

}