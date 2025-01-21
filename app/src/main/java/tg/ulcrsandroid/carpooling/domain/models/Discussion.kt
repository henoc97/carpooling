package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Discussion(
    var idDiscussion: String,
    var idExpediteur: String,
//    var destinataire: Utilisateur,
    var message: String,
    var horodatage: Date
)