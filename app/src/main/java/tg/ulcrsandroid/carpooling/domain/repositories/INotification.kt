package tg.ulcrsandroid.carpooling.domain.repositories

interface INotification {
    fun envoyerNotification(deviceToken: String, title: String, body: String)
    fun consulterNotification()
}