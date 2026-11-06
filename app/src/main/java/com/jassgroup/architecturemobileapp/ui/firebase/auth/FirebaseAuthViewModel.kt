package com.jassgroup.architecturemobileapp.ui.firebase.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para el demo de Firebase Authentication.
 *
 * Administra el estado de autenticación con [FirebaseAuth], registro y cierre de sesión.
 */
class FirebaseAuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FirebaseAuthUiState(currentUser = auth.currentUser)
    )
    val uiState: StateFlow<FirebaseAuthUiState> = _uiState.asStateFlow()

    init {
        // Escuchar cambios de estado de autenticación en Firebase
        auth.addAuthStateListener { firebaseAuth ->
            _uiState.update { it.copy(currentUser = firebaseAuth.currentUser) }
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(emailInput = email, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(passwordInput = password, error = null) }
    }

    fun loginWithEmail() {
        val email = _uiState.value.emailInput.trim()
        val password = _uiState.value.passwordInput.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(error = "Por favor ingresa correo y contraseña") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, message = null) }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = result.user,
                        message = "Sesión iniciada con éxito: ${result.user?.email}"
                    )
                }
            }
            .addOnFailureListener { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Error al iniciar sesión"
                    )
                }
            }
    }

    fun registerWithEmail() {
        val email = _uiState.value.emailInput.trim()
        val password = _uiState.value.passwordInput.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(error = "Por favor ingresa correo y contraseña para registrarte") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, message = null) }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = result.user,
                        message = "Usuario registrado con éxito: ${result.user?.email}"
                    )
                }
            }
            .addOnFailureListener { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Error al registrar usuario"
                    )
                }
            }
    }

    fun signInWithGoogleSimulation() {
        _uiState.update { it.copy(isLoading = true, error = null, message = null) }
        // Autenticación anónima o simulación de cuenta Google Gmail
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val gmailAccount = "usuario.demo.google@gmail.com"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = result.user,
                        simulatedGoogleEmail = gmailAccount,
                        message = "Sesión iniciada con Google Gmail ($gmailAccount)"
                    )
                }
            }
            .addOnFailureListener { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Error en autenticación Google"
                    )
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _uiState.update {
            it.copy(
                currentUser = null,
                simulatedGoogleEmail = null,
                message = "Sesión cerrada",
                error = null
            )
        }
    }
}
