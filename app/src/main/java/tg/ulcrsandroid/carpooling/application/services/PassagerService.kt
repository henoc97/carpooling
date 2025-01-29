package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.repositories.IPassager

object PassagerService : IPassager {
    private val database = FirebaseDatabase.getInstance().reference

    override fun rechercherTrajet(lieuDepart: String, lieuArrivee: String, onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit) {
        database.child("trajets").orderByChild("lieuDepart").equalTo(lieuDepart).get()
            .addOnSuccessListener { dataSnapshot ->
                val trajets = dataSnapshot.children.mapNotNull { it.getValue(Trajet::class.java) }
                onSuccess(trajets.filter { it.lieuArrivee == lieuArrivee })
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la recherche de trajets : ${e.message}")
            }
    }

    override fun reserverTrajet(reservation: Reservation, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val idReservation = database.child("reservations").push().key ?: ""
        reservation.idReservation = idReservation
        database.child("reservations").child(idReservation).setValue(reservation)
            .addOnSuccessListener {
                println("Réservation effectuée avec succès.")
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la réservation du trajet : ${e.message}")
            }
    }

    override fun consulterDetailsReservation(idReservation: String, onSuccess: (Reservation?) -> Unit, onError: (String) -> Unit) {
        database.child("reservations").child(idReservation).get()
            .addOnSuccessListener { dataSnapshot ->
                val reservation = dataSnapshot.getValue(Reservation::class.java)
                onSuccess(reservation)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la consultation des détails de réservation : ${e.message}")
            }
    }

    override fun evaluerConducteur(idTrajet: String, idUtilisateur: String, note: Float, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val noteRef = database.child("notes").child(idTrajet).child(idUtilisateur)
        noteRef.setValue(note)
            .addOnSuccessListener {
                println("Évaluation du conducteur effectuée avec succès.")
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de l'évaluation du conducteur : ${e.message}")
            }
    }
}