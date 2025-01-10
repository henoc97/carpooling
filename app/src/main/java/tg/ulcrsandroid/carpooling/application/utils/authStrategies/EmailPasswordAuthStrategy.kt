package tg.ulcrsandroid.carpooling.application.utils.authStrategies

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EmailPasswordAuthStrategy : IAuthStrategy {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun sInscrire(email: String?, password: String?, fullName: String?) {
        if (email == null || password == null || fullName == null) {
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

    override fun seConnecter(email: String?, password: String?) {
        if (email == null || password == null) {
            println("Email ou mot de passe manquant.")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Connexion réussie.")
                } else {
                    println("Erreur de connexion : ${task.exception?.message}")
                }
            }
    }
}
