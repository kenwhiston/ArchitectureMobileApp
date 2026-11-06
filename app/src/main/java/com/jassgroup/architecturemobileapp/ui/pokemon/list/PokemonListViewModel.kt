package com.jassgroup.architecturemobileapp.ui.pokemon.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jassgroup.architecturemobileapp.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para el listado de Pokémon.
 *
 * Administra el estado de carga desde [PokemonRepository] y soporta filtrado por búsqueda.
 */
class PokemonListViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    /**
     * Carga la lista inicial de Pokémon desde PokéAPI.
     */
    fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.value = PokemonListUiState.Loading
            repository.getPokemonList(limit = 151, offset = 0)
                .onSuccess { list ->
                    _uiState.value = PokemonListUiState.Success(
                        pokemonList = list,
                        filteredList = list
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = PokemonListUiState.Error(
                        throwable.localizedMessage ?: "Error de conexión al cargar PokéAPI"
                    )
                }
        }
    }

    /**
     * Filtra la lista según el texto ingresado en la barra de búsqueda.
     */
    fun onSearchQueryChanged(query: String) {
        val currentState = _uiState.value
        if (currentState is PokemonListUiState.Success) {
            val filtered = if (query.isBlank()) {
                currentState.pokemonList
            } else {
                currentState.pokemonList.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true) ||
                    pokemon.getId().toString() == query.trim()
                }
            }
            _uiState.value = currentState.copy(
                searchQuery = query,
                filteredList = filtered
            )
        }
    }
}
