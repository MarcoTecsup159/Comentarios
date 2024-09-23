package com.example.comentarios

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReseñaDao {
    @Query("SELECT * FROM Reseña")
    suspend fun getAll(): List<Reseña>

    @Query("DELETE FROM Reseña WHERE ui = :id")
    suspend fun deleteById(id: Int)

    @Update
    suspend fun update(reseña: Reseña)

    @Insert
    suspend fun insert(reseña: Reseña)}