package com.jassgroup.architecturemobileapp.ui.notifications

/**
 * Estado UI inmutable para el demo de Notificaciones.
 */
data class NotificationsUiState(
    val hasNotificationPermission: Boolean = true,
    val progressValue: Int = 0,
    val isProgressRunning: Boolean = false,
    val lastTriggeredType: String? = null
)
