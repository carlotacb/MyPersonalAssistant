package com.example.myassistant.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R
import com.example.myassistant.getWeatherIcon
import com.example.myassistant.models.CityItem

class CityAdapter(private val cityList: List<CityItem>) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(eventsView: View): RecyclerView.ViewHolder(eventsView) {
        val cityName : TextView = eventsView.findViewById(R.id.city_name_list)
        val cityTemp : TextView = eventsView.findViewById(R.id.city_temperature_list)
        val cityWeather: ImageView = eventsView.findViewById(R.id.city_weather_icon)
        val futureTemps: RecyclerView = eventsView.findViewById(R.id.future_temps_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val cities = LayoutInflater.from(parent.context).inflate(R.layout.weather_card, parent, false)
        return CityViewHolder(cities)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentItem = cityList[position]

        holder.cityName.text = currentItem.city
        holder.cityTemp.text = currentItem.currentTemperature.toString().plus("ºC")

        holder.cityWeather.setImageResource(getWeatherIcon(currentItem.weather))
        holder.futureTemps.adapter =
            ListWeatherAdapter(currentItem.list)
        holder.futureTemps.layoutManager = LinearLayoutManager(holder.futureTemps.context)
        holder.futureTemps.setHasFixedSize(true)
    }
}