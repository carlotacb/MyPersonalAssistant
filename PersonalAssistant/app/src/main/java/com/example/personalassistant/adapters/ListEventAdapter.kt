package com.example.personalassistant.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personalassistant.R
import com.example.personalassistant.models.EventItem
import com.example.personalassistant.models.EventModel
import java.text.SimpleDateFormat

class ListEventAdapter(private val events: ArrayList<EventModel>) : RecyclerView.Adapter<ListEventAdapter.ListEventHolder>() {

    class ListEventHolder(eventsView: View): RecyclerView.ViewHolder(eventsView) {
        val eventTime : TextView = eventsView.findViewById(R.id.dialog_event_time)
        val eventName : TextView = eventsView.findViewById(R.id.dialog_event_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEventHolder {
        val weather = LayoutInflater.from(parent.context).inflate(R.layout.event_card2, parent, false)
        return ListEventHolder(weather)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: ListEventHolder, position: Int) {
        val currentItem = events[position]

        val formatter = SimpleDateFormat("hh:mm")

        holder.eventTime.text = formatter.format(currentItem.start)
        holder.eventName.text = currentItem.name
    }
}