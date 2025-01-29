package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Notification(
    var notificationId: String,
    //var recipient: Utilisateur,
    var idNotification: String,
    var destinataire: Utilisateur,
    var message: String,
    var horodatage: Date
)