//package com.gmail.maystruks08.whatweathernow.data
//
//import com.gmail.maystruks08.whatweathernow.data.JsonUtil.fromJson
//import com.gmail.maystruks08.whatweathernow.data.JsonUtil.toJson
//import com.gmail.maystruks08.whatweathernow.data.database.entity.CurrentWeatherTable
//import com.gmail.maystruks08.whatweathernow.data.database.entity.DailyForecast7DaysTable
//import com.gmail.maystruks08.whatweathernow.data.database.entity.WeatherForecastData
//import com.gmail.maystruks08.whatweathernow.data.dto.current.CurrentWeatherData
//import com.gmail.maystruks08.whatweathernow.data.dto.daily7days.DailyForecast7Days
//import com.gmail.maystruks08.whatweathernow.data.dto.hourly5days.HourlyForecast5Days
//import javax.inject.Inject
//
//class WeatherMapper @Inject constructor() {
//
//    fun mapCurrentWeather(
//        currentWeatherData: CurrentWeatherData
//    ) = CurrentWeatherTable(
//        0L,
//        currentWeatherData.main.temp.toString(),
//        currentWeatherData.wind.speed,
//        currentWeatherData.main.humidity.toString(),
//        currentWeatherData.sys.sunrise,
//        currentWeatherData.sys.sunset
//    )
//
//    fun mapForecast7DaysWeather(weather: HourlyForecast5Days): List<WeatherForecastData> {
//        val forecast = arrayListOf<WeatherForecastData>()
//        val periodCount = 7
//
//        for (position in 0..periodCount) {
//            val w = WeatherForecastData(
//                position,
//                (weather.list[position].main.temp).toInt(),
//                weather.list[position].main.humidity.toString().plus(" %"),
//                weather.list[position].dt_txt.substring(11, 16),
//                weather.list[position].weather[0].icon,
//                weather.city.name,
//                weather.city.coord.lat,
//                weather.city.coord.lon
//            )
//            forecast.add(w)
//        }
//
//        return forecast
//    }
//
//}
