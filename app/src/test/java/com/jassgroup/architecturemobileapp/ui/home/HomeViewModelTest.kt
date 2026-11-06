package com.jassgroup.architecturemobileapp.ui.home

import app.cash.turbine.test
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [HomeViewModel].
 */
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: TaskRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
    }

    @Test
    fun `uiState emite Success con las tareas cargadas del repositorio`() = runTest {
        val tasks = listOf(
            TaskEntity(id = 1, title = "Tarea 1", isCompleted = false),
            TaskEntity(id = 2, title = "Tarea 2", isCompleted = true)
        )
        every { repository.getAllTasks() } returns flowOf(tasks)

        viewModel = HomeViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Success)
            val successState = state as HomeUiState.Success
            assertEquals(2, successState.tasks.size)
            assertEquals("Tarea 1", successState.tasks[0].title)
        }
    }

    @Test
    fun `toggleTaskCompletion llama a updateTask invirtiendo isCompleted`() = runTest {
        every { repository.getAllTasks() } returns flowOf(emptyList())
        viewModel = HomeViewModel(repository)

        val task = TaskEntity(id = 1, title = "Tarea Test", isCompleted = false)
        viewModel.toggleTaskCompletion(task)

        coVerify(exactly = 1) {
            repository.updateTask(task.copy(isCompleted = true))
        }
    }

    @Test
    fun `deleteTask llama a deleteTask en el repositorio`() = runTest {
        every { repository.getAllTasks() } returns flowOf(emptyList())
        viewModel = HomeViewModel(repository)

        val task = TaskEntity(id = 5, title = "Tarea Eliminar")
        viewModel.deleteTask(task)

        coVerify(exactly = 1) {
            repository.deleteTask(task)
        }
    }
}
