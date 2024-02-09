package com.speakwithai.basestructure.common.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.speakwithai.basestructure.R

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]

            createNotification(title, message)
        }
    }

    private fun createNotification(title: String?, message: String?) {
        val channelId = "default_channel" // Bildirim kanalı kimliği
        val notificationId = 1 // Bildirim kimliği

        // Bildirim kanalını oluştur (yalnızca Android 8.0 ve sonrası için gereklidir)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Default Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
            }

            // Bildirim yöneticisine kanalı ekle
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Bildirim oluştur
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logosuper) // Bildirim simgesi
            .setContentTitle(title) // Bildirim başlığı
            .setContentText(message) // Bildirim metni
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Bildirimi göster
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
}