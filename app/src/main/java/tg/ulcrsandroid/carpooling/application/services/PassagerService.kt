package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.firebase.ui.auth.data.model.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.application.utils.UserManager
import tg.ulcrsandroid.carpooling.application.utils.managers.PassagerManager
import tg.ulcrsandroid.carpooling.domain.models.Passager
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.models.Reservation
import tg.ulcrsandroid.carpooling.domain.repositories.IPassager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PassagerService : IPassager {

    private val database = FirebaseDatabase.getInstance().reference.child("passagers")

    fun supprimerReservation(passager: Passager, idReservation: String) {
        passager.reservationsIds?.removeAll(listOf(idReservation))
        persisterPassager(passager)
    }

    suspend fun recupererPassager(idPassager: String) : Passager? {
        return suspendCoroutine { continuation ->
            val ref = database.child(idPassager)
            ref.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val passager = snapshot.getValue<Passager>()
                    PassagerManager.setPassagerActuel(passager)
                    Log.d("Carpooling", "PassagerService:recupererPassager ---> PASSAGER ---> $passager")
                    continuation.resume(passager)
                } else {
                    Log.d("Carpooling", "PassagerService:recupererPassager ---> ERROR: Passager introuvable")
                    val passager = Passager(UserManager.getCurrentUser()!!)
                    persisterPassager(passager)
                    PassagerManager.setPassagerActuel(passager)
                    Log.d("Carpooling", "PassagerService:recupererPassager ---> ERROR: Passager ajouté $passager")
                    continuation.resume(passager)
                }
            }.addOnFailureListener { error ->
                Log.d("Carpooling", "PassagerService:recupererPassager ---> ERROR: ${error.message}")
                continuation.resume(null)
            }
        }
    }

    fun persisterPassager(passager: Passager) {
        val ref = database.child(passager.idUtilisateur)
        ref.setValue(passager).addOnSuccessListener {
            Log.d("Carpooling", "PassagerService:persisterPassager ---> Passager persister avec succes ---> $passager")
        }.addOnFailureListener {
            Log.d("Carpooling", "PassagerService:persisterPassager ---> ERROR: ${it.message}")
        }
    }

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