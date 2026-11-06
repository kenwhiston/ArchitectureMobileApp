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
import com.jassgroup.architecturemobileapp.ui.camera.CameraScreen
import com.jassgroup.architecturemobileapp.ui.camera.CameraViewModel
import com.jassgroup.architecturemobileapp.ui.factory.AppViewModelProvider
import com.jassgroup.architecturemobileapp.ui.home.HomeScreen
import com.jassgroup.architecturemobileapp.ui.home.HomeViewModel
import com.jassgroup.architecturemobileapp.ui.main_menu.MainSelectionScreen
import com.jassgroup.architecturemobileapp.ui.pokemon.detail.PokemonDetailScreen
import com.jassgroup.architecturemobileapp.ui.pokemon.detail.PokemonDetailViewModel
import com.jassgroup.architecturemobileapp.ui.pokemon.list.PokemonListScreen
import com.jassgroup.architecturemobileapp.ui.pokemon.list.PokemonListViewModel

/**
 * Grafo de navegación central de la aplicación.
 *
 * Administra el flujo entre el Menú Principal y los tres demos:
 * 1. Demo Tareas / Notas (Room DB + Flow)
 * 2. Demo Pokédex (PokéAPI + Retrofit + Coil)
 * 3. Demo Cámara (CameraX + Jetpack Compose)
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route,
        modifier = modifier
    ) {
        // --- MENÚ PRINCIPAL ---
        composable(route = Screen.MainMenu.route) {
            MainSelectionScreen(
                onNavigateToTasksDemo = {
                    navController.navigate(Screen.Home.route)
                },
                onNavigateToPokemonDemo = {
                    navController.navigate(Screen.PokemonList.route)
                },
                onNavigateToCameraDemo = {
                    navController.navigate(Screen.CameraDemo.route)
                }
            )
        }

        // --- DEMO 1: TAREAS (ROOM DB) ---

        // Listado de Tareas
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddTask.route)
                },
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.EditTask.createRoute(taskId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Agregar Nueva Tarea
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

        // Editar Tarea Existente
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

        // --- DEMO 2: POKÉAPI (RETROFIT + COIL) ---

        // Listado de Pokémon (Grid)
        composable(route = Screen.PokemonList.route) {
            val pokemonListViewModel: PokemonListViewModel = viewModel(factory = AppViewModelProvider.Factory)
            PokemonListScreen(
                viewModel = pokemonListViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetail = { pokemonIdOrName ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonIdOrName))
                }
            )
        }

        // Detalle de Pokémon
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument(Screen.POKEMON_ID_ARG) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val pokemonIdOrName = backStackEntry.arguments?.getString(Screen.POKEMON_ID_ARG) ?: ""
            val pokemonDetailViewModel: PokemonDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
            PokemonDetailScreen(
                pokemonIdOrName = pokemonIdOrName,
                viewModel = pokemonDetailViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // --- DEMO 3: CAMERAX (CÁMARA Y CAPTURA DE FOTOS) ---
        composable(route = Screen.CameraDemo.route) {
            val cameraViewModel: CameraViewModel = viewModel(factory = AppViewModelProvider.Factory)
            CameraScreen(
                viewModel = cameraViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
