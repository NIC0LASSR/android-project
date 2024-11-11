package com.example.misfinanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.misfinanzas.database.AppDatabase
import com.example.misfinanzas.screens.MainScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val transactionDao = database.transactionDao()

        setContent {
            val navController = rememberNavController()
            MainScreen(navController = navController, transactionDao = transactionDao)
        }
    }
}
