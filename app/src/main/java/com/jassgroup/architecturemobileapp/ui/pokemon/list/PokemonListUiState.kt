package com.jassgroup.architecturemobileapp.ui.pokemon.list

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonEntryDto

/**
 * Estado UI inmutable para la pantalla de Listado de Pokémon.
 */
sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data class Success(
        val pokemonList: List<PokemonEntryDto> = emptyList(),
        val searchQuery: String = "",
        val filteredList: List<PokemonEntryDto> = emptyList()
    ) : PokemonListUiState
    data class Error(val message: String) : PokemonListUiState
}
