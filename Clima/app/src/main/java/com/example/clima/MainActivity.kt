package com.example.clima

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure to set the correct layout

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        // Check if lat and long are not null
        if (lat != null && long != null) {
            window.statusBarColor = Color.parseColor("#1383C3")
            getJsonData(lat, long)
        } else {
            Toast.makeText(this, "Location data is missing", Toast.LENGTH_LONG).show()
        }
    }

    private fun getJsonData(lat: String, long: String) {
        val API_KEY = "17baaf414194b8cc29d1a5774b277d14"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=$API_KEY"

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject) {
        val city = findViewById<TextView>(R.id.cityCard_text)
        city.text = response.getString("name")

        val lat = response.getJSONObject("coord").getString("lat")
        val long = response.getJSONObject("coord").getString("lon")
        val coordinates = findViewById<TextView>(R.id.coordinator)
        coordinates.text = "$lat , $long"

        val weather = findViewById<TextView>(R.id.weather)
        weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")

        val tempJson = response.getJSONObject("main").getString("temp")
        val tempCelsius = (((tempJson.toFloat() - 273.15)).toInt()).toString()

        val temp = findViewById<TextView>(R.id.temperature)
        temp.text = "$tempCelsius°C"

        val windSpeed = findViewById<TextView>(R.id.windSpeed)
        windSpeed.text = response.getJSONObject("wind").getString("speed")
    }
}
