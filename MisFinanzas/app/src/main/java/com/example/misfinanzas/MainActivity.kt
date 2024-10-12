package com.example.misfinanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
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
                MainScreen(navController)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            SetupNavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    AndroidView(
        factory = { context ->
            com.google.android.material.bottomnavigation.BottomNavigationView(context).apply {
                inflateMenu(R.menu.bottom_nav_menu)
                setOnItemSelectedListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.nav_saldo_actual -> {
                            navController.navigate("saldo_screen")
                            true
                        }
                        R.id.nav_historial -> {
                            navController.navigate("historial_screen")
                            true
                        }
                        R.id.nav_graficos -> {
                            navController.navigate("graficos_screen")
                            true
                        }
                        R.id.nav_transacciones -> {
                            navController.navigate("transacciones_screen")
                            true
                        }
                        else -> false
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "saldo_screen"
    ) {
        composable("saldo_screen") {
            SaldoScreen(navController = navController)
        }
        composable("historial_screen") {
            HistorialScreen(navController = navController)
        }
        composable("graficos_screen") {
            GraficosScreen(navController = navController)
        }
        composable("transacciones_screen") {
            TransaccionesScreen(navController = navController)
        }
    }
}

@Composable
fun SaldoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Saldo Actual: $1000", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun HistorialScreen(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Historial de Transacciones", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun GraficosScreen(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Gráficos de Finanzas", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun TransaccionesScreen(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Registrar nueva Transacción", style = MaterialTheme.typography.headlineMedium)
    }
}