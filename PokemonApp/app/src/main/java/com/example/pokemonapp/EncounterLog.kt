package com.example.pokemonapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EncounterLog: Fragment() {
    private var pokemons = encounterLog
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
        val view = inflater.inflate(R.layout.fragment_encounter_log, container, false)

        pokeRecyclerView = view.findViewById(R.id.log_view)
        pokeAdapter = PokeAdapter(view.context,pokemons)
        pokeRecyclerView.adapter = pokeAdapter

        pokeRecyclerView.layoutManager = LinearLayoutManager(view.context).also{
            val dividerItem = DividerItemDecoration(view.context, it.orientation)
            pokeRecyclerView.addItemDecoration(dividerItem)
        }
        pokeAdapter.notifyDataSetChanged()


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