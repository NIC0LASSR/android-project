package com.example.misfinanzas.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.misfinanzas.dao.TransactionDao

class TransaccionesViewModelFactory(
    private val transactionDao: TransactionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaccionesViewModel::class.java)) {
            return TransaccionesViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
