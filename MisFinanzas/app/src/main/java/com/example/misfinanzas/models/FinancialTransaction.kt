package com.example.misfinanzas.models
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class FinancialTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,       // Fecha de la transacción
    val amount: Double,     // Monto de la transacción
    val type: String,       // Tipo: "Ingreso" o "Gasto"
    val category: String    // Categoría: por ejemplo, "Salario", "Alquiler"
)
