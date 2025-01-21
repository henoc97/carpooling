package tg.ulcrsandroid.carpooling.domain.models

class Passager(
    idUtilisateur: String,
    email: String,
    nomComplet: String,
    motDePasse: String,
    typeUtilisateur: String,
    var historiqueReservations: List<Reservation>
) : Utilisateur(idUtilisateur, email, nomComplet, motDePasse, typeUtilisateur)