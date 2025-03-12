package com.example.expenseapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expenseapp.entity.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM categories WHERE active = 1 AND isDeleted = 0")
    suspend fun getAllActiveCategories(): List<Category>

    @Query("UPDATE categories SET isDeleted = 1 WHERE id = :categoryId")
    suspend fun markCategoryAsDeleted(categoryId: Long)

    @Query("UPDATE categories SET active = 0 WHERE id = :categoryId")
    suspend fun deactivateCategory(categoryId: Long)
}