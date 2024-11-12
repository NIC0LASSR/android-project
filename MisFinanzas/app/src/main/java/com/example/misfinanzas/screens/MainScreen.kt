package com.example.misfinanzas.screens

import HistorialScreen
import SaldoScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.misfinanzas.R
import com.example.misfinanzas.dao.TransactionDao

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController, transactionDao: TransactionDao) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SetupNavGraph(navController = navController, transactionDao = transactionDao)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationItem(
            iconId = R.drawable.ic_saldo,
            description = "Saldo",
            onClick = { navController.navigate("saldo_screen") },
            modifier = Modifier.weight(1f)
        )
        NavigationItem(
            iconId = R.drawable.ic_historial,
            description = "Historial",
            onClick = { navController.navigate("historial_screen") },
            modifier = Modifier.weight(1f)
        )
        NavigationItem(
            iconId = R.drawable.ic_graficos,
            description = "Gráficos",
            onClick = { navController.navigate("graficos_screen") },
            modifier = Modifier.weight(1f)
        )
        NavigationItem(
            iconId = R.drawable.ic_transacciones,
            description = "Transacciones",
            onClick = { navController.navigate("transacciones_screen") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun NavigationItem(
    iconId: Int,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = description
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(navController: NavHostController, transactionDao: TransactionDao) {
    // Obtener el ViewModel usando Hilt si está configurado
    val transaccionesViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModelFactory(transactionDao))

    NavHost(
        navController = navController,
        startDestination = "saldo_screen"
    ) {
        composable("saldo_screen") {
            SaldoScreen(viewModel = transaccionesViewModel)
        }
        composable("historial_screen") {
            HistorialScreen(viewModel = transaccionesViewModel)
        }
        composable("graficos_screen") {
            GraficosScreen(viewModel = transaccionesViewModel)
        }
        composable("transacciones_screen") {
            TransaccionesScreen(
                transactionState = transaccionesViewModel.transactionState.collectAsState().value,
                onDateSelected = { date -> transaccionesViewModel.onDateSelected(date) },
                onSaveClicked = { date, amount, type, category ->
                    transaccionesViewModel.insertTransaction(date, amount, type, category)
                },
                onCancelClicked = { transaccionesViewModel.onCancelTransaction() }
            )
        }
    }
}
