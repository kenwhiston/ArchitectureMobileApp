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
import com.jassgroup.architecturemobileapp.ui.firebase.ai.FirebaseAiScreen
import com.jassgroup.architecturemobileapp.ui.firebase.ai.FirebaseAiViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.auth.FirebaseAuthScreen
import com.jassgroup.architecturemobileapp.ui.firebase.auth.FirebaseAuthViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.crashlytics.CrashlyticsScreen
import com.jassgroup.architecturemobileapp.ui.firebase.crashlytics.CrashlyticsViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.firestore.FirestoreScreen
import com.jassgroup.architecturemobileapp.ui.firebase.firestore.FirestoreViewModel
import com.jassgroup.architecturemobileapp.ui.firebase.menu.FirebaseMenuScreen
import com.jassgroup.architecturemobileapp.ui.firebase.remote_config.RemoteConfigScreen
import com.jassgroup.architecturemobileapp.ui.firebase.remote_config.RemoteConfigViewModel
import com.jassgroup.architecturemobileapp.ui.home.HomeScreen
import com.jassgroup.architecturemobileapp.ui.home.HomeViewModel
import com.jassgroup.architecturemobileapp.ui.main_menu.MainSelectionScreen
import com.jassgroup.architecturemobileapp.ui.notifications.NotificationsScreen
import com.jassgroup.architecturemobileapp.ui.notifications.NotificationsViewModel
import com.jassgroup.architecturemobileapp.ui.pokemon.detail.PokemonDetailScreen
import com.jassgroup.architecturemobileapp.ui.pokemon.detail.PokemonDetailViewModel
import com.jassgroup.architecturemobileapp.ui.pokemon.list.PokemonListScreen
import com.jassgroup.architecturemobileapp.ui.pokemon.list.PokemonListViewModel

/**
 * Grafo de navegación central de la aplicación.
 *
 * Administra el flujo entre el Menú Principal y los cinco módulos:
 * 1. Demo Tareas / Notas (Room DB + Flow)
 * 2. Demo Pokédex (PokéAPI + Retrofit + Coil)
 * 3. Demo Cámara (CameraX + Jetpack Compose)
 * 4. Batería de Demos Firebase (Auth, Firestore, Remote Config, Crashlytics, AI Logic)
 * 5. Demo Notificaciones (NotificationCompat + Canales Múltiples)
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
                },
                onNavigateToFirebaseDemo = {
                    navController.navigate(Screen.FirebaseMenu.route)
                },
                onNavigateToNotificationsDemo = {
                    navController.navigate(Screen.NotificationsDemo.route)
                }
            )
        }

        // --- DEMO 1: TAREAS (ROOM DB) ---
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

        // --- DEMO 3: CAMERAX ---
        composable(route = Screen.CameraDemo.route) {
            val cameraViewModel: CameraViewModel = viewModel(factory = AppViewModelProvider.Factory)
            CameraScreen(
                viewModel = cameraViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // --- DEMO 4: BATERÍA DE DEMOS FIREBASE ---
        composable(route = Screen.FirebaseMenu.route) {
            FirebaseMenuScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAuth = { navController.navigate(Screen.FirebaseAuth.route) },
                onNavigateToFirestore = { navController.navigate(Screen.Firestore.route) },
                onNavigateToRemoteConfig = { navController.navigate(Screen.RemoteConfig.route) },
                onNavigateToCrashlytics = { navController.navigate(Screen.Crashlytics.route) },
                onNavigateToAi = { navController.navigate(Screen.FirebaseAi.route) }
            )
        }

        composable(route = Screen.FirebaseAuth.route) {
            val authViewModel: FirebaseAuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
            FirebaseAuthScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Firestore.route) {
            val firestoreViewModel: FirestoreViewModel = viewModel(factory = AppViewModelProvider.Factory)
            FirestoreScreen(
                viewModel = firestoreViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.RemoteConfig.route) {
            val remoteConfigViewModel: RemoteConfigViewModel = viewModel(factory = AppViewModelProvider.Factory)
            RemoteConfigScreen(
                viewModel = remoteConfigViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Crashlytics.route) {
            val crashlyticsViewModel: CrashlyticsViewModel = viewModel(factory = AppViewModelProvider.Factory)
            CrashlyticsScreen(
                viewModel = crashlyticsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.FirebaseAi.route) {
            val aiViewModel: FirebaseAiViewModel = viewModel(factory = AppViewModelProvider.Factory)
            FirebaseAiScreen(
                viewModel = aiViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // --- DEMO 5: NOTIFICACIONES (NOTIFICATIONCOMPAT) ---
        composable(route = Screen.NotificationsDemo.route) {
            val notificationsViewModel: NotificationsViewModel = viewModel(factory = AppViewModelProvider.Factory)
            NotificationsScreen(
                viewModel = notificationsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
