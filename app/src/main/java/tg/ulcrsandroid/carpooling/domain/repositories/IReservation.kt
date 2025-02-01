package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Reservation

interface IReservation {
    fun confirmerReservation(reservation: Reservation)
    fun annulerReservation(idReservation: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun consulterDetailsReservation(idReservation: String, onSuccess: (Reservation?) -> Unit, onError: (String) -> Unit)
}