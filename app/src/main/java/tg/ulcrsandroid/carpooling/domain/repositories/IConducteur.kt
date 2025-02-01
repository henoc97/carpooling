package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Trajet
import tg.ulcrsandroid.carpooling.domain.models.Reservation

interface IConducteur {
    fun creerTrajet(trajet: Trajet)
    fun gererReservations(idTrajet: String, onSuccess: (List<Reservation>) -> Unit, onError: (String) -> Unit)
    // idUtilisateur est l'identifiant du conducteur qui a créé le trajet (conducteur hérite de utilisateur)
    fun consulterHistoriqueTrajets(idConducteur: String, onSuccess: (List<Trajet>) -> Unit, onError: (String) -> Unit)
}