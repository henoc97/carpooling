package tg.ulcrsandroid.carpooling

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.MyFireBaseMessagingService
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.android.play.core.integrity.m

class MainActivity : AppCompatActivity() {
    private lateinit var authContext: AuthContext
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_notification)

        val btnSendNotification = findViewById<Button>(R.id.btnSendNotification)

        // Tester l'envoi de notification
        btnSendNotification.setOnClickListener {
            val messagingService = MyFireBaseMessagingService()

            // Récupérer le token actuel (assurez-vous que l'application est bien connectée à Firebase)
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Erreur lors de la récupération du token", task.exception)
                    return@addOnCompleteListener
                }

                // Récupérer le token
                val userToken = task.result
                Log.d("FCM", "Token actuel : $userToken")

                // Appeler la méthode `sendNotification`
                val message = "Votre trajet a été confirmé !"
                messagingService.sendNotification(this, message, userToken)
            }
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




