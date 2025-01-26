package tg.ulcrsandroid.carpooling.domain.models

class Conducteur(
    idUtilisateur: String = "",
    email: String = "",
    nomComplet: String = "",
    motDePasse: String = "",
    typeUtilisateur: String = "",
    var detailsVehicule: String = "",
    var placesDisponibles: Int = 0
) : Utilisateur(idUtilisateur, email, nomComplet, motDePasse, typeUtilisateur) {
    // Constructeur sans arguments (nécessaire pour Firebase)
    constructor() : this("", "", "", "", "", "", 0)
}