package com.example.expenseapp.ui.add

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expenseapp.R
import com.example.expenseapp.dao.CategoryDao
import com.example.expenseapp.db.AppDatabase
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFragment : Fragment() {

    companion object {
        fun newInstance() = AddFragment()
    }
    lateinit var db:AppDatabase
    lateinit var categoryDao: CategoryDao

    private val viewModel: BlankViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chipGroup=view.findViewById<ChipGroup>(R.id.chipGroup2)
        db=AppDatabase.getDatabase(requireContext())
        categoryDao=db.categoryDao()

        chipGroup.removeAllViews()

        chipGroup.isSingleSelection=true
        chipGroup.isSelectionRequired=true
        CoroutineScope(Dispatchers.IO).launch {
            val categories=categoryDao.getAllActiveCategories()
            withContext(Dispatchers.Main){
                for(cat in categories){
                    val chip = Chip(requireContext()).apply {
                      text = cat.name
                      isClickable = true
                      isCheckable = true
                  }
                  chipGroup.addView(chip)
                }
            }
        }

    }
}