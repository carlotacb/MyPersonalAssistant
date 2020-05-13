package com.example.myassistant

public fun getWeatherIcon(weather: String) : Int {
    if (weather == "sunny") {
        return R.drawable.ic_weather_sun
    } else if (weather == "cloudy") {
        return R.drawable.ic_weather_cloudy
    } else if (weather == "moon") {
        return R.drawable.ic_weather_moon
    } else if (weather == "moon-cloudy") {
        return R.drawable.ic_weather_mooncloudy
    } else if (weather == "snowy") {
        return R.drawable.ic_weather_snow
    } else if (weather == "storm") {
        return R.drawable.ic_weather_storm
    } else if (weather == "sunny-cloudy") {
        return R.drawable.ic_weather_suncloudy
    } else if (weather == "rainy") {
        return R.drawable.ic_weather_rain
    }

    return R.drawable.ic_weather_sun
}