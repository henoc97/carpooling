package tg.ulcrsandroid.carpooling.application.services

import android.app.Activity
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tg.ulcrsandroid.carpooling.MainActivity
import tg.ulcrsandroid.carpooling.domain.repositories.INotification

object NotificationService : INotification {
    override fun envoyerNotification(deviceToken: String, title: String, body: String) {
        // Implémentation de l'envoi de notification
        performNetworkOperation(deviceToken, title, body)
    }

    override fun consulterNotification() {
        // Implémentation de la consultation de notification
    }

    private fun performNetworkOperation(deviceToken: String, title: String, body: String, activity: Activity = MainActivity()) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Perform network operation here
                val activity = MainActivity()
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
