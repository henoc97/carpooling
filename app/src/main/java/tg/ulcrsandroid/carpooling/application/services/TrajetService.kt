package tg.ulcrsandroid.carpooling.application.services

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.repositories.ITrajet
import java.util.Date
import kotlin.text.get

object TrajetService : ITrajet {
    private val database = FirebaseDatabase.getInstance().reference.child("trajets")

    override fun creerTrajet(trajet: Trajet, idConducteur: String) {
        val newTrajetRef = database.push()
        val idTrajet = newTrajetRef.key ?: ""
        val trajetMap = mapOf(
            "idTrajet" to idTrajet,
            "lieuDepart" to trajet.lieuDepart,
            "lieuArrivee" to trajet.lieuArrivee,
            "heureDepart" to trajet.heureDepart, // Utiliser le timestamp
            "prixParPassager" to trajet.prixParPassager,
            "placesDisponibles" to trajet.placesDisponibles,
            "idConducteur" to idConducteur,
            "creeA" to trajet.creeA // Utiliser le timestamp
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

    fun mesTrajetsCrees(idConducteur: String, onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit) {
        database.orderByChild("idConducteur").equalTo(idConducteur)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val trajets = mutableListOf<Trajet>()
                for (snapshot in dataSnapshot.children) {
                    val trajet = snapshot.getValue(Trajet::class.java)
                    trajet?.let { trajets.add(it) }
                }
                onSuccess(trajets)
            }
            .addOnFailureListener { exception ->
                onError("Erreur lors de la récupération des trajets: ${exception.message}")
            }
    }

    // lister les trajets dont leur heureDepart n est pas encore passer
    fun listerTrajetsFuturs(onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit) {
        database.get()
            .addOnSuccessListener { dataSnapshot ->
                val trajetsFuturs = mutableListOf<Trajet>()
                val heureActuelle = System.currentTimeMillis() // Timestamp actuel

                for (snapshot in dataSnapshot.children) {
                    val trajet = snapshot.getValue(Trajet::class.java)
                    if (trajet != null && trajet.heureDepart > heureActuelle) {
                        trajetsFuturs.add(trajet)
                    }
                }

                // Ajouter des logs pour déboguer
                Log.d("TrajetService", "Nombre de trajets trouvés : ${dataSnapshot.childrenCount}")
                Log.d("TrajetService", "Nombre de trajets futurs : ${trajetsFuturs.size}")
                Log.d("TrajetService", "Heure actuelle : $heureActuelle")

                onSuccess(trajetsFuturs)
            }
            .addOnFailureListener { exception ->
                onError("Erreur lors de la récupération des trajets : ${exception.message}")
            }
    }
}