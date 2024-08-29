package com.example.clima

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.clima.ui.theme.ClimaTheme
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.math.ceil


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lat=intent.getStringExtra("lat")
        var long=intent.getStringExtra("long")

        window.statusBarColor= Color.parseColor("#1383C3")
        getJsonData(lat,long)

    }

    private fun getJsonData(lat: String?, long: String?) {
        val API_KEY = "17baaf414194b8cc29d1a5774b277d14"
        val queue = Volley.newRequestQueue(this)
        var url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })

        queue.add(jsonRequest)
    }
    private fun setValues(response:JSONObject){

        var city = findViewById<TextView>(R.id.cityCard_text)
        city.text=response.getString("name")

        var lat = response.getJSONObject("coord").getString("lat")
        var long = response.getJSONObject("coord").getString("lon")
        var coordinates = findViewById<TextView>(R.id.coordinator)
        coordinates.text="${lat} , ${long}"

        var weather = findViewById<TextView>(R.id.weather)
        weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")

        var tempJson =response.getJSONObject("main").getString("temp")
        tempJson=((((tempJson).toFloat()-273.15)).toInt()).toString()

        var temp = findViewById<TextView>(R.id.temperature)
        temp.text="${tempJson}°C"

        var windSpeed = findViewById<TextView>(R.id.windSpeed)
        windSpeed.text=response.getJSONObject("wind").getString("speed")

