package com.example.bitfitpart2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.bitfitpart1.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        lifecycleScope.launch(Dispatchers.IO){
            val foodDao = (activity?.application as FoodApplication).db.FoodDao()

            val totalCal = foodDao.totalCalories()
            val avgCal = foodDao.avgCalories()
            val maxCal = foodDao.maxCalories()
            val minCal = foodDao.minCalories()

            view.findViewById<TextView>(R.id.TotalView)?.text = "Total Calories: $totalCal"
            view.findViewById<TextView>(R.id.AverageView)?.text = "Average Calories: ${"%.2f".format(avgCal)}"
            view.findViewById<TextView>(R.id.MaximumView)?.text = "Max Calories: $maxCal"
            view.findViewById<TextView>(R.id.MinimumView)?.text = "Minimum Calories: $minCal"

        }
        return view
    }

    companion object {
        fun newInstance(): DashboardFragment{
            return DashboardFragment()
        }
    }
}