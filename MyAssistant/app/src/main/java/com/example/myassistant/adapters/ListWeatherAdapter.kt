package com.example.myassistant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R
import com.example.myassistant.models.DayTemperature

class ListWeatherAdapter(private val dataSource: List<DayTemperature>) : RecyclerView.Adapter<ListWeatherAdapter.ListWeatherViewHolder>() {
    class ListWeatherViewHolder (temperatures: View): RecyclerView.ViewHolder(temperatures) {
        val dayName : TextView = temperatures.findViewById(R.id.day)
        val maxTemp : TextView = temperatures.findViewById(R.id.max_temp)
        val minTemp : TextView = temperatures.findViewById(R.id.min_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWeatherViewHolder {
        val cities = LayoutInflater.from(parent.context).inflate(R.layout.weather_list_items, parent, false)
        return ListWeatherViewHolder(
            cities
        )
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ListWeatherViewHolder, position: Int) {
        val currentItem = dataSource[position]

        holder.dayName.text = currentItem.day
        holder.maxTemp.text = currentItem.max.toString().plus("ºC")
        holder.minTemp.text = currentItem.min.toString().plus("ºC")
    }
}