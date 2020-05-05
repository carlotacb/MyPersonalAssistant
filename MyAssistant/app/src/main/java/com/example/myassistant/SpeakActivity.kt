package com.example.myassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button

class SpeakActivity : AppCompatActivity() {

    //SPEECH CODE
    private var SPEECH_REQUEST_CODE: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speak)

        // Toolbar
        val toolbar = supportActionBar
        toolbar!!.title = "Speak activity"
        toolbar.setDisplayHomeAsUpEnabled(true)

        val speakButton: Button = findViewById(R.id.speak_button)
        speakButton.setOnClickListener{ displaySpeechRecognizer() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displaySpeechRecognizer() {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en")
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }
}
