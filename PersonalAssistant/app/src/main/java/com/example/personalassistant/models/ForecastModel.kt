package com.example.personalassistant.models

class ForecastModel {

    var weather: String
        get() = field
        set(value) {
            field = value
        }

    var maxTemp: Double
        get() = field
        set(value) {
            field = value
        }

    var minTemp: Double
        get() = field
        set(value) {
            field = value
        }

    var description: String
        get() = field
        set(value) {
            field = value
        }

    var date: Int
        get() = field
        set(value) {
            field = value
        }

    constructor(weather: String, maxTemp: Double, minTemp: Double, description: String, date: Int ){
        this.maxTemp = maxTemp
        this.minTemp = minTemp
        this.weather = weather
        this.description = description
        this.date = date
    }
}