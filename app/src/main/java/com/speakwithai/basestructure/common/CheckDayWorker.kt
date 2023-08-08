package com.speakwithai.basestructure.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.utils.MessageManager

class CheckDayWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        if (MessageManager.isOneDayPassed()) {
            createNotificationChannel(applicationContext)
            sendNotification(applicationContext)
            return Result.success()
        }
        return Result.failure()
    }

    private val CHANNEL_ID = "OneDayPassedChannel"

    internal fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "1 Gün Geçti"
            val descriptionText = "1 günün geçtiğini belirten bildirimler için kanal."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    internal fun sendNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logoo)
            .setContentTitle(context.getString(R.string.one_day_passed))
            .setContentText(context.getString(R.string.ask_now))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }


}
