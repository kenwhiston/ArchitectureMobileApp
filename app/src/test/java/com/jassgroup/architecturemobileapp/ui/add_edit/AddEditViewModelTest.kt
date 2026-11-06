package com.jassgroup.architecturemobileapp.ui.add_edit

import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [AddEditViewModel].
 */
class AddEditViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: TaskRepository
    private lateinit var viewModel: AddEditViewModel

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = AddEditViewModel(repository)
    }

    @Test
    fun `onTitleChanged actualiza el titulo en el estado y limpia errores`() = runTest {
        viewModel.onTitleChanged("Nuevo Título")

        val state = viewModel.uiState.value
        assertEquals("Nuevo Título", state.title)
        assertFalse(state.isTitleError)
    }

    @Test
    fun `onDescriptionChanged actualiza la descripcion en el estado`() = runTest {
        viewModel.onDescriptionChanged("Nueva Descripción")

        val state = viewModel.uiState.value
        assertEquals("Nueva Descripción", state.description)
    }

    @Test
    fun `loadTask carga la tarea existente y precarga los datos en el estado`() = runTest {
        val existingTask = TaskEntity(id = 5, title = "Tarea A Editar", description = "Desc Previa", isCompleted = true)
        every { repository.getTaskById(5) } returns flowOf(existingTask)

        viewModel.loadTask(5)

        val state = viewModel.uiState.value
        assertEquals(5, state.taskId)
        assertEquals("Tarea A Editar", state.title)
        assertEquals("Desc Previa", state.description)
        assertTrue(state.isCompleted)
        assertTrue(state.isEditing)
        assertFalse(state.isLoading)
    }

    @Test
    fun `saveTask con titulo vacio activa isTitleError`() = runTest {
        viewModel.onTitleChanged("   ")
        viewModel.saveTask()

        val state = viewModel.uiState.value
        assertTrue(state.isTitleError)
        assertEquals("El título de la tarea no puede estar vacío", state.titleErrorMessage)
        assertFalse(state.isTaskSaved)
    }

    @Test
    fun `saveTask con titulo valido crea nueva tarea cuando no esta editando`() = runTest {
        viewModel.onTitleChanged("Comprar leche")
        viewModel.onDescriptionChanged("Supermercado")
        viewModel.saveTask()

        val state = viewModel.uiState.value
        assertTrue(state.isTaskSaved)

        coVerify(exactly = 1) {
            repository.insertTask(
                match {
                    it.title == "Comprar leche" && it.description == "Supermercado" && it.id == 0
                }
            )
        }
    }

    @Test
    fun `saveTask actualiza tarea existente cuando esta en modo edicion`() = runTest {
        val existingTask = TaskEntity(id = 10, title = "Título Antiguo", description = "Desc Antigua")
        every { repository.getTaskById(10) } returns flowOf(existingTask)

        viewModel.loadTask(10)
        viewModel.onTitleChanged("Título Modificado")
        viewModel.saveTask()

        val state = viewModel.uiState.value
        assertTrue(state.isTaskSaved)

        coVerify(exactly = 1) {
            repository.updateTask(
                match {
                    it.id == 10 && it.title == "Título Modificado"
                }
            )
        }
    }
}
