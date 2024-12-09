package com.example.pokemonapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.pokemonapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Headers
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val firstEnco: Fragment = firstEnco()
    val pokedex: Fragment = Pokedex()
    val interaction: Fragment = Interaction()
    val encounterLog: Fragment = EncounterLog()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            val dao = db.pokeDao()

            // Run the database operation in the IO dispatcher
            val count = withContext(Dispatchers.IO) {
                dao.getCount() // This accesses the database off the main thread
            }

            // Store the appropriate fragment based on database count
            val fragmentToShow = if (count == 0) firstEnco else pokedex

            // Store the fragment to show later
            runOnUiThread {
                replaceFragment(fragmentToShow)
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.botBar)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_interaction -> {
                    replaceFragment(interaction)
                    true
                }
                R.id.nav_dex -> {
                    replaceFragment(pokedex)
                    true
                }
                R.id.nav_inspect -> {
                    replaceFragment(encounterLog)
                    true
                }

                else -> false
            }
        }
        replaceFragment(firstEnco)
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}