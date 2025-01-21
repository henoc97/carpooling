package tg.ulcrsandroid.carpooling.application.services
<<<<<<< HEAD
import tg.ulcrsandroid.carpooling.domain.models.Notification
=======

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
<<<<<<< HEAD
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
=======
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
import tg.ulcrsandroid.carpooling.domain.repositories.INotification
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.MyFireBaseNotificationService

@SuppressLint("StaticFieldLeak")
object NotificationService : INotification {
<<<<<<< HEAD
<<<<<<< HEAD

    private val FBNS: MyFireBaseNotificationService = MyFireBaseNotificationService
    override fun envoyerNotification(notification: Notification) {
        FBNS.envoyerNotification(notification)
=======
=======
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
    private var currentActivity: Activity? = null

    fun setActivity(activity: Activity) {
        currentActivity = activity
    }

    override fun envoyerNotification(deviceToken: String, title: String, body: String) {
        currentActivity?.let {
            performNetworkOperation(it, deviceToken, title, body)
        } ?: Log.e("NotificationService", "Activity is not set. Call setActivity() first.")
<<<<<<< HEAD
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
=======
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
    }

    override fun consulterNotification() {
        // Implémentation de la consultation de notification
    }

<<<<<<< HEAD
<<<<<<< HEAD


=======
=======
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
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
<<<<<<< HEAD
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
=======
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
}


