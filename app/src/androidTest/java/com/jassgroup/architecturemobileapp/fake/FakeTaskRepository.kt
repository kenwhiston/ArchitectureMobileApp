package com.jassgroup.architecturemobileapp.fake

import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Repositorio Fake de Tareas para Pruebas Instrumentales UI.
 */
class FakeTaskRepository(
    initialTasks: List<TaskEntity> = emptyList()
) : TaskRepository {

    private val tasksFlow = MutableStateFlow(initialTasks)

    override fun getAllTasks(): Flow<List<TaskEntity>> = tasksFlow

    override fun getTaskById(id: Int): Flow<TaskEntity?> {
        return tasksFlow.map { list -> list.find { it.id == id } }
    }

    override suspend fun insertTask(task: TaskEntity) {
        tasksFlow.update { current ->
            val newId = if (task.id == 0) (current.maxOfOrNull { it.id } ?: 0) + 1 else task.id
            current + task.copy(id = newId)
        }
    }

    override suspend fun updateTask(task: TaskEntity) {
        tasksFlow.update { current ->
            current.map { if (it.id == task.id) task else it }
        }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasksFlow.update { current ->
            current.filterNot { it.id == task.id }
        }
    }

    override suspend fun deleteTaskById(id: Int) {
        tasksFlow.update { current ->
            current.filterNot { it.id == id }
        }
    }
}
