package tg.ulcrsandroid.carpooling.application.services

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.domain.repositories.IUtilisateur

object UtilisateurService : IUtilisateur {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    var utilisateurActuel: Utilisateur? = null
    var utilisateurID: String? = null

    fun sauvegarderUtilisateurID(context: Context) {
        Log.i("Carpooling", "SAUVEGARDE DE L'ID $utilisateurID")
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", utilisateurID).apply()
    }

    private fun recupererUtilisateurID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        utilisateurID = sharedPreferences.getString("user_id", null)
        return utilisateurID
    }

    fun initialiserIdUtilisateur(context: Context) {
        utilisateurID = recupererUtilisateurID(context)
    }

    fun getUsersList(liste: MutableList<String>): MutableList<Utilisateur?> {
        val database = Firebase.database
        val users: MutableList<Utilisateur?> = mutableListOf()
        liste.forEach { s ->
            val ref = database.getReference("users/$s")
            ref.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val utilisateur = dataSnapshot.getValue<Utilisateur>()
                    users.add(utilisateur);
                } else {
                    Log.i("Carpooling", "UtilisateurService ---> L'UTILISATEUR REFÉRENCÉ PAR $s N'EXISTE PAS")
                }
            }
        }
        return users
    }

    private fun concatenerUsersEmail(s: String?): String? {
        if (s != null) {
            var liste = mutableListOf<String>(s, utilisateurActuel?.email!!).sorted()
//            val concatened = liste.joinToString("#").replace("@", "$")
            return liste.joinToString("-").replace("@", "$")
        }
        return null
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
