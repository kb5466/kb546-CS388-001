package com.example.lab1

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var count = 0;
        var total = 0;
        var multiplier = 1;
        var level = 1;
        var goal = 100;
        findViewById<Button>(R.id.showIcon).setOnClickListener{ v->
            if(count >= 100){
                count -=100;
                findViewById<Button>(R.id.showIcon).visibility = View.INVISIBLE;
                findViewById<Button>(R.id.tap).visibility = View.INVISIBLE;
                findViewById<ImageButton>(R.id.pops).visibility = View.VISIBLE;
            }else{
                Toast.makeText(this,"NOT ENOUGH POINTS",Toast.LENGTH_SHORT).show()
            }


        }
        findViewById<Button>(R.id.themeShow).setOnClickListener{v->
            findViewById<ImageView>(R.id.regShow).visibility = View.VISIBLE;
            findViewById<TextView>(R.id.Points).setTextColor(Color.WHITE)
            findViewById<TextView>(R.id.Points).setBackgroundColor(Color.BLACK)

            findViewById<TextView>(R.id.nextLevel).setTextColor(Color.WHITE)
            findViewById<TextView>(R.id.nextLevel).setBackgroundColor(Color.BLACK)

            findViewById<TextView>(R.id.tpts).setTextColor(Color.WHITE)
            findViewById<TextView>(R.id.tpts).setBackgroundColor(Color.BLACK)

            findViewById<TextView>(R.id.goalText).setTextColor(Color.WHITE)
            findViewById<TextView>(R.id.goalText).setBackgroundColor(Color.BLACK)
        }
        findViewById<Button>(R.id.tap).setOnClickListener{ v->
            count+=multiplier;
            total+=multiplier;
            findViewById<TextView>(R.id.Points).setText("Points: $count")
            findViewById<TextView>(R.id.tpts).setText("Total: $total")
            if(total >= goal){
                level++;
                goal+= 100*(level+1)
                Toast.makeText(this,"You reached the goal!",Toast.LENGTH_LONG).show()
                findViewById<TextView>(R.id.goalText).text = "GOAL $level: $goal"
            }

        }
        findViewById<ImageButton>(R.id.pops).setOnClickListener{ v->
            count+=multiplier;
            total+=multiplier;
            findViewById<TextView>(R.id.Points).setText("Points: $count")
            findViewById<TextView>(R.id.tpts).setText("Total: $total")
            if(total >= goal){
                level++;
                goal+= 100*(level+1)
                Toast.makeText(this,"You reached the goal!",Toast.LENGTH_LONG).show()
                findViewById<TextView>(R.id.goalText).text = "GOAL $level: $goal"
            }

        }
        var cost = 100;
        findViewById<Button>(R.id.upgrade).setOnClickListener{v->
            if (count >= cost) {
                multiplier+=1;
                count -=cost;
                cost *=2
                findViewById<TextView>(R.id.upgrade).setText("$cost")
                findViewById<TextView>(R.id.Points).setText("Points: $count")
                findViewById<Button>(R.id.tap).text = "TAP x ${multiplier}"
                Toast.makeText(this,"TAP UPGRADED", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"NOT ENOUGH POINTS!", Toast.LENGTH_LONG).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }
}