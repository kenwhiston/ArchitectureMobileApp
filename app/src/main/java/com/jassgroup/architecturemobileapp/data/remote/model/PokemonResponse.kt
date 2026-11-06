package com.jassgroup.architecturemobileapp.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Respuesta del endpoint de listado: GET /api/v2/pokemon?limit=151&offset=0
 */
data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonEntryDto>
)

/**
 * Representa un ítem básico en el listado de Pokémon.
 */
data class PokemonEntryDto(
    val name: String,
    val url: String
) {
    /**
     * Extrae el ID del Pokémon a partir de su URL.
     * Ejemplo: "https://pokeapi.co/api/v2/pokemon/25/" -> ID 25
     */
    fun getId(): Int {
        return url.trimEnd('/').split('/').lastOrNull()?.toIntOrNull() ?: 0
    }

    /**
     * Retorna la URL de la imagen de arte oficial (Official Artwork) de alta calidad.
     */
    fun getImageUrl(): String {
        val id = getId()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }
}

/**
 * Respuesta del endpoint de detalle: GET /api/v2/pokemon/{id_or_name}
 */
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlotDto>,
    val stats: List<StatSlotDto>,
    val sprites: SpritesDto?
) {
    /**
     * Retorna la URL de la imagen oficial del Pokémon.
     */
    fun getImageUrl(): String {
        return sprites?.other?.officialArtwork?.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }
}

data class TypeSlotDto(
    val slot: Int,
    val type: NamedApiResource
)

data class StatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedApiResource
)

data class NamedApiResource(
    val name: String,
    val url: String
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @SerializedName("front_default") val frontDefault: String?
)
