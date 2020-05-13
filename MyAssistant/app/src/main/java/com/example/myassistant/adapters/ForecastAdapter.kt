package com.example.myassistant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R
import com.example.myassistant.getWeatherIcon

data class ForecastItem(val day: String, val weather: String, val max: String, val min: String)

class ForecastAdapter(private val forecastItems: List<ForecastItem>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(eventsView: View): RecyclerView.ViewHolder(eventsView) {
        val forecastDay : TextView = eventsView.findViewById(R.id.forecast_list_day)
        val forecastImage : ImageView = eventsView.findViewById(R.id.forecast_list_weatherIcon)
        val forecastMin : TextView = eventsView.findViewById(R.id.forecast_list_min)
        val forecastMax : TextView = eventsView.findViewById(R.id.forecast_list_max)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val weather = LayoutInflater.from(parent.context).inflate(R.layout.forecast_list_item, parent, false)
        return ForecastViewHolder(weather)
    }

    override fun getItemCount(): Int {
        return forecastItems.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentItem = forecastItems[position]

        holder.forecastDay.text = currentItem.day
        holder.forecastImage.setImageResource(getWeatherIcon(currentItem.weather))
        holder.forecastMin.text = "Min: ${currentItem.min}ºC"
        holder.forecastMax.text = "Max: ${currentItem.max}ºC"
    }
}