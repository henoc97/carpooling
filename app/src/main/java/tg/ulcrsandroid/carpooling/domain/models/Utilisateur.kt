package tg.ulcrsandroid.carpooling.domain.models

open class Utilisateur(
    var idUtilisateur: String,
    var email: String,
    var nomComplet: String,
    var motDePasse: String,
    var typeUtilisateur: String,
    var mesChats: MutableList<String> = mutableListOf()
) {

    constructor() : this("", "", "", "", "")

    fun ajouterIdDiscussion(idDiscussion: String) {
        mesChats.add(idDiscussion)
    }

}
