package com.example.misfinanzas.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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

    //Categoria
    val category: String
)
