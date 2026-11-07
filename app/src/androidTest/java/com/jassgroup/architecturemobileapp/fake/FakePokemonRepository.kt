package com.jassgroup.architecturemobileapp.fake

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto
import com.jassgroup.architecturemobileapp.data.repository.PokemonRepository
import java.util.Locale

/**
 * Repositorio Fake de Pokémon para Pruebas Instrumentales UI.
 */
class FakePokemonRepository(
    private val pokemonListResult: Result<List<PokemonEntryDto>> = Result.success(emptyList()),
    private val pokemonDetailMap: Map<String, Result<PokemonDetailDto>> = emptyMap()
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<PokemonEntryDto>> {
        return pokemonListResult
    }

    override suspend fun getPokemonDetail(idOrName: String): Result<PokemonDetailDto> {
        return pokemonDetailMap[idOrName.lowercase(Locale.getDefault())]
            ?: Result.failure(RuntimeException("Pokémon $idOrName no encontrado"))
    }
}
