package com.example.misfinanzas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.database.AppDatabase
import com.example.misfinanzas.models.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao = AppDatabase.getDatabase(application).transactionDao()
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    // Calcula el saldo total basado en las transacciones
    val totalBalance: LiveData<Double> = allTransactions.map { transactions ->
        transactions.sumOf { transaction ->
            if (transaction.type == "Ingreso") transaction.amount else -transaction.amount
        }
    }

    fun insert(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction)
        }
    }

    fun update(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.update(transaction)
        }
    }

    fun delete(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction)
        }
    }
}