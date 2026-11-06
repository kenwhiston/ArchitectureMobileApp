package com.jassgroup.architecturemobileapp.ui.pokemon.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jassgroup.architecturemobileapp.data.remote.model.PokemonDetailDto
import com.jassgroup.architecturemobileapp.data.remote.model.StatSlotDto
import java.util.Locale

/**
 * Pantalla de Detalle del Pokémon.
 *
 * Muestra imagen oficial, tipos elemento con colores personalizados, altura, peso y estadísticas base con barras de progreso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonIdOrName: String,
    viewModel: PokemonDetailViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonIdOrName) {
        viewModel.loadPokemonDetail(pokemonIdOrName)
    }

    val capitalizedTitle = pokemonIdOrName.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(capitalizedTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is PokemonDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PokemonDetailUiState.Success -> {
                    PokemonDetailContent(pokemon = state.pokemon)
                }
                is PokemonDetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadPokemonDetail(pokemonIdOrName) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Contenido principal de la pantalla de detalle de Pokémon.
 */
@Composable
private fun PokemonDetailContent(
    pokemon: PokemonDetailDto
) {
    val formattedId = String.format(Locale.getDefault(), "#%03d", pokemon.id)
    val capitalizedName = pokemon.name.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tarjeta con la imagen y el ID
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = formattedId,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.End)
                )

                AsyncImage(
                    model = pokemon.getImageUrl(),
                    contentDescription = capitalizedName,
                    modifier = Modifier
                        .size(200.dp)
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = capitalizedName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tipos de Pokémon (Badges con colores)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            pokemon.types.forEach { typeSlot ->
                val typeName = typeSlot.type.name
                TypeBadge(typeName = typeName)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ficha técnica (Altura y Peso)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoCard(title = "Altura", value = "${pokemon.height / 10.0} m")
            InfoCard(title = "Peso", value = "${pokemon.weight / 10.0} kg")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de Estadísticas Base (Base Stats)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estadísticas Base",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                pokemon.stats.forEach { statSlot ->
                    StatBarItem(stat = statSlot)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Chip/Badge para el Tipo de Pokémon con color representativo.
 */
@Composable
private fun TypeBadge(typeName: String) {
    val color = getTypeColor(typeName)
    val capitalizedTypeName = typeName.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = capitalizedTypeName,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

/**
 * Ficha informativa para la altura y peso.
 */
@Composable
private fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Barra de progreso para una estadística individual (HP, Ataque, Defensa, etc.).
 */
@Composable
private fun StatBarItem(stat: StatSlotDto) {
    val maxStatValue = 200f
    val progress = (stat.baseStat / maxStatValue).coerceIn(0f, 1f)
    val statLabel = getStatLabel(stat.stat.name)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = statLabel,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.width(90.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stat.baseStat.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ),
            color = if (stat.baseStat >= 90) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

/**
 * Mapeo de nombre de tipo a su color tradicional en la franquicia Pokémon.
 */
private fun getTypeColor(typeName: String): Color {
    return when (typeName.lowercase(Locale.getDefault())) {
        "grass" -> Color(0xFF78C850)
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "electric" -> Color(0xFFF8D030)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "flying" -> Color(0xFFA890F0)
        "bug" -> Color(0xFFA8B820)
        "normal" -> Color(0xFFA8A878)
        "fairy" -> Color(0xFFEE99AC)
        "fighting" -> Color(0xFFC03028)
        "psychic" -> Color(0xFFF85888)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "dragon" -> Color(0xFF7038F8)
        "steel" -> Color(0xFFB8B8D0)
        "ice" -> Color(0xFF98D8D8)
        "dark" -> Color(0xFF705848)
        else -> Color(0xFF68A090)
    }
}

/**
 * Traduce el nombre técnico del stat a un nombre corto y legible.
 */
private fun getStatLabel(statName: String): String {
    return when (statName.lowercase(Locale.getDefault())) {
        "hp" -> "HP"
        "attack" -> "Ataque"
        "defense" -> "Defensa"
        "special-attack" -> "Sp. Atk"
        "special-defense" -> "Sp. Def"
        "speed" -> "Velocidad"
        else -> statName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}
