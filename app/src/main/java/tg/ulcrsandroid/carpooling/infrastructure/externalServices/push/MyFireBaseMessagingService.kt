package tg.ulcrsandroid.carpooling.infrastructure.externalServices.push

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService

class MyFireBaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", "Message received: ${remoteMessage.data}")

        // Log the message
        Log.d("FCM", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            sendNotification(it.body, it.title)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        UtilisateurService.updateTokenInDatabase(token)
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(messageBody: String?, messageTitle: String?) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val channelDescription = getString(R.string.default_notification_channel_description)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Ensure ic_notification exists
            .setContentTitle(messageTitle) // Use a placeholder title
            .setContentText(messageBody) // Use the message body
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            notify(1, notificationBuilder.build()) // Use a unique notification ID
        }
    }
}