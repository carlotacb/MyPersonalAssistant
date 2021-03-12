package com.example.personalassistant.models

class WeatherModel {
    var temp: Double
        get() = field
        set(value) {
            field = value
        }
    var sunrise: Int
        get() = field
        set(value) {
            field = value
        }
    var sunset: Int
        get() = field
        set(value) {
            field = value
        }
    var humidity: Int
        get() = field
        set(value) {
            field = value
        }
    var windSpeed: Double
        get() = field
        set(value) {
            field = value
        }
    var weather: String
        get() = field
        set(value) {
            field = value
        }

    var preassure: Int
        get() = field
        set(value) {
            field = value
        }

    var feeling: Double
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


    constructor(weather: String, temp: Double, sunrise: Int, sunset:Int, humidity: Int, preassure: Int, maxTemp: Double, minTemp: Double, feeling: Double, windSpeed: Double, description: String ){
        this.humidity = humidity
        this.temp = temp
        this.preassure = preassure
        this.feeling = feeling
        this.maxTemp = maxTemp
        this.minTemp = minTemp
        this.sunrise = sunrise
        this.sunset = sunset
        this.windSpeed = windSpeed
        this.weather = weather
        this.description = description
    }
}