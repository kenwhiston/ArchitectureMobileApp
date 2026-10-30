package com.jassgroup.architecturemobileapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa la tabla 'tasks' en la base de datos local SQLite.
 *
 * En la arquitectura MVVM:
 * Esta clase forma parte del Modelo (Model), definiendo la estructura de datos
 * que se almacenará de manera persistente.
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    // Clave primaria autogenerada por SQLite/Room
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Título de la tarea (obligatorio)
    val title: String,

    // Descripción detallada de la tarea (opcional)
    val description: String = "",

    // Estado de finalización de la tarea (true si ya fue realizada)
    val isCompleted: Boolean = false,

    // Fecha de creación representada en milisegundos (System.currentTimeMillis())
    val createdAt: Long = System.currentTimeMillis()
)
