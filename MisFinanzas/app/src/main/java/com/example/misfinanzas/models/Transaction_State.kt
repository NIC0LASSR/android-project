package com.example.misfinanzas.models

sealed class TransactionState {
    object Idle : TransactionState()
    object Loading : TransactionState()
    data class Success(val data: List<Any> = emptyList()) : TransactionState()
    data class Error(val message: String) : TransactionState()
}