package tg.ulcrsandroid.carpooling

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

class CarpoolingApplication: Application() {

//    lateinit var utilisateurActuel: Utilisateur
    lateinit var utilisateurService: UtilisateurService

    override fun onCreate() {
        super.onCreate()
        val databas = Firebase.database
        val idUtilisateur = UtilisateurService.recupererUtilisateurID(this)
        val userRef = databas.getReference("users/${idUtilisateur}")
        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                utilisateurService.utilisateurActuel = dataSnapshot.getValue<Utilisateur>() // Récupérer l'utilisateur depuis Firebase
            } else {
                println("L'utilisateur référencé par $idUtilisateur n'existe pas")
            }
        }.addOnFailureListener { exception ->
            println("Erreur lors de la récupération de l'utilisateur : $exception")
        }

    }
}