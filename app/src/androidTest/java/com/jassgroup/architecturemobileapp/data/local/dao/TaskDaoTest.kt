package com.jassgroup.architecturemobileapp.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jassgroup.architecturemobileapp.data.local.database.TaskDatabase
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de integración instrumentation para [TaskDao] utilizando Room en memoria.
 */
@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var database: TaskDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertTaskAndGetAllTasks() = runBlocking {
        val task1 = TaskEntity(id = 1, title = "Primera Tarea", createdAt = 1000)
        val task2 = TaskEntity(id = 2, title = "Segunda Tarea", createdAt = 2000)

        taskDao.insertTask(task1)
        taskDao.insertTask(task2)

        val tasks = taskDao.getAllTasks().first()

        assertEquals(2, tasks.size)
        // Ordenadas descendentemente por createdAt (task2 primero)
        assertEquals("Segunda Tarea", tasks[0].title)
        assertEquals("Primera Tarea", tasks[1].title)
    }

    @Test
    fun getTaskByIdReturnsCorrectTask() = runBlocking {
        val task = TaskEntity(id = 10, title = "Tarea ID 10", description = "Descripción Detallada")
        taskDao.insertTask(task)

        val retrievedTask = taskDao.getTaskById(10).first()

        assertEquals(10, retrievedTask?.id)
        assertEquals("Tarea ID 10", retrievedTask?.title)
        assertEquals("Descripción Detallada", retrievedTask?.description)
    }

    @Test
    fun updateTaskModifiesExistingRecord() = runBlocking {
        val task = TaskEntity(id = 5, title = "Original", isCompleted = false)
        taskDao.insertTask(task)

        val updatedTask = task.copy(title = "Modificado", isCompleted = true)
        taskDao.updateTask(updatedTask)

        val result = taskDao.getTaskById(5).first()

        assertEquals("Modificado", result?.title)
        assertTrue(result?.isCompleted == true)
    }

    @Test
    fun deleteTaskRemovesItemFromDatabase() = runBlocking {
        val task = TaskEntity(id = 3, title = "Para Eliminar")
        taskDao.insertTask(task)

        taskDao.deleteTask(task)

        val result = taskDao.getTaskById(3).first()
        assertNull(result)
    }

    @Test
    fun deleteTaskByIdRemovesItemByUniqueId() = runBlocking {
        val task = TaskEntity(id = 8, title = "Eliminar por ID")
        taskDao.insertTask(task)

        taskDao.deleteTaskById(8)

        val result = taskDao.getTaskById(8).first()
        assertNull(result)
    }

    @Test
    fun getAllTitlesFiltersTitlesCorrectly() = runBlocking {
        taskDao.insertTask(TaskEntity(id = 1, title = "1"))
        taskDao.insertTask(TaskEntity(id = 2, title = "2"))
        taskDao.insertTask(TaskEntity(id = 3, title = "Otra Tarea"))

        val titles = taskDao.getAllTitles().first()

        assertEquals(2, titles.size)
        assertTrue(titles.contains("1"))
        assertTrue(titles.contains("2"))
    }
}
