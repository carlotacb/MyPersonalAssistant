package com.example.personalassistant.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistant.R
import com.example.personalassistant.adapters.EventsAdapter
import com.example.personalassistant.mockedData.TodayEvents

class CalendarFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)

        val list = TodayEvents()

        val recyclerView: RecyclerView = root.findViewById(R.id.calendar_events)
        recyclerView.adapter = EventsAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        return root
    }
}
