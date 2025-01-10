package tg.ulcrsandroid.carpooling.domain.repositories

interface IUtilisateur {
    fun sInscrire(email: String, password: String, fullName: String)
    fun seConnecter(email: String, password: String)
    fun mettreAJourProfil(email: String, fullName: String)
    fun consulterProfil()
}