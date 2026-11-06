package com.jassgroup.architecturemobileapp

import android.app.Application
import com.jassgroup.architecturemobileapp.data.local.database.TaskDatabase
import com.jassgroup.architecturemobileapp.data.remote.api.RetrofitClient
import com.jassgroup.architecturemobileapp.data.repository.PokemonRepository
import com.jassgroup.architecturemobileapp.data.repository.PokemonRepositoryImpl
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import com.jassgroup.architecturemobileapp.data.repository.TaskRepositoryImpl

/**
 * Clase Application personalizada para la aplicación.
 *
 * Mantiene las instancias únicas del Repositorio de Tareas (Room) y del Repositorio de Pokémon (PokéAPI).
 */
class TaskApplication : Application() {

    // Instancia perezosa de la base de datos de tareas
    val database: TaskDatabase by lazy {
        TaskDatabase.getDatabase(this)
    }

    // Repositorio de tareas (Room)
    val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(database.taskDao())
    }

    // Alias 'repository' para mantener compatibilidad
    val repository: TaskRepository get() = taskRepository

    // Repositorio de Pokémon (PokéAPI)
    val pokemonRepository: PokemonRepository by lazy {
        PokemonRepositoryImpl(RetrofitClient.pokeApiService)
    }
}
