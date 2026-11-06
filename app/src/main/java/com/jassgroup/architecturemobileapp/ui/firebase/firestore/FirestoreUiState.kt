package com.jassgroup.architecturemobileapp.ui.firebase.firestore

/**
 * Modelo de documento Firestore.
 */
data class FirestoreNote(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Estado UI inmutable para Cloud Firestore.
 */
sealed interface FirestoreUiState {
    data object Loading : FirestoreUiState
    data class Success(val notes: List<FirestoreNote> = emptyList()) : FirestoreUiState
    data class Error(val message: String) : FirestoreUiState
}
