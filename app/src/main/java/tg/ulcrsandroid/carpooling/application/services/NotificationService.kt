package tg.ulcrsandroid.carpooling.application.services

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tg.ulcrsandroid.carpooling.domain.models.Notification
import tg.ulcrsandroid.carpooling.domain.repositories.INotification

@SuppressLint("StaticFieldLeak")
object NotificationService : INotification {
    private var currentActivity: Activity? = null

    fun setActivity(activity: Activity) {
        currentActivity = activity
    }

    override fun envoyerNotification(notification: Notification) {
        TODO("Not yet implemented")
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
}