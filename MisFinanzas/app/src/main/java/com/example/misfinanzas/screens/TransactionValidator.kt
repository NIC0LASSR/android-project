package com.example.misfinanzas.screens

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

class TransactionValidator {
    fun validateAmount(amount: String): ValidationResult {
        return when {
            amount.isEmpty() -> ValidationResult.Error("El monto no puede estar vacío")
            amount.toDoubleOrNull() == null -> ValidationResult.Error("El monto debe ser un número válido")
            amount.toDouble() <= 0 -> ValidationResult.Error("El monto debe ser mayor a 0")
            else -> ValidationResult.Success
        }
    }

    fun validateDate(date: Long): ValidationResult {
        return when {
            date <= 0 -> ValidationResult.Error("Fecha inválida")
            date > System.currentTimeMillis() -> ValidationResult.Error("La fecha no puede ser futura")
            else -> ValidationResult.Success
        }
    }

    fun validateCategory(category: String, validCategories: List<String>): ValidationResult {
        return when {
            category.isEmpty() -> ValidationResult.Error("Debe seleccionar una categoría")
            !validCategories.contains(category) -> ValidationResult.Error("Categoría inválida")
            else -> ValidationResult.Success
        }
    }

    fun validateType(type: String): ValidationResult {
        val validTypes = listOf("Ingreso", "Gasto")
        return when {
            type.isEmpty() -> ValidationResult.Error("Debe seleccionar un tipo")
            !validTypes.contains(type) -> ValidationResult.Error("Tipo inválido")
            else -> ValidationResult.Success
        }
    }

}