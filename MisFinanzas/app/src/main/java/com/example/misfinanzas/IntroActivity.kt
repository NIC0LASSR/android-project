package com.example.misfinanzas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro) // Asocia la actividad con el archivo intro.xml

        // Agregar un retraso de 8 segundos antes de pasar a MainActivity
        android.os.Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Iniciar MainActivity
            finish() // Cierra IntroActivity para que no se pueda volver a ella
        }, 8000) // 8000 ms = 8 segundos
    }
}
