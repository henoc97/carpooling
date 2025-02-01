package tg.ulcrsandroid.carpooling.infrastructure.externalServices.push

import com.google.auth.oauth2.GoogleCredentials
import android.content.Context
import java.io.InputStream

fun obtenirAccessToken(context: Context): String? {
    return try {
        val credentials: GoogleCredentials = GoogleCredentials.fromStream(context.assets.open("serviceAccountKey.json"))
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        credentials.refreshIfExpired()
        credentials.accessToken.tokenValue
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
