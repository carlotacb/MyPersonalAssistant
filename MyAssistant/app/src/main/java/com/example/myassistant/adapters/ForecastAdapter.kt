package com.example.myassistant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R
import com.example.myassistant.getWeatherIcon
import com.example.myassistant.models.ForecastItem


class ForecastAdapter(private val forecastItems: List<ForecastItem>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(forecastView: View): RecyclerView.ViewHolder(forecastView) {
        val forecastDay : TextView = forecastView.findViewById(R.id.forecast_list_day)
        val forecastImage : ImageView = forecastView.findViewById(R.id.forecast_list_weatherIcon)
        val forecastMin : TextView = forecastView.findViewById(R.id.forecast_list_min)
        val forecastMax : TextView = forecastView.findViewById(R.id.forecast_list_max)
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