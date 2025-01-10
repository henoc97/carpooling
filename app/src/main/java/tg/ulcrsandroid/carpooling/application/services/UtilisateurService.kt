package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.repositories.IUtilisateur

object UtilisateurService : IUtilisateur {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun sInscrire(email: String, password: String, fullName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    if (userId != null) {
                        // Ajout des informations utilisateur dans Firebase Realtime Database
                        val user = mapOf(
                            "userId" to userId,
                            "email" to email,
                            "fullName" to fullName
                        )
                        database.child("users").child(userId).setValue(user)
                            .addOnSuccessListener {
                                println("Utilisateur enregistré avec succès.")
                            }
                            .addOnFailureListener { e ->
                                println("Erreur d'enregistrement : ${e.message}")
                            }
                    }
                } else {
                    println("Erreur d'inscription : ${task.exception?.message}")
                }
            }
    }

    override fun seConnecter(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Connexion réussie")
                } else {
                    println("Erreur de connexion : ${task.exception?.message}")
                }
            }
    }

    override fun mettreAJourProfil(email: String, fullName: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Mise à jour de l'email dans Firebase Authentication
            user.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Mise à jour des informations dans Firebase Realtime Database
                        val updates = mapOf(
                            "email" to email,
                            "fullName" to fullName
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
                    val fullName = snapshot.child("fullName").value.toString()
                    println("Profil utilisateur : Email = $email, Nom = $fullName")
                }
                .addOnFailureListener { e ->
                    println("Erreur lors de la récupération du profil : ${e.message}")
                }
        } ?: run {
            println("Aucun utilisateur connecté")
        }
    }
}
