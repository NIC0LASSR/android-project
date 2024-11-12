package com.example.misfinanzas.models

import kotlinx.datetime.Month

data class MonthlyData(
    val month: Month,
    val totalAmount: Double
)
