package com.jassgroup.architecturemobileapp.ui.camera

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector

/**
 * Estado UI inmutable para la pantalla del Demo CameraX.
 */
data class CameraUiState(
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val capturedImage: Bitmap? = null,
    val isCapturing: Boolean = false,
    val errorMessage: String? = null
)
