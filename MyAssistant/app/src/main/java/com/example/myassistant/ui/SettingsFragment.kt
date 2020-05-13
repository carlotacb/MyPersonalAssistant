package com.example.myassistant.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.myassistant.R

class SettingsFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        // TODO: Get current location
        val currentLocation: TextView = root.findViewById(R.id.selected_location)
        currentLocation.text = "Barcelona"

        val buttonLocation: Button = root.findViewById(R.id.buttonLocation)
        buttonLocation.text = "Change current location"
        return root
    }

}
