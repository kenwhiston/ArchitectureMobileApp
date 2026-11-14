package com.jassgroup.architecturemobileapp.ui.main_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Pantalla principal del menú de selección de Demos.
 *
 * Permite al usuario elegir entre:
 * 1. Demo de Tareas (Room Database + Flow + MVVM)
 * 2. Demo Pokédex (PokéAPI + Retrofit + Coil + MVVM)
 * 3. Demo Cámara (CameraX + Jetpack Compose + MVVM)
 * 4. Batería de Demos Firebase (Auth, Firestore, Remote Config, Crashlytics, AI Logic)
 * 5. Demo Notificaciones (NotificationCompat + Canales Múltiples)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSelectionScreen(
    onNavigateToTasksDemo: () -> Unit,
    onNavigateToPokemonDemo: () -> Unit,
    onNavigateToCameraDemo: () -> Unit,
    onNavigateToFirebaseDemo: () -> Unit,
    onNavigateToNotificationsDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Demos de Arquitectura Android") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona un Demo para explorar:",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Todos los demos siguen la arquitectura recomendada por Google (MVVM + Repository + StateFlow + Navigation Compose).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Opción 1: Demo de Tareas / Notas (Room)
            DemoOptionCard(
                title = "Demo Tareas / Notas",
                subtitle = "Persistencia local con Room Database, Flow reactivo, gestos de deslizamiento (Swipe to Edit/Delete) y formularios.",
                icon = Icons.Default.Checklist,
                badgeText = "Room + MVVM",
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                onClick = onNavigateToTasksDemo
            )

            // Opción 2: Demo PokéAPI (Retrofit + Coil)
            DemoOptionCard(
                title = "Demo Pokédex PokéAPI",
                subtitle = "Consumo de API REST remota con Retrofit 2, carga asíncrona de imágenes de arte oficial con Coil, buscador en tiempo real y estadísticas.",
                icon = Icons.Default.CatchingPokemon,
                badgeText = "Retrofit + Coil + MVVM",
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                onClick = onNavigateToPokemonDemo
            )

            // Opción 3: Demo Cámara (CameraX)
            DemoOptionCard(
                title = "Demo Cámara CameraX",
                subtitle = "Uso de la librería Jetpack CameraX, vista previa en tiempo real (PreviewView), alternado de lentes frontal/trasera y captura de fotografías.",
                icon = Icons.Default.PhotoCamera,
                badgeText = "CameraX + Compose",
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                onClick = onNavigateToCameraDemo
            )

            // Opción 4: Batería de Demos Firebase
            DemoOptionCard(
                title = "Batería Demos Firebase",
                subtitle = "Catálogo completo con 5 funcionalidades: Auth (Correo/Gmail), Cloud Firestore (NoSQL), Remote Config, Crashlytics y Firebase AI Logic (Gemini).",
                icon = Icons.Default.LocalActivity,
                badgeText = "Firebase Services + Compose",
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                onClick = onNavigateToFirebaseDemo
            )

            // Opción 5: Demo Notificaciones
            DemoOptionCard(
                title = "Demo Notificaciones",
                subtitle = "Manejo de NotificationCompat, 4 canales dinámicos (NotificationChannel), permiso POST_NOTIFICATIONS (Android 13+), imágenes, acciones y barra de progreso.",
                icon = Icons.Default.Notifications,
                badgeText = "NotificationCompat + Channels",
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                onClick = onNavigateToNotificationsDemo
            )
        }
    }
}

/**
 * Tarjeta personalizable para cada opción de Demo.
 */
@Composable
private fun DemoOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    containerColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
