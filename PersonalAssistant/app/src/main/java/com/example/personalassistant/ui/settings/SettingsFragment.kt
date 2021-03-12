package com.example.personalassistant.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.example.personalassistant.R
import java.util.*

class SettingsFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

        val defaultLocation: TextView = root.findViewById(R.id.default_location)
        defaultLocation.text = sharedPreferences.getString("DefLocation", "Barcelona")

        val buttonLocation: Button = root.findViewById(R.id.buttonLocation)
        buttonLocation.text = getString(R.string.button_change_location)
        buttonLocation.setOnClickListener{
            val view = LayoutInflater.from(this.context).inflate(R.layout.dialog_add_city, null)
            val builder = AlertDialog.Builder(this.context)
            builder.setView(view)
            builder.setTitle("Change default location")
            builder.setPositiveButton("Change", DialogInterface.OnClickListener { dialog, id ->
                val newCity : TextView = view.findViewById(R.id.new_city_name)
                if (newCity.text.isEmpty()) {
                    Toast.makeText(this.context, "You must select a city", Toast.LENGTH_SHORT).show()
                }
                else {
                    defaultLocation.text = newCity.text.toString()
                    val geocoder = Geocoder(this.context, Locale.getDefault())
                    val loc = geocoder.getFromLocationName(newCity.text.toString(), 1)
                    val changeLat = loc.get(0).latitude
                    val changeLong = loc.get(0).longitude

                    val editorSharedPref: SharedPreferences.Editor = sharedPreferences.edit()
                    editorSharedPref.putString("DefLocation", newCity.text.toString())
                    editorSharedPref.putString("DefLatitude", changeLat.toString())
                    editorSharedPref.putString("DefLongitude", changeLong.toString())
                    editorSharedPref.apply()
                    Log.d("The new city", newCity.text.toString())
                }
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                Log.d("The new city", "Canceled")
            })
            builder.show()
        }

        val currentLocation: TextView = root.findViewById(R.id.current_location)
        currentLocation.text = sharedPreferences.getString("Location", "Barcelona")

        return root
    }
}
