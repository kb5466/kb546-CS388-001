package com.example.bitfitpart2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bitfitpart1.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FoodEntry: AppCompatActivity() {
    private lateinit var food_input: TextInputEditText
    private lateinit var calorie_input : TextInputEditText
    private lateinit var enter_button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calorie_entry)
        food_input = findViewById(R.id.food_input)
        calorie_input = findViewById(R.id.calories_input)
        enter_button = findViewById(R.id.entry_button)

        enter_button.setOnClickListener{
            val food = DisplayFood(
                food_input.text.toString(),
                calorie_input.text.toString()
            )
            food_input.setText("")
            calorie_input.setText("")

            lifecycleScope.launch(IO) {
                (application as FoodApplication).db.FoodDao().insert(
                    FoodEntity(
                        food = food.food_name,
                        calories = food.calory_num
                    )
                )
            }
            lifecycleScope.launch{
                (application as FoodApplication).db.FoodDao().getAll().collect(){ dbList ->
                    dbList.map { entity->
                        DisplayFood(
                            entity.food,
                            entity.calories
                        )
                    }
                }
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}