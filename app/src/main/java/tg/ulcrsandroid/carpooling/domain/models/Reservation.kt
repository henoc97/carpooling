package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Reservation(
    var bookingId: String,
    var passenger: Passager,
    var trip: Trajet,
    var bookingTime: Date,
    var status: String
)
