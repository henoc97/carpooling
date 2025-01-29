package tg.ulcrsandroid.carpooling.domain.models

import java.util.Date

class Notification(
<<<<<<< HEAD
<<<<<<< HEAD
    var notificationId: String,
    //var recipient: Utilisateur,
=======
    var idNotification: String,
    var destinataire: Utilisateur,
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
=======
    var idNotification: String,
    var destinataire: Utilisateur,
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
    var message: String,
    var horodatage: Date
)