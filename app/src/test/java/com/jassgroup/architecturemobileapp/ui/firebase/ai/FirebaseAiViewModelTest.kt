package com.jassgroup.architecturemobileapp.ui.firebase.ai

import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [FirebaseAiViewModel].
 */
class FirebaseAiViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FirebaseAiViewModel

    @Before
    fun setUp() {
        viewModel = FirebaseAiViewModel()
    }

    @Test
    fun `onPromptChanged actualiza el prompt en el estado`() = runTest {
        viewModel.onPromptChanged("Explicar MVVM en Android")
        assertEquals("Explicar MVVM en Android", viewModel.uiState.value.promptInput)
    }

    @Test
    fun `generateAiResponse con prompt vacio muestra error`() = runTest {
        viewModel.onPromptChanged("")
        viewModel.generateAiResponse()

        assertNotNull(viewModel.uiState.value.error)
    }
}
