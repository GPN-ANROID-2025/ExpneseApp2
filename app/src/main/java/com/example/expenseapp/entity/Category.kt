package com.example.expenseapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String?,
    val active: Boolean = true, // Indicates whether the category is still active in business logic
    val isDeleted: Boolean = false // Indicates whether the category is marked as deleted (soft delete)
)
