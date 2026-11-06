package com.jassgroup.architecturemobileapp.ui.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jassgroup.architecturemobileapp.ui.theme.ArchitectureMobileAppTheme
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas instrumentales de integración de la Navegación (End-to-End).
 */
class NavigationComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navegacionCompleta_menuPrincipalATareasYVolver() {
        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                AppNavGraph()
            }
        }

        // Pantalla de inicio
        composeTestRule.onNodeWithText("Demos de Arquitectura Android").assertIsDisplayed()

        // Navegar a Demo Tareas
        composeTestRule.onNodeWithText("Demo Tareas / Notas").performClick()
        composeTestRule.onNodeWithText("Mis Tareas (MVVM)").assertIsDisplayed()

        // Volver al Menú Principal
        composeTestRule.onNodeWithContentDescription("Menú Principal").performClick()
        composeTestRule.onNodeWithText("Demos de Arquitectura Android").assertIsDisplayed()
    }

    @Test
    fun navegacionCompleta_menuPrincipalAPokemonYVolver() {
        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                AppNavGraph()
            }
        }

        // Pantalla de inicio
        composeTestRule.onNodeWithText("Demos de Arquitectura Android").assertIsDisplayed()

        // Navegar a Demo Pokédex
        composeTestRule.onNodeWithText("Demo Pokédex PokéAPI").performClick()
        composeTestRule.onNodeWithText("Pokédex (PokéAPI)").assertIsDisplayed()

        // Volver al Menú Principal
        composeTestRule.onNodeWithContentDescription("Menú Principal").performClick()
        composeTestRule.onNodeWithText("Demos de Arquitectura Android").assertIsDisplayed()
    }
}
