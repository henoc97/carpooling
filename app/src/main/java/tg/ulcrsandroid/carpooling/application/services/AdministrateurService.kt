package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.repositories.IAdministrateur

object AdministrateurService : IAdministrateur {
    private val database = FirebaseDatabase.getInstance().reference

    override fun gererUtilisateurs(onSuccess: (List<Utilisateur>) -> Unit, onError: (String) -> Unit) {
        database.child("utilisateurs").get()
            .addOnSuccessListener { dataSnapshot ->
                val utilisateurs = dataSnapshot.children.mapNotNull { it.getValue(Utilisateur::class.java) }
                onSuccess(utilisateurs)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la gestion des utilisateurs : ${e.message}")
            }
    }

    override fun gererTrajets(onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit) {
        database.child("trajets").get()
            .addOnSuccessListener { dataSnapshot ->
                val trajets = dataSnapshot.children.mapNotNull { it.getValue(Trajet::class.java) }
                onSuccess(trajets)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la gestion des trajets : ${e.message}")
            }
    }

    override fun surveillerActivite(onSuccess: (List<String>) -> Unit, onError: (String) -> Unit) {
        database.child("activites").get()
            .addOnSuccessListener { dataSnapshot ->
                val activites = dataSnapshot.children.mapNotNull { it.getValue(String::class.java) }
                onSuccess(activites)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la surveillance des activités : ${e.message}")
            }
    }
}