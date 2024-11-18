package com.example.bitfitpart2

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.window.application
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfitpart1.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class log_fragment : Fragment() {
    private var foods = mutableListOf<DisplayFood>()
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_log_fragment, container, false)

        val layoutManager = LinearLayoutManager(context)
        foodRecyclerView = view.findViewById(R.id.log_recycler_view)
        foodAdapter = FoodAdapter(view.context, foods)
        foodRecyclerView.adapter = foodAdapter

        foodRecyclerView.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItem = DividerItemDecoration(view.context, it.orientation)
            foodRecyclerView.addItemDecoration(dividerItem)
        }

        lifecycleScope.launch {
            val foodApplication = requireActivity().application as FoodApplication

            foodApplication.db.FoodDao().getAll().collect { dbList ->
                val mappedList = dbList.map { entity ->
                    DisplayFood(
                        entity.food,
                        entity.calories
                    )
                }
                launch(Dispatchers.Main) {
                    foods.clear()
                    foods.addAll(mappedList)
                    foodAdapter.notifyDataSetChanged()
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    companion object {
        fun newInstance(): log_fragment{
            return log_fragment()
        }

    }
}