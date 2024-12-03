package com.example.pokemonapp

import com.google.gson.annotations.SerializedName

class pokemon (
    @SerializedName("name")
    val pokemon_name: String? = null,

    @SerializedName("stats")
    val statsList: List<Stats>,

    @SerializedName("types")
    val types: List<type>

)
data class Stats(
    @SerializedName("base_stat") val baseStat:Int?,
    @SerializedName("stat") val statList: List<stat>
)
data class stat(
    @SerializedName("name") val name: String?
)

data class type(
    @SerializedName("name")
    val name: String?
)
