package com.example.misfinanzas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro)  // Utiliza el layout de intro.xml

        // Mostrar la pantalla de introducción durante 3 segundos antes de ir a MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()  // Finaliza la actividad para evitar que el usuario vuelva a ella
        }, 3000)  // 3000 ms = 3 segundos
    }
}
