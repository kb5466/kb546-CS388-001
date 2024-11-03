package com.example.bitfitpart1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitfitpart1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var switchCompat: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences

    private var foods = mutableListOf<DisplayFood>()
    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val isNight = sharedPreferences.getBoolean("night_mode",false)
        if(isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_main)
        switchCompat = findViewById(R.id.switch1)
        switchCompat.isChecked = isNight

        switchCompat.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Save the user preference
            sharedPreferences.edit().putBoolean("night_mode", isChecked).apply()
        }
        foodRecyclerView = findViewById(R.id.food_list)
        foodAdapter = FoodAdapter(this, foods)
        foodRecyclerView.adapter = foodAdapter

        foodRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItem = DividerItemDecoration(this, it.orientation)
            foodRecyclerView.addItemDecoration(dividerItem)
        }

        lifecycleScope.launch {
            (application as FoodApplication).db.FoodDao().getAll().collect { dbList ->
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

        val addButton = findViewById<Button>(R.id.add_food)
        addButton.setOnClickListener {
            val intent = Intent(this, FoodEntry::class.java)
            startActivity(intent)
        }
    }
}
