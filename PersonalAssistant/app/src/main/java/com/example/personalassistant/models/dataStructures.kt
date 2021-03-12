package com.example.personalassistant.models


data class CityItem(val city: String, val currentTemperature: Int, val weather: String, val list: List<DayTemperature>)

data class EventItem(val time: String, val name: String)

data class ForecastItem(val day: String, val weather: Int, val max: String, val min: String)

data class DayTemperature(val day: String, val min: Int, val max: Int)
