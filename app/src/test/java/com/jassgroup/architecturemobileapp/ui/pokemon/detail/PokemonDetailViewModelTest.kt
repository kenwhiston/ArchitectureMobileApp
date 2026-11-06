package com.jassgroup.architecturemobileapp.ui.pokemon.detail

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
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
 * Pruebas unitarias para [PokemonDetailViewModel].
 */
class PokemonDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PokemonRepository
    private lateinit var viewModel: PokemonDetailViewModel

    private val sampleDetail = PokemonDetailDto(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        types = emptyList(),
        stats = emptyList(),
        sprites = null
    )

    @Before
    fun setUp() {
        repository = mockk()
        viewModel = PokemonDetailViewModel(repository)
    }

    @Test
    fun `loadPokemonDetail actualiza uiState a Success con el detalle obtenido`() = runTest {
        coEvery { repository.getPokemonDetail("pikachu") } returns Result.success(sampleDetail)

        viewModel.loadPokemonDetail("pikachu")

        val state = viewModel.uiState.value
        assertTrue(state is PokemonDetailUiState.Success)
        val successState = state as PokemonDetailUiState.Success
        assertEquals("pikachu", successState.pokemon.name)
        assertEquals(25, successState.pokemon.id)
    }

    @Test
    fun `loadPokemonDetail actualiza uiState a Error en caso de falla`() = runTest {
        coEvery { repository.getPokemonDetail("desconocido") } returns Result.failure(RuntimeException("Pokemon no encontrado"))

        viewModel.loadPokemonDetail("desconocido")

        val state = viewModel.uiState.value
        assertTrue(state is PokemonDetailUiState.Error)
        assertEquals("Pokemon no encontrado", (state as PokemonDetailUiState.Error).message)
    }
}
