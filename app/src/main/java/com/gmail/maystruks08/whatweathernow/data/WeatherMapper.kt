package com.gmail.maystruks08.whatweathernow.data

import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherCurrentData
import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
import com.gmail.maystruks08.whatweathernow.data.models.current.CurrentWeather
import com.gmail.maystruks08.whatweathernow.data.models.forecast.ForecastWeather
import javax.inject.Inject

class WeatherMapper @Inject constructor() {

    fun mapCurrentWeather(
        currentWeather: CurrentWeather
    ) = WeatherCurrentData(
        0L,
        currentWeather.main.temp.toString(),
        currentWeather.wind.speed,
        currentWeather.main.humidity.toString(),
        currentWeather.sys.sunrise,
        currentWeather.sys.sunset
    )

    fun mapForecastWeather(weather: ForecastWeather): List<WeatherForecastData> {
        val forecast = arrayListOf<WeatherForecastData>()
        val periodCount = 7

        for (position in 0..periodCount) {
            val w = WeatherForecastData(
                position,
                (weather.list[position].main.temp).toInt(),
                weather.list[position].main.humidity.toString().plus(" %"),
                weather.list[position].dt_txt.substring(11, 16),
                weather.list[position].weather[0].icon,
                weather.city.name,
                weather.city.coord.lat,
                weather.city.coord.lon

            )
            forecast.add(w)
        }

        return forecast
    }

}
