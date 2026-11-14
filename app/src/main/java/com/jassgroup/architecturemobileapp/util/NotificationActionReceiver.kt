package com.jassgroup.architecturemobileapp.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * BroadcastReceiver para procesar clics en los botones de acción dentro de las notificaciones.
 */
class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val actionName = intent.getStringExtra(EXTRA_ACTION_NAME) ?: "Acción"
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)

        Toast.makeText(
            context,
            "🔔 Notificación recibida: \"$actionName\"",
            Toast.LENGTH_SHORT
        ).show()

        if (notificationId != -1) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }

    companion object {
        const val EXTRA_ACTION_NAME = "extra_action_name"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }
}
