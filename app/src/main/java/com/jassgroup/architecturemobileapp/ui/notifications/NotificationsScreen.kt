package com.jassgroup.architecturemobileapp.ui.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Pantalla principal del demo de Notificaciones en Android.
 *
 * Muestra una lista para lanzar 6 tipos de notificaciones sobre 4 canales diferentes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Manejo de permiso POST_NOTIFICATIONS para Android 13+ (API 33+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updatePermissionStatus(isGranted)
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            viewModel.updatePermissionStatus(isGranted)

            if (!isGranted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Demo Notificaciones") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (!uiState.hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Se requiere permiso de notificaciones (Android 13+)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
                            Text("Solicitar Permiso")
                        }
                    }
                }
            }

            // Explicación de los Canales
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "4 Canales Configurados (NotificationChannel):",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "• Alertas Importantes (HIGH - Banner Flotante)", style = MaterialTheme.typography.bodySmall)
                    Text(text = "• Notificaciones Generales (DEFAULT)", style = MaterialTheme.typography.bodySmall)
                    Text(text = "• Promociones e Imágenes (DEFAULT)", style = MaterialTheme.typography.bodySmall)
                    Text(text = "• Progreso de Tareas (LOW - Silencioso)", style = MaterialTheme.typography.bodySmall)
                }
            }

            Text(
                text = "Toca cualquier opción para disparar la notificación:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            // Opción 1: Corta (Texto Básico)
            NotificationOptionCard(
                title = "1. Notificación Corta",
                subtitle = "Texto básico de título y mensaje estándar.",
                icon = Icons.Default.Message,
                channelTag = "Canal General",
                onClick = { viewModel.sendBasicNotification() }
            )

            // Opción 2: Extensa (BigTextStyle)
            NotificationOptionCard(
                title = "2. Notificación Extensa",
                subtitle = "Uso de BigTextStyle para desplegar párrafos largos de información.",
                icon = Icons.Default.Article,
                channelTag = "Canal General",
                onClick = { viewModel.sendBigTextNotification() }
            )

            // Opción 3: Con Imagen Grande (BigPictureStyle)
            NotificationOptionCard(
                title = "3. Notificación con Imagen",
                subtitle = "Uso de BigPictureStyle para mostrar una imagen o fotografía grande.",
                icon = Icons.Default.Image,
                channelTag = "Canal Medios",
                onClick = { viewModel.sendBigPictureNotification() }
            )

            // Opción 4: Con Botones de Acción
            NotificationOptionCard(
                title = "4. Notificación con Acciones",
                subtitle = "Incluye botones de 'Aceptar' y 'Rechazar' conectados a PendingIntent.",
                icon = Icons.Default.SmartButton,
                channelTag = "Canal General",
                onClick = { viewModel.sendActionNotification() }
            )

            // Opción 5: Con Barra de Progreso
            NotificationOptionCard(
                title = "5. Notificación con Progreso",
                subtitle = if (uiState.isProgressRunning) "Progreso: ${uiState.progressValue}%..." else "Simula la descarga de un archivo con barra de progreso dinámico.",
                icon = Icons.Default.HourglassTop,
                channelTag = "Canal Progreso",
                onClick = { viewModel.sendProgressNotification() }
            )

            // Opción 6: Alta Prioridad (Heads-up)
            NotificationOptionCard(
                title = "6. Notificación Alta Prioridad",
                subtitle = "Aviso urgente con banner flotante en la parte superior de la pantalla.",
                icon = Icons.Default.Warning,
                channelTag = "Canal Importante",
                onClick = { viewModel.sendHighPriorityNotification() }
            )
        }
    }
}

@Composable
private fun NotificationOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    channelTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = channelTag,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
