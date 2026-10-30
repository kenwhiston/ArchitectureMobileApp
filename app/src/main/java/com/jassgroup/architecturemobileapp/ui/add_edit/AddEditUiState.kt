package com.jassgroup.architecturemobileapp.ui.add_edit

/**
 * Estado UI inmutable para la pantalla de Creación y Edición de Tareas.
 */
data class AddEditUiState(
    val taskId: Int = 0,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val isEditing: Boolean = false,
    val isTitleError: Boolean = false,
    val titleErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isTaskSaved: Boolean = false
)
