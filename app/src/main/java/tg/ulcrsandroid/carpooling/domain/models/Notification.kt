package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Notification(
    var idNotification: String,
    var destinataire: Utilisateur,
    var message: String,
    var horodatage: Date
)