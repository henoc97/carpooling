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
import tg.ulcrsandroid.carpooling.application.utils.notification.FirebaseTokenManager

class GoogleAuthStrategy(private val activity: Activity) : IAuthStrategy {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val googleSignInClient: GoogleSignInClient

    init {
        // Configuration de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)) // ID client OAuth 2.0
            .requestEmail() // Demander l'adresse e-mail de l'utilisateur
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    /**
     * Retourne l'Intent pour démarrer le processus de connexion Google.
     */
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    /**
     * Gère le résultat de la connexion Google.
     *
     * @param data Intent contenant les données de connexion.
     * @param onSuccess Callback appelé en cas de succès.
     * @param onError Callback appelé en cas d'erreur.
     */
    fun handleGoogleSignInResult(data: Intent?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (data == null) {
            onError("Aucune donnée de connexion Google trouvée.")
            return
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        task.addOnCompleteListener { signInTask ->
            if (signInTask.isSuccessful) {
                val account = signInTask.result
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken, onSuccess, onError)
                } else {
                    onError("Compte Google non trouvé.")
                }
            } else {
                onError(signInTask.exception?.message ?: "Erreur de connexion avec Google.")
            }
        }
    }

    /**
     * Authentifie l'utilisateur avec Firebase en utilisant les informations de Google.
     *
     * @param idToken Token d'identification Google.
     * @param onSuccess Callback appelé en cas de succès.
     * @param onError Callback appelé en cas d'erreur.
     */
    private fun firebaseAuthWithGoogle(idToken: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (idToken == null) {
            onError("Token d'identification Google invalide.")
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Enregistrer les informations de l'utilisateur dans la base de données
                        val userMap = mapOf(
                            "idUtilisateur" to user.uid,
                            "email" to user.email,
                            "nomComplet" to user.displayName
                        )
                        database.child("users").child(user.uid).setValue(userMap)
                            .addOnSuccessListener {
                                FirebaseTokenManager.updateToken(user.uid) // Mise à jour du token
                                onSuccess() // Appeler onSuccess si tout est réussi
                            }
                            .addOnFailureListener { e ->
                                onError("Erreur d'enregistrement : ${e.message}")
                            }
                    } else {
                        onError("Utilisateur Firebase non trouvé après connexion.")
                    }
                } else {
                    onError(authTask.exception?.message ?: "Erreur lors de l'authentification avec Google.")
                }
            }
    }

    override fun sInscrire(email: String?, password: String?, nomComplet: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Non applicable pour Google Auth
        onError("Méthode non prise en charge pour Google Auth.")
    }

    override fun seConnecter(email: String?, password: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Non applicable pour Google Auth
        onError("Méthode non prise en charge pour Google Auth.")
    }
}