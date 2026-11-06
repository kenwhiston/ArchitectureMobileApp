package com.jassgroup.architecturemobileapp.ui.camera

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para gestionar el estado de CameraX (Lente seleccionado, Foto capturada, etc.).
 */
class CameraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    /**
     * Alterna la cámara entre la lente trasera y la frontal.
     */
    fun toggleCameraLens() {
        _uiState.update { currentState ->
            val nextLens = if (currentState.lensFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            currentState.copy(lensFacing = nextLens)
        }
    }

    /**
     * Asigna la foto capturada en el estado.
     */
    fun onImageCaptured(bitmap: Bitmap) {
        _uiState.update {
            it.copy(
                capturedImage = bitmap,
                isCapturing = false,
                errorMessage = null
            )
        }
    }

    /**
     * Establece si el obturador está procesando una captura.
     */
    fun setCapturing(isCapturing: Boolean) {
        _uiState.update { it.copy(isCapturing = isCapturing) }
    }

    /**
     * Limpia la foto previsualizada para regresar a la cámara.
     */
    fun clearCapturedImage() {
        _uiState.update { it.copy(capturedImage = null) }
    }

    /**
     * Registra un error de la cámara.
     */
    fun onError(message: String) {
        _uiState.update {
            it.copy(
                errorMessage = message,
                isCapturing = false
            )
        }
    }
}
