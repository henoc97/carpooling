package tg.ulcrsandroid.carpooling

import android.os.Bundle
import android.util.Log
<<<<<<< HEAD
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import tg.ulcrsandroid.carpooling.application.services.NotificationService
import tg.ulcrsandroid.carpooling.domain.models.Notification
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var authContext: AuthContext
    private val notificationService : NotificationService = NotificationService
    private val notification = Notification(
        notificationId = "12345",
        message = "Votre trajet est confirmé !",
        timestamp = Date()
    )

=======
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tg.ulcrsandroid.carpooling.application.services.NotificationService
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.GoogleAuthStrategy
import tg.ulcrsandroid.carpooling.application.utils.lottie.loadJsonFromRaw
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.envoyerNotification
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.obtenirAccessToken
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.requestNotificationPermission
import tg.ulcrsandroid.carpooling.presentation.activities.LogInActivity
import tg.ulcrsandroid.carpooling.presentation.activities.signUpActivity

class MainActivity : AppCompatActivity() {
    private lateinit var authContext: AuthContext
    var token = "";
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        notificationService.envoyerNotification(notification)
       /* FirebaseApp.initializeApp(this)
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
        }*/

        // Commentaire fait pas Sylvain GOSSOU

        // Par défaut, stratégie Email/Password
       /* val emailPasswordAuthStrategy = EmailPasswordAuthStrategy()
=======
        setContentView(R.layout.activity_landing)
        requestNotificationPermission(this)
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
        authContext = AuthContext()

        // Initialisation l'animation de landings
        val landingAnimation: LottieAnimationView = findViewById(R.id.landingAnimation)
        landingAnimation.visibility = View.VISIBLE

        // Initialisation les boutons
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val loginButton: Button = findViewById(R.id.loginButton)
        val googleButton: Button = findViewById(R.id.googleSignInButton)

        // Configuration les clics sur les boutons
        signUpButton.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
            // Définition de l'activité actuelle dans NotificationService
            // NotificationService.setActivity(this)
            // NotificationService.envoyerNotification(token, "Title henoc 2", "Body benito 2")
        }

<<<<<<< HEAD
        // Bouton pour l'inscription via Google
        findViewById<Button>(R.id.googleSignInButton).setOnClickListener {
            val googleStrategy = GoogleAuthStrategy(this)
            authContext.updateStrategy(googleStrategy)
            startActivityForResult(googleStrategy.getSignInIntent(), 100)
        }*/
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
=======
        loginButton.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        googleButton.setOnClickListener {
           val googleStrategy = GoogleAuthStrategy(this)
           authContext.updateStrategy(googleStrategy)
           startActivityForResult(googleStrategy.getSignInIntent(), 100)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
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




