package com.example.pokemonapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.compose.ui.window.application
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.Headers
import org.json.JSONException

/*
* Where users are brought when they first open the app
*
* */
class firstEnco : Fragment() {
    private val pokedex: Fragment = Pokedex()
    private val starterImgList: MutableList<ImageView> = mutableListOf()
    private val starterPokemons: MutableList<Pokemon> = mutableListOf()
    private val pokeList = arrayListOf(1, 4, 7, 10, 16, 25)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_enco, container, false)

        // Getting all ImageViews
        for (i in 1..6) {
            val resId = resources.getIdentifier("enco$i", "id", "com.example.pokemonapp")
            val imageView = view.findViewById<ImageView>(resId)
            starterImgList.add(imageView)
        }

        // Safe to call displayPokemon here
        displayPokemon()

        starterImgList.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (index < starterPokemons.size) {
                        val pokeEntry = starterPokemons[index]
                        val app = requireActivity().application as? PokeApplication
                        app?.db?.pokeDao()?.insert(
                            pokeEntity(
                                sprite = pokeEntry.sprite,
                                name = pokeEntry.name,
                                health = pokeEntry.health,
                                attack = pokeEntry.attack,
                                defense = pokeEntry.defense,
                                type = pokeEntry.type
                            )
                        )
                        val fragmentTransaction = parentFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_layout, pokedex)
                        fragmentTransaction.addToBackStack(null) // Optional, allows navigating back
                        fragmentTransaction.commit()
                    }
                }
            }
        }
        return view
    }

    private fun displayPokemon() {
        for (i in pokeList.indices) {
            val pokeEntry = pokeList[i]
            getPokemon(pokeEntry) { pokeInfo ->
                if (pokeInfo.isNotEmpty()) {
                    val pokemon = Pokemon(
                        sprite = pokeInfo[0] as String,
                        name = pokeInfo[1] as String,
                        type = pokeInfo[2] as String,
                        health = pokeInfo[3] as String,
                        attack = pokeInfo[4] as String,
                        defense = pokeInfo[5] as String
                    )
                    starterPokemons.add(pokemon)

                    if (i < starterImgList.size) {
                        val spriteUrl = pokeInfo[0] as String
                        starterImgList[i].let { imageView ->
                            context?.let { context ->
                                Glide.with(context)
                                    .load(spriteUrl)
                                    .into(imageView)
                            }
                        }
                    }
                } else {
                    Log.e("DisplayPokemon", "Failed to fetch data for Pokémon ID: $pokeEntry")
                }
            }
        }
    }

    private fun getPokemon(pokeEntry: Int, callback: (ArrayList<Any>) -> Unit) {
        val pokeInfo = arrayListOf<Any>()
        val url = "https://pokeapi.co/api/v2/pokemon/$pokeEntry"
        val client = AsyncHttpClient()

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e("FAIL", "Failed to fetch data: $response")
                callback(pokeInfo)
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try {
                    val hpJSON = json.jsonObject.getJSONArray("stats")
                        .getJSONObject(0).getInt("base_stat").toString()
                    val atkJSON = json.jsonObject.getJSONArray("stats")
                        .getJSONObject(1).getInt("base_stat").toString()
                    val defJSON = json.jsonObject.getJSONArray("stats")
                        .getJSONObject(2).getInt("base_stat").toString()
                    val name = json.jsonObject.getString("name")
                    val sprite = json.jsonObject.getJSONObject("sprites").getString("front_default")
                    val typeJSON = json.jsonObject.getJSONArray("types")
                    val typeString = (0 until typeJSON.length()).joinToString("/") { index ->
                        typeJSON.getJSONObject(index).getJSONObject("type").getString("name")
                    }

                    pokeInfo.add(sprite)
                    pokeInfo.add(name)
                    pokeInfo.add(typeString)
                    pokeInfo.add(hpJSON)
                    pokeInfo.add(atkJSON)
                    pokeInfo.add(defJSON)

                    callback(pokeInfo)
                } catch (e: JSONException) {
                    Log.e("Error", "Parsing error", e)
                    callback(pokeInfo)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(): firstEnco {
            return firstEnco()
        }
    }
}
