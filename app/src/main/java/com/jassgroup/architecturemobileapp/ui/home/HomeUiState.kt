package com.jassgroup.architecturemobileapp.ui.home

import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity

/**
 * Estado UI inmutable para la pantalla Home.
 *
 * Implementa el patrón Unidirectional Data Flow (UDF).
 */
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val tasks: List<TaskEntity> = emptyList()) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
