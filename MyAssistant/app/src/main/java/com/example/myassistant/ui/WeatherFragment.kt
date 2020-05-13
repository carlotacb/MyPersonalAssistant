package com.example.myassistant.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R
import com.example.myassistant.adapters.CityAdapter
import com.example.myassistant.adapters.ListWeatherAdapter
import com.example.myassistant.getWeatherIcon
import com.example.myassistant.mockedData.CurrentLocationForecastWeather
import com.example.myassistant.mockedData.citiesWeather
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog_add_city.view.*

class WeatherFragment : Fragment() {

    // TODO: Get current location
    private var city = "Barcelona"
    // TODO: Get temperature from current location
    private var temperature = 25
    // TODO: Get current weather
    private var weather = "cloudy"

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_weather, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.city_weather)
        recyclerView.adapter = CityAdapter(citiesWeather())
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        val currentName: TextView = root.findViewById(R.id.city_name_current)
        currentName.text = city

        val currentTemperature: TextView = root.findViewById(R.id.current_city_temperature)
        currentTemperature.text = temperature.toString().plus("ºC")

        val currentWeather: ImageView = root.findViewById(R.id.current_weather_icon)
        currentWeather.setImageResource(getWeatherIcon(weather))

        val currentFutureTemps: RecyclerView = root.findViewById(R.id.future_temps)
        currentFutureTemps.adapter = ListWeatherAdapter(CurrentLocationForecastWeather())
        currentFutureTemps.layoutManager = LinearLayoutManager(activity)
        currentFutureTemps.setHasFixedSize(true)

        val button : FloatingActionButton = root.findViewById(R.id.addcity)
        button.setOnClickListener{
            val view = LayoutInflater.from(this.context).inflate(R.layout.dialog_add_city, null)
            val builder = AlertDialog.Builder(this.context)
            builder.setView(view)
            builder.setTitle("Add new city")
            builder.setPositiveButton("Add", DialogInterface.OnClickListener { dialog, id ->
                val newCity = view.new_city_name.text.toString()
                if (newCity.isEmpty()) {
                    Toast.makeText(this.context, "You must select a city", Toast.LENGTH_SHORT).show()
                }
                else {
                    dialog.dismiss()
                    // TODO: Add city to list (preferences)
                    Log.d("The new city", newCity)
                }
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                Log.d("The new city", "Canceled")
            })
            builder.show()
        }

        return root
    }
}
