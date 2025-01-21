package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Reservation(
    var idReservation: String,
    var passager: Passager,
    var trajet: Trajet,
    var heureReservation: Date,
    var statut: String
)