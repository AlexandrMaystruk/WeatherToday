package com.gmail.maystruks08.whatweathernow.ui.forecast.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gmail.maystruks08.whatweathernow.R
import com.gmail.maystruks08.whatweathernow.ui.forecast.DayWeatherItemUi
import com.gmail.maystruks08.whatweathernow.ui.forecast.HourWeatherItemUi
import com.gmail.maystruks08.whatweathernow.ui.forecast.WeatherItemUi
import kotlinx.android.synthetic.main.day_weather_item.view.*
import kotlinx.android.synthetic.main.weather_item.view.*
import kotlin.math.absoluteValue


class HourlyForecastRecyclerAdapter :
    RecyclerView.Adapter<HourlyForecastRecyclerAdapter.BaseItemViewHolder<WeatherItemUi>>() {

    private var maxTemp = 0f
    private var minTemp = 0f
    private var progressShift = 0f

    private var forecastItems: List<WeatherItemUi> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun update(minTemp: Float, maxTemp: Float, items: List<WeatherItemUi>) {
        this.maxTemp = maxTemp
        this.minTemp = minTemp

        if (minTemp < 0) {
            this.maxTemp += minTemp.absoluteValue
            this.progressShift = minTemp.absoluteValue
        }

        this.forecastItems = items
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseItemViewHolder<WeatherItemUi> {
        return when (viewType) {
            0 -> HourlyItemViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.weather_item, parent, false)
            ) as BaseItemViewHolder<WeatherItemUi>
            1 -> DailyViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.day_weather_item, parent, false)
            ) as BaseItemViewHolder<WeatherItemUi>
            else -> throw RuntimeException("Incorrect view type")
        }
    }

    override fun onBindViewHolder(holder: BaseItemViewHolder<WeatherItemUi>, position: Int) {
        holder.bind(forecastItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (forecastItems[position]) {
            is HourWeatherItemUi -> 0
            else -> 1
        }
    }

    override fun getItemCount() = forecastItems.size


    abstract class BaseItemViewHolder<in UIItem : WeatherItemUi>(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        abstract fun bind(item: UIItem)

    }

    inner class HourlyItemViewHolder(view: View) : BaseItemViewHolder<HourWeatherItemUi>(view) {

        override fun bind(item: HourWeatherItemUi) {
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

    inner class DailyViewHolder(view: View) : BaseItemViewHolder<DayWeatherItemUi>(view) {

        override fun bind(item: DayWeatherItemUi) {
            val minTemp = "min ${item.tempMin}"
            val maxTemp = "max ${item.tempMax}"
            view.apply {
                tvDay.text = item.dateTime
                tvHumidity.text = item.humidity
                tvPressure.text = item.pressure
                tvTemperatureMin.text = minTemp
                tvTemperatureMax.text = maxTemp
                tvRain.text = item.rain
                tvUltraviolet.text = item.ultraviolet

                Glide
                    .with(context)
                    .load(Uri.parse(item.icon))
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ) = true

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            ivWeatherIcon.setImageDrawable(resource)
                            return true
                        }
                    }).submit()

            }
        }
    }
}
