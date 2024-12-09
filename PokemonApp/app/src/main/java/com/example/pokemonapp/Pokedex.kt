package com.example.pokemonapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Pokedex: Fragment(){
    private var pokemons =  mutableListOf<Pokemon>()
    private lateinit var pokeRecyclerView: RecyclerView
    private lateinit var pokeAdapter: PokeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokedex, container, false)

        val layoutManager = LinearLayoutManager(context)
        pokeRecyclerView = view.findViewById(R.id.pokedex_recycler)
        pokeAdapter = PokeAdapter(view.context,pokemons)
        pokeRecyclerView.adapter = pokeAdapter

        pokeRecyclerView.layoutManager = LinearLayoutManager(view.context).also{
            val dividerItem = DividerItemDecoration(view.context, it.orientation)
            pokeRecyclerView.addItemDecoration(dividerItem)
        }

        lifecycleScope.launch(Dispatchers.IO){
            val pokeApplication = requireActivity().application as PokeApplication

            pokeApplication.db.pokeDao().getAll().collect{ dbList ->
                val mappedList = dbList.map{ entity ->
                    Pokemon(
                        entity.sprite,
                        entity.name,
                        entity.type,
                        entity.health,
                        entity.attack,
                        entity.defense,
                        entity.date
                    )
                }
                launch(Dispatchers.Main) {
                    pokemons.clear()
                    pokemons.addAll(mappedList)
                    pokeAdapter.notifyDataSetChanged()
                }
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    companion object {
        fun newInstance(): Pokedex{
            return Pokedex()
        }

    }
}