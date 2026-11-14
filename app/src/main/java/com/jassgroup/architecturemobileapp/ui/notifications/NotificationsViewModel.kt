package com.jassgroup.architecturemobileapp.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jassgroup.architecturemobileapp.util.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el disparo de los distintos tipos de notificaciones.
 */
class NotificationsViewModel(
    application: Application,
    private val notificationHelper: NotificationHelper = NotificationHelper(application)
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun updatePermissionStatus(isGranted: Boolean) {
        _uiState.update { it.copy(hasNotificationPermission = isGranted) }
    }

    /**
     * 1. Dispara Notificación Corta (Simple Text)
     */
    fun sendBasicNotification() {
        notificationHelper.showBasicNotification(
            title = "Notificación Corta",
            text = "Este es un mensaje de texto básico y corto."
        )
        _uiState.update { it.copy(lastTriggeredType = "Corta (Texto Básico)") }
    }

    /**
     * 2. Dispara Notificación Extensa (BigTextStyle)
     */
    fun sendBigTextNotification() {
        notificationHelper.showBigTextNotification(
            title = "Notificación Extensa",
            shortText = "Desliza hacia abajo para leer más...",
            bigText = "Esta es una notificación con estilo BigTextStyle. Permite incluir párrafos extensos de información sin recortar el texto, ofreciendo una vista previa detallada para el usuario directamente desde el panel de notificaciones de Android."
        )
        _uiState.update { it.copy(lastTriggeredType = "Extensa (BigTextStyle)") }
    }

    /**
     * 3. Dispara Notificación con Imagen Grande (BigPictureStyle)
     */
    fun sendBigPictureNotification() {
        notificationHelper.showBigPictureNotification(
            title = "Notificación con Imagen",
            text = "Desliza para ver la fotografía a tamaño completo."
        )
        _uiState.update { it.copy(lastTriggeredType = "Con Imagen (BigPictureStyle)") }
    }

    /**
     * 4. Dispara Notificación con Botones de Acción
     */
    fun sendActionNotification() {
        notificationHelper.showActionNotification(
            title = "Confirmación Requerida",
            text = "Presiona Aceptar o Rechazar para ejecutar la acción."
        )
        _uiState.update { it.copy(lastTriggeredType = "Con Botones de Acción") }
    }

    /**
     * 5. Dispara Notificación con Barra de Progreso Simulada
     */
    fun sendProgressNotification() {
        if (_uiState.value.isProgressRunning) return

        _uiState.update { it.copy(isProgressRunning = true, progressValue = 0, lastTriggeredType = "Con Barra de Progreso") }

        viewModelScope.launch {
            for (p in 0..100 step 20) {
                notificationHelper.showProgressNotification("Descargando Actualización...", p)
                _uiState.update { it.copy(progressValue = p) }
                delay(800)
            }
            _uiState.update { it.copy(isProgressRunning = false) }
        }
    }

    /**
     * 6. Dispara Notificación de Alta Prioridad (Banner Flotante / Heads-up)
     */
    fun sendHighPriorityNotification() {
        notificationHelper.showHighPriorityNotification(
            title = "🚨 Alerta Urgente",
            text = "Esta es una notificación de alta prioridad con banner flotante."
        )
        _uiState.update { it.copy(lastTriggeredType = "Alta Prioridad (Heads-up)") }
    }
}
