package com.example.misfinanzas.screens

import GraficosScreen
import HistorialScreen
import SaldoScreen
import TransaccionesScreen
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
        NavigationItem(iconId = R.drawable.ic_saldo, description = "Saldo") {
            navController.navigate("saldo_screen")
        }
        NavigationItem(iconId = R.drawable.ic_historial, description = "Historial") {
            navController.navigate("historial_screen")
        }
        NavigationItem(iconId = R.drawable.ic_graficos, description = "Gráficos") {
            navController.navigate("graficos_screen")
        }
        NavigationItem(iconId = R.drawable.ic_transacciones, description = "Transacciones") {
            navController.navigate("transacciones_screen")
        }
    }
}

@Composable
fun NavigationItem(iconId: Int, description: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
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
                balance = transaccionesViewModel.balance.collectAsState().value,
                transactions = transaccionesViewModel.transactions.collectAsState().value,
                onDateSelected = { date -> transaccionesViewModel.onDateSelected(date) },
                onSaveClicked = { date, amount, type, category ->
                    transaccionesViewModel.insertTransaction(date, amount, type, category)
                },
                onCancelClicked = { transaccionesViewModel.onCancelTransaction() }
            )
        }
    }
}
