package com.jassgroup.architecturemobileapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jassgroup.architecturemobileapp.data.local.entity.TaskEntity
import com.jassgroup.architecturemobileapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla Home.
 *
 * En MVVM:
 * Gestiona el estado de la UI ([uiState]) y procesa las intenciones/eventos del usuario
 * (marcar tarea como completada, eliminar tarea, etc.).
 */
class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    /**
     * Estado observable expuesto a la Compose UI como un [StateFlow].
     *
     * Transforma el [Flow] continuo del repositorio en [HomeUiState].
     * `SharingStarted.WhileSubscribed(5000)` mantiene el flujo activo durante
     * cambios de configuración (rotación) sin desperdiciar recursos.
     */
    val uiState: StateFlow<HomeUiState> = repository.getAllTasks()
        .map <List<TaskEntity>, HomeUiState> { tasks ->
            HomeUiState.Success(tasks)
        }
        .catch { throwable ->
            emit(HomeUiState.Error(throwable.localizedMessage ?: "Error al cargar las tareas"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    /**
     * Alterna el estado de completado de una tarea (Check/Uncheck).
     */
    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    /**
     * Elimina una tarea de la base de datos Room.
     */
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
