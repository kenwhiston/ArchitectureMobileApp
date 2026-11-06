package com.jassgroup.architecturemobileapp.ui.navigation

/**
 * Definición de las rutas de navegación para todas las pantallas de los demos.
 */
sealed class Screen(val route: String) {
    // Menú Principal
    data object MainMenu : Screen("main_menu")

    // Demo Tareas (Room)
    data object Home : Screen("tasks_home")
    data object AddTask : Screen("add_task")
    data object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int): String = "edit_task/$taskId"
    }

    // Demo Pokédex (PokéAPI)
    data object PokemonList : Screen("pokemon_list")
    data object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: String): String = "pokemon_detail/$pokemonId"
    }

    // Demo Cámara (CameraX)
    data object CameraDemo : Screen("camera_demo")

    // Demos de Firebase Services
    data object FirebaseMenu : Screen("firebase_menu")
    data object FirebaseAuth : Screen("firebase_auth")
    data object Firestore : Screen("firebase_firestore")
    data object RemoteConfig : Screen("firebase_remote_config")
    data object Crashlytics : Screen("firebase_crashlytics")
    data object FirebaseAi : Screen("firebase_ai")

    companion object {
        const val TASK_ID_ARG = "taskId"
        const val POKEMON_ID_ARG = "pokemonId"
    }
}
