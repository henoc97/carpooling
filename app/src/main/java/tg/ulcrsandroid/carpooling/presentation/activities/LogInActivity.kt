package tg.ulcrsandroid.carpooling.presentation.activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.databinding.ActivityLoginBinding

class LogInActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authContext: AuthContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Par défaut, stratégie Email/Password
        val emailPasswordAuthStrategy = EmailPasswordAuthStrategy()
        authContext = AuthContext()

        authContext.updateStrategy(emailPasswordAuthStrategy)

        val loginButton: Button = binding.loginButton
        val emailInput: TextInputEditText = binding.emailInput
        val passwordInput: TextInputEditText = binding.passwordInput

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authContext.seConnecter(email, password)
                Toast.makeText(this, "Connexion en cours...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }

}