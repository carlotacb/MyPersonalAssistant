package com.example.personalassistant.asyncCalls

import android.os.AsyncTask
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistant.R
import com.example.personalassistant.adapters.ForecastAdapter
import com.example.personalassistant.services.OpenWeatherService
import com.example.personalassistant.models.ForecastItem
import com.example.personalassistant.models.ForecastModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastAsyncTask(private var forecastList: RecyclerView, private var long: String?, private var lat: String?) : AsyncTask<Void, ArrayList<ForecastModel>, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherService()
        val data: ArrayList<ForecastModel> = service.getForecastByLocation(lat!!, long!!)
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: ArrayList<ForecastModel>) {

        val list = ArrayList<ForecastItem>()

        var data = params[0]

        for(i in 1 until data.size) {
            val day = Date(data[i].date.toLong() * 1000)
            val formatedDate = SimpleDateFormat("dd/MM")

            list += ForecastItem(formatedDate.format(day), getIcon(data[i].weather, data[i].description), data[i].maxTemp.toInt().toString(), data[i].minTemp.toInt().toString())
        }

        Log.d("THIS IS THE LIST", list.toString())
        forecastList.adapter = ForecastAdapter(list)
    }

    private fun getIcon(weather: String, description: String): Int {
        if (weather == "Clear") {
            return R.drawable.ic_weather_sun
        } else if (weather == "Clouds") {
            if (description == "few clouds") {
                return R.drawable.ic_weather_suncloudy
            } else return R.drawable.ic_weather_cloudy
        } else if (weather == "Rain" || weather == "Drizzle") {
            return R.drawable.ic_weather_rain
        } else if (weather == "Thunderstorm") {
            return R.drawable.ic_weather_storm
        } else if (weather == "Snow") {
            return R.drawable.ic_weather_snow
        } else {
            return R.drawable.ic_foggy
        }
    }

}