package tg.ulcrsandroid.carpooling.application.services

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.domain.repositories.IUtilisateur

object UtilisateurService : IUtilisateur {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    var utilisateurActuel: Utilisateur? = null
    var utilisateurID: String? = null

    fun sauvegarderUtilisateurID(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", utilisateurID).apply()
    }

    fun recupererUtilisateurID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    override fun mettreAJourProfil(email: String, nomComplet: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Mise à jour de l'email dans Firebase Authentication
            user.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Mise à jour des informations dans Firebase Realtime Database
                        val updates = mapOf(
                            "email" to email,
                            "nomComplet" to nomComplet
                        )
                        database.child("users").child(user.uid).updateChildren(updates)
                            .addOnSuccessListener {
                                println("Profil mis à jour avec succès.")
                            }
                            .addOnFailureListener { e ->
                                println("Erreur de mise à jour : ${e.message}")
                            }
                    } else {
                        println("Erreur lors de la mise à jour de l'email : ${task.exception?.message}")
                    }
                }
        } ?: run {
            println("Aucun utilisateur connecté")
        }
    }

    override fun consulterProfil() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            database.child("users").child(user.uid).get()
                .addOnSuccessListener { snapshot ->
                    val email = snapshot.child("email").value.toString()
                    val nomComplet = snapshot.child("nomComplet").value.toString()
                    println("Profil utilisateur : Email = $email, Nom = $nomComplet")
                }
                .addOnFailureListener { e ->
                    println("Erreur lors de la récupération du profil : ${e.message}")
                }
        } ?: run {
            println("Aucun utilisateur connecté")
        }
    }
}
