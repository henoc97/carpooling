package tg.ulcrsandroid.carpooling.application.services

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.suspendCancellableCoroutine
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.domain.repositories.IUtilisateur
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    fun mettreAJourProfil(utilisateur: Utilisateur) {
        database.child("users").child(utilisateur.idUtilisateur).setValue(utilisateur)
            .addOnSuccessListener {
                println("Profil mis à jour avec succès.")
            }
            .addOnFailureListener { e ->
                println("Erreur de mise à jour : ${e.message}")
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

    suspend fun initialiserUtilisateurSynchronement() {
        val database = FirebaseDatabase.getInstance()
        val idUtilisateur = UtilisateurService.utilisateurID
        val userRef = database.getReference("users/$idUtilisateur")

        try {
            val utilisateurSnapshot = userRef.awaitGetValue() // Fonction utilitaire
            if (utilisateurSnapshot.exists()) {
                UtilisateurService.utilisateurActuel =
                    utilisateurSnapshot.getValue(Utilisateur::class.java)
                Log.d(
                    "Carpooling",
                    "MainActivity ---> UTILISATEUR ACTUEL : ${UtilisateurService.utilisateurActuel?.nomComplet}"
                )
            } else {
                Log.d(
                    "Carpooling",
                    "MainActivity ---> L'UTILISATEUR REFERENCE PAR $idUtilisateur N'EXISTE PAS"
                )
            }
        } catch (exception: Exception) {
            Log.d(
                "Carpooling",
                "MainActivity ----> ERREUR LORS DE LA RECUPERATION DE L'UTILISATEUR: $exception"
            )
        }
    }

    // Extension pour transformer l'écoute de Firebase en coroutine
    private suspend fun DatabaseReference.awaitGetValue(): DataSnapshot =
        suspendCancellableCoroutine { continuation ->
            this.get().addOnSuccessListener { dataSnapshot ->
                continuation.resume(dataSnapshot)
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
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
