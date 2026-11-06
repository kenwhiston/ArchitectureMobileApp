package com.jassgroup.architecturemobileapp.ui.pokemon.detail

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto

/**
 * Estado UI inmutable para la pantalla de Detalle de Pokémon.
 */
sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val pokemon: PokemonDetailDto) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}
