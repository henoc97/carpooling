package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.repositories.IReservation

object ReservationService : IReservation {
    private val database = FirebaseDatabase.getInstance().reference.child("reservations")

    override fun confirmerReservation(reservation: Reservation) {
        val reservationRef = database.child(reservation.idReservation)
        reservation.statut = "confirmée"
        reservationRef.setValue(reservation)
            .addOnSuccessListener {
                println("Réservation confirmée avec succès.")
            }
            .addOnFailureListener { e ->
                println("Erreur lors de la confirmation de la réservation : ${e.message}")
            }
    }

    override fun annulerReservation(idReservation: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val reservationRef = database.child(idReservation)
        reservationRef.child("statut").setValue("annulée")
            .addOnSuccessListener {
                println("Réservation annulée avec succès.")
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de l'annulation de la réservation : ${e.message}")
            }
    }

    override fun consulterDetailsReservation(idReservation: String, onSuccess: (Reservation?) -> Unit, onError: (String) -> Unit) {
        database.child(idReservation).get()
            .addOnSuccessListener { dataSnapshot ->
                val reservation = dataSnapshot.getValue(Reservation::class.java)
                onSuccess(reservation)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la consultation des détails de la réservation : ${e.message}")
            }
    }
}