package com.jassgroup.architecturemobileapp.data.repository

import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del Repositorio de Tareas.
 *
 * En la arquitectura MVVM:
 * El Repositorio actúa como punto único de acceso a los datos, desacoplando la capa
 * de presentación (ViewModels) del origen de datos concreto (Room, API REST, etc.).
 */
interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskEntity>>
    fun getTaskById(id: Int): Flow<TaskEntity?>
    suspend fun insertTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun deleteTaskById(id: Int)
}
