package com.example.misfinanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.misfinanzas.ui.theme.MisFinanzasTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MisFinanzasTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "intro_screen"
    ) {
        composable("intro_screen") {
            IntroScreen(navController)
        }
        composable("saldo_screen") {
            SaldoScreen()
        }
    }
}

@Composable
fun IntroScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(3000) // Espera de 3 segundos
        navController.navigate("saldo_screen") {
            popUpTo("intro_screen") { inclusive = true }
        }
    }

}

@Composable
fun SaldoScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Balance $1000",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    MisFinanzasTheme {
        IntroScreen(navController = rememberNavController())
    }
}
