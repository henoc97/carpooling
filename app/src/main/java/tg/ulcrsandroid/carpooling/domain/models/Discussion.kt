package tg.ulcrsandroid.carpooling.domain.models

import tg.ulcrsandroid.carpooling.application.services.UtilisateurService
import java.util.Date

class Discussion(
    var idDiscussion: String,
    var idExpediteur: String,
//    var destinataire: Utilisateur,
    var message: String,
    var horodatage: Date
) {
    fun isSent(): Boolean {
        return if (idExpediteur == UtilisateurService.utilisateurID) true else false
    }
}