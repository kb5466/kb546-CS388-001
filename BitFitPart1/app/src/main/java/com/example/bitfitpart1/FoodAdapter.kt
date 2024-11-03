package com.example.bitfitpart1

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView

const val FOOD_EXTRA = "FOOD_EXTRA"
private const val TAG = "FoodAdapter"
class FoodAdapter(private val context: Context, private val foods: List<DisplayFood>) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.calory_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodAdapter.ViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)
    }

    override fun getItemCount() = foods.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        private val food_text = itemView.findViewById<TextView>(R.id.food_text)
        private val cal_text = itemView.findViewById<TextView>(R.id.cal_text)
        init{
            itemView.setOnClickListener(this)
        }
        fun bind(food: DisplayFood) {
            food_text.text = food.food_name
            cal_text.text = food.calory_num + "\n Calories"
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }
}