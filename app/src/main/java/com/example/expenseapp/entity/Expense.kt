package com.example.expenseapp.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val description: String?,
    val categoryId: Long,
    val date: Long, // Timestamp for the expense
    val active: Boolean = true, // Indicates whether the expense is still active in business logic
    val isDeleted: Boolean = false // Indicates whether the expense is marked as deleted (soft delete)
)
