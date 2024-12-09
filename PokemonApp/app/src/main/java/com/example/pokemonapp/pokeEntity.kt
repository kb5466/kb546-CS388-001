package com.example.pokemonapp

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "poke_db")
data class pokeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "sprite") val sprite: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "hp") val health: String?,
    @ColumnInfo(name = "attack") val attack: String?,
    @ColumnInfo(name = "defense") val defense: String?,
    @ColumnInfo(name= "type")val type: String?,
    @ColumnInfo(name = "encounter-date")val date: String = getCurrentDate()
){
    companion object {
        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return dateFormat.format(Calendar.getInstance().time)
        }
    }
}
