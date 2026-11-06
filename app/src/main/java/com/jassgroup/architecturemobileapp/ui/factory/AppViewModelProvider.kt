package com.jassgroup.architecturemobileapp.ui.factory

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jassgroup.architecturemobileapp.TaskApplication
import com.jassgroup.architecturemobileapp.ui.add_edit.AddEditViewModel
import com.jassgroup.architecturemobileapp.ui.camera.CameraViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.ai.FirebaseAiViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.auth.FirebaseAuthViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.crashlytics.CrashlyticsViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.firestore.FirestoreViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.remote_config.RemoteConfigViewModel
import com.jassgroup.architecturemobileapp.ui.home.HomeViewModel
import com.jassgroup.architecturemobileapp.ui.pokemon.detail.PokemonDetailViewModel
import com.jassgroup.architecturemobileapp.ui.pokemon.list.PokemonListViewModel

/**
 * Fábrica de ViewModels centralizada.
 *
 * Inyecta los repositorios correspondientes en cada ViewModel.
 */
object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        // Inicializador para HomeViewModel (Demo Tareas)
        initializer {
            HomeViewModel(
                repository = taskApplication().taskRepository
            )
        }

        // Inicializador para AddEditViewModel (Demo Tareas)
        initializer {
            AddEditViewModel(
                repository = taskApplication().taskRepository
            )
        }

        // Inicializador para PokemonListViewModel (Demo PokéAPI)
        initializer {
            PokemonListViewModel(
                repository = taskApplication().pokemonRepository
            )
        }

        // Inicializador para PokemonDetailViewModel (Demo PokéAPI)
        initializer {
            PokemonDetailViewModel(
                repository = taskApplication().pokemonRepository
            )
        }

        // Inicializador para CameraViewModel (Demo CameraX)
        initializer {
            CameraViewModel()
        }

        // Inicializadores para ViewModels de Firebase
        initializer { FirebaseAuthViewModel() }
        initializer { FirestoreViewModel() }
        initializer { RemoteConfigViewModel() }
        initializer { CrashlyticsViewModel() }
        initializer { FirebaseAiViewModel() }
    }
}

/**
 * Función de extensión para obtener [TaskApplication] desde [CreationExtras].
 */
fun CreationExtras.taskApplication(): TaskApplication {
    return (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskApplication)
}
