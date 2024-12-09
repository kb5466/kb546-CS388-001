package com.example.pokemonapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [pokeEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun pokeDao(): pokeDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "poke_db"
            )
                .fallbackToDestructiveMigration() // Automatically handle schema changes by recreating the database
                .build()
    }

}