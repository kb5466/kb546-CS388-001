package com.example.pokemonapp

import android.app.Application

class PokeApplication: Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}