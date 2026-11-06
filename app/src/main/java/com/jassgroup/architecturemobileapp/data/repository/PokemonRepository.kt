package com.jassgroup.architecturemobileapp.data.repository

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto

/**
 * Interfaz del Repositorio de Pokémon.
 *
 * En MVVM:
 * Centraliza la obtención de datos remotos mediante [Result] para un manejo
 * seguro de éxitos y errores.
 */
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int = 151, offset: Int = 0): Result<List<PokemonEntryDto>>
    suspend fun getPokemonDetail(idOrName: String): Result<PokemonDetailDto>
}
