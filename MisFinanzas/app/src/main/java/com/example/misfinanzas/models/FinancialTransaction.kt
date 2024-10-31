package com.example.misfinanzas.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale

// Define la entidad 'transactions' para la base de datos Room
@Entity(tableName = "transactions")
data class FinancialTransaction(
    // ID
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Fecha de la transacción, almacenada como milisegundos
    val date: Long,

    // Monto de la transacción
    val amount: Double,

    // Tipo de transacción, puede ser "Ingreso" o "Gasto"
    val type: String,

    // Categoría
    val category: String
) {
    // Función para obtener la fecha en formato legible
    val dateFormatted: String
        get() {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return formatter.format(date)
        }
}
