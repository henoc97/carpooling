package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Trajet(
    var tripId: String,
    var startLocation: String,
    var endLocation: String,
    var departureTime: Date,
    var pricePerPassenger: Float,
    var driver: Conducteur,
    var seatsAvailable: Int
)
