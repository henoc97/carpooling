package tg.ulcrsandroid.carpooling.application.utils.authStrategies

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okhttp3.internal.Util
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class EmailPasswordAuthStrategy : IAuthStrategy {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun sInscrire(email: String?, password: String?, nomComplet: String?) {
        if (email == null || password == null || nomComplet == null) {
            println("Email, mot de passe ou nom complet manquant.")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    if (userId != null) {
                        val user = mapOf(
                            "userId" to userId,
                            "email" to email,
                            "nomComplet" to nomComplet
                        )
                        val utilisateur = Utilisateur(userId, email, nomComplet, password, "client")
//                        val newEmail = UtilisateurService.formaterEmail(email)
//                        Log.i("Carpooling", "EmailPasswordAuthStrategy ---> EMAIL ENVOYE --> $newEmail")

                        // Utilisation de l'email plutot que l'id pour référencer un utilisateur
                        Log.d("Carpooling", "Utilisateur Id ---> ${userId}")
                        database.child("users").child(userId).setValue(utilisateur)
                            .addOnSuccessListener {
                                println("Utilisateur enregistré avec succès. ${userId}")
                                Log.d("Carpooling", "Utilisateur enregistré avec succès. ${userId}")
                                UtilisateurService.utilisateurID = userId
                            }
                            .addOnFailureListener { e ->
                                Log.d("Carpooling", "Erreur d'enregistrement : ${e.message}")
                                println("Erreur d'enregistrement : ${e.message}")
                            }
                    }
                } else {
                    println("Erreur d'inscription : ${task.exception?.message}")
                }
            }
    }

    override fun seConnecter(email: String?, password: String?) {
        if (email == null || password == null) {
            println("Email ou mot de passe manquant.")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val userId = task.result?.user?.uid

                // Récupérer l'id de l'utilisateur et le stocker dans sharred preferences
                if (task.isSuccessful) {
                    println("Connexion réussie. ${userId}")
                    UtilisateurService.utilisateurID = userId
                } else {
                    println("Erreur de connexion : ${task.exception?.message}")
                }
            }
    }
}
