package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.databinding.ActivityLoginBinding

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authContext: AuthContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser le View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser AuthContext avec la stratégie Email/Password
        authContext = AuthContext()
        authContext.updateStrategy(EmailPasswordAuthStrategy())

        // Configurer le clic sur le bouton de connexion
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Lancer la connexion avec les callbacks
                authContext.seConnecter(
                    email = email,
                    password = password,
                    onSuccess = {
                        // Rediriger vers HomeActivity si la connexion réussit
                        Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Fermer l'activité actuelle
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

    override fun onDestroy() {
//        UtilisateurService.sauvegarderUtilisateurID(this)
//        Log.d("Carpooling", "SAUVEGARDE DE L'ID DE L'UTILISATEUR !")
        super.onDestroy()
    }

}