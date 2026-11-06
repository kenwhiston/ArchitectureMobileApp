package com.jassgroup.architecturemobileapp.ui.firebase.crashlytics

/**
 * Estado UI inmutable para Firebase Crashlytics.
 */
data class CrashlyticsUiState(
    val customLogCount: Int = 0,
    val lastLogMessage: String? = null,
    val userRoleKey: String = "developer_test",
    val message: String? = null
)
