package com.example.pokemonapp

import java.io.Serializable

data class Pokemon(
    val sprite: String?,
    val name: String?,
    val type: String?,
    val health: String?,
    val attack: String?,
    val defense: String?,
    val date: String? = null
):Serializable

