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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            launch{
                foodDao.totalCalories().collectLatest{totalCal ->
                    view.findViewById<TextView>(R.id.TotalView)?.text = "Total Calories: $totalCal"
                }
            }
            launch{
                foodDao.avgCalories().collectLatest {avgCal ->
                    view.findViewById<TextView>(R.id.AverageView)?.text = "Average Calories: ${"%.2f".format(avgCal)}"
                }
            }
            launch {
                foodDao.maxCalories().collectLatest { maxCal ->
                    println("MAX CAL: $maxCal")
                    withContext(Dispatchers.Main) {
                        view?.findViewById<TextView>(R.id.MaximumView)?.text = "Max Calories: ${maxCal ?: 0}"
                    }
                }
            }
            launch {
                foodDao.minCalories().collectLatest { minCal->
                    withContext(Dispatchers.Main){
                        view.findViewById<TextView>(R.id.MinimumView)?.text = "minimum Calories: $minCal"
                    }
                }
            }

        }
        return view
    }

    companion object {
        fun newInstance(): DashboardFragment{
            return DashboardFragment()
        }
    }
}