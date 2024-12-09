package com.example.pokemonapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface pokeDao {
    @Query("SELECT COUNT(*) FROM poke_db")
    fun getCount(): Int

    @Query("SELECT * FROM poke_db")
    fun getAll(): Flow<List<pokeEntity>>

    @Insert
    fun insert(pokemon:pokeEntity)

    @Query("DELETE FROM poke_db WHERE name = :pokemonName")
    fun delete(pokemonName: String)
}