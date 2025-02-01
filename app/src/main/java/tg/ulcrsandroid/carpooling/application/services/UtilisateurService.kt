package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.domain.repositories.IUtilisateur

object UtilisateurService : IUtilisateur {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

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

    fun updateTokenInDatabase(newToken: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            database.child("users").child(user.uid).child("notifications").child("fcmToken")
                .setValue(newToken)
                .addOnSuccessListener {
                    Log.d("FCM", "Token mis à jour dans la base de données.")
                }
                .addOnFailureListener { e ->
                    Log.e("FCM", "Erreur lors de la mise à jour du token : ${e.message}")
                }
        }

    }

    fun getFcmTokenById(userId: String, onSuccess: (String?) -> Unit, onError: (String) -> Unit) {
        database.child("users").child(userId).child("notifications").child("fcmToken")
            .get()
            .addOnSuccessListener { snapshot ->
                val token = snapshot.value as? String
                onSuccess(token) // Retourner le token via le callback
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la récupération du token FCM : ${e.message}")
            }
    }

    fun getCurrentUserId(): String? {
        val currentUser = auth.currentUser
        return currentUser?.let { user -> user.uid}
    }

    suspend fun getCurrentUser(): Utilisateur? {
        val currentUser = auth.currentUser
        return currentUser?.let { user ->
            // Fetch the user data from the database
            val userSnapshot = database.child("users").child(user.uid).get().await()

            // Check if the user data exists
            if (userSnapshot.exists()) {
                // Map the data to the Utilisateur class
                val idUtilisateur = userSnapshot.child("idUtilisateur").getValue(String::class.java) ?: ""
                val email = userSnapshot.child("email").getValue(String::class.java) ?: ""
                val nomComplet = userSnapshot.child("nomComplet").getValue(String::class.java) ?: ""
                val motDePasse = userSnapshot.child("motDePasse").getValue(String::class.java) ?: ""
                val typeUtilisateur = userSnapshot.child("typeUtilisateur").getValue(String::class.java) ?: ""

                // Create and return the Utilisateur object
                Utilisateur(idUtilisateur, email, nomComplet, motDePasse, typeUtilisateur)
            } else {
                // If the user data doesn't exist, return null
                null
            }
        }
    }

    // Exemple d'utilisation de la fonction
//    UtilisateurService.getFcmTokenById(
//    userId = "someUserId",
//    onSuccess = { token ->
//        if (token != null) {
//            println("Token FCM récupéré : $token")
//        } else {
//            println("Aucun token FCM trouvé pour cet utilisateur.")
//        }
//    },
//    onError = { errorMessage ->
//        println(errorMessage)
//    }
//    )

}
