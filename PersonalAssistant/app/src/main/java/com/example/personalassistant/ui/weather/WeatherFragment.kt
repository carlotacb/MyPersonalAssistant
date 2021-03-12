package com.example.personalassistant.ui.weather

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.personalassistant.R
import com.example.personalassistant.asyncCalls.CurrentWeatherAsyncTask
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject

lateinit var currentWeatherTask: CurrentWeatherAsyncTask

class WeatherFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_weather, container, false)

        /*val recyclerView: RecyclerView = root.findViewById(R.id.city_weather)
        recyclerView.adapter = CityAdapter(citiesWeather())
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)*/

        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)
        val city = sharedPreferences.getString("DefLocation", "Barcelona")
        val currentName: TextView = root.findViewById(R.id.city_name_card)
        currentName.text = city

        val currentWeather: ImageView = root.findViewById(R.id.card_weatherIcon)
        val currentPreasure: TextView = root.findViewById(R.id.card_preasure)
        val currentTemp: TextView = root.findViewById(R.id.card_temp)
        val currentHumidity: TextView = root.findViewById(R.id.card_humidity)
        val currentWind: TextView = root.findViewById(R.id.card_uvIndex)
        val currentFeels: TextView = root.findViewById(R.id.card_feels)
        val currentMaxMin: TextView = root.findViewById(R.id.card_max_min)


        currentWeatherTask = CurrentWeatherAsyncTask(
            currentPreasure,
            currentHumidity,
            currentWind,
            currentFeels,
            currentMaxMin,
            currentTemp,
            currentWeather,
            city
        )
        currentWeatherTask.execute()

        val button : FloatingActionButton = root.findViewById(R.id.addcity)
        button.setOnClickListener{
            val view = LayoutInflater.from(this.context).inflate(R.layout.dialog_add_city, null)
            val builder = AlertDialog.Builder(this.context)
            builder.setView(view)
            builder.setTitle("Add new city")
            builder.setPositiveButton("Add", DialogInterface.OnClickListener { dialog, id ->
                val newCity : TextView = view.findViewById(R.id.new_city_name)
                if (newCity.text.isEmpty()) {
                    Toast.makeText(this.context, "You must select a city", Toast.LENGTH_SHORT).show()
                }
                else {
                    dialog.dismiss()
                    var json = JSONObject()
                    json.put("cities", JSONArray().put(newCity.text.toString()))
                    Log.d("The new city", newCity.text.toString())
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
