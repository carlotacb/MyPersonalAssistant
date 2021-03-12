package com.example.personalassistant

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
import android.provider.Settings
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
import com.example.personalassistant.adapters.ListEventAdapter
import com.example.personalassistant.asyncCalls.CurrentWeatherAsyncTask
import com.example.personalassistant.asyncCalls.ForecastAsyncTask
import com.example.personalassistant.asyncCalls.TomorrowWeatherAsyncTask
import com.example.personalassistant.mockedData.TodayEvents
import com.example.personalassistant.mockedData.TomorrowEvents
import com.example.personalassistant.services.CalendarHelper
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.dialog_forecast_information.view.*
import kotlinx.android.synthetic.main.dialog_planned_events.view.*
import kotlinx.android.synthetic.main.dialog_weather_information.view.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    // ASYNC TASKS
    lateinit var currentWeatherTask: CurrentWeatherAsyncTask
    lateinit var forecastTask: ForecastAsyncTask
    lateinit var tomorrowWeatherTask: TomorrowWeatherAsyncTask

    // SERVICES
    private val calendarService = CalendarHelper(this)

    // SPEECH VARIABLES
    private var SPEECH_CODE: Int = 24
    lateinit var speaker: TextToSpeech

    // LOCATION VARIABLES
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view -> displaySpeechRecognizer() }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_weather, R.id.nav_settings, R.id.nav_help, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                locationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.interval = 0
                locationRequest.fastestInterval = 0
                locationRequest.numUpdates = 1

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                mFusedLocationClient.requestLocationUpdates(locationRequest, gpsLocationCallback, Looper.myLooper())
            } else {
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR), PERMISSION_ID)
        }
    }

    private val gpsLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            updateLocationPreferences(mLastLocation.latitude,mLastLocation.longitude)
        }
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

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun updateLocationPreferences(lat: Double, long: Double) {
        val sharedPref: SharedPreferences = getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
        val city = addresses[0].locality

        val editorSharedPref: SharedPreferences.Editor = sharedPref.edit()
        editorSharedPref.putString("Location", city)
        editorSharedPref.putString("DefLocation", city)
        editorSharedPref.putString("DefLatitude", lat.toString())
        editorSharedPref.putString("DefLongitude", long.toString())
        editorSharedPref.apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dialogBuilder = AlertDialog.Builder(this)

        if (requestCode == SPEECH_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result : List<String> = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText : String = result.get(0)

                Log.d("SpokenText", spokenText)

                val sharedPreferences : SharedPreferences = this.getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)

                // ------------------- WEATHER QUESTIONS

                if (spokenText == "what's the weather today" || spokenText == "what's the current weather" || spokenText == "what's the weather") {
                    val location = sharedPreferences.getString("DefLocation", "Barcelona")

                    speaker.speak("Here you can find the weather for today in $location", TextToSpeech.QUEUE_FLUSH, null)

                    val dialogWeatherInformation = LayoutInflater.from(this).inflate(R.layout.dialog_weather_information, null)
                    dialogBuilder.setView(dialogWeatherInformation)
                    val title = SpannableString("Today's weather")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogWeatherInformation.city_name_dialog.text = location
                    currentWeatherTask = CurrentWeatherAsyncTask(
                        dialogWeatherInformation.dialog_preasure,
                        dialogWeatherInformation.dialog_humidity,
                        dialogWeatherInformation.dialog_uvIndex,
                        dialogWeatherInformation.dialog_feels,
                        dialogWeatherInformation.dialog_max_min,
                        dialogWeatherInformation.dialog_temp,
                        dialogWeatherInformation.dialog_weatherIcon,
                        location
                    )
                    currentWeatherTask.execute()
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "what will be the weather tomorrow" || spokenText == "weather for tomorrow") {
                    val location = sharedPreferences.getString("DefLocation", "Barcelona")
                    speaker.speak("Here is the weather for tomorrow in $location", TextToSpeech.QUEUE_FLUSH, null)

                    val latitude = sharedPreferences.getString("DefLatitude", "41.390205")
                    val longitude = sharedPreferences.getString("DefLongitude", "2.154007")

                    val dialogWeatherInformation = LayoutInflater.from(this).inflate(R.layout.dialog_weather_information, null)
                    dialogBuilder.setView(dialogWeatherInformation)
                    val title = SpannableString("Tomorrow's weather")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    tomorrowWeatherTask = TomorrowWeatherAsyncTask(
                        dialogWeatherInformation.dialog_preasure,
                        dialogWeatherInformation.dialog_humidity,
                        dialogWeatherInformation.dialog_uvIndex,
                        dialogWeatherInformation.dialog_feels,
                        dialogWeatherInformation.dialog_max_min,
                        dialogWeatherInformation.dialog_temp,
                        dialogWeatherInformation.dialog_weatherIcon,
                        latitude, longitude
                    )
                    tomorrowWeatherTask.execute()
                    dialogWeatherInformation.city_name_dialog.text = location
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "what's the weather forecast" || spokenText == "what's the forecast for the following days" || spokenText == "weather forecast") {
                    val location = sharedPreferences.getString("DefLocation", "Barcelona")
                    speaker.speak("Here you can see the forecast for the nexts days in $location", TextToSpeech.QUEUE_FLUSH, null)

                    val latitude = sharedPreferences.getString("DefLatitude", "41.390205")
                    val longitude = sharedPreferences.getString("DefLongitude", "2.154007")

                    val dialogForecastInformation = LayoutInflater.from(this).inflate(R.layout.dialog_forecast_information, null)
                    dialogBuilder.setView(dialogForecastInformation)
                    val title = SpannableString("$location forecast")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    forecastTask = ForecastAsyncTask(dialogForecastInformation.forecast_list, longitude, latitude)
                    forecastTask.execute()
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText.contains("what's the weather in"))  {
                    var city = spokenText.replace("what's the weather in ", "")
                    speaker.speak("Here you have the weather details in $city", TextToSpeech.QUEUE_FLUSH, null)
                    val dialogWeatherInformation = LayoutInflater.from(this).inflate(R.layout.dialog_weather_information, null)
                    dialogBuilder.setView(dialogWeatherInformation)
                    val title = SpannableString("$city weather")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogWeatherInformation.city_name_dialog.text = city
                    currentWeatherTask = CurrentWeatherAsyncTask(
                        dialogWeatherInformation.dialog_preasure,
                        dialogWeatherInformation.dialog_humidity,
                        dialogWeatherInformation.dialog_uvIndex,
                        dialogWeatherInformation.dialog_feels,
                        dialogWeatherInformation.dialog_max_min,
                        dialogWeatherInformation.dialog_temp,
                        dialogWeatherInformation.dialog_weatherIcon,
                        city
                    )
                    currentWeatherTask.execute()
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                // ------------------- LOCATION QUESTIONS

                else if (spokenText == "where am I" || spokenText == "what's my current location") {
                    val location = sharedPreferences.getString("Location", "")
                    speaker.speak("Your current location is $location", TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText == "what's my default location") {
                    val location = sharedPreferences.getString("DefLocation", "")
                    speaker.speak("Your default location is $location", TextToSpeech.QUEUE_FLUSH, null)
                }

                else if (spokenText.contains("change my default location to")) {
                    val changeCity = spokenText.substring(spokenText.lastIndexOf(" ")+1)

                    val geocoder = Geocoder(this, Locale.getDefault())
                    val loc = geocoder.getFromLocationName(changeCity, 1)
                    val changeLat = loc.get(0).latitude
                    val changeLong = loc.get(0).longitude

                    val sharedPref: SharedPreferences = getSharedPreferences("shared-preference-file", Context.MODE_PRIVATE)
                    val editorSharedPref: SharedPreferences.Editor = sharedPref.edit()
                    editorSharedPref.putString("DefLocation", changeCity)
                    editorSharedPref.putString("DefLatitude", changeLat.toString())
                    editorSharedPref.putString("DefLongitude", changeLong.toString())
                    editorSharedPref.apply()

                    speaker.speak("Your location has been changed to $changeCity", TextToSpeech.QUEUE_FLUSH, null)
                }

                // ------------------- CALENDAR QUESTIONS

                else if (spokenText == "what are my events for today" || spokenText == "what I have to do today") {
                    Log.d("EVENTS", calendarService.getTodayEvents().toString())
                    speaker.speak("Here you can find your events for today", TextToSpeech.QUEUE_FLUSH, null)
                    val dialogTodayEvents = LayoutInflater.from(this).inflate(R.layout.dialog_planned_events, null)
                    dialogBuilder.setView(dialogTodayEvents)
                    val title = SpannableString("Today events")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogTodayEvents.planned_events.adapter = ListEventAdapter(calendarService.getTodayEvents())
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "what are my events for tomorrow" || spokenText == "what I have to do tomorrow") {
                    Log.d("EVENTS", calendarService.getTomorrowEvents().toString())
                    speaker.speak("Here you can find your events for tomorrow", TextToSpeech.QUEUE_FLUSH, null)
                    val dialogTomorrowEvents = LayoutInflater.from(this).inflate(R.layout.dialog_planned_events, null)
                    dialogBuilder.setView(dialogTomorrowEvents)
                    val title = SpannableString("Tomorrow events")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)
                    dialogBuilder.setTitle(title)
                    dialogTomorrowEvents.planned_events.adapter = ListEventAdapter(calendarService.getTomorrowEvents())
                    dialogBuilder.setPositiveButton("Close", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                    dialogBuilder.show()
                }

                else if (spokenText == "create a new event" || spokenText == "new event") {
                    calendarService.addEvent()
                }

                // ------------------- THE QUESTION IS NOT CORRECT
                else {
                    speaker.speak("sorry I don't understand what you just say, try with some of the Help screen questions", TextToSpeech.QUEUE_FLUSH, null)
                    // TODO: Change to Help Screen
                }

            } else {
                Toast.makeText(this, "I can't hear you", Toast.LENGTH_LONG).show()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
