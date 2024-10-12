package com.example.misfinanzas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro) // Asocia el layout XML de introducción

        // Temporizador de 3 segundos antes de pasar a la pantalla principal
        Handler(Looper.getMainLooper()).postDelayed({
            // Iniciar la actividad principal (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra la actividad de introducción para que no se pueda regresar
        }, 3000) // 3000 milisegundos = 3 segundos
    }
}
