package tg.ulcrsandroid.carpooling.domain.repositories

interface IReservation {
    fun confirmerReservation()
    fun annulerReservation()
    fun consulterDetailsReservation()
}