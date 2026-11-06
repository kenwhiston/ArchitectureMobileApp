package com.jassgroup.architecturemobileapp.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import com.jassgroup.architecturemobileapp.ui.theme.ArchitectureMobileAppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas instrumentales para [HomeScreen].
 */
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_muestraTituloYEstadoVacio() {
        val repository: TaskRepository = mockk()
        every { repository.getAllTasks() } returns flowOf(emptyList())
        val viewModel = HomeViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAddTask = {},
                    onNavigateToEditTask = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Mis Tareas (MVVM)").assertIsDisplayed()
        composeTestRule.onNodeWithText("¡No tienes tareas pendientes!").assertIsDisplayed()
    }

    @Test
    fun homeScreen_muestraListaDeTareas() {
        val tasks = listOf(
            TaskEntity(id = 1, title = "Tarea A"),
            TaskEntity(id = 2, title = "Tarea B")
        )
        val repository: TaskRepository = mockk()
        every { repository.getAllTasks() } returns flowOf(tasks)
        val viewModel = HomeViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAddTask = {},
                    onNavigateToEditTask = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Tarea A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tarea B").assertIsDisplayed()
    }

    @Test
    fun clicEnFAB_ejecutaCallback() {
        val repository: TaskRepository = mockk()
        every { repository.getAllTasks() } returns flowOf(emptyList())
        val viewModel = HomeViewModel(repository)
        var fabClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAddTask = { fabClicked = true },
                    onNavigateToEditTask = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Agregar Tarea").performClick()
        assertTrue(fabClicked)
    }
}
