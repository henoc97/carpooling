package tg.ulcrsandroid.carpooling.infrastructure.externalServices.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.nearby.messages.Message
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.MainActivity
import java.net.HttpURLConnection
import java.net.URL

class MyFireBaseMessagingService : FirebaseMessagingService() {

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

    // override fun onMessageReceived(remoteMessage: RemoteMessage) {
    //     super.onMessageReceived(remoteMessage)

    //     // Log the message
    //     Log.d("FCM", "From: ${remoteMessage.from}")

    //     // Check if message contains a data payload.
    //     remoteMessage.data.isNotEmpty().let {
    //         Log.d("FCM", "Message data payload: ${remoteMessage.data}")
    //     }

    //     // Check if message contains a notification payload.
    //     remoteMessage.notification?.let {
    //         Log.d("FCM", "Message Notification Body: ${it.body}")
    //         sendNotification(it.body)
    //     }
    // }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // Implémentez la logique pour envoyer le token à Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().reference
        val userId = "user_id_example" // Remplacez par l'ID utilisateur actuel
        database.child("users").child(userId).child("fcmToken").setValue(token)
            .addOnSuccessListener {
                Log.d("FCM", "Token envoyé au serveur Firebase avec succès : $token")
            }
            .addOnFailureListener { e ->
                Log.e("FCM", "Erreur lors de l'envoi du token au serveur Firebase : ${e.message}")
            }
    }

    private val token = "dV7YGB9sRcman34rQqHpsL:APA91bHd-cv28k9Y2N3PHNRwv8_61VlInQWMzF38sJayPIC7FtjpIRdvz2s0osNCyWO3C-YKVwctz82N-el3gQCQKFxTCiELIEM01PBRP9aZThxXWRXiUy8"
    public fun sendNotification(context: Context, messageBody: String, token: String = this.token) {
        // Ce token d'enregistrement provient des SDK client FCM.
        val registrationToken = "YOUR_REGISTRATION_TOKEN"

        // Voir la documentation pour définir une charge utile du message.
                val message = Message.builder()
                    .putData("score", "850")
                    .putData("time", "2:45")
                    .setToken(registrationToken)
                    .build()

        // Envoyer un message au dispositif correspondant au token d'enregistrement fourni.
                val response = FirebaseMessaging.getInstance().send(message)
        // La réponse est un identifiant de message sous forme de chaîne.
                println("Message envoyé avec succès : $response")

            }
    
}