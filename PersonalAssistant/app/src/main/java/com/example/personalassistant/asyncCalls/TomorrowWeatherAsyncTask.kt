package com.example.personalassistant.asyncCalls

import android.os.AsyncTask
import android.widget.ImageView
import android.widget.TextView
import com.example.personalassistant.R
import com.example.personalassistant.services.OpenWeatherService
import com.example.personalassistant.models.WeatherModel

class TomorrowWeatherAsyncTask(
    private var preassureText: TextView,
    private var humidityText: TextView,
    private var windText: TextView,
    private var feelsTemperatureText: TextView,
    private var maxminText: TextView,
    private var tempratureText: TextView,
    private var currentWeatherMainImage: ImageView,
    private var lat: String?,
    private var long: String?) : AsyncTask<Void, WeatherModel, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherService()
        val data: WeatherModel = service.getTomorrowByLocation(this.lat!!, this.long!!)
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: WeatherModel) {

        tempratureText.text = ""
        humidityText.text = "Humidity: " + params.get(0).humidity.toString() + " %"
        preassureText.text = "Preasure: " + params.get(0).preassure.toString() + " mb"
        windText.text = "Wind Speed: " + params.get(0).windSpeed.toString() + " km/h"
        feelsTemperatureText.text = "Feels like: " + params.get(0).feeling.toInt().toString() + "ºC"
        maxminText.text = "Min: " + params.get(0).maxTemp.toInt().toString() + "ºC" + " / Max: " + params.get(0).minTemp.toInt().toString() + "ºC"

        if (params.get(0).weather == "Clear") {
            currentWeatherMainImage.setImageResource(R.drawable.ic_weather_sun)
        } else if (params.get(0).weather == "Clouds") {
            if (params.get(0).description == "few clouds") {
                currentWeatherMainImage.setImageResource(R.drawable.ic_weather_suncloudy)
            } else currentWeatherMainImage.setImageResource(R.drawable.ic_weather_cloudy)
        } else if (params.get(0).weather == "Rain" || params.get(0).weather == "Drizzle") {
            currentWeatherMainImage.setImageResource(R.drawable.ic_weather_rain)
        } else if (params.get(0).weather == "Thunderstorm") {
            currentWeatherMainImage.setImageResource(R.drawable.ic_weather_storm)
        } else if (params.get(0).weather == "Snow") {
            currentWeatherMainImage.setImageResource(R.drawable.ic_weather_snow)
        } else {
            currentWeatherMainImage.setImageResource(R.drawable.ic_foggy)
        }
    }
}