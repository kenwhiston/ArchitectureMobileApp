package com.jassgroup.architecturemobileapp.ui.firebase.crashlytics

import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para el demo de Firebase Crashlytics.
 */
class CrashlyticsViewModel(
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrashlyticsUiState())
    val uiState: StateFlow<CrashlyticsUiState> = _uiState.asStateFlow()

    init {
        crashlytics.setCustomKey("user_role", "developer_demo")
        crashlytics.setCustomKey("app_architecture", "MVVM_Compose")
    }

    fun logCustomEvent(logText: String) {
        if (logText.isBlank()) return
        crashlytics.log("Log Personalizado: $logText")
        _uiState.update {
            it.copy(
                customLogCount = it.customLogCount + 1,
                lastLogMessage = logText,
                message = "Log registrado en Crashlytics: \"$logText\""
            )
        }
    }

    fun triggerTestCrash(): Nothing {
        crashlytics.log("Disparando crash forzado de prueba desde UI")
        throw RuntimeException("¡Crash de Prueba para Firebase Crashlytics!")
    }
}
