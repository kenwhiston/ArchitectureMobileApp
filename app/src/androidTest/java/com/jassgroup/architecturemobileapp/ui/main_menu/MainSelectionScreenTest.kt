package com.jassgroup.architecturemobileapp.ui.main_menu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jassgroup.architecturemobileapp.ui.theme.ArchitectureMobileAppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas instrumentales para [MainSelectionScreen].
 */
class MainSelectionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainSelectionScreen_muestraTitulosYTarjetas() {
        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = {},
                    onNavigateToPokemonDemo = {},
                    onNavigateToCameraDemo = {},
                    onNavigateToFirebaseDemo = {},
                    onNavigateToNotificationsDemo = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Demos de Arquitectura Android").assertIsDisplayed()
        composeTestRule.onNodeWithText("Demo Tareas / Notas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Demo Pokédex PokéAPI").assertIsDisplayed()
        composeTestRule.onNodeWithText("Demo Cámara CameraX").assertIsDisplayed()
        composeTestRule.onNodeWithText("Batería Demos Firebase").assertIsDisplayed()
        composeTestRule.onNodeWithText("Demo Notificaciones").assertIsDisplayed()
    }

    @Test
    fun clicEnTarjetaTareas_ejecutaCallback() {
        var tasksClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = { tasksClicked = true },
                    onNavigateToPokemonDemo = {},
                    onNavigateToCameraDemo = {},
                    onNavigateToFirebaseDemo = {},
                    onNavigateToNotificationsDemo = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Demo Tareas / Notas").performClick()
        assertTrue(tasksClicked)
    }

    @Test
    fun clicEnTarjetaPokemon_ejecutaCallback() {
        var pokemonClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = {},
                    onNavigateToPokemonDemo = { pokemonClicked = true },
                    onNavigateToCameraDemo = {},
                    onNavigateToFirebaseDemo = {},
                    onNavigateToNotificationsDemo = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Demo Pokédex PokéAPI").performClick()
        assertTrue(pokemonClicked)
    }

    @Test
    fun clicEnTarjetaCamara_ejecutaCallback() {
        var cameraClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = {},
                    onNavigateToPokemonDemo = {},
                    onNavigateToCameraDemo = { cameraClicked = true },
                    onNavigateToFirebaseDemo = {},
                    onNavigateToNotificationsDemo = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Demo Cámara CameraX").performClick()
        assertTrue(cameraClicked)
    }

    @Test
    fun clicEnTarjetaFirebase_ejecutaCallback() {
        var firebaseClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = {},
                    onNavigateToPokemonDemo = {},
                    onNavigateToCameraDemo = {},
                    onNavigateToFirebaseDemo = { firebaseClicked = true },
                    onNavigateToNotificationsDemo = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Batería Demos Firebase").performClick()
        assertTrue(firebaseClicked)
    }

    @Test
    fun clicEnTarjetaNotificaciones_ejecutaCallback() {
        var notificationsClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = {},
                    onNavigateToPokemonDemo = {},
                    onNavigateToCameraDemo = {},
                    onNavigateToFirebaseDemo = {},
                    onNavigateToNotificationsDemo = { notificationsClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Demo Notificaciones").performClick()
        assertTrue(notificationsClicked)
    }
}
