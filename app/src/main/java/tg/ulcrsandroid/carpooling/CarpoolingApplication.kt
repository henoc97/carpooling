package tg.ulcrsandroid.carpooling

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import tg.ulcrsandroid.carpooling.application.services.DiscussionService
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.presentation.activities.ChatActivity
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CarpoolingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        UtilisateurService.initialiserIdUtilisateur(this)


//        // Vérifier si l'id de l'utilisateur et différent de null
//        if (UtilisateurService.utilisateurID != null) {
//            runBlocking {
//                initialiserUtilisateurSynchronement() // Récupérer les infos utilisateur et créer un objet utilisateur
//            }
////            initialiserUtilisateur()
//        }

        Log.d(
            "Carpooling",
            "CarpoolingApplication:onCreate ---> INITIALISATION DE L'ID DE L'UTILISATEUR ---> ${UtilisateurService.utilisateurID}"
        )
    }

    private fun initialiserUtilisateur() {
        val databas = Firebase.database
        val idUtilisateur = UtilisateurService.utilisateurID
        val userRef = databas.getReference("users/${idUtilisateur}")
        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                UtilisateurService.utilisateurActuel =
                    dataSnapshot.getValue<Utilisateur>() // Récupérer l'utilisateur depuis Firebase
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
        }.addOnFailureListener { exception ->
            Log.d(
                "Carpooling",
                "MainActivity ----> ERREUR LORS DE LA RECUPERATION DE L'UTILISATEUR: $exception"
            )
        }
    }

//    private suspend fun initialiserUtilisateurSynchronement() {
//        val database = FirebaseDatabase.getInstance()
//        val idUtilisateur = UtilisateurService.utilisateurID
//        val userRef = database.getReference("users/$idUtilisateur")
//
//        try {
//            val utilisateurSnapshot = userRef.awaitGetValue() // Fonction utilitaire
//            if (utilisateurSnapshot.exists()) {
//                UtilisateurService.utilisateurActuel =
//                    utilisateurSnapshot.getValue(Utilisateur::class.java)
//                Log.d(
//                    "Carpooling",
//                    "MainActivity ---> UTILISATEUR ACTUEL : ${UtilisateurService.utilisateurActuel?.nomComplet}"
//                )
//            } else {
//                Log.d(
//                    "Carpooling",
//                    "MainActivity ---> L'UTILISATEUR REFERENCE PAR $idUtilisateur N'EXISTE PAS"
//                )
//            }
//        } catch (exception: Exception) {
//            Log.d(
//                "Carpooling",
//                "MainActivity ----> ERREUR LORS DE LA RECUPERATION DE L'UTILISATEUR: $exception"
//            )
//        }
//    }
//
//    // Extension pour transformer l'écoute de Firebase en coroutine
//    private suspend fun DatabaseReference.awaitGetValue(): DataSnapshot =
//        suspendCancellableCoroutine { continuation ->
//            this.get().addOnSuccessListener { dataSnapshot ->
//                continuation.resume(dataSnapshot)
//            }.addOnFailureListener { exception ->
//                continuation.resumeWithException(exception)
//            }
//        }

}