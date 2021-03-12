package com.example.personalassistant.services

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.example.personalassistant.models.EventModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarHelper(private var context: Context) {

    val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.BEGIN,
        CalendarContract.Instances.END,
        CalendarContract.Instances.TITLE
    )

    private val PROJECTION_BEGIN_INDEX: Int = 0
    private val PROJECTION_END_INDEX: Int = 1
    private val PROJECTION_TITLE_INDEX: Int = 2

    fun addEvent() {
        val intent = Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
        context.startActivity(intent)
    }

    fun getTodayEvents(): ArrayList<EventModel> {
        var result = ArrayList<EventModel>()
        var startCalendar = Calendar.getInstance()
        startCalendar.time = Date()
        startCalendar.add(Calendar.DATE,0)

        val startMillis: Long = Calendar.getInstance().run {
            set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE))
            timeInMillis
        }

        var endCalendar = Calendar.getInstance()
        endCalendar.time = Date()
        endCalendar.add(Calendar.DATE,0)
        val endMillis: Long = Calendar.getInstance().run {
            set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 23, 59)
            timeInMillis
        }

        // Construct the query with the desired date range.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        val cur: Cursor = context.contentResolver.query(
            builder.build(),
            EVENT_PROJECTION,
            null,null, CalendarContract.Instances.BEGIN
        )!!

        while (cur.moveToNext()) {
            // Get the field values
            val beginVal: Long = cur.getLong(PROJECTION_BEGIN_INDEX)
            val endVal: Long = cur.getLong(PROJECTION_END_INDEX)
            val title: String = cur.getString(PROJECTION_TITLE_INDEX)

            var event = EventModel(title,beginVal,endVal)
            result.add(event)
        }

        return result
    }

    fun getTomorrowEvents(): ArrayList<EventModel> {
        var result = ArrayList<EventModel>()

        var startCalendar = Calendar.getInstance()
        startCalendar.time = Date()
        startCalendar.add(Calendar.DATE,1)
        val startMillis: Long = Calendar.getInstance().run {
            set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE))
            timeInMillis
        }

        var endCalendar = Calendar.getInstance()
        endCalendar.time = Date()
        endCalendar.add(Calendar.DATE,1)
        val endMillis: Long = Calendar.getInstance().run {
            set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 23, 59)
            timeInMillis
        }

        // Construct the query with the desired date range.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        val cur: Cursor = context.contentResolver.query(
            builder.build(),
            EVENT_PROJECTION,
            null,null, CalendarContract.Instances.BEGIN
        )!!

        while (cur.moveToNext()) {
            // Get the field values
            val beginVal: Long = cur.getLong(PROJECTION_BEGIN_INDEX)
            val endVal: Long = cur.getLong(PROJECTION_END_INDEX)
            val title: String = cur.getString(PROJECTION_TITLE_INDEX)

            var event = EventModel(title,beginVal,endVal)
            result.add(event)
        }

        return result
    }
}