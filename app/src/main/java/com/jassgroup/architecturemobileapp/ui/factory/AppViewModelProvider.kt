package com.jassgroup.architecturemobileapp.ui.factory

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jassgroup.architecturemobileapp.TaskApplication
import com.jassgroup.architecturemobileapp.ui.add_edit.AddEditViewModel
import com.jassgroup.architecturemobileapp.ui.home.HomeViewModel

/**
 * Fábrica de ViewModels centralizada.
 *
 * Permite inyectar las dependencias necesarias (como [TaskRepository])
 * en los constructores de los ViewModels sin requerir frameworks de DI adicionales.
 */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        // Inicializador para HomeViewModel
        initializer {
            HomeViewModel(
                repository = taskApplication().repository
            )
        }

        // Inicializador para AddEditViewModel
        initializer {
            AddEditViewModel(
                repository = taskApplication().repository
            )
        }
    }
}

/**
 * Función de extensión para obtener [TaskApplication] desde [CreationExtras].
 */
fun CreationExtras.taskApplication(): TaskApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)
}
