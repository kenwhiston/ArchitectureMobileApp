package com.jassgroup.architecturemobileapp.ui.firebase.remote_config

/**
 * Estado UI inmutable para Firebase Remote Config.
 */
data class RemoteConfigUiState(
    val welcomeMessage: String = "Cargando configuración...",
    val discountBannerEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val lastFetchTime: String = "No sincronizado",
    val message: String? = null
)
