package com.example.myassistant.ui.weather

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
import com.example.myassistant.getWeatherIcon
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog_add_city.view.*
import java.io.Console

class WeatherFragment : Fragment() {

    // TODO: Get current location
    private var city = "Barcelona"
    // TODO: Get temperature from current location
    private var temperature = 25
    // TODO: Get current weather
    private var weather = "cloudy"

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_weather, container, false)

        val list = dataGenerated()
        val list2 = dataGenerated2()

        val recyclerView: RecyclerView = root.findViewById(R.id.city_weather)
        recyclerView.adapter = CityAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        val currentName: TextView = root.findViewById(R.id.city_name_current)
        currentName.text = city

        val currentTemperature: TextView = root.findViewById(R.id.current_city_temperature)
        currentTemperature.text = temperature.toString().plus("ºC")

        val currentWeather: ImageView = root.findViewById(R.id.current_weather_icon)
        currentWeather.setImageResource(getWeatherIcon(weather))

        val currentFutureTemps: RecyclerView = root.findViewById(R.id.future_temps)
        currentFutureTemps.adapter = ListWeatherAdapter(list2)
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

    private fun dataGenerated2(): List<DayTemperature> {
        val list = ArrayList<DayTemperature>()

        list += DayTemperature("Monday", 20,25)
        list += DayTemperature("Tuesday", 18,22)
        list += DayTemperature("Wednesday", 19,23)
        list += DayTemperature("Thursday", 22,24)

        return list
    }

    private fun dataGenerated(): List<CityItem> {
        // TODO: Get information from selected cities

        val bcn = ArrayList<DayTemperature>()

        bcn += DayTemperature("Monday", 20,25)
        bcn += DayTemperature("Tuesday", 18,22)
        bcn += DayTemperature("Wednesday", 19,23)
        bcn += DayTemperature("Thursday", 22,24)

        val ny = ArrayList<DayTemperature>()

        ny += DayTemperature("Monday", 15,19)
        ny += DayTemperature("Tuesday", 18,20)
        ny += DayTemperature("Wednesday", 13,18)
        ny += DayTemperature("Thursday", 20,22)

        val lnd = ArrayList<DayTemperature>()

        lnd += DayTemperature("Monday", 18,22)
        lnd += DayTemperature("Tuesday", 15,20)
        lnd += DayTemperature("Wednesday", 12,18)
        lnd += DayTemperature("Thursday", 13,17)

        val list = ArrayList<CityItem>()

        list += CityItem("Paris", 22, "cloudy", bcn)
        list += CityItem("New York", 25, "moon", ny)
        list += CityItem("London", 15, "storm", lnd)

        return list
    }
}
