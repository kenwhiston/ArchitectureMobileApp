package com.jassgroup.architecturemobileapp.data.repository

import com.jassgroup.architecturemobileapp.data.remote.api.PokeApiService
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonListResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para [PokemonRepositoryImpl].
 */
class PokemonRepositoryImplTest {

    private lateinit var apiService: PokeApiService
    private lateinit var repository: PokemonRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        repository = PokemonRepositoryImpl(apiService)
    }

    @Test
    fun `getPokemonList retorna Result success con la lista de pokemon`() = runTest {
        val sampleResponse = PokemonListResponse(
            count = 151,
            next = null,
            previous = null,
            results = listOf(
                PokemonEntryDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                PokemonEntryDto("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/")
            )
        )
        coEvery { apiService.getPokemonList(151, 0) } returns sampleResponse

        val result = repository.getPokemonList(151, 0)

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("bulbasaur", result.getOrNull()?.get(0)?.name)
    }

    @Test
    fun `getPokemonList retorna Result failure cuando ocurre una excepcion`() = runTest {
        coEvery { apiService.getPokemonList(151, 0) } throws RuntimeException("Error de conexion")

        val result = repository.getPokemonList(151, 0)

        assertTrue(result.isFailure)
        assertEquals("Error de conexion", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getPokemonDetail retorna Result success con el detalle del pokemon`() = runTest {
        val sampleDetail = PokemonDetailDto(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            types = emptyList(),
            stats = emptyList(),
            sprites = null
        )
        coEvery { apiService.getPokemonDetail("pikachu") } returns sampleDetail

        val result = repository.getPokemonDetail("pikachu")

        assertTrue(result.isSuccess)
        assertEquals(25, result.getOrNull()?.id)
        assertEquals("pikachu", result.getOrNull()?.name)
    }

    @Test
    fun `getPokemonDetail retorna Result failure cuando falla la API`() = runTest {
        coEvery { apiService.getPokemonDetail("desconocido") } throws RuntimeException("404 Not Found")

        val result = repository.getPokemonDetail("desconocido")

        assertTrue(result.isFailure)
        assertEquals("404 Not Found", result.exceptionOrNull()?.message)
    }
}
