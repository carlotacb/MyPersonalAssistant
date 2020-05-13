package com.example.myassistant.mockedData

import com.example.myassistant.adapters.EventItem

fun TodayEvents(): List<EventItem> {
    // TODO: Get elements from calendar
    val list = ArrayList<EventItem>()

    list += EventItem(
        "10:00 (1 hour)",
        "Daily Meeting"
    )
    list += EventItem(
        "12:00 (1 hour)",
        "Videollamada familiar"
    )
    list += EventItem(
        "18:00 (2 hours)",
        "Fitness Class"
    )

    return list
}

fun TomorrowEvents(): List<EventItem> {
    // TODO: Get elements from calendar
    val list = ArrayList<EventItem>()

    list += EventItem(
        "9:00 (1 hour)",
        "PickUp purchase"
    )
    list += EventItem(
        "10:00 (1 hour)",
        "Daily Meeting"
    )

    return list
}