package com.jassgroup.architecturemobileapp.ui.firebase.remote_config

import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel para el demo de Firebase Remote Config.
 */
class RemoteConfigViewModel(
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RemoteConfigUiState())
    val uiState: StateFlow<RemoteConfigUiState> = _uiState.asStateFlow()

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(10)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        val defaultValues = mapOf(
            "welcome_message" to "¡Bienvenido a Firebase Remote Config Demo!",
            "discount_banner_enabled" to true
        )
        remoteConfig.setDefaultsAsync(defaultValues)

        updateStateFromConfig()
        fetchAndActivate()
    }

    fun fetchAndActivate() {
        _uiState.update { it.copy(isLoading = true, message = null) }

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                val success = task.isSuccessful
                updateStateFromConfig()
                val sdf = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
                val now = sdf.format(Date())

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        lastFetchTime = now,
                        message = if (success) "Valores sincronizados desde Firebase" else "Se usan valores predeterminados"
                    )
                }
            }
    }

    private fun updateStateFromConfig() {
        val welcomeMsg = remoteConfig.getString("welcome_message")
        val isDiscountEnabled = remoteConfig.getBoolean("discount_banner_enabled")

        _uiState.update {
            it.copy(
                welcomeMessage = welcomeMsg,
                discountBannerEnabled = isDiscountEnabled
            )
        }
    }
}
