package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Notification

interface INotification {
<<<<<<< HEAD
    fun envoyerNotification(notification:Notification)
=======
    fun envoyerNotification(deviceToken: String, title: String, body: String)
>>>>>>> 62e3402154032eb2e237a1116947cfbe63bd5cc2
    fun consulterNotification()
}