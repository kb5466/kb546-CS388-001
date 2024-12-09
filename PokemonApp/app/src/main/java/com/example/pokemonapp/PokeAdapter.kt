package com.example.pokemonapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val POKEMON_EXTRA = "pokemon_extra"
private const val TAG = "PokeAdapter"
class PokeAdapter(private val context: Context, private val pokemons: List<Pokemon>) :
    RecyclerView.Adapter<PokeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pokemon_item, parent,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val item_sprite = itemView.findViewById<ImageView>(R.id.image_item)
        val item_name = itemView.findViewById<TextView>(R.id.item_name)
        val item_type = itemView.findViewById<TextView>(R.id.item_type)
        val item_date = itemView.findViewById<TextView>(R.id.item_date)
        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val pokemon = pokemons[absoluteAdapterPosition]
            val intent = Intent(context, Inspect::class.java)
            intent.putExtra(POKEMON_EXTRA, pokemon)
            context.startActivity(intent)
        }

    }

    override fun onBindViewHolder(holder: PokeAdapter.ViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.item_name.text = pokemon.name
        holder.item_type.text = pokemon.type
        holder.item_date.text = pokemon.date
        Glide.with(holder.itemView)
            .load(pokemon.sprite)
            .centerInside()
            .into(holder.item_sprite)
    }

    override fun getItemCount() = pokemons.size


}