package tg.ulcrsandroid.carpooling.application.services
import tg.ulcrsandroid.carpooling.domain.models.Notification
import tg.ulcrsandroid.carpooling.domain.repositories.INotification
import tg.ulcrsandroid.carpooling.infrastructure.externalServices.push.MyFireBaseNotificationService

object NotificationService : INotification {

    private val FBNS: MyFireBaseNotificationService = MyFireBaseNotificationService
    override fun envoyerNotification(notification: Notification) {
        FBNS.envoyerNotification(notification)
    }

    override fun consulterNotification() {
        // Implémentation de la consultation de notification
    }



}


