package tg.ulcrsandroid.carpooling.application.services

import tg.ulcrsandroid.carpooling.domain.models.Notification


import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import tg.ulcrsandroid.carpooling.domain.repositories.INotification
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.MyFireBaseNotificationService

@SuppressLint("StaticFieldLeak")
object NotificationService : INotification {

    private val FBNS: MyFireBaseNotificationService = MyFireBaseNotificationService
    override fun envoyerNotification(notification: Notification) {
        FBNS.envoyerNotification(notification)

    private var currentActivity: Activity? = null

    fun setActivity(activity: Activity) {
        currentActivity = activity
    }

    override fun envoyerNotification(deviceToken: String, title: String, body: String) {
        currentActivity?.let {
            performNetworkOperation(it, deviceToken, title, body)
        } ?: Log.e("NotificationService", "Activity is not set. Call setActivity() first.")

    }

    override fun consulterNotification() {
        // Implémentation de la consultation de notification
    }


    private fun performNetworkOperation(activity: Activity, deviceToken: String, title: String, body: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Perform network operation here
                tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.envoyerNotification(
                    activity,
                    deviceToken,
                    title,
                    body
                )
                withContext(Dispatchers.Main) {
                    // Update UI if needed
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("performNetworkOperation", e.toString())
                }
            }
        }
    }




