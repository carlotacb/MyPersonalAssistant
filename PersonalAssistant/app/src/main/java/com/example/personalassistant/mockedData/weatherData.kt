package com.example.personalassistant.mockedData

import com.example.personalassistant.models.CityItem
import com.example.personalassistant.models.DayTemperature
import com.example.personalassistant.models.ForecastItem


fun CurrentLocationForecastWeather(): List<DayTemperature> {
    val list = ArrayList<DayTemperature>()

    list += DayTemperature("Monday", 20, 25)
    list += DayTemperature("Tuesday",18,22)
    list += DayTemperature("Wednesday",19,23)
    list += DayTemperature("Thursday",22,24)

    return list
}

fun citiesWeather(): List<CityItem> {
    // TODO: Get information from selected cities

    val bcn = ArrayList<DayTemperature>()

    bcn += DayTemperature("Monday", 20, 25)
    bcn += DayTemperature("Tuesday", 18, 22)
    bcn += DayTemperature("Wednesday",19,23)
    bcn += DayTemperature("Thursday", 22, 24 )

    val ny = ArrayList<DayTemperature>()

    ny += DayTemperature("Monday", 15, 19)
    ny += DayTemperature("Tuesday", 18, 20)
    ny += DayTemperature("Wednesday",13,18)
    ny += DayTemperature("Thursday", 20, 22)

    val lnd = ArrayList<DayTemperature>()

    lnd += DayTemperature("Monday", 18, 22)
    lnd += DayTemperature("Tuesday", 15, 20)
    lnd += DayTemperature("Wednesday", 12, 18)
    lnd += DayTemperature("Thursday", 13, 17)

    val list = ArrayList<CityItem>()

    list += CityItem("Paris",22,"cloudy", bcn)
    list += CityItem( "New York", 25, "moon", ny)
    list += CityItem( "London", 15, "storm", lnd )

    return list
}

/*fun ForecastCurrentLocation() : List<ForecastItem> {

    val list = ArrayList<ForecastItem>()

    list += ForecastItem("18/05", R.drawable, "23", "20")
    list += ForecastItem("17/05", "storm", "17", "14")
    list += ForecastItem("16/05", "sunny-cloudy", "18", "13")
    list += ForecastItem("15/05", "sunny", "20", "15")

    return list

}*/