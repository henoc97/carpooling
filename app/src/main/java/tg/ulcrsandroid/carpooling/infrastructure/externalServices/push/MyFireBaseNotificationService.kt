package tg.ulcrsandroid.carpooling.infrastructure.externalServices.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import tg.ulcrsandroid.carpooling.domain.models.Notification
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


object MyFireBaseNotificationService {
        private const val FCM_URL = "https://fcm.googleapis.com/fcm/send"
        private const val SERVER_KEY = "AIzaSyCL0UE69_U2cKGNhD5u9Eotwlt-6pXM6zQ"

        /**
         * Méthode pour envoyer une notification via Firebase
         * @param Token Token de l'utilisateur à qui envoyer la notification
         */
        fun  envoyerNotification(notification: Notification) {
            recupererToken { token ->
                if (token != null) {
                    val client = OkHttpClient()

                    // Créer le JSON du message
                    val json = JSONObject()
                    val notificationJson = JSONObject()
                    try {
                        notificationJson.put("Message", notification)
                        json.put("to", token)
                        json.put("notification", notificationJson)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // Créer la requête POST
                    val requestBody = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        json.toString()
                    )
                    val request = Request.Builder()
                        .url(FCM_URL)
                        .post(requestBody)
                        .addHeader("Authorization", "key=$SERVER_KEY")
                        .addHeader("Content-Type", "application/json")
                        .build()

                    // Envoyer la requête
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            val e1 = Log.e("NotificationUtils", "Erreur d'envoi : ${e.message}")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                Log.d("NotificationUtils", "Notification envoyée avec succès !")
                            } else {
                                Log.e("NotificationUtils", "Erreur d'envoi : ${response.message}")
                            }
                        }
                    })
                } else {
                    Log.e("NotificationUtils", "Impossible de récupérer le token pour envoyer la notification")
                }
            }
        }

//Recuperation du token du device
private fun recupererToken(callback: (String?) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token utilisateur : $token")
                    callback(token) // Retourne le token via le callback
                } else {
                    Log.e("FCM", "Erreur lors de la récupération du token", task.exception)
                    callback(null) // Retourne null en cas d'erreur
                }
            }
        }

    }



