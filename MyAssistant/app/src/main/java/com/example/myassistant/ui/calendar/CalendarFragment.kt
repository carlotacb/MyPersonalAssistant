package com.example.myassistant.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)

        val list = dataGenerated()

        val recyclerView: RecyclerView = root.findViewById(R.id.calendar_events)
        recyclerView.adapter = EventsAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        val eventButton: Button = root.findViewById(R.id.button)
        calendarViewModel.text.observe(viewLifecycleOwner, Observer {
            eventButton.text = it
        })
        return root
    }

    private fun dataGenerated(): List<EventItem> {
        val list = ArrayList<EventItem>()

        list += EventItem("10:00 - 11:00", "Daily Meeting")
        list += EventItem("18:00 - 20:00", "Fitness Class")

        return list
    }
}
