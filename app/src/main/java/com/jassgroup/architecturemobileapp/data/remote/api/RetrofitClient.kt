package com.jassgroup.architecturemobileapp.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Singleton de Retrofit configurado para conectarse a PokéAPI.
 */
object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val pokeApiService: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }
}
