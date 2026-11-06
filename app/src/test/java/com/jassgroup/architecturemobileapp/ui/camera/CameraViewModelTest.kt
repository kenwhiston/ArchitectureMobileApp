package com.jassgroup.architecturemobileapp.ui.camera

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [CameraViewModel].
 */
class CameraViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CameraViewModel

    @Before
    fun setUp() {
        viewModel = CameraViewModel()
    }

    @Test
    fun `toggleCameraLens conmuta entre lente trasera y frontal`() = runTest {
        assertEquals(CameraSelector.LENS_FACING_BACK, viewModel.uiState.value.lensFacing)

        viewModel.toggleCameraLens()
        assertEquals(CameraSelector.LENS_FACING_FRONT, viewModel.uiState.value.lensFacing)

        viewModel.toggleCameraLens()
        assertEquals(CameraSelector.LENS_FACING_BACK, viewModel.uiState.value.lensFacing)
    }

    @Test
    fun `onImageCaptured actualiza el bitmap y resetea isCapturing`() = runTest {
        val mockBitmap: Bitmap = mockk()

        viewModel.setCapturing(true)
        assertTrue(viewModel.uiState.value.isCapturing)

        viewModel.onImageCaptured(mockBitmap)

        val state = viewModel.uiState.value
        assertEquals(mockBitmap, state.capturedImage)
        assertEquals(false, state.isCapturing)
        assertNull(state.errorMessage)
    }

    @Test
    fun `clearCapturedImage resetea el bitmap a null`() = runTest {
        val mockBitmap: Bitmap = mockk()
        viewModel.onImageCaptured(mockBitmap)

        viewModel.clearCapturedImage()

        assertNull(viewModel.uiState.value.capturedImage)
    }

    @Test
    fun `onError actualiza el mensaje de error y resetea isCapturing`() = runTest {
        viewModel.setCapturing(true)

        viewModel.onError("Error de camara")

        val state = viewModel.uiState.value
        assertEquals("Error de camara", state.errorMessage)
        assertEquals(false, state.isCapturing)
    }
}
