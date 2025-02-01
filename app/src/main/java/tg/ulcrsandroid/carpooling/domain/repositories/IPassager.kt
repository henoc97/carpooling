package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.models.Reservation

interface IPassager {
    fun rechercherTrajet(lieuDepart: String, lieuArrivee: String, onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit)
    fun reserverTrajet(reservation: Reservation, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun consulterDetailsReservation(idReservation: String, onSuccess: (Reservation?) -> Unit, onError: (String) -> Unit)
    fun evaluerConducteur(idTrajet: String, idUtilisateur: String, note: Float, onSuccess: () -> Unit, onError: (String) -> Unit)
}