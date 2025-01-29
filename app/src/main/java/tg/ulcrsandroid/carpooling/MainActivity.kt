package tg.ulcrsandroid.carpooling

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.GoogleAuthStrategy
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.requestNotificationPermission
import tg.ulcrsandroid.carpooling.presentation.activities.LogInActivity
import tg.ulcrsandroid.carpooling.presentation.activities.signUpActivity
import tg.ulcrsandroid.carpooling.databinding.ActivityLandingBinding // Import généré automatiquement
import tg.ulcrsandroid.carpooling.presentation.activities.HomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var authContext: AuthContext
    private lateinit var binding: ActivityLandingBinding // Déclarer le binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser le binding
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Demander la permission de notification
        requestNotificationPermission(this)

        // Initialiser AuthContext
        authContext = AuthContext()

        // Configurer l'animation de landing
        binding.landingAnimation.visibility = View.VISIBLE

        // Configurer les clics sur les boutons
        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        binding.googleSignInButton.setOnClickListener {
            val googleStrategy = GoogleAuthStrategy(this)
            authContext.updateStrategy(googleStrategy)
            startActivityForResult(googleStrategy.getSignInIntent(), 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val googleStrategy = authContext.strategy as? GoogleAuthStrategy
            googleStrategy?.handleGoogleSignInResult(
                data,
                onSuccess = {
                    // Rediriger vers HomeActivity si la connexion réussit
                    Toast.makeText(this, "Connexion Google réussie !", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Fermer l'activité actuelle
                },
                onError = { error ->
                    // Afficher un message d'erreur en cas d'échec
                    Toast.makeText(this, "Erreur : $error", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}