package tg.ulcrsandroid.carpooling.application.services

import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.repositories.ITrajet
import java.util.Date

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
            "creeA" to Date()
        )
        newTrajetRef.setValue(trajetMap)
            .addOnSuccessListener {
                println("Trajet créé avec succès.")
            }
            .addOnFailureListener { e ->
                println("Erreur lors de la création du trajet : ${e.message}")
            }
    }

    override fun mettreAJourTrajet(trajet: Trajet) {
        database.child(trajet.idTrajet.toString()).setValue(trajet)
            .addOnSuccessListener {
                println("Trajet mis à jour avec succès.")
            }
            .addOnFailureListener { e ->
                println("Erreur lors de la mise à jour du trajet : ${e.message}")
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
}