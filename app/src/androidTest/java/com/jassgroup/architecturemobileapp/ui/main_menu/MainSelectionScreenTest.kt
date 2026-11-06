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
                    onNavigateToPokemonDemo = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Demos de Arquitectura Android").assertIsDisplayed()
        composeTestRule.onNodeWithText("Demo Tareas / Notas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Demo Pokédex PokéAPI").assertIsDisplayed()
    }

    @Test
    fun clicEnTarjetaTareas_ejecutaCallback() {
        var tasksClicked = false

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                MainSelectionScreen(
                    onNavigateToTasksDemo = { tasksClicked = true },
                    onNavigateToPokemonDemo = {}
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
                    onNavigateToPokemonDemo = { pokemonClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Demo Pokédex PokéAPI").performClick()
        assertTrue(pokemonClicked)
    }
}
