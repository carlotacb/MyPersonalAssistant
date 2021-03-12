package com.example.personalassistant.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalassistant.MainActivity

class HomeViewModel : ViewModel() {

    // private val sharedPreferences: SharedPreferences = MainActivity().getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

    private val _location = MutableLiveData<String>().apply {
        // value = sharedPreferences.getString("DefLocation", "")
        // val sharedPreferences: SharedPreferences = MainActivity().getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)
        value = "Paris"
    }

    val location: LiveData<String> = _location
}