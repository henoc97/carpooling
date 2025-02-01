package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.repositories.ITrajet

object TrajetService : ITrajet {

    private val database = FirebaseDatabase.getInstance().reference.child("trajets")

    override fun creerTrajet(trajet: Trajet, idConducteur: String) {
        val newTrajetRef = database.push()
        val idTrajet = newTrajetRef.key ?: ""
        val trajetMap = mapOf(
            "idTrajet" to idTrajet,
            "lieuDepart" to trajet.lieuDepart,
            "lieuArrivee" to trajet.lieuArrivee,
            "heureDepart" to trajet.heureDepart,
            "prixParPassager" to trajet.prixParPassager,
            "placesDisponibles" to trajet.placesDisponibles,
            "idConducteur" to idConducteur,
            "creeA" to trajet.creeA
        )
        newTrajetRef.setValue(trajetMap)
            .addOnSuccessListener {
                Log.d("TrajetService", "Trajet créé avec succès.")
            }
            .addOnFailureListener { e ->
                Log.e("TrajetService", "Erreur lors de la création du trajet : ${e.message}")
            }
    }

    override fun mettreAJourTrajet(trajet: Trajet) {
        database.child(trajet.idTrajet).setValue(trajet)
            .addOnSuccessListener {
                Log.d("TrajetService", "Trajet mis à jour avec succès.")
            }
            .addOnFailureListener { e ->
                Log.e("TrajetService", "Erreur lors de la mise à jour du trajet : ${e.message}")
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
        val isTimeWithinTolerance = trajet.heureDepart in (heureDepart - timeToleranceMillis)..(heureDepart + timeToleranceMillis)

        return isDepartureMatch && isDestinationMatch && isTimeWithinTolerance
    }

}