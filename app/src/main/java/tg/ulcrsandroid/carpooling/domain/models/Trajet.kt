package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Trajet(
    var idTrajet: String,
    var lieuDepart: String,
    var lieuArrivee: String,
    var heureDepart: Date,
    var prixParPassager: Float,
    var conducteur: Conducteur,
    var placesDisponibles: Int
)