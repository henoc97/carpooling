package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.databinding.ActivitySignUpBinding

class signUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var authContext: AuthContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Par défaut, stratégie Email/Password
        val emailPasswordAuthStrategy = EmailPasswordAuthStrategy()
        authContext = AuthContext()
        authContext.updateStrategy(emailPasswordAuthStrategy)

        // Configurer le clic sur le bouton d'inscription
        binding.signUpButton.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                // Lancer l'inscription
                authContext.sInscrire(email, password, fullName,
                    onSuccess = {
                        // Rediriger vers HomeActivity si l'inscription réussit
                        Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Fermer l'activité actuelle pour empêcher l'utilisateur de revenir en arrière
                    },
                    onError = { error ->
                        // Afficher un message d'erreur en cas d'échec
                        Toast.makeText(this, "Erreur : $error", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }
}