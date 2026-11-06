package com.jassgroup.architecturemobileapp.ui.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [FirebaseAuthViewModel].
 */
class FirebaseAuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: FirebaseAuthViewModel

    @Before
    fun setUp() {
        auth = mockk(relaxed = true)
        every { auth.currentUser } returns null
        viewModel = FirebaseAuthViewModel(auth)
    }

    @Test
    fun `onEmailChanged actualiza el correo en el estado`() = runTest {
        viewModel.onEmailChanged("test@gmail.com")
        assertEquals("test@gmail.com", viewModel.uiState.value.emailInput)
    }

    @Test
    fun `onPasswordChanged actualiza la contrasena en el estado`() = runTest {
        viewModel.onPasswordChanged("123456")
        assertEquals("123456", viewModel.uiState.value.passwordInput)
    }

    @Test
    fun `loginWithEmail con campos vacios muestra error`() = runTest {
        viewModel.onEmailChanged("")
        viewModel.onPasswordChanged("")
        viewModel.loginWithEmail()

        assertNotNull(viewModel.uiState.value.error)
    }
}
