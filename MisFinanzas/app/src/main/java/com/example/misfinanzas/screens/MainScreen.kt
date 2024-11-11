package com.example.misfinanzas.screens

import GraficosScreen
import HistorialScreen
import SaldoScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
            modifier = Modifier.weight(1f) // Espacio equitativo
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
    modifier: Modifier = Modifier // Aceptar un modificador
) {
    IconButton(
        onClick = onClick,
        modifier = modifier // Aplicar el modificador
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = description
        )
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController, transactionDao: TransactionDao) {
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
