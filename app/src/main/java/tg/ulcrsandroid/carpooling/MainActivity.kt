package tg.ulcrsandroid.carpooling

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.GoogleAuthStrategy

class MainActivity : AppCompatActivity() {
    private lateinit var authContext: AuthContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Par défaut, stratégie Email/Password
        val emailPasswordAuthStrategy = EmailPasswordAuthStrategy()
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
