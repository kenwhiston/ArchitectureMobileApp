package com.jassgroup.architecturemobileapp.data.repository

import com.jassgroup.architecturemobileapp.data.local.dao.TaskDao
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * Implementación del repositorio [TaskRepository] que delega las llamadas al [TaskDao].
 */
class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    override fun getTaskById(id: Int): Flow<TaskEntity?> {
        return taskDao.getTaskById(id)
    }

    override suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    override suspend fun deleteTaskById(id: Int) {
        taskDao.deleteTaskById(id)
    }
}
