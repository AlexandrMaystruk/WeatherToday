package com.gmail.maystruks08.whatweathernow.ui.forecast.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import kotlinx.android.synthetic.main.weather_item.view.*


class HourlyForecastRecyclerAdapter(
    var mForecast: List<WeatherForecastData>,
    val context: Context?
) :
    RecyclerView.Adapter<HourlyForecastRecyclerAdapter.MyViewHolder>() {

    private var min: Int = 0
    private var max: Int = 0

    init {
        //find diapason
        min = mForecast[0].temp
        max = mForecast[0].temp

        for (i in 0 until mForecast.size) {

            if (mForecast[i].temp < min) min = mForecast[i].temp
            if (mForecast[i].temp > max) max = mForecast[i].temp

        }
        min -= 273
        max -= 273

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.cwTvTemperature.text = (mForecast[position].temp - 273).toString().plus("°C")
        holder.cwTvTime.text = mForecast[position].dt_txt
        holder.cwTvHumidity.text = mForecast[position].humidity

        holder.cwValueTemperature.max = max - min
        holder.cwValueTemperature.progress = mForecast[position].temp - 273

        val iconUrl = "http://openweathermap.org/img/w/${mForecast[position].icon}.png"

        Glide
            .with(context!!)
            .load(Uri.parse(iconUrl))
            .into(holder.cwIvIcon)


    }

    override fun getItemCount(): Int {
        return mForecast.size
    }


    fun update(forecast: List<WeatherForecastData>) {
        this.mForecast = forecast
        notifyDataSetChanged()
    }


    inner class MyViewHolder(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {

        val cwTvTime: TextView = v.cwTvTime
        val cwTvHumidity: TextView = v.cwTvHumidity
        val cwTvTemperature: TextView = v.cwTvTemperature
        val cwValueTemperature: ProgressBar = v.cwValueTemperature
        val cwIvIcon: ImageView = v.cwIvIcon


    }

}