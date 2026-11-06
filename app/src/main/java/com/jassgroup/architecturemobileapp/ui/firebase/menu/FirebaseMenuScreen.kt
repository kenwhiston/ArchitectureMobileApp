package com.jassgroup.architecturemobileapp.ui.firebase.menu

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Storage
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Pantalla de menú para la Batería de Demos de Firebase.
 *
 * Muestra el listado de ejemplos disponibles:
 * 1. Authentication (Correo & Google Gmail)
 * 2. Cloud Firestore
 * 3. Remote Config
 * 4. Crashlytics
 * 5. Firebase AI Logic (Vertex AI / Gemini)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseMenuScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToFirestore: () -> Unit,
    onNavigateToRemoteConfig: () -> Unit,
    onNavigateToCrashlytics: () -> Unit,
    onNavigateToAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Batería de Demos Firebase") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Menú Principal"
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona una funcionalidad de Firebase:",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Ejemplo 1: Authentication
            FirebaseOptionCard(
                title = "1. Firebase Authentication",
                subtitle = "Registro e inicio de sesión con Correo/Contraseña y simulación de cuenta Google Gmail.",
                icon = Icons.Default.Lock,
                badgeText = "FirebaseAuth",
                onClick = onNavigateToAuth
            )

            // Ejemplo 2: Cloud Firestore
            FirebaseOptionCard(
                title = "2. Cloud Firestore",
                subtitle = "Base de datos NoSQL reactiva en tiempo real para crear, editar y eliminar documentos.",
                icon = Icons.Default.Storage,
                badgeText = "FirebaseFirestore",
                onClick = onNavigateToFirestore
            )

            // Ejemplo 3: Remote Config
            FirebaseOptionCard(
                title = "3. Firebase Remote Config",
                subtitle = "Obtención y activación de parámetros dinámicos y banderas de características desde la nube.",
                icon = Icons.Default.CloudSync,
                badgeText = "FirebaseRemoteConfig",
                onClick = onNavigateToRemoteConfig
            )

            // Ejemplo 4: Crashlytics
            FirebaseOptionCard(
                title = "4. Firebase Crashlytics",
                subtitle = "Reportes de errores, logs personalizados y botón para forzar crash de prueba.",
                icon = Icons.Default.BugReport,
                badgeText = "FirebaseCrashlytics",
                onClick = onNavigateToCrashlytics
            )

            // Ejemplo 5: Firebase AI Logic
            FirebaseOptionCard(
                title = "5. Firebase AI Logic (Vertex AI)",
                subtitle = "Generación de texto inteligente mediante prompts conectados al modelo Gemini de Google.",
                icon = Icons.Default.AutoAwesome,
                badgeText = "FirebaseVertexAI",
                onClick = onNavigateToAi
            )
        }
    }
}

@Composable
private fun FirebaseOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                modifier = Modifier.size(40.dp),
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
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
