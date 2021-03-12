package com.example.personalassistant.services

import android.util.Log
import com.example.personalassistant.models.ForecastModel
import com.example.personalassistant.models.WeatherModel

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OpenWeatherService {

    private val APPID: String = "21b84c986d1646cd32ed2d2740b74cff"

    fun getCurrentWeatherByCityName(city: String): WeatherModel {

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&APPID=$APPID")
            .build()
        var response: Response? = null

        try {
            response = client.newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val responseJSON = response!!.body?.string()
        Log.d("JSON", responseJSON)
        val responseObject = JSONObject(responseJSON)

        //parse Weather Data & create WeatherModel Object
        val weather: String =
            responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
        val temp: Double = responseObject.getJSONObject("main").getDouble("temp")
        val sunrise = responseObject.getJSONObject("sys").getInt("sunrise")
        val sunset = responseObject.getJSONObject("sys").getInt("sunset")
        val humidity = responseObject.getJSONObject("main").getInt("humidity")
        val preassure = responseObject.getJSONObject("main").getInt("pressure")
        val minTemp = responseObject.getJSONObject("main").getDouble("temp_min")
        val maxTemp = responseObject.getJSONObject("main").getDouble("temp_max")
        val feelsLike = responseObject.getJSONObject("main").getDouble("feels_like")
        val windSpeed = responseObject.getJSONObject("wind").getDouble("speed")
        val description: String = responseObject.getJSONArray("weather").getJSONObject(0).getString("description")

        return WeatherModel(
            weather,
            temp,
            sunrise,
            sunset,
            humidity,
            preassure,
            minTemp,
            maxTemp,
            feelsLike,
            windSpeed,
            description
        )
    }

    fun getTomorrowByLocation(lat: String, long: String): WeatherModel {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&exclude=hourly,minutely&units=metric&appid=$APPID")
            .build()
        var response: Response? = null

        try {
            response = client.newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val responseJSON = response?.body?.string()
        val responseObject = JSONObject(responseJSON)

        val tomorrowWeather: JSONObject = responseObject.getJSONArray("daily").getJSONObject(1)

        val weather: String = tomorrowWeather.getJSONArray("weather").getJSONObject(0).getString("main")
        val humidity = tomorrowWeather.getInt("humidity")
        val preassure = tomorrowWeather.getInt("pressure")
        val minTemp = tomorrowWeather.getJSONObject("temp").getDouble("min")
        val maxTemp = tomorrowWeather.getJSONObject("temp").getDouble("max")
        val feelsLike = tomorrowWeather.getJSONObject("feels_like").getDouble("day")
        val windSpeed = tomorrowWeather.getDouble("wind_speed")
        val description: String = tomorrowWeather.getJSONArray("weather").getJSONObject(0).getString("description")

        return WeatherModel(
            weather,
            0.0,
            0,
            0,
            humidity,
            preassure,
            minTemp,
            maxTemp,
            feelsLike,
            windSpeed,
            description
        )
    }

    fun getForecastByLocation(lat: String, long: String): ArrayList<ForecastModel> {

        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&exclude=hourly,minutely&units=metric&appid=$APPID")
            .build()
        var response: Response? = null

        try {
            response = client.newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val responseJSON = response?.body?.string()
        val responseObject = JSONObject(responseJSON)

        var responseArray = ArrayList<ForecastModel>()

        val daysArray: JSONArray = responseObject.getJSONArray("daily")
        for(i in 0 until daysArray.length()){
            var dayWeather = daysArray.getJSONObject(i)

            var weather: String = dayWeather.getJSONArray("weather").getJSONObject(0).getString("main")
            var description: String = dayWeather.getJSONArray("weather").getJSONObject(0).getString("description")
            var date = dayWeather.getInt("dt")
            var minTemp = dayWeather.getJSONObject("temp").getDouble("min")
            var maxTemp = dayWeather.getJSONObject("temp").getDouble("max")

            responseArray.add(ForecastModel(weather, minTemp, maxTemp, description, date))
        }

        Log.d("RESPONSE FORECAST", responseArray.toString())
        return responseArray
    }


}