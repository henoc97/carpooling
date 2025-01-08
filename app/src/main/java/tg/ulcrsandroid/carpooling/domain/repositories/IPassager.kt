package tg.ulcrsandroid.carpooling.domain.repositories

interface IPassager {
    fun rechercherTrajet()
    fun reserverTrajet()
    fun consulterDetailsReservation()
    fun evaluerConducteur()
}