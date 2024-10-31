package com.example.misfinanzas.models

sealed class Transaction_State {
    object Idle : Transaction_State()
    object Loading : Transaction_State()
    object Success : Transaction_State()
    data class Error(val message: String) : Transaction_State()
}
