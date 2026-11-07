package com.jassgroup.architecturemobileapp.ui.add_edit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jassgroup.architecturemobileapp.fake.FakeTaskRepository
import com.jassgroup.architecturemobileapp.ui.theme.ArchitectureMobileAppTheme
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas instrumentales de UI para [AddEditTaskScreen].
 */
class AddEditTaskScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addEditTaskScreen_modoCreacionMuestraTituloNuevaTarea() {
        val repository = FakeTaskRepository()
        val viewModel = AddEditViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                AddEditTaskScreen(
                    taskId = 0,
                    viewModel = viewModel,
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Nueva Tarea").assertIsDisplayed()
        composeTestRule.onNodeWithText("Guardar Tarea").assertIsDisplayed()
    }

    @Test
    fun addEditTaskScreen_permiteIngresarTextoEnCampos() {
        val repository = FakeTaskRepository()
        val viewModel = AddEditViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                AddEditTaskScreen(
                    taskId = 0,
                    viewModel = viewModel,
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Título de la tarea *").performTextInput("Estudiar Jetpack Compose")
        composeTestRule.onNodeWithText("Descripción (opcional)").performTextInput("Repasar StateFlow y UI Testing")

        composeTestRule.onNodeWithText("Estudiar Jetpack Compose").assertIsDisplayed()
        composeTestRule.onNodeWithText("Repasar StateFlow y UI Testing").assertIsDisplayed()
    }

    @Test
    fun clicEnGuardarSinTitulo_muestraMensajeDeError() {
        val repository = FakeTaskRepository()
        val viewModel = AddEditViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                AddEditTaskScreen(
                    taskId = 0,
                    viewModel = viewModel,
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Guardar Tarea").performClick()
        composeTestRule.onNodeWithText("El título de la tarea no puede estar vacío").assertIsDisplayed()
    }
}
