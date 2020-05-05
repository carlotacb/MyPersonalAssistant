package com.example.myassistant.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myassistant.R

data class EventItem(val time: String, val name: String)

class EventsAdapter(private val eventsList: List<EventItem>) : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    class EventsViewHolder(eventsView: View): RecyclerView.ViewHolder(eventsView) {
        val timeEvent : TextView = eventsView.findViewById(R.id.event_time)
        val nameEvent : TextView = eventsView.findViewById(R.id.event_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val events = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return EventsViewHolder(events)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val currentItem = eventsList[position]

        holder.nameEvent.text = currentItem.name
        holder.timeEvent.text = currentItem.time
    }
}