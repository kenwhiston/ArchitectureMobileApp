package com.jassgroup.architecturemobileapp.ui.pokemon.list

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto
import com.jassgroup.architecturemobileapp.data.repository.PokemonRepository
import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [PokemonListViewModel].
 */
class PokemonListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PokemonRepository
    private lateinit var viewModel: PokemonListViewModel

    private val samplePokemonList = listOf(
        PokemonEntryDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
        PokemonEntryDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/"),
        PokemonEntryDto("squirtle", "https://pokeapi.co/api/v2/pokemon/7/")
    )

    @Before
    fun setUp() {
        repository = mockk()
    }

    @Test
    fun `loadPokemonList actualiza uiState a Success con los datos cargados`() = runTest {
        coEvery { repository.getPokemonList(151, 0) } returns Result.success(samplePokemonList)

        viewModel = PokemonListViewModel(repository)

        val state = viewModel.uiState.value
        assertTrue(state is PokemonListUiState.Success)
        val successState = state as PokemonListUiState.Success
        assertEquals(3, successState.pokemonList.size)
        assertEquals(3, successState.filteredList.size)
    }

    @Test
    fun `loadPokemonList actualiza uiState a Error en caso de fallo en la API`() = runTest {
        coEvery { repository.getPokemonList(151, 0) } returns Result.failure(RuntimeException("Error de red"))

        viewModel = PokemonListViewModel(repository)

        val state = viewModel.uiState.value
        assertTrue(state is PokemonListUiState.Error)
        assertEquals("Error de red", (state as PokemonListUiState.Error).message)
    }

    @Test
    fun `onSearchQueryChanged filtra la lista por nombre correctamente`() = runTest {
        coEvery { repository.getPokemonList(151, 0) } returns Result.success(samplePokemonList)

        viewModel = PokemonListViewModel(repository)
        viewModel.onSearchQueryChanged("char")

        val state = viewModel.uiState.value
        assertTrue(state is PokemonListUiState.Success)
        val successState = state as PokemonListUiState.Success
        assertEquals(1, successState.filteredList.size)
        assertEquals("char", successState.searchQuery)
        assertEquals("charmander", successState.filteredList[0].name)
    }

    @Test
    fun `onSearchQueryChanged filtra la lista por ID de pokemon`() = runTest {
        coEvery { repository.getPokemonList(151, 0) } returns Result.success(samplePokemonList)

        viewModel = PokemonListViewModel(repository)
        viewModel.onSearchQueryChanged("7")

        val state = viewModel.uiState.value
        assertTrue(state is PokemonListUiState.Success)
        val successState = state as PokemonListUiState.Success
        assertEquals(1, successState.filteredList.size)
        assertEquals("squirtle", successState.filteredList[0].name)
    }
}
