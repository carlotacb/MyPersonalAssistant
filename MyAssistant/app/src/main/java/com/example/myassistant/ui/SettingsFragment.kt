package com.example.myassistant.ui

import android.content.Context
import android.content.SharedPreferences
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

        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

        // TODO: Get current location
        val currentLocation: TextView = root.findViewById(R.id.current_location)
        currentLocation.text = sharedPreferences.getString("Location", "")

        val buttonLocation: Button = root.findViewById(R.id.buttonLocation)
        buttonLocation.text = getString(R.string.button_change_location)

        val defaultLocation: TextView = root.findViewById(R.id.current_location)
        defaultLocation.text = sharedPreferences.getString("DefLocation", "")

        return root
    }
}
