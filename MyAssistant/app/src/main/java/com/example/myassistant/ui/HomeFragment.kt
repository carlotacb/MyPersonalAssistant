package com.example.myassistant.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myassistant.R
import com.example.myassistant.getWeatherIcon
import java.util.*

class HomeFragment : Fragment() {

    enum class DayMoments(val moment: String) {
        MORNING("Good Morning"),
        AFTERNOON("Good Afternoon"),
        EVENING("Good Evening"),
        NIGHT("Good Night")
    }

    private var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    // TODO: Get temperature from current location
    private var temperature = 25
    // TODO: Get current weather
    private var weather = "sunny"
    // TODO: Get number events planned for today
    private var events = 2
    // TODO: Get user (?)
    private var user = "Carlota"

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val title: TextView = root.findViewById(R.id.text_home)
        if (hour > 6 && hour <= 12) {
            title.text = DayMoments.MORNING.moment.plus(" ").plus(user).plus("!!")
        } else if (hour > 12 && hour <= 18) {
            title.text = DayMoments.AFTERNOON.moment.plus(" ").plus(user).plus("!!")
        } else if (hour > 18 && hour <= 20) {
            title.text = DayMoments.EVENING.moment.plus(" ").plus(user).plus("!!")
        } else {
            title.text = DayMoments.NIGHT.moment.plus(" ").plus(user).plus("!!")
        }

        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

        val currentCity: TextView = root.findViewById(R.id.default_location)
        currentCity.text = sharedPreferences.getString("Location", "Madrid")

        val currentTemperature: TextView = root.findViewById(R.id.current_temperature)
        currentTemperature.text = temperature.toString().plus("ºC")

        val weatherIcon: ImageView = root.findViewById(R.id.weather_icon)
        weatherIcon.setImageResource(getWeatherIcon(weather))

        val eventsPlanned: TextView = root.findViewById(R.id.events)
        eventsPlanned.text = events.toString().plus(" events planned for today")

        return root
    }
}
