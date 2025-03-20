package com.example.expenseapp.ui.add

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.expenseapp.R
import com.example.expenseapp.dao.CategoryDao
import com.example.expenseapp.dao.ExpenseDao
import com.example.expenseapp.db.AppDatabase
import com.example.expenseapp.entity.Category
import com.example.expenseapp.entity.Expense
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.text.toInt

class AddFragment : Fragment() {

    companion object {
        fun newInstance() = AddFragment()
    }
    lateinit var db:AppDatabase
    lateinit var categoryDao: CategoryDao
    lateinit var btnAdd: Button
    lateinit var etName: TextInputEditText
    lateinit var etAmount: TextInputEditText
    lateinit var categories: List<Category>
    lateinit var expenseDao: ExpenseDao
    lateinit var chipGroup: ChipGroup

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
         chipGroup=view.findViewById<ChipGroup>(R.id.chipGroup2)
        btnAdd=view.findViewById<Button>(R.id.btnAdd)
        etName=view.findViewById<TextInputEditText>(R.id.etName)
        etAmount=view.findViewById<TextInputEditText>(R.id.etAmount)
        db=AppDatabase.getDatabase(requireContext())
        categoryDao=db.categoryDao()
        expenseDao=db.expenseDao()
        chipGroup.removeAllViews()




        chipGroup.isSingleSelection=true
        chipGroup.isSelectionRequired=true
        CoroutineScope(Dispatchers.IO).launch {
//            categoryDao.insert(Category(name = "Drinks", description = "cold drinks expenses"))
//            categoryDao.insert(Category(name = "Cloths", description = "cold drinks expenses"))
            categories=categoryDao.getAllActiveCategories()
            withContext(Dispatchers.Main){
                chipGroup.setOnCheckedStateChangeListener(ChipListener(categories,requireContext()))
                for(cat in categories){
                    val chip = Chip(requireContext()).apply {
                      text = cat.name
                      isClickable = true
                      isCheckable = true
                        tag=cat
                        id=cat.id.toInt()
                  }
                    if(cat.id.toInt() ==1){
                        chip.isChecked=true
                        chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.check_circle_24px)

                    }

                    chip.setOnCheckedChangeListener { _, isChecked ->
                        if (!isChecked) {
                            // Hide the text when selected (set text to empty for only the selected chip)
                            chip.chipIcon = null
                        } else {
                            // Restore the text when deselected (for only the deselected chip)
                            chip.chipIcon = ContextCompat.getDrawable(requireContext(), R.drawable.check_circle_24px)
                        }
                    }
                  chipGroup.addView(chip)

                }
            }
        }




        btnAdd.setOnClickListener {
            Log.d("mytag",""+chipGroup.checkedChipId)
            var selectedCategory: Category?=null
            for(cat in categories){

                if(cat.id.toInt() ==chipGroup.checkedChipId)
                {
                    selectedCategory=cat
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                val count=expenseDao.insert(Expense(
                    amount = etAmount.text.toString().toDouble(),
                    description = "",
                    categoryId = selectedCategory?.id ?: 1,
                    date = Date().time
                ))



                withContext(Dispatchers.Main){
                    if(count>1){
                        etAmount.text?.clear()
                        etName.text?.clear()
                    }
                }
                Log.d("mytag","Inserted $count")
            }

        }



    }
    class ChipListener(val categories: List<Category>,val context:Context): ChipGroup.OnCheckedStateChangeListener
    {
        override fun onCheckedChanged(
            group: ChipGroup,
            checkedIds: List<Int?>
        ) {
            for(cat in categories){

                if(cat.id.toInt() ==group.checkedChipId)
                {
                    val chip=group.findViewById<Chip>(cat.id.toInt())
                    //chip.chipIcon = ContextCompat.getDrawable(context, R.drawable.check_circle_24px)

                }
            }
        }
    }

}