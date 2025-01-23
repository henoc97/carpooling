package tg.ulcrsandroid.carpooling.application.utils.notification

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseTokenManager {
    private val database = FirebaseDatabase.getInstance().reference

    fun updateToken(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Token reçu : $token")

                if (token != null) {
                    val userTokenData = mapOf(
                        "fcmToken" to token,
                        "updatedAt" to System.currentTimeMillis() // Timestamp pour suivi
                    )

                    database.child("users").child(userId).child("notifications").setValue(userTokenData)
                        .addOnSuccessListener {
                            Log.d("FCM", "Token mis à jour dans la base de données pour l'utilisateur $userId.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("FCM", "Erreur lors de la mise à jour du token : ${e.message}")
                        }
                }
            } else {
                Log.w("FCM", "Échec de la récupération du token", task.exception)
            }
        }
    }
}
