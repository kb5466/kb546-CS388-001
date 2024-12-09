package com.example.pokemonapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Headers
import org.json.JSONException

var encounterLog = mutableListOf<Pokemon>()
class Interaction : Fragment() {
    val pokedex: Fragment = Pokedex()
    private var pokemons = mutableListOf<Pokemon>()

    private lateinit var user_sprite: ImageView
    private lateinit var user_name: TextView
    private lateinit var user_hp: TextView

    private lateinit var enemy_sprite: ImageView
    private lateinit var enemy_name: TextView
    private lateinit var enemy_hp: TextView


    private lateinit var encounter_count: TextView
    private lateinit var battle_log_text: TextView
    private lateinit var attack_button: Button
    private lateinit var catch_button: Button
    private lateinit var run_button: Button

    var user_curr_hp = ""
    var user_max_hp = ""
    var enemy_curr_hp = ""
    var enemy_max_hp = ""

    var user_atk = ""
    var enemy_atk =""

    var user_def = ""
    var enemy_def = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_interaction, container, false)

        user_name = view.findViewById(R.id.user_name)
        user_hp = view.findViewById(R.id.user_hp)
        user_sprite = view.findViewById(R.id.user_sprite)

        enemy_name = view.findViewById(R.id.enemy_name)
        enemy_hp = view.findViewById(R.id.enemy_hp)
        enemy_sprite = view.findViewById(R.id.enemy_sprite)

        battle_log_text = view.findViewById(R.id.battle_log_text)
        encounter_count = view.findViewById(R.id.encounter_count)
        attack_button = view.findViewById(R.id.attack_button)
        catch_button = view.findViewById(R.id.catch_button)
        run_button = view.findViewById(R.id.run_button)
        // Retrieve 1 pokemon and use it in battle
        lifecycleScope.launch(Dispatchers.IO) {
            val pokeApplication = requireActivity().application as PokeApplication

            pokeApplication.db.pokeDao().getAll().collect { dbList ->
                val mappedList = dbList.map { entity ->
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
                    // Retrieve one random Pokémon
                    if (pokemons.isNotEmpty()) {
                        val randomPokemon = pokemons.random()
                        user_name.text = randomPokemon.name
                        user_curr_hp = randomPokemon.health.toString()
                        user_max_hp = randomPokemon.health.toString()
                        user_atk = randomPokemon.attack.toString()
                        user_def = randomPokemon.defense.toString()

                        user_hp.text = "HP: ${user_curr_hp}/${randomPokemon.health}"
                        context?.let { context ->
                            Glide.with(context)
                                .load(randomPokemon.sprite)
                                .into(user_sprite)
                        }
                    }
                }
            }
        }

        // Retrieve 1 random pokemon and add it to battle
        var randomId = (1..1025).random()
        lifecycleScope.launch(Dispatchers.IO){
            encounter_count.text = "Encounter Count: ${encounterLog.size}"
            getPokemon(randomId){ pokeInfo ->
                val enemyPoke = Pokemon(
                    sprite = pokeInfo[0] as String,
                    name = pokeInfo[1] as String,
                    type = pokeInfo[2] as String,
                    health = pokeInfo[3] as String,
                    attack = pokeInfo[4] as String,
                    defense = pokeInfo[5] as String
                )
                encounterLog.add(enemyPoke)
                enemy_curr_hp = enemyPoke.health.toString()
                enemy_max_hp = enemyPoke.health.toString()
                enemy_atk = enemyPoke.attack.toString()
                enemy_def = enemyPoke.defense.toString()
                lifecycleScope.launch(Dispatchers.Main) {
                    // Display Enemy Pokemon
                    context?.let { context ->
                        Glide.with(context)
                            .load(enemyPoke.sprite)
                            .into(enemy_sprite)
                    }
                    enemy_hp.text = "HP: ${enemy_curr_hp}/${enemyPoke.health}"
                    enemy_name.text = enemyPoke.name
                }
            }
        }
        attack_button.setOnClickListener{
            val critical_rate = listOf(1, 1, 1, 1, 2, 2, 2, 3,3).random()
            val user_damage = kotlin.math.round(((((2 * 2/5) *((user_atk.toInt() *(2.5))/(0.9*enemy_def.toInt()))) / 50 )+3)*critical_rate).toInt()
            val user_text = "${user_name.text} dealt $user_damage to ${enemy_name.text}"
            enemy_curr_hp = (enemy_curr_hp.toDouble() - user_damage).toInt().toString()
            enemy_hp.text = "HP: ${enemy_curr_hp}/${enemy_max_hp}"

            val enemy_critical_rate = listOf(1, 1, 1, 1, 1,2, 3, 3, 3, 3).random()
            val enemy_damage = kotlin.math.round(((((2 * 1/5) *((enemy_atk.toInt() *(2.25))/(user_def.toInt()))) / 50 )+3)*enemy_critical_rate).toInt()
            val enemy_text = "${enemy_name.text} dealt $enemy_damage to ${user_name.text}"
            user_curr_hp = (user_curr_hp.toDouble() - enemy_damage).toInt().toString()
            user_hp.text = "HP: ${user_curr_hp}/${user_max_hp}"

            val catchRate = ((3 * enemy_max_hp.toInt()) - (3 * enemy_curr_hp.toInt())) / (3 * enemy_max_hp.toInt().toDouble())
            val formatted = "%.2f".format(catchRate).toDouble()
            catch_button.text = "CATCH:${formatted * 100}%"


            battle_log_text.text = "Battle Log:\n${user_text}\n${enemy_text}"

            if(enemy_curr_hp.toInt() <= 0){
                battle_log_text.text = "Battle Log: ${enemy_name.text} fainted,\n prepare for another encounter!"

                randomId = (1..1025).random()
                lifecycleScope.launch(Dispatchers.IO){
                    encounter_count.text = "Encounter Count: ${encounterLog.size}"
                    getPokemon(randomId){ pokeInfo ->
                        val enemyPoke = Pokemon(
                            sprite = pokeInfo[0] as String,
                            name = pokeInfo[1] as String,
                            type = pokeInfo[2] as String,
                            health = pokeInfo[3] as String,
                            attack = pokeInfo[4] as String,
                            defense = pokeInfo[5] as String
                        )
                        encounterLog.add(enemyPoke)
                        enemy_curr_hp = enemyPoke.health.toString()
                        enemy_max_hp = enemyPoke.health.toString()
                        enemy_atk = enemyPoke.attack.toString()
                        enemy_def = enemyPoke.defense.toString()
                        lifecycleScope.launch(Dispatchers.Main) {
                            // Display Enemy Pokemon
                            context?.let { context ->
                                Glide.with(context)
                                    .load(enemyPoke.sprite)
                                    .into(enemy_sprite)
                            }
                            enemy_hp.text = "HP: ${enemy_curr_hp}/${enemyPoke.health}"
                            enemy_name.text = enemyPoke.name
                        }
                    }
                }
            }

            if(user_curr_hp.toInt() <= 0){
                lifecycleScope.launch(Dispatchers.IO) {
                    val pokeApplication = requireActivity().application as PokeApplication
                    pokeApplication.db.pokeDao().delete(user_name.text.toString())
                }
                battle_log_text.text = "Battle Log:\n${enemy_text}\n${user_name.text} fainted and left the party"
            }
        }

        run_button.setOnClickListener{
            battle_log_text.text = "Player ran away!"
            randomId = (1..1025).random()
            lifecycleScope.launch(Dispatchers.IO){
                encounter_count.text = "Encounter Count: ${encounterLog.size}"
                getPokemon(randomId){ pokeInfo ->
                    val enemyPoke = Pokemon(
                        sprite = pokeInfo[0] as String,
                        name = pokeInfo[1] as String,
                        type = pokeInfo[2] as String,
                        health = pokeInfo[3] as String,
                        attack = pokeInfo[4] as String,
                        defense = pokeInfo[5] as String
                    )
                    encounterLog.add(enemyPoke)
                    enemy_curr_hp = enemyPoke.health.toString()
                    enemy_max_hp = enemyPoke.health.toString()
                    enemy_atk = enemyPoke.attack.toString()
                    enemy_def = enemyPoke.defense.toString()
                    lifecycleScope.launch(Dispatchers.Main) {
                        // Display Enemy Pokemon
                        context?.let { context ->
                            Glide.with(context)
                                .load(enemyPoke.sprite)
                                .into(enemy_sprite)
                        }
                        enemy_hp.text = "HP: ${enemy_curr_hp}/${enemyPoke.health}"
                        enemy_name.text = enemyPoke.name
                    }
                }
            }
        }

        catch_button.setOnClickListener{
            val catchRate = ((3 * enemy_max_hp.toInt()) - (3 * enemy_curr_hp.toInt())) / (3 * enemy_max_hp.toInt().toDouble())
            val catchChance = (1..100).random()

            // If pokemon gets caught
            if(catchChance <= (catchRate * 100).toInt()){
                encounter_count.text = "Encounter Count: ${encounterLog.size}"
                getPokemon(randomId){ pokeInfo->
                    val pokemon = Pokemon(
                        sprite = pokeInfo[0] as String,
                        name = pokeInfo[1] as String,
                        type = pokeInfo[2] as String,
                        health = pokeInfo[3] as String,
                        attack = pokeInfo[4] as String,
                        defense = pokeInfo[5] as String
                    )
                    lifecycleScope.launch(Dispatchers.IO){
                        val pokeApplication = requireActivity().application as PokeApplication
                        pokeApplication.db.pokeDao().insert(
                            pokeEntity(
                                sprite = pokemon.sprite,
                                name = pokemon.name,
                                health = pokemon.health,
                                attack = pokemon.attack,
                                defense = pokemon.defense,
                                type = pokemon.type
                            )
                        )
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        pokemons.add(pokemon)
                        Toast.makeText(context, "${pokemon.name} was caught!", Toast.LENGTH_SHORT).show()
                    }

                }
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, pokedex)
                fragmentTransaction.addToBackStack(null) // Optional, allows navigating back
                fragmentTransaction.commit()

            }
            else{
                Toast.makeText(context, "The Pokémon escaped!", Toast.LENGTH_SHORT).show()
                val enemy_damage = kotlin.math.round(((((2 * 1/5) *((enemy_atk.toInt() *(2.25))/(user_def.toInt()))) / 50 )+3)).toInt()
                Toast.makeText(context,"${enemy_name.text} dealt $enemy_damage to ${user_name.text}",Toast.LENGTH_SHORT).show()
                user_curr_hp = (user_curr_hp.toDouble() - enemy_damage).toInt().toString()
                user_hp.text = "HP: ${user_curr_hp}/${user_max_hp}"

                val enemy_text = "${enemy_name.text} dealt $enemy_damage to ${user_name.text}"
                user_curr_hp = (user_curr_hp.toDouble() - enemy_damage).toInt().toString()
                user_hp.text = "HP: ${user_curr_hp}/${user_max_hp}"
                battle_log_text.text = "Battle Log:\nPlayer tried catching pokemon but escaped. \n ${enemy_text}"
            }


        }

        return view
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
        fun newInstance(param1: String, param2: String){

        }

    }
}