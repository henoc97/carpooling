package tg.ulcrsandroid.carpooling.infrastructure.externalServices.push
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MyFireBaseMessagingService {
    fun initializeToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
        }

        // Obtenir le token
        val token = task.result
        Log.d("FCM Token", token ?: "Token non disponible")
        return@addOnCompleteListener

        }
    }

}