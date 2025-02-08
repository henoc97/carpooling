package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import androidx.compose.animation.core.snap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.repositories.ITrajet
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object TrajetService : ITrajet {

    private val database = FirebaseDatabase.getInstance().reference.child("trajets")
    var trajets = emptyList<Trajet>()

    /**
     * Récupère un trajet par son id
     * @param idTrajet Id du trajet à récupérer
     * @return Un objet <code>Trajet</code>
     */
    suspend fun recupererTrajet(idTrajet: String): Trajet? {
        return suspendCoroutine { continuation ->
            val ref = database.child(idTrajet)
            ref.get().addOnSuccessListener { snapshot ->
                val trajet = snapshot.getValue<Trajet>()
                Log.d("Carpooling", "TrajetService:recupererTrajet ---> TRAJET ---> $trajet")
                continuation.resume(trajet)
            }.addOnFailureListener { exception ->
                Log.d("Carpooling", "TrajetService:recupererTrajet ---> ERREUR ---> ${exception.message}")
                continuation.resume(null)
            }
        }
    }

    /**
     * Rajoute les objets reservation liées à un objet <code>Trajet</code> et renvoie cet objet
     * @param trajet L'objet <code>Trajet</code> sur lequel on vas jouter les reservations
     *
     * @return Un objet <code>Trajet</code> avec les reservations
     */
    suspend fun ajouterObjetsReservations(trajet: Trajet): Trajet {
        trajet.reservationsIds.forEach { id ->
            trajet.reservations?.add(ReservationService.recupererReservation(id)!!)
        }
        return trajet
    }

    /**
     * Ajoute l'id d'une reservation à un trajet (liste des réservationsIds)
     * @param idTrajet Id du trqjet sur lequel on doit ajouter la reservation
     * @param idReservation Id de la reservation à ajouter
     */
    fun ajouterReservation(idTrajet: String, idReservation: String) {
        val ref = database.child(idTrajet)
        ref.get().addOnSuccessListener { snapshot ->
            val trajet = snapshot.getValue<Trajet>()
            trajet!!.reservationsIds.add(idReservation)
            Log.d("Carpooling", "TrajetService:ajouterReservation ---> TRAJET ---> $trajet")
            ref.setValue(trajet)
        }.addOnFailureListener { e ->
            Log.d("Carpooling", "TrajetService:ajouterReservation ---> ERREUR ---> ${e.message}")
        }
    }

    suspend fun supprimerReservation(idTrajet: String, idReservation: String) : Boolean? {
        val trajet = recupererTrajet(idTrajet)
        if (!trajet?.reservationsIds!!.contains(idReservation)) {
            return true
        }
        val bool = trajet?.reservationsIds?.removeAll(listOf(idReservation))
        mettreAJourTrajet(trajet!!)
        // TODO Prendre en compte le cas ou la reservation a ete dja supprimer
        return bool
    }

    override fun creerTrajet(trajet: Trajet, idConducteur: String) {
        val newTrajetRef = database.push()
        val idTrajet = newTrajetRef.key ?: ""
        trajet.idConducteur = idConducteur
        trajet.idTrajet = idTrajet
        newTrajetRef.setValue(trajet)
            .addOnSuccessListener {
                Log.d("TrajetService", "Trajet créé avec succès.")
            }
            .addOnFailureListener { e ->
                Log.e("TrajetService", "Erreur lors de la création du trajet : ${e.message}")
            }
    }

    override fun mettreAJourTrajet(trajet: Trajet) {
        trajet.reservations = null
        Log.d("TrajetService", "TrajetService:mettreAJourTrajet ---> TRAJET ---> $trajet")
        database.child(trajet.idTrajet).setValue(trajet)
            .addOnSuccessListener {
                Log.d("TrajetService", "Trajet mis à jour avec succès.")
            }
            .addOnFailureListener { e ->
                Log.d("TrajetService", "Erreur lors de la mise à jour du trajet : ${e.message}")
            }
    }

    override fun consulterDetailsTrajet(idTrajet: String, onSuccess: (Trajet?) -> Unit, onError: (String) -> Unit) {
        database.child(idTrajet).get()
            .addOnSuccessListener { dataSnapshot ->
                val trajet = dataSnapshot.getValue(Trajet::class.java)
                onSuccess(trajet)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la consultation des détails du trajet : ${e.message}")
            }
    }

    override fun rechercherTrajets(
        depart: String,
        destination: String,
        heureDepart: Long,
        distanceToleranceMeters: Int,
        timeToleranceMillis: Int,
        onSuccess: (List<Trajet>) -> Unit,
        onError: (String) -> Unit
    ) {
        database.get()
            .addOnSuccessListener { dataSnapshot ->
                val trajets = mutableListOf<Trajet>()
                for (snapshot in dataSnapshot.children) {
                    val trajet = snapshot.getValue(Trajet::class.java)
                    if (trajet != null && isTrajetWithinTolerance(trajet, depart, destination, heureDepart, distanceToleranceMeters, timeToleranceMillis)) {
                        trajets.add(trajet)
                    }
                }
                onSuccess(trajets)
            }
            .addOnFailureListener { e ->
                onError("Erreur lors de la recherche des trajets : ${e.message}")
            }
    }

    /**
     * Récupère tous les trajets créés par un conducteur spécifique.
     *
     * @param idConducteur L'ID du conducteur.
     * @param onSuccess Callback appelé en cas de succès, retourne la liste des trajets.
     * @param onError Callback appelé en cas d'erreur, retourne un message d'erreur.
     */
    fun mesTrajetsCrees(
        idConducteur: String,
        onSuccess: (List<Trajet>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Requête Firebase pour récupérer les trajets où l'idConducteur correspond
        database.orderByChild("idConducteur").equalTo(idConducteur)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val trajets = mutableListOf<Trajet>()

                // Parcourir les résultats de la requête
                for (snapshot in dataSnapshot.children) {
                    val trajet = snapshot.getValue(Trajet::class.java)
                    if (trajet != null) {
                        trajets.add(trajet)
                    }
                }

                // Appeler le callback onSuccess avec la liste des trajets
                onSuccess(trajets)
            }
            .addOnFailureListener { exception ->
                // En cas d'erreur, appeler le callback onError avec un message d'erreur
                val errorMessage = "Erreur lors de la récupération des trajets: ${exception.message}"
                Log.e("TrajetService", errorMessage)
                onError(errorMessage)
            }
    }

    private fun isTrajetWithinTolerance(
        trajet: Trajet,
        depart: String,
        destination: String,
        heureDepart: Long,
        distanceToleranceMeters: Int,
        timeToleranceMillis: Int
    ): Boolean {
        // Check if the departure and destination match (you can add distance calculation logic here)
        val isDepartureMatch = trajet.lieuDepart.equals(depart, ignoreCase = true)
        val isDestinationMatch = trajet.lieuArrivee.equals(destination, ignoreCase = true)

        // Check if the time is within tolerance
        val isTimeWithinTolerance = trajet.heureDepart < System.currentTimeMillis()

        return isDepartureMatch && isDestinationMatch // && isTimeWithinTolerance
    }

}