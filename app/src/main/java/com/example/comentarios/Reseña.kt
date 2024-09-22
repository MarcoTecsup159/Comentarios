package com.example.comentarios

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reseña (
    @PrimaryKey(autoGenerate = true) val ui: Int = 0,
    @ColumnInfo(name = "bus") val bus: String?,
    @ColumnInfo(name = "comentario ") val comentario: String?,
    @ColumnInfo(name = "calificacion") val calificacion: Float
)