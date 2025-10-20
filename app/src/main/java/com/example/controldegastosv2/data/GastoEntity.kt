package com.example.controldegastosv2.data


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "gastos")
data class GastoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val fecha: Long,
    val fotoUri: String? = null
)