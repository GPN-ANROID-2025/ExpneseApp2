package com.example.expenseapp.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.anychart.palettes.DistinctColors
import com.anychart.palettes.RangeColors
import com.example.expenseapp.R
import com.example.expenseapp.dao.ExpenseDao
import com.example.expenseapp.databinding.FragmentDashboardBinding
import com.example.expenseapp.datamodel.CategoryExpenseSummary
import com.example.expenseapp.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var anyChartView: AnyChartView
    lateinit var pie: Pie
    lateinit var progressBar: ProgressBar
    lateinit var summeryList:List<CategoryExpenseSummary>
    lateinit var expenseDao: ExpenseDao
    lateinit var db:AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anyChartView=view.findViewById<AnyChartView>(R.id.any_chart_view)
        progressBar=view.findViewById<ProgressBar>(R.id.progress_bar)
        anyChartView.setProgressBar(progressBar)
        pie= AnyChart.pie()
        db= AppDatabase.getDatabase(requireContext())
        expenseDao=db.expenseDao()


        CoroutineScope(Dispatchers.IO).launch {

            summeryList=expenseDao.getCategoryExpenseSummary()

            withContext(Dispatchers.Main){
                val data = ArrayList<DataEntry>();
               for(item in summeryList){
                   data.add(ValueDataEntry(item.categoryName, item.totalAmount));
               }
                pie.data(data);



                val colors = arrayOf("#FF5733", "#33FF57", "#3357FF", "#F0E68C")

                // Apply the custom color list to the pie chart using palette()
                pie.palette(colors)
                anyChartView.setChart(pie);
                pie.title("Expenses Summery");

                pie.labels().position("outside");

                pie.legend().title().enabled(true);
                pie.legend().title()
                    .text("categories")
                    .padding(0, 0, 10, 0);

                pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);
            }

        }



    }
}