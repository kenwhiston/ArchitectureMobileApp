package com.jassgroup.architecturemobileapp.ui.navigation

/**
 * Definición de las rutas de navegación de la aplicación.
 *
 * En Navigation Compose, cada pantalla tiene una ruta ("route") única.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddTask : Screen("add_task")
    data object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int): String = "edit_task/$taskId"
    }

    companion object {
        const val TASK_ID_ARG = "taskId"
    }
}
