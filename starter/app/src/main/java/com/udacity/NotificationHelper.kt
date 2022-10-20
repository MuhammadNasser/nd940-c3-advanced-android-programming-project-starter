package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    fun createChannel(
        channelId: String,
        channelName: String,
        notificationManager: NotificationManager
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.description = R.string.notification_description.toString()
            notificationChannel.apply {
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun buildNotification(
        activity: MainActivity,
        body: String,
        fileName: String,
        status: String
    ) {
        val contentIntent = Intent(activity, MainActivity::class.java)
        val contentPendingIntent =
            PendingIntent.getActivity(activity, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE)

        val detailsIntent = Intent(activity, DetailActivity::class.java)
        detailsIntent.putExtra(DetailActivity.STATUS, status)
        detailsIntent.putExtra(DetailActivity.FILE_NAME, fileName)

        val pendingIntent =
            PendingIntent.getActivity(activity, 0, detailsIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(activity, MainActivity.CHANNEL_ID)

        val notificationImage =
            BitmapFactory.decodeResource(activity.resources, R.drawable.ic_launcher_foreground)

        val bigPictureStyle =
            NotificationCompat.BigPictureStyle().bigPicture(notificationImage).bigLargeIcon(null)

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(activity.resources.getString(R.string.notification_title))
            .setContentText(body)
            .setContentIntent(contentPendingIntent)
            .setStyle(bigPictureStyle)
            .setLargeIcon(notificationImage)
            .addAction(R.drawable.ic_launcher_foreground, "Details", pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(activity).notify(1001, builder.build())
    }
}