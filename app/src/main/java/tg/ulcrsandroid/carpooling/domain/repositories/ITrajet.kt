package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Trajet

interface ITrajet {
    fun creerTrajet(trajet: Trajet, idConducteur: String)
    fun mettreAJourTrajet(trajet: Trajet)
    fun consulterDetailsTrajet(idTrajet: String, onSuccess: (Trajet?) -> Unit, onError: (String) -> Unit)
    fun rechercherTrajets(
        depart: String,
        destination: String,
        heureDepart: Long,
        distanceToleranceMeters: Int,
        timeToleranceMillis: Int,
        onSuccess: (List<Trajet>) -> Unit,
        onError: (String) -> Unit
    )
//    fun rechercherTrajets(depart: String, destination: String, string: String, lng: Long, onSuccess: Any, onFailure: Any)
}