package com.jassgroup.architecturemobileapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.jassgroup.architecturemobileapp.MainActivity
import com.jassgroup.architecturemobileapp.R

/**
 * Helper para administrar y disparar diferentes tipos de notificaciones utilizando [NotificationCompat]
 * y múltiples canales de notificación ([NotificationChannel]).
 */
class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        // Canales de Notificación
        const val CHANNEL_IMPORTANT_ID = "channel_important"
        const val CHANNEL_GENERAL_ID = "channel_general"
        const val CHANNEL_MEDIA_ID = "channel_media"
        const val CHANNEL_PROGRESS_ID = "channel_progress"

        // IDs para cada tipo de notificación
        const val NOTIF_BASIC_ID = 101
        const val NOTIF_BIG_TEXT_ID = 102
        const val NOTIF_BIG_PICTURE_ID = 103
        const val NOTIF_ACTION_ID = 104
        const val NOTIF_PROGRESS_ID = 105
        const val NOTIF_HIGH_PRIORITY_ID = 106
    }

    init {
        createNotificationChannels()
    }

    /**
     * Crea los 4 Canales de Notificación en Android 8.0+ (API 26+).
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importantChannel = NotificationChannel(
                CHANNEL_IMPORTANT_ID,
                "Alertas Importantes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de alta prioridad con banners flotantes y sonido"
                enableVibration(true)
            }

            val generalChannel = NotificationChannel(
                CHANNEL_GENERAL_ID,
                "Notificaciones Generales",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisos cotidianos y textos extensos"
            }

            val mediaChannel = NotificationChannel(
                CHANNEL_MEDIA_ID,
                "Promociones e Imágenes",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones con vistas previas de imágenes"
            }

            val progressChannel = NotificationChannel(
                CHANNEL_PROGRESS_ID,
                "Progreso de Tareas",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Barra de progreso silenciosa para tareas continuas"
            }

            notificationManager.createNotificationChannels(
                listOf(importantChannel, generalChannel, mediaChannel, progressChannel)
            )
        }
    }

    private fun getPendingIntentToApp(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * 1. Notificación Corta (Texto Básico) - Canal General
     */
    fun showBasicNotification(title: String, text: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_GENERAL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntentToApp())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIF_BASIC_ID, notification)
    }

    /**
     * 2. Notificación Extensa (BigTextStyle) - Canal General
     */
    fun showBigTextNotification(title: String, shortText: String, bigText: String) {
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(bigText)
            .setBigContentTitle(title)
            .setSummaryText("Detalle Expandido")

        val notification = NotificationCompat.Builder(context, CHANNEL_GENERAL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(shortText)
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntentToApp())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIF_BIG_TEXT_ID, notification)
    }

    /**
     * Convierte un recurso Drawable/Mipmap (incluyendo VectorDrawables XML) en un [Bitmap] válido.
     */
    private fun getBitmapFromDrawable(resId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, resId)
            ?: return Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)

        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return drawable.bitmap
        }

        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 300
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 300

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * 3. Notificación con Imagen Grande (BigPictureStyle) - Canal Medios
     */
    fun showBigPictureNotification(title: String, text: String, bitmap: Bitmap? = null) {
        val launcherBitmap = bitmap ?: getBitmapFromDrawable(R.mipmap.ic_launcher)

        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(launcherBitmap)
            .setBigContentTitle(title)
            .setSummaryText("Imagen de ícono de launcher de la aplicación")

        val notification = NotificationCompat.Builder(context, CHANNEL_MEDIA_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setLargeIcon(launcherBitmap)
            .setStyle(bigPictureStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntentToApp())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIF_BIG_PICTURE_ID, notification)
    }

    /**
     * 4. Notificación con Botones de Acción (Acciones) - Canal General
     */
    fun showActionNotification(title: String, text: String) {
        // PendingIntent Acción 1: Aceptar
        val acceptIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra(NotificationActionReceiver.EXTRA_ACTION_NAME, "Aceptar")
            putExtra(NotificationActionReceiver.EXTRA_NOTIFICATION_ID, NOTIF_ACTION_ID)
        }
        val acceptPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // PendingIntent Acción 2: Rechazar
        val rejectIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra(NotificationActionReceiver.EXTRA_ACTION_NAME, "Rechazar")
            putExtra(NotificationActionReceiver.EXTRA_NOTIFICATION_ID, NOTIF_ACTION_ID)
        }
        val rejectPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_GENERAL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .addAction(R.drawable.ic_launcher_foreground, "Aceptar", acceptPendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Rechazar", rejectPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntentToApp())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIF_ACTION_ID, notification)
    }

    /**
     * 5. Notificación con Barra de Progreso - Canal Progreso
     */
    fun showProgressNotification(title: String, progress: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_PROGRESS_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText("Descargando archivo: $progress%")
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(progress < 100)
            .setAutoCancel(progress == 100)
            .build()

        notificationManager.notify(NOTIF_PROGRESS_ID, notification)
    }

    /**
     * 6. Notificación de Alta Prioridad (Banner Flotante / Heads-up) - Canal Importante
     */
    fun showHighPriorityNotification(title: String, text: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_IMPORTANT_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(getPendingIntentToApp())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIF_HIGH_PRIORITY_ID, notification)
    }
}
