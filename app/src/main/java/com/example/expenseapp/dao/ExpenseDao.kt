package com.example.expenseapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expenseapp.datamodel.CategoryExpenseSummary
import com.example.expenseapp.entity.Expense

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense):Long

    @Update
    suspend fun update(expense: Expense)

    @Query("SELECT * FROM expenses WHERE active = 1 AND isDeleted = 0")
    suspend fun getAllActiveExpenses(): List<Expense>

    @Query("UPDATE expenses SET isDeleted = 1 WHERE id = :expenseId")
    suspend fun markExpenseAsDeleted(expenseId: Long)

    @Query("UPDATE expenses SET active = 0 WHERE id = :expenseId")
    suspend fun deactivateExpense(expenseId: Long)

    @Query("""         
SELECT c.id AS categoryId,                 
c.name AS categoryName,                 
SUM(e.amount) AS totalAmount,                 
AVG(e.amount) AS averageAmount         
FROM expenses e         
INNER JOIN categories c ON e.categoryId = c.id         
WHERE e.isDeleted = 0 AND c.isDeleted = 0         
GROUP BY e.categoryId 
   """)
    fun getCategoryExpenseSummary(): List<CategoryExpenseSummary>
}