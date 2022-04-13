package com.gmail.maystruks08.whatweathernow.ui.forecast.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.ui.forecast.HourItemUi
import kotlinx.android.synthetic.main.weather_item.view.*
import kotlin.math.absoluteValue


class HourlyForecastRecyclerAdapter :
    RecyclerView.Adapter<HourlyForecastRecyclerAdapter.MyViewHolder>() {

    private var maxTemp = 0f
    private var minTemp = 0f
    private var progressShift = 0f

    private var forecastItems: List<HourItemUi> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun update(minTemp: Float, maxTemp: Float, items: List<HourItemUi>) {
        this.maxTemp = maxTemp
        this.minTemp = minTemp

        if (minTemp < 0) {
            this.maxTemp += minTemp.absoluteValue
            this.progressShift = minTemp.absoluteValue
        }

        this.forecastItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.weather_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(forecastItems[position])
    }

    override fun getItemCount() = forecastItems.size

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: HourItemUi) {
            val temperature = "${item.temperature}°C"
            view.apply {
                cwTvTime.text = item.time
                cwTvHumidity.text = item.humidity
                cwValueTemperature.max = maxTemp.toInt()
                cwValueTemperature.progress = (item.temperature + progressShift).toInt()
                cwTvTemperature.text = temperature

                Glide
                    .with(context)
                    .load(Uri.parse(item.weatherIconUrl))
                    .into(cwIvIcon)
            }
        }
    }
}
