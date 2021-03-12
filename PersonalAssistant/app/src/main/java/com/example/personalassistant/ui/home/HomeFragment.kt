package com.example.personalassistant.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.personalassistant.R
import com.example.personalassistant.asyncCalls.CurrentWeatherAsyncTask
import com.example.personalassistant.getWeatherIcon
import com.example.personalassistant.ui.weather.currentWeatherTask
import java.util.*

class HomeFragment : Fragment() {

    enum class DayMoments(val moment: String) {
        MORNING("Good Morning"),
        AFTERNOON("Good Afternoon"),
        EVENING("Good Evening"),
        NIGHT("Good Night")
    }

    private var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private lateinit var title: TextView

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        title = root.findViewById(R.id.text_home)

        if (hour > 6 && hour <= 12) {
            title.text = DayMoments.MORNING.moment.plus(" ").plus("!!")
        } else if (hour > 12 && hour <= 18) {
            title.text = DayMoments.AFTERNOON.moment.plus(" ").plus("!!")
        } else if (hour > 18 && hour <= 20) {
            title.text = DayMoments.EVENING.moment.plus(" ").plus("!!")
        } else {
            title.text = DayMoments.NIGHT.moment.plus(" ").plus("!!")
        }

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

        /*val eventsPlanned: TextView = root.findViewById(R.id.events)
        eventsPlanned.text = events.toString().plus(" events planned for today")*/

        return root
    }

}
