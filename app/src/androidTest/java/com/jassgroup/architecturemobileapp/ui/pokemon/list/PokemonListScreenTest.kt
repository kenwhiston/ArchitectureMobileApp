package com.jassgroup.architecturemobileapp.ui.pokemon.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto
import com.jassgroup.architecturemobileapp.fake.FakePokemonRepository
import com.jassgroup.architecturemobileapp.ui.theme.ArchitectureMobileAppTheme
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas instrumentales de UI para [PokemonListScreen].
 */
class PokemonListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pokemonListScreen_muestraTituloYTarjetasEnGrid() {
        val sampleList = listOf(
            PokemonEntryDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonEntryDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/")
        )
        val repository = FakePokemonRepository(pokemonListResult = Result.success(sampleList))
        val viewModel = PokemonListViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                PokemonListScreen(
                    viewModel = viewModel,
                    onNavigateBack = {},
                    onNavigateToDetail = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Pokédex (PokéAPI)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charmander").assertIsDisplayed()
    }

    @Test
    fun pokemonListScreen_filtradoEnBuscadorOcultaItemsQueNoCoinciden() {
        val sampleList = listOf(
            PokemonEntryDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonEntryDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/")
        )
        val repository = FakePokemonRepository(pokemonListResult = Result.success(sampleList))
        val viewModel = PokemonListViewModel(repository)

        composeTestRule.setContent {
            ArchitectureMobileAppTheme {
                PokemonListScreen(
                    viewModel = viewModel,
                    onNavigateBack = {},
                    onNavigateToDetail = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Buscar Pokémon por nombre o N°...").performTextInput("bulba")
        composeTestRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeTestRule.onNodeWithText("Charmander").assertDoesNotExist()
    }
}
