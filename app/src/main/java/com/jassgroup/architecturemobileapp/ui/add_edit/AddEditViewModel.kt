package com.jassgroup.architecturemobileapp.ui.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para las pantallas de Agregar y Editar Tarea.
 *
 * Maneja la lógica de negocio de validación de campos y persistencia
 * en Room utilizando el [TaskRepository].
 */
class AddEditViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    /**
     * Carga una tarea existente por su ID para entrar en modo Edición.
     */
    fun loadTask(taskId: Int) {
        if (taskId <= 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val task = repository.getTaskById(taskId).firstOrNull()
            if (task != null) {
                _uiState.update {
                    it.copy(
                        taskId = task.id,
                        title = task.title,
                        description = task.description,
                        isCompleted = task.isCompleted,
                        isEditing = true,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Actualiza el valor del título en el estado.
     */
    fun onTitleChanged(newTitle: String) {
        _uiState.update {
            it.copy(
                title = newTitle,
                isTitleError = false,
                titleErrorMessage = null
            )
        }
    }

    /**
     * Actualiza la descripción en el estado.
     */
    fun onDescriptionChanged(newDescription: String) {
        _uiState.update {
            it.copy(description = newDescription)
        }
    }

    /**
     * Valida los datos e inserta o actualiza la tarea en Room.
     */
    fun saveTask() {
        val currentTitle = _uiState.value.title.trim()

        if (currentTitle.isEmpty()) {
            _uiState.update {
                it.copy(
                    isTitleError = true,
                    titleErrorMessage = "El título de la tarea no puede estar vacío"
                )
            }
            return
        }

        viewModelScope.launch {
            val currentState = _uiState.value
            val taskToSave = TaskEntity(
                id = if (currentState.isEditing) currentState.taskId else 0,
                title = currentTitle,
                description = currentState.description.trim(),
                isCompleted = currentState.isCompleted
            )

            if (currentState.isEditing) {
                repository.updateTask(taskToSave)
            } else {
                repository.insertTask(taskToSave)
            }

            _uiState.update { it.copy(isTaskSaved = true) }
        }
    }
}
