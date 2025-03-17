package com.example.expenseapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expenseapp.entity.Category
import com.example.expenseapp.dao.CategoryDao
import com.example.expenseapp.entity.Expense
import com.example.expenseapp.dao.ExpenseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Category::class, Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton pattern to prevent multiple instances of the database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Database name
                )
                    .fallbackToDestructiveMigration() // Allows database migration if schema changes
                    .addCallback(AddCategoryCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AddCategoryCallback:RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val dao= INSTANCE?.categoryDao()
                CoroutineScope(Dispatchers.IO).launch {
                    if (dao != null) {
                        dao.insert(Category(name = "Default", description = ""))
                    }
                }
            }
        }
    }
}
