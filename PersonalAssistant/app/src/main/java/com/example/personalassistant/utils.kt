package com.example.personalassistant

import android.util.Log
import com.example.personalassistant.keys.WEATHER_KEY
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


fun getWeatherIcon(weather: String) : Int {
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

/*
fun getCurrentWeatherByCity(city: String): JSONObject {
    val client = OkHttpClient()
    val request: Request = Request.Builder().url("api.openweathermap.org/data/2.5/weather?q=$city&appid=$WEATHER_KEY&units=metric").build()
    var response: Response? = null

    try {
        response = client.newCall(request).execute()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val responseData = response?.body.toString()

    Log.d("RESPONSE", responseData)

    return JSONObject(responseData)
}*/