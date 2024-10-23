package com.example.misfinanzas.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.models.FinancialTransaction
import kotlinx.coroutines.launch

class TransaccionesViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    // Lista de categorías válidas (debe ser consistente con las definidas en strings.xml)
    private val validCategories = listOf("Salario", "Alquiler", "Comida", "Transporte", "Entretenimiento")

    // Lista de tipos válidos para las transacciones
    private val validTypes = listOf("Ingreso", "Gasto")

    fun insertTransaction(
        date: Long,           // Cambiar la fecha a tipo Long
        amount: Double,
        type: String,
        category: String
    ) {
        // Validación de entrada

        if (amount <= 0 || category.isEmpty() || !validCategories.contains(category) || !validTypes.contains(type)) {

            // No realizar ninguna operación si los valores no son válidos
            return
        }

        // Ajustar el monto según el tipo

        val adjustedAmount = if (type == "Ingreso") amount else -amount

        // Crear la transacción

        val transaction = FinancialTransaction(
            date = date,
            amount = adjustedAmount,
            type = type,
            category = category
        )

        // Insertar la transacción en la base de datos usando corrutinas

        viewModelScope.launch {
            try {
                transactionDao.insertTransaction(transaction)
            } catch (e: Exception) {
                // Manejo de errores (puede ser un log o mostrar un mensaje al usuario)
                e.printStackTrace()
            }
        }
    }
}
