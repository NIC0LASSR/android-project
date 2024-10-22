package com.example.misfinanzas.dao

import androidx.room.*
import com.example.misfinanzas.models.FinancialTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: FinancialTransaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<FinancialTransaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<FinancialTransaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Ingreso'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'Gasto'")
    fun getTotalExpenses(): Flow<Double?>

    @Query("SELECT SUM(CASE WHEN type = 'Ingreso' THEN amount ELSE -amount END) FROM transactions")
    fun getCurrentBalance(): Flow<Double?>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<FinancialTransaction>>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type = :type GROUP BY category ORDER BY total DESC")
    fun getTransactionSummaryByCategory(type: String): Flow<List<CategorySummary>>

    @Delete
    suspend fun deleteTransaction(transaction: FinancialTransaction)

    @Update
    suspend fun updateTransaction(transaction: FinancialTransaction)

    @Query("DELETE FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    suspend fun deleteTransactionsByDateRange(startDate: String, endDate: String)
}

// Clase de datos para el resumen por categoría
data class CategorySummary(
    val category: String,
    val total: Double
)
