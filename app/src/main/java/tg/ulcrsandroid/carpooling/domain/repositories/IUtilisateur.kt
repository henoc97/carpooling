package tg.ulcrsandroid.carpooling.domain.repositories

interface IUtilisateur {
    fun mettreAJourProfil(email: String, nomComplet: String)
    fun consulterProfil()
}