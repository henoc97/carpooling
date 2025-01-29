package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Notification

interface INotification {
    fun envoyerNotification(notification:Notification)
    fun envoyerNotification(deviceToken: String, title: String, body: String)
    fun consulterNotification()
}