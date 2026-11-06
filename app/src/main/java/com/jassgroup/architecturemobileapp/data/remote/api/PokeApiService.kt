package com.jassgroup.architecturemobileapp.data.remote.api

import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz Retrofit para consumir la PokéAPI.
 *
 * En la arquitectura MVVM:
 * Abstrae las llamadas HTTP a la API REST de PokeAPI (https://pokeapi.co/api/v2/).
 */
interface PokeApiService {

    /**
     * Obtiene una lista paginada de Pokémon (por defecto los primeros 151 de la 1ª Generación).
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 151,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    /**
     * Obtiene el detalle completo de un Pokémon por su nombre o ID.
     */
    @GET("pokemon/{idOrName}")
    suspend fun getPokemonDetail(
        @Path("idOrName") idOrName: String
    ): PokemonDetailDto
}
