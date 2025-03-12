package com.example.expenseapp.datamodel

data class CategoryExpenseSummary(
    val categoryId: Long,
    val categoryName: String,
    val totalAmount: Double,
    val averageAmount: Double
)
