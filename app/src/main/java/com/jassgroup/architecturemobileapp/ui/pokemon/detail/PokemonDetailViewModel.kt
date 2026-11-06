package com.jassgroup.architecturemobileapp.ui.pokemon.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jassgroup.architecturemobileapp.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para el detalle de un Pokémon.
 *
 * Carga las estadísticas, tipos, peso y altura mediante [PokemonRepository].
 */
class PokemonDetailViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga el detalle del Pokémon dado su ID o nombre.
     */
    fun loadPokemonDetail(pokemonIdOrName: String) {
        if (pokemonIdOrName.isBlank()) return

        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            repository.getPokemonDetail(pokemonIdOrName)
                .onSuccess { detail ->
                    _uiState.value = PokemonDetailUiState.Success(detail)
                }
                .onFailure { throwable ->
                    _uiState.value = PokemonDetailUiState.Error(
                        throwable.localizedMessage ?: "Error al cargar la información del Pokémon"
                    )
                }
        }
    }
}
