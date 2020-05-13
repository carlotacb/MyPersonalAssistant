package com.example.myassistant

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myassistant.adapters.ForecastAdapter
import com.example.myassistant.mockedData.ForecastCurrentLocation
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.dialog_forecast_information.view.*
import kotlinx.android.synthetic.main.dialog_weather_information.view.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    // SPEECH CODE
    private var SPEECH_CODE: Int = 24
    lateinit var speaker: TextToSpeech

    // LOCATION CODE
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var lat = 41.369371
    private var long = 2.055964

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref: SharedPreferences = getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
        val city = addresses[0].locality

        val editorSharedPref: SharedPreferences.Editor = sharedPref.edit()
        editorSharedPref.putString("Location", city)
        editorSharedPref.putString("DefLocation", city)
        Log.d("CITY", city)
        editorSharedPref.apply()

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view -> displaySpeechRecognizer() }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_calendar, R.id.nav_weather, R.id.nav_settings, R.id.nav_help, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun displaySpeechRecognizer() {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en")
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(recognizerIntent, SPEECH_CODE)

        speaker = TextToSpeech(this, TextToSpeech.OnInitListener {
                status: Int ->
            if (status == TextToSpeech.SUCCESS) {
                speaker.language = Locale.US

            } else {
                Log.e("TextToSpeech", "initialization has failed")
            }
        })
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        // TODO: Check that this can be only a toast
                        requestNewLocationData()
                    } else {
                        lat = location.latitude
                        long = location.longitude
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID )
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates( mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            lat = mLastLocation.latitude
            long = mLastLocation.longitude
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dialogBuilder = AlertDialog.Builder(this)

        if (requestCode == SPEECH_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result : List<String> = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText : String = result.get(0)

                Log.d("SpokenText", spokenText)

                val sharedPreferences : SharedPreferences = this.getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

                // TODO: Create enum with the possibles questions

                // ------------------- WEATHER QUESTIONS

                if (spokenText == "what's the weather today") {
                    // TODO: Get current temperature and weather
                    speaker.speak("Today we have a sunny day in Barcelona, the temperature is 25 degrees with a feeling of 30 degrees", TextToSpeech.QUEUE_FLUSH, null)

                    val dialogWeatherInformation = LayoutInflater.from(this).inflate(R.layout.dialog_weather_information, null)
                    dialogBuilder.setView(dialogWeatherInformation)
                    val title = SpannableString("Today's weather")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogWeatherInformation.city_name_dialog.text = "Barcelona"
                    dialogWeatherInformation.dialog_preasure.text = "Preasure: 1011 mb"
                    dialogWeatherInformation.dialog_temp.text = "25ºC"
                    dialogWeatherInformation.dialog_humidity.text = "Humidity: 30%"
                    dialogWeatherInformation.dialog_feels.text = "Feels like: 30ºC"
                    dialogWeatherInformation.dialog_uvIndex.text = "UV Index: 4"
                    dialogWeatherInformation.dialog_weatherIcon.setImageResource(getWeatherIcon("sunny"))
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "what will be the weather tomorrow") {
                    // TODO: Get tomorrow weather
                    speaker.speak("Tomorrow will be a sunny day in Barcelona, the temperature will be 20 degrees with a feeling of 22 degrees", TextToSpeech.QUEUE_FLUSH, null)

                    val dialogWeatherInformation = LayoutInflater.from(this).inflate(R.layout.dialog_weather_information, null)
                    dialogBuilder.setView(dialogWeatherInformation)
                    val title = SpannableString("Tomorrow's weather")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogWeatherInformation.city_name_dialog.text = "Barcelona"
                    dialogWeatherInformation.dialog_preasure.text = "Preasure: 1050 mb"
                    dialogWeatherInformation.dialog_temp.text = "20ºC"
                    dialogWeatherInformation.dialog_humidity.text = "Humidity: 40%"
                    dialogWeatherInformation.dialog_feels.text = "Feels like: 22ºC"
                    dialogWeatherInformation.dialog_uvIndex.text = "UV Index: 4"
                    dialogWeatherInformation.dialog_weatherIcon.setImageResource(getWeatherIcon("sunny"))
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "what's the weather forecast") {
                    // TODO: Get weather for next days
                    speaker.speak("In Barcelona the following days the temperature will be between 13 and 23 degrees", TextToSpeech.QUEUE_FLUSH, null)

                    val dialogForecastInformation = LayoutInflater.from(this).inflate(R.layout.dialog_forecast_information, null)
                    dialogBuilder.setView(dialogForecastInformation)
                    val title = SpannableString("Barcelona forecast")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogForecastInformation.forecast_list.adapter = ForecastAdapter(ForecastCurrentLocation())
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "what's the weather in London") {
                    // TODO: Make london a variable
                    // TODO: Get weather from X city
                    speaker.speak("In london it's a rainy day, the temperature is 15 degrees with a feeling of 12 degrees", TextToSpeech.QUEUE_FLUSH, null)
                    val dialogWeatherInformation = LayoutInflater.from(this).inflate(R.layout.dialog_weather_information, null)
                    dialogBuilder.setView(dialogWeatherInformation)
                    val title = SpannableString("London's weather")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogWeatherInformation.city_name_dialog.text = "London"
                    dialogWeatherInformation.dialog_preasure.text = "Preasure: 1020 mb"
                    dialogWeatherInformation.dialog_temp.text = "15ºC"
                    dialogWeatherInformation.dialog_humidity.text = "Humidity: 10%"
                    dialogWeatherInformation.dialog_feels.text = "Feels like: 12ºC"
                    dialogWeatherInformation.dialog_uvIndex.text = "UV Index: 3"
                    dialogWeatherInformation.dialog_weatherIcon.setImageResource(getWeatherIcon("rainy"))
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "where I am") {
                    val loc = "Your current location is ".plus(sharedPreferences.getString("Location", ""))
                    speaker.speak(loc, TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText == "what's my default location") {
                    val loc = "Your default location is Barcelona"
                    speaker.speak(loc, TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText == "change my default location to Tarragona") {
                    // TODO: Change location
                    // TODO: Check fragment and change to settings fragment if it's not there
                    speaker.speak("Your location has been changed to Tarragona", TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText == "what are my events for today") {
                    // TODO: Check events for the current day
                    // TODO: Check fragment and change to calendar fragment if it's not there
                    speaker.speak("Your calendar today is full of events, you have 3 events", TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText == "what are my events for tomorrow") {
                    // TODO: Check events for tomorrow
                    // TODO: Check fragment and change to calendar fragment if it's not there
                    speaker.speak("Tomorrow you have 4 events planned, the fist one is at 10 the daily meeting, at 12 University class, at 14 Familiar lunch and the last one at 18 Ballet class", TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText == "create a new event") {
                    // TODO: Demanar més informació
                    speaker.speak("The event has been created", TextToSpeech.QUEUE_FLUSH, null)
                }

                else {
                    speaker.speak("sorry I don't understand what you just say, try with some of the following questions: what's the weather today?, what will be the weather tomorrow?, where I am? ... you can find it all in the Help screen", TextToSpeech.QUEUE_FLUSH, null)
                    displaySpeechRecognizer()
                }
                // Current location --> GPS - Default location --> La que marques tu

            } else {
                Toast.makeText(this, "I can't hear you", Toast.LENGTH_LONG).show()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
