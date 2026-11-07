package com.jassgroup.architecturemobileapp.ui.pokemon.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.jassgroup.architecturemobileapp.data.remote.model.NamedApiResource
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.StatSlotDto
import com.jassgroup.architecturemobileapp.data.remote.model.TypeSlotDto
import com.jassgroup.architecturemobileapp.fake.FakePokemonRepository
import com.jassgroup.architecturemobileapp.ui.theme.ArchitectureMobileAppTheme
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas instrumentales de UI para [PokemonDetailScreen].
 */
class PokemonDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pokemonDetailScreen_muestraInformacionYEstadisticasDelPokemon() {
        val sampleDetail = PokemonDetailDto(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            types = listOf(
                TypeSlotDto(1, NamedApiResource("electric", ""))
            ),
            stats = listOf(
                StatSlotDto(35, NamedApiResource("hp", "")),
                StatSlotDto(55, NamedApiResource("attack", ""))
            ),
            sprites = null
        )

        val repository = FakePokemonRepository(
            pokemonDetailMap = mapOf("pikachu" to Result.success(sampleDetail))
        )
        val viewModel = PokemonDetailViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                PokemonDetailScreen(
                    pokemonIdOrName = "pikachu",
                    viewModel = viewModel,
                    onNavigateBack = {}
                )
            }
        }

        // Dado que "Pikachu" aparece en la TopBar y en la tarjeta del detalle, verificamos los nodos coincidentes
        composeTestRule.onAllNodesWithText("Pikachu")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("#025").assertIsDisplayed()
        composeTestRule.onNodeWithText("Electric").assertIsDisplayed()
        composeTestRule.onNodeWithText("0.4 m").assertIsDisplayed()
        composeTestRule.onNodeWithText("6.0 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Estadísticas Base").assertIsDisplayed()
        composeTestRule.onNodeWithText("HP").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ataque").assertIsDisplayed()
    }
}
