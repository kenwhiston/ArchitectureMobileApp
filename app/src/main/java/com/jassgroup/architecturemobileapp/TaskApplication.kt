package com.jassgroup.architecturemobileapp

import android.app.Application
import com.jassgroup.architecturemobileapp.data.local.database.TaskDatabase
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import com.jassgroup.architecturemobileapp.data.repository.TaskRepositoryImpl

/**
 * Clase Application personalizada para la aplicación.
 *
 * Se utiliza para instanciar la base de datos [TaskDatabase] y el repositorio [TaskRepository]
 * al iniciar la aplicación, asegurando que existan como instancias únicas en el ciclo de vida del proceso.
 */
class TaskApplication : Application() {

    // Instancia perezosa (lazy) de la base de datos
    val database: TaskDatabase by lazy {
        TaskDatabase.getDatabase(this)
    }

    // Instancia perezosa (lazy) del repositorio
    val repository: TaskRepository by lazy {
        TaskRepositoryImpl(database.taskDao())
    }
}
