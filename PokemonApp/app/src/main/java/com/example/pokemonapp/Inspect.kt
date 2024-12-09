package com.example.pokemonapp

import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class Inspect : AppCompatActivity(){
    private lateinit var inspectSprite: ImageView
    private lateinit var inspectName: TextView
    private lateinit var inspectType: TextView
    private lateinit var inspectHp: TextView
    private lateinit var inspectAtk: TextView
    private lateinit var inspectDef: TextView
    private lateinit var inspectDate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_inspect)

        inspectSprite = findViewById(R.id.poke_photo)
        inspectName = findViewById(R.id.poke_name)
        inspectType = findViewById(R.id.poke_type)
        inspectHp = findViewById(R.id.poke_hp)
        inspectAtk = findViewById(R.id.poke_atk)
        inspectDef = findViewById(R.id.poke_def)
        inspectDate = findViewById(R.id.poke_date)

        val poke = intent.getSerializableExtra(POKEMON_EXTRA) as Pokemon
        inspectName.text = poke.name
        inspectType.text = poke.type
        inspectHp.text = "HP: ${poke.health}"
        inspectAtk.text = "Atk: ${poke.attack}"
        inspectDef.text = "Def: ${poke.defense}"
        inspectDate.text = "Caught date: ${poke.date}"

        Glide.with(this)
            .load(poke.sprite)
            .into(inspectSprite)

    }
}