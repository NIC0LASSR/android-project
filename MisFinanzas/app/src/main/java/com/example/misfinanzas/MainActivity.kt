package com.example.misfinanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.database.AppDatabase
import com.example.misfinanzas.ui.theme.MisFinanzasTheme

class MainActivity : ComponentActivity() {
    //Variables para base de datos
    private lateinit var database: AppDatabase
    private lateinit var transactionDao: TransactionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Base de datos
        database = AppDatabase.getDatabase(this)
        transactionDao = database.transactionDao()

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
    // Mostrar el layout de saldo usando AndroidView para inflar XML
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.activy_saldo_actual, null, false)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun HistorialScreen(navController: NavHostController) {
    // Mostrar el layout de historial usando AndroidView para inflar XML
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.acvity_historial, null, false)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun GraficosScreen(navController: NavHostController) {
    // Mostrar el layout de gráficos usando AndroidView para inflar XML
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.activity_graficos, null, false)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun TransaccionesScreen(navController: NavHostController) {
    // Mostrar el layout de transacciones usando AndroidView para inflar XML
    AndroidView(
        factory = { context ->
            android.view.LayoutInflater.from(context).inflate(R.layout.activity_transaccion, null, false)
        },
        modifier = Modifier.fillMaxSize()
    )
}
