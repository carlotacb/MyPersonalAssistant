package com.example.personalassistant.models

import java.util.*

class EventModel {
    var name: String
        get() = field
        set(value) {
            field = value
        }

    var start: Long
        get() = field
        set(value) {
            field = value
        }

    var end: Long
        get() = field
        set(value) {
            field = value
        }


    constructor(name: String, start: Long, end: Long){
        this.name = name
        this.start = start
        this.end = end
    }
}