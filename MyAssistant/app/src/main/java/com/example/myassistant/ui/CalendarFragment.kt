package com.example.myassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R
import com.example.myassistant.adapters.EventItem
import com.example.myassistant.adapters.EventsAdapter

class CalendarFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)

        val list = dataGenerated()

        val recyclerView: RecyclerView = root.findViewById(R.id.calendar_events)
        recyclerView.adapter =
            EventsAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        return root
    }

    private fun dataGenerated(): List<EventItem> {
        // TODO: Get elements from calendar
        val list = ArrayList<EventItem>()

        list += EventItem(
            "10:00 (1 hour)",
            "Daily Meeting"
        )
        list += EventItem(
            "12:00 (1 hour)",
            "Videollamada familiar"
        )
        list += EventItem(
            "18:00 (2 hours)",
            "Fitness Class"
        )

        return list
    }
}
