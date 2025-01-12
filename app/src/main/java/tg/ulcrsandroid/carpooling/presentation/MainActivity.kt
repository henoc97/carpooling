package tg.ulcrsandroid.carpooling.presentation

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.MyFireBaseMessagingService

class MainActivity : AppCompatActivity() {
    private lateinit var authContext: AuthContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_notification)
        val tokentext=  findViewById<TextView>(R.id.token)



        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Obtenir le token
            val token = task.result
            Log.d("FCM Token", token ?: "Token non disponible")
            tokentext.text= token
        }

        // Commentaire fait pas Sylvain GOSSOU

        // Par défaut, stratégie Email/Password
       /* val emailPasswordAuthStrategy = EmailPasswordAuthStrategy()
        authContext = AuthContext()
        authContext.updateStrategy(emailPasswordAuthStrategy)

        // Bouton pour l'inscription par Email/Password
        findViewById<Button>(R.id.emailSignUpButton).setOnClickListener {
            authContext.sInscrire("test9785@example.com", "password123", "John Doe")
        }

        // Bouton pour l'inscription via Google
        findViewById<Button>(R.id.googleSignInButton).setOnClickListener {
            val googleStrategy = GoogleAuthStrategy(this)
            authContext.updateStrategy(googleStrategy)
            startActivityForResult(googleStrategy.getSignInIntent(), 100)
        }*/
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val googleStrategy = authContext.strategy as? GoogleAuthStrategy
            googleStrategy?.handleGoogleSignInResult(data,
                onSuccess = {
                    println("Connexion réussie via Google")
                },
                onError = { error ->
                    println("Erreur : $error") // This is where you're printing the error
                }
            )
        }
    }*/
}




