package tg.ulcrsandroid.carpooling.domain.repositories

import tg.ulcrsandroid.carpooling.domain.models.Notification

interface INotification {
    fun envoyerNotification(notification:Notification)
    fun consulterNotification()
}