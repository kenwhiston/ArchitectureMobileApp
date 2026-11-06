package com.jassgroup.architecturemobileapp.data.repository

import com.jassgroup.architecturemobileapp.data.remote.api.PokeApiService
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto

/**
 * Implementación de [PokemonRepository] que consulta la PokéAPI utilizando [PokeApiService].
 */
class PokemonRepositoryImpl(
    private val apiService: PokeApiService
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<PokemonEntryDto>> {
        return runCatching {
            apiService.getPokemonList(limit, offset).results
        }
    }

    override suspend fun getPokemonDetail(idOrName: String): Result<PokemonDetailDto> {
        return runCatching {
            apiService.getPokemonDetail(idOrName)
        }
    }
}
