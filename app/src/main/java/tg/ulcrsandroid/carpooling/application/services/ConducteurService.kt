package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.repositories.IConducteur

object ConducteurService : IConducteur {
    private val database = FirebaseDatabase.getInstance().reference

    override fun creerTrajet(trajet: Trajet) {
        // TrajetService.creerTrajet(trajet)
    }

    override fun gererReservations(idTrajet: String, onSuccess: (List<Reservation>) -> Unit, onError: (String) -> Unit) {
        database.child("reservations").orderByChild("trajet/idTrajet").equalTo(idTrajet).get()
            .addOnSuccessListener { dataSnapshot ->
                val reservations = dataSnapshot.children.mapNotNull { it.getValue(Reservation::class.java) }
                onSuccess(reservations)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la gestion des réservations : ${e.message}")
            }
    }
    
    // idUtilisateur est l'identifiant du conducteur qui a créé le trajet (conducteur hérite de utilisateur)
    override fun consulterHistoriqueTrajets(idConducteur: String, onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit) {
        database.child("trajets").orderByChild("conducteur/idUtilisateur").equalTo(idConducteur).get()
            .addOnSuccessListener { dataSnapshot ->
                val trajets = dataSnapshot.children.mapNotNull { it.getValue(Trajet::class.java) }
                onSuccess(trajets)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la consultation de l'historique des trajets : ${e.message}")
            }
    }
}