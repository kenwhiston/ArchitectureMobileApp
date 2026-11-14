package com.jassgroup.architecturemobileapp.ui.notifications

import android.app.Application
import com.jassgroup.architecturemobileapp.util.MainDispatcherRule
import com.jassgroup.architecturemobileapp.util.NotificationHelper
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para [NotificationsViewModel].
 */
class NotificationsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var application: Application
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var viewModel: NotificationsViewModel

    @Before
    fun setUp() {
        application = mockk(relaxed = true)
        notificationHelper = mockk(relaxed = true)
        viewModel = NotificationsViewModel(application, notificationHelper)
    }

    @Test
    fun `updatePermissionStatus actualiza el estado del permiso`() = runTest {
        viewModel.updatePermissionStatus(false)
        assertFalse(viewModel.uiState.value.hasNotificationPermission)

        viewModel.updatePermissionStatus(true)
        assertTrue(viewModel.uiState.value.hasNotificationPermission)
    }

    @Test
    fun `sendBasicNotification registra el tipo de notificacion`() = runTest {
        viewModel.sendBasicNotification()

        assertEquals("Corta (Texto Básico)", viewModel.uiState.value.lastTriggeredType)
        verify(exactly = 1) { notificationHelper.showBasicNotification(any(), any()) }
    }

    @Test
    fun `sendBigTextNotification registra el tipo de notificacion`() = runTest {
        viewModel.sendBigTextNotification()

        assertEquals("Extensa (BigTextStyle)", viewModel.uiState.value.lastTriggeredType)
        verify(exactly = 1) { notificationHelper.showBigTextNotification(any(), any(), any()) }
    }

    @Test
    fun `sendBigPictureNotification registra el tipo de notificacion`() = runTest {
        viewModel.sendBigPictureNotification()

        assertEquals("Con Imagen (BigPictureStyle)", viewModel.uiState.value.lastTriggeredType)
        verify(exactly = 1) { notificationHelper.showBigPictureNotification(any(), any(), any()) }
    }

    @Test
    fun `sendActionNotification registra el tipo de notificacion`() = runTest {
        viewModel.sendActionNotification()

        assertEquals("Con Botones de Acción", viewModel.uiState.value.lastTriggeredType)
        verify(exactly = 1) { notificationHelper.showActionNotification(any(), any()) }
    }

    @Test
    fun `sendHighPriorityNotification registra el tipo de notificacion`() = runTest {
        viewModel.sendHighPriorityNotification()

        assertEquals("Alta Prioridad (Heads-up)", viewModel.uiState.value.lastTriggeredType)
        verify(exactly = 1) { notificationHelper.showHighPriorityNotification(any(), any()) }
    }
}
