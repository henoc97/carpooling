package tg.ulcrsandroid.carpooling.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import tg.ulcrsandroid.carpooling.R
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.databinding.ActivityLoginBinding
import tg.ulcrsandroid.carpooling.databinding.ActivitySignUpBinding

class signUpActivity : AppCompatActivity(){
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

        val signUpButton: Button = binding.signUpButton
        val fullNameInput: TextInputEditText = binding.fullNameInput
        val emailInput: TextInputEditText = binding.emailInput
        val passwordInput: TextInputEditText = binding.passwordInput

        signUpButton.setOnClickListener {
            var fullName = fullNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                Log.d("Carpooling", "Calling function to add user")
                authContext.sInscrire(email, password, fullName)
                Toast.makeText(this, "Inscription en cours...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }



////            Start a new activity
//            val intent = Intent(this, ChatActivity::class.java)
//            startActivity(intent)
        }
    }

    override fun onDestroy() {
        Log.d("Carpooling", "signUpActivity:onDestroy ---> SAUVEGARDE DE l'ID ---> ${UtilisateurService.utilisateurID}")
        UtilisateurService.sauvegarderUtilisateurID(this)
        super.onDestroy()
    }

}