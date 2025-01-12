package tg.ulcrsandroid.carpooling.application.utils.authStrategies

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.R

class GoogleAuthStrategy(private val activity: Activity) : IAuthStrategy {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(data: Intent?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        task.addOnCompleteListener { signInTask ->
            if (signInTask.isSuccessful) {
                val account = signInTask.result
                firebaseAuthWithGoogle(account?.idToken, onSuccess, onError)
            } else {
                onError(signInTask.exception?.message ?: "Erreur de connexion avec Google.")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val userMap = mapOf(
                            "userId" to it.uid,
                            "email" to it.email,
                            "fullName" to it.displayName
                        )
                        database.child("users").child(it.uid).setValue(userMap)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                onError("Erreur d'enregistrement : ${e.message}")
                            }
                    }
                } else {
                    onError(authTask.exception?.message ?: "Erreur lors de l'authentification avec Google.")
                }
            }
    }

    override fun sInscrire(email: String?, password: String?, fullName: String?) {
        println("L'inscription avec Google se fait via l'intent Google Sign-In.")
    }

    override fun seConnecter(email: String?, password: String?) {
        println("La connexion avec Google se fait via l'intent Google Sign-In.")
    }
}
