package com.jassgroup.architecturemobileapp.ui.firebase.auth

import com.google.firebase.auth.FirebaseUser

/**
 * Estado UI inmutable para el demo de Firebase Authentication.
 */
data class FirebaseAuthUiState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val currentUser: FirebaseUser? = null,
    val simulatedGoogleEmail: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
