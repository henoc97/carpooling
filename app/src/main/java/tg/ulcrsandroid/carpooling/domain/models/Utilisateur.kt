package tg.ulcrsandroid.carpooling.domain.models

import tg.ulcrsandroid.carpooling.application.services.UtilisateurService

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
        UtilisateurService.mettreAJourProfil(this) // Mettre à jour le profil de l'utilisateur
    }

}
