package com.example.misfinanzas.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.models.FinancialTransaction // Asegúrate de usar esta importación
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransaccionesScreen(
    navController: NavHostController,
    transactionDao: TransactionDao // Pasar DAO como parámetro
) {
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }
    var monto by remember { mutableStateOf("") }
    var isIngreso by remember { mutableStateOf(true) }
    var categoria by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Lista de categorías para el Spinner
    val categorias = listOf("Comida", "Transporte", "Entretenimiento", "Servicios", "Otros")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botón para guardar la transacción
        Button(
            onClick = {
                scope.launch {
                    val financialTransaction = FinancialTransaction(
                        date = fecha,
                        amount = monto.toDoubleOrNull() ?: 0.0,
                        type = if (isIngreso) "Ingreso" else "Gasto",
                        category = categoria
                    )
                    // Corregir aquí: usa la variable financialTransaction
                    transactionDao.insertTransaction(financialTransaction)
                    navController.navigate("saldo_screen") // Navegar a la pantalla principal
                }
            }
        ) {
            Text("Aceptar")
        }
    }
}
