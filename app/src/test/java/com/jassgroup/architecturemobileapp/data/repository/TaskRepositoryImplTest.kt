package com.jassgroup.architecturemobileapp.data.repository

import app.cash.turbine.test
import com.jassgroup.architecturemobileapp.data.local.dao.TaskDao
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para [TaskRepositoryImpl].
 */
class TaskRepositoryImplTest {

    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp() {
        taskDao = mockk(relaxed = true)
        repository = TaskRepositoryImpl(taskDao)
    }

    @Test
    fun `getAllTasks emite la lista de tareas obtenida del DAO`() = runTest {
        val sampleTasks = listOf(
            TaskEntity(id = 1, title = "Tarea 1", description = "Desc 1"),
            TaskEntity(id = 2, title = "Tarea 2", description = "Desc 2")
        )
        every { taskDao.getAllTasks() } returns flowOf(sampleTasks)

        repository.getAllTasks().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("Tarea 1", result[0].title)
            awaitComplete()
        }

        verify(exactly = 1) { taskDao.getAllTasks() }
    }

    @Test
    fun `getTaskById emite la tarea correspondiente del DAO`() = runTest {
        val sampleTask = TaskEntity(id = 10, title = "Tarea 10")
        every { taskDao.getTaskById(10) } returns flowOf(sampleTask)

        repository.getTaskById(10).test {
            val result = awaitItem()
            assertEquals("Tarea 10", result?.title)
            awaitComplete()
        }

        verify(exactly = 1) { taskDao.getTaskById(10) }
    }

    @Test
    fun `insertTask delega la insercion al DAO`() = runTest {
        val task = TaskEntity(title = "Nueva Tarea")

        repository.insertTask(task)

        coVerify(exactly = 1) { taskDao.insertTask(task) }
    }

    @Test
    fun `updateTask delega la actualizacion al DAO`() = runTest {
        val task = TaskEntity(id = 1, title = "Tarea Editada")

        repository.updateTask(task)

        coVerify(exactly = 1) { taskDao.updateTask(task) }
    }

    @Test
    fun `deleteTask delega la eliminacion al DAO`() = runTest {
        val task = TaskEntity(id = 1, title = "Tarea a eliminar")

        repository.deleteTask(task)

        coVerify(exactly = 1) { taskDao.deleteTask(task) }
    }

    @Test
    fun `deleteTaskById delega la eliminacion por ID al DAO`() = runTest {
        repository.deleteTaskById(5)

        coVerify(exactly = 1) { taskDao.deleteTaskById(5) }
    }
}
