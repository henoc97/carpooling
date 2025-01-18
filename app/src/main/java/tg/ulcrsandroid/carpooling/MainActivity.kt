package tg.ulcrsandroid.carpooling

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        requestNotificationPermission(this)
        authContext = AuthContext()

        // Initialiser l'animation de landings
//        val landingAnimation: LottieAnimationView = findViewById(R.id.landingAnimation)
//        landingAnimation.visibility = View.VISIBLE
//
//        // Initialiser les boutons
        val signUpButton: Button = findViewById(R.id.signUpButton)
//        val loginButton: Button = findViewById(R.id.loginButton)
//        val googleButton: Button = findViewById(R.id.googleSignInButton)
//
//        // Configurer les clics sur les boutons
        signUpButton.setOnClickListener {
            // val intent = Intent(this, signUpActivity::class.java)
            // startActivity(intent)
            performNetworkOperation(token)
        }
//        loginButton.setOnClickListener {
//            val intent = Intent(this, LogInActivity::class.java)
//            startActivity(intent)
//        }
//        googleButton.setOnClickListener {
//           val googleStrategy = GoogleAuthStrategy(this)
//           authContext.updateStrategy(googleStrategy)
//           startActivityForResult(googleStrategy.getSignInIntent(), 100)
//        }

        // Commentaire fait pas Sylvain GOSSOU

        // Demander à Firebase d'obtenir un token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Récupérer le token d'inscription FCM
                token = task.result
                Log.d("FCM", "Token reçu: $token")
                // Afficher le token ou l'envoyer à votre backend
                Toast.makeText(this, "Token FCM: $token", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("FCM", "Échec de la récupération du token", task.exception)
            }

//            performNetworkOperation(token)
        }
    }

    private fun performNetworkOperation(deviceToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Perform network operation here
                envoyerNotification(this@MainActivity, deviceToken, "Title henoc ", "Body benito")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }
}
