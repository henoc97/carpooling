package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.managers.PassagerManager
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.repositories.IReservation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ReservationService : IReservation {

    val EN_ATTENTE = "En attente"
    val ACCEPTEE = "Acceptée"
    val REJETEE = "Rejetée"

    private val database = FirebaseDatabase.getInstance().reference.child("reservations")

    fun supprimerReservation(idReservation: String) {
        val ref = database.child(idReservation)
        ref.removeValue().addOnSuccessListener {
            Log.d("Carpooling", "ReservationService:supprimerReservation ---> Reservation supprimé avec succes")
        }
    }

    suspend fun ajouterObjetTrajet(reservation: Reservation): Reservation{
        Log.d("Carpooling", "ReservationService:ajouterObjetTrajet ---> Reservation ---> $reservation")
        val trajet = TrajetService.recupererTrajet(reservation.idTrajet)
        reservation.trajet = trajet
        return reservation
    }

    suspend fun mesReservations(): List<Reservation> {
        Log.d("Carpooling", "ReservationService:mesReservations ---> Passager ---> ${PassagerManager.getPassagerActuel()}")
        val ids = PassagerManager.getPassagerActuel()?.reservationsIds
        Log.d("Carpooling", "ReservationService:mesReservations ---> Ids reservations ${ids}")
        val reservations = mutableListOf<Reservation>()
        ids?.forEach { s ->
            reservations.add(recupererReservation(s)!!)
        }
        Log.d("Carpooling", "ReservationService:mesReservations ---> Ids reservations ${reservations}")
        return reservations
    }

    suspend fun recupererReservation(idReservation: String) : Reservation? {
        return suspendCoroutine { continuation ->
            val ref = database.child(idReservation)
            ref.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val reservation = snapshot.getValue<Reservation>()
                    continuation.resume(reservation)
                } else {
                    continuation.resume(null)
                }
            }.addOnFailureListener { error ->
                Log.d("Carpooling", "ReservationService:recupererReservation ---> ERROR: ${error.message}")
                continuation.resume(null)
            }
        }
    }

    fun persisterReservation(reservartion: Reservation) {
        val ref = database.push()
        if (reservartion.idReservation == "") {
            reservartion.idReservation = ref.key?: ""
            ref.setValue(reservartion)
            // Ajouter la reservation crée au trajet en question
            TrajetService.ajouterReservation(reservartion.idTrajet, reservartion.idReservation)
            // Ajouter la reservation au passager
            PassagerManager.getPassagerActuel()?.reservationsIds?.add(reservartion.idReservation)
            PassagerManager.mettreAjourPassager()
        }
    }

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