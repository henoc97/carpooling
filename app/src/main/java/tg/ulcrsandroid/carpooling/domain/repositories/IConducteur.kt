package tg.ulcrsandroid.carpooling.domain.repositories

interface IConducteur {
    fun creerTrajet()
    fun gererReservations()
    fun consulterHistoriqueTrajets()
}