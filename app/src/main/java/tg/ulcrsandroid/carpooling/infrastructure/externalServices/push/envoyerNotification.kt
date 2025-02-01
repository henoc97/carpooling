package tg.ulcrsandroid.carpooling.infrastructure.externalServices.push

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

fun envoyerNotification(
    context: Context,
    targetToken: String,
    title: String,
    body: String
) {
    val accessToken = obtenirAccessToken(context)
    if (accessToken == null) {
        Log.e("FCM", "Impossible de récupérer le token OAuth")
        return
    }

    val url = "https://fcm.googleapis.com/v1/projects/carpooling-f829f/messages:send"
    val json = JSONObject().apply {
        put("message", JSONObject().apply {
            put("token", targetToken)
            put("notification", JSONObject().apply {
                put("title", title)
                put("body", body)
            })
        })
    }

    val client = OkHttpClient()
    val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("Authorization", "Bearer $accessToken")
        .addHeader("Content-Type", "application/json")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FCM", "Erreur d'envoi de notification : ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("FCM", "Notification envoyée avec succès")
            } else {
                Log.e("FCM", "Erreur : ${response.code}, ${response.message}")
            }
        }
    })
}
