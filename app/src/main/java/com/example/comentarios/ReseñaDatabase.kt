package com.example.comentarios

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Reseña::class], version = 1)
abstract class ReseñaDatabase: RoomDatabase() {
    abstract fun reseñaDao(): ReseñaDao
}