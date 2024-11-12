package com.example.misfinanzas.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misfinanzas.dao.TransactionDao
import com.example.misfinanzas.models.FinancialTransaction
import com.example.misfinanzas.models.MonthlyData
import com.example.misfinanzas.models.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaccionesViewModel @Inject constructor(
    private val transactionDao: TransactionDao
) : ViewModel() {

    private val _gastosMensuales = MutableStateFlow<List<MonthlyData>>(emptyList())
    val gastosMensuales = _gastosMensuales.asStateFlow()

    private val _ingresosMensuales = MutableStateFlow<List<MonthlyData>>(emptyList())
    val ingresosMensuales = _ingresosMensuales.asStateFlow()

    private val _transactionState = MutableStateFlow<TransactionState>(TransactionState.Idle)
    val transactionState = _transactionState.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

    private val _transactions = MutableStateFlow<List<FinancialTransaction>>(emptyList())
    val transactions: StateFlow<List<FinancialTransaction>> = _transactions

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate

    init {
        loadTransactions()
        calculateBalance()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            try {
                transactionDao.getAllTransactions().collect { transactions ->
                    _transactions.value = transactions
                    calculateBalance()
                }
            } catch (e: Exception) {
                _transactionState.value = TransactionState.Error("Error al cargar transacciones: ${e.message}")
            }
        }
    }

    private fun calculateBalance() {
        _balance.value = _transactions.value.sumOf { it.amount }
    }

    fun insertTransaction(
        date: Long,
        amount: String,
        type: String,
        category: String
    ) {
        viewModelScope.launch {
            try {
                _transactionState.value = TransactionState.Loading

                val amountValue = amount.toDouble()
                val adjustedAmount = if (type == "Ingreso") amountValue else -amountValue

                val transaction = FinancialTransaction(
                    date = date,
                    amount = adjustedAmount,
                    type = type,
                    category = category
                )

                transactionDao.insertTransaction(transaction)
                _transactionState.value = TransactionState.Success()

                delay(200)
                _transactionState.value = TransactionState.Idle

            } catch (e: Exception) {
                _transactionState.value = TransactionState.Error(
                    e.message ?: "Error desconocido al guardar la transacción"
                )
            }
        }
    }

    fun onDateSelected(date: Long) {
        _selectedDate.value = date
    }

    fun deleteTransaction(transaction: FinancialTransaction) {
        viewModelScope.launch {
            try {
                transactionDao.deleteTransaction(transaction)
                _transactionState.value = TransactionState.Success()
                delay(200)
                _transactionState.value = TransactionState.Idle
            } catch (e: Exception) {
                _transactionState.value = TransactionState.Error("Error al eliminar la transacción: ${e.message}")
            }
        }
    }

    fun onCancelTransaction() {
        _transactionState.value = TransactionState.Idle
    }
}