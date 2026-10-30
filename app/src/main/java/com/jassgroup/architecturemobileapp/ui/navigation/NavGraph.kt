package com.jassgroup.architecturemobileapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jassgroup.architecturemobileapp.ui.add_edit.AddEditTaskScreen
import com.jassgroup.architecturemobileapp.ui.add_edit.AddEditViewModel
import com.jassgroup.architecturemobileapp.ui.factory.AppViewModelProvider
import com.jassgroup.architecturemobileapp.ui.home.HomeScreen
import com.jassgroup.architecturemobileapp.ui.home.HomeViewModel

/**
 * Grafo de navegación central de la aplicación utilizando Navigation Compose.
 *
 * Administra el intercambio entre las 3 pantallas principales:
 * 1. Pantalla Home
 * 2. Pantalla Agregar Tarea
 * 3. Pantalla Editar Tarea (pasando taskId como argumento)
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Pantalla 1: Home (Listado de Tareas)
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddTask.route)
                },
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.EditTask.createRoute(taskId))
                }
            )
        }

        // Pantalla 2: Agregar Nueva Tarea
        composable(route = Screen.AddTask.route) {
            val addEditViewModel: AddEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
            AddEditTaskScreen(
                taskId = 0,
                viewModel = addEditViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla 3: Editar Tarea Existente (recibe el argumento taskId)
        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument(Screen.TASK_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt(Screen.TASK_ID_ARG) ?: 0
            val addEditViewModel: AddEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
            AddEditTaskScreen(
                taskId = taskId,
                viewModel = addEditViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
