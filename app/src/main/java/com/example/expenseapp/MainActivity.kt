package com.example.expenseapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.expenseapp.db.AppDatabase
import com.example.expenseapp.entity.Category
import com.example.expenseapp.entity.Expense
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = AppDatabase.getDatabase(this@MainActivity)
        val categoryDao = db.categoryDao()
        val expenseDao = db.expenseDao()

        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        chipGroup.removeAllViews()
        chipGroup.isSingleSelection = true
        chipGroup.isSelectionRequired = true

       CoroutineScope(Dispatchers.IO).launch {
           val categories = categoryDao.getAllActiveCategories()
           withContext(Dispatchers.Main){
              for (category in categories) {
                  val chip = Chip(this@MainActivity).apply {
                      text = category.name
                      isClickable = true
                      isCheckable = true
                  }
                  chipGroup.addView(chip)
              }
          }
       }



    }


}