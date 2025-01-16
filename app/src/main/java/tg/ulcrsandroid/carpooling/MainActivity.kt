package tg.ulcrsandroid.carpooling

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.AuthContext
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.EmailPasswordAuthStrategy
import tg.ulcrsandroid.carpooling.application.utils.authStrategies.GoogleAuthStrategy
import tg.ulcrsandroid.carpooling.application.utils.lottie.loadJsonFromRaw
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.activities.ChatActivity
import tg.ulcrsandroid.carpooling.presentation.activities.LogInActivity
import tg.ulcrsandroid.carpooling.presentation.activities.signUpActivity

class MainActivity : AppCompatActivity() {

    private lateinit var authContext: AuthContext
    private var utilisateur: Utilisateur? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiserUtilisateur()
//        Log.i("Carpooling", "MainActivity ---> UTILISATEUR ACTUEL : ${UtilisateurService.utilisateurActuel?.nomComplet}")

        setContentView(R.layout.activity_landing)

        val idUtilisateur = UtilisateurService.utilisateurID
        if (idUtilisateur != null) {
            Log.i("Carpooling", "Utilisateur connecté : ${utilisateur?.nomComplet}")
            // Appeler HomeActivity si l'utilisateur est déjà connecté
            val intent  = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        } else {
            Log.i("Carpooling", "MainActivity ---> UTILISATEUR ACTUEL : NULL")
        }

        authContext = AuthContext()

        // Initialiser l'animation de landings
        val landingAnimation: LottieAnimationView = findViewById(R.id.landingAnimation)
        landingAnimation.visibility = View.VISIBLE

        // Initialiser les boutons
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val loginButton: Button = findViewById(R.id.loginButton)
        val googleButton: Button = findViewById(R.id.googleSignInButton)

        // Configurer les clics sur les boutons
        signUpButton.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }
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

    private fun initialiserUtilisateur() {
        val databas = Firebase.database
        val idUtilisateur = UtilisateurService.utilisateurID
        val userRef = databas.getReference("users/${idUtilisateur}")
        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                UtilisateurService.utilisateurActuel = dataSnapshot.getValue<Utilisateur>() // Récupérer l'utilisateur depuis Firebase
                Log.i("Carpooling", "MainActivity ---> UTILISATEUR ACTUEL : ${UtilisateurService.utilisateurActuel?.nomComplet}")
            } else {
                println("L'utilisateur référencé par $idUtilisateur n'existe pas")
            }
        }.addOnFailureListener { exception ->
            println("Erreur lors de la récupération de l'utilisateur : $exception")
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
