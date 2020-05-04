package com.example.myassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SpeakActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speak)

        // Toolbar
        val toolbar = supportActionBar
        toolbar!!.title = "Speak activity"
        toolbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
