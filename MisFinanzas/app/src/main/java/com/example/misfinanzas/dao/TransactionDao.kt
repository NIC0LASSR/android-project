package com.example.misfinanzas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.misfinanzas.models.FinancialTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // Inserta una transacción en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: FinancialTransaction)

    // Obtiene todas las transacciones ordenadas por fecha descendente
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<FinancialTransaction>>

    // Elimina una transacción específica
    @Delete
    suspend fun deleteTransaction(transaction: FinancialTransaction)
}
