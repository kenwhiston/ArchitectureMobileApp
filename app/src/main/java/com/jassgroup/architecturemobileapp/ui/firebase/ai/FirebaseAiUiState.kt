package com.jassgroup.architecturemobileapp.ui.firebase.ai

/**
 * Estado UI inmutable para Firebase AI Logic (Vertex AI / Gemini).
 */
data class FirebaseAiUiState(
    val promptInput: String = "",
    val responseText: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
