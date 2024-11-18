package com.example.bitfitpart2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitfitpart1.R
import com.example.bitfitpart1.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var switchCompat: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    val log_fragment: Fragment = log_fragment()
    val DashboardFragment: Fragment = DashboardFragment()

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
        val addButton = findViewById<Button>(R.id.add_food)
        addButton.setOnClickListener {
            val intent = Intent(this, FoodEntry::class.java)
            startActivity(intent)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_log -> {
                    replaceFragment(log_fragment)
                    true
                }
                R.id.nav_line -> {
                    replaceFragment(DashboardFragment)
                    true
                }
                else -> false
            }
        }

        //Default fragment
        replaceFragment(log_fragment)
    }
    // Helper method to replace the fragment
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.article_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
