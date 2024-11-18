package com.example.bitfitpart2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM Food_table")
    fun getAll(): Flow<List<FoodEntity>>

    @Insert
    fun insertAll(foods: List<FoodEntity>)

    @Insert
    fun insert(food: FoodEntity)

    @Query("DELETE FROM Food_table")
    fun deleteAll()

    @Query("SELECT MAX(calories) FROM Food_table")
    fun maxCalories(): Int

    @Query("SELECT MIN(calories) FROM Food_table")
    fun minCalories(): Int

    @Query("SELECT AVG(calories) FROM Food_table")
    fun avgCalories(): Double

    @Query("SELECT SUM(calories) FROM Food_table")
    fun totalCalories(): Int

}