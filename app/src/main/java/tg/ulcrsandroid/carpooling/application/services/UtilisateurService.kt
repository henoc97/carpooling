package tg.ulcrsandroid.carpooling.application.services

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.domain.repositories.IUtilisateur
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object UtilisateurService : IUtilisateur {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    var utilisateurActuel: Utilisateur? = null
    var utilisateurID: String? = null

    suspend fun initialiserUtilisateurActuel(s: String) : Utilisateur? {
        val databas = Firebase.database
        val userRef = databas.getReference("users/$s")
        return retreiveUser(userRef)
    }

    private suspend fun retreiveUser(ref: DatabaseReference): Utilisateur? {
        return suspendCoroutine { continuation ->
            ref.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    this.utilisateurActuel =
                        dataSnapshot.getValue<Utilisateur>() // Récupérer l'utilisateur depuis Firebase
                    continuation.resume(this.utilisateurActuel)
                    Log.d(
                        "Carpooling",
                        "UtilisateurService:retreiveUser ---> UTILISATEUR ACTUEL : ${this.utilisateurActuel?.nomComplet}"
                    )
                } else {
                    Log.d(
                        "Carpooling",
                        "UtilisateurService:retreiveUser ---> L'UTILISATEUR REFERENCE N'EXISTE PAS"
                    )
                    continuation.resume(null)
                }
            }.addOnFailureListener { exception ->
                Log.d(
                    "Carpooling",
                    "UtilisateurService:retreiveUser ----> ERREUR LORS DE LA RECUPERATION DE L'UTILISATEUR: $exception"
                )
            }
        }
    }

    fun sauvegarderUtilisateurID(context: Context) {
        Log.i("Carpooling", "SAUVEGARDE DE L'ID $utilisateurID")
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", utilisateurID).apply()
    }

    fun recupererUtilisateurID(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        utilisateurID = sharedPreferences.getString("user_id", null)
        Log.d("Carpooling", "UtilisateurService:recupererUtilisateurID ---> USER-ID ---> $utilisateurID")
        return utilisateurID
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
                userSnapshot.getValue<Utilisateur>()
            } else {
            // If the user data doesn't exist, return null
            null
            }
        }
    }

}
