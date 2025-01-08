package tg.ulcrsandroid.carpooling.domain.repositories

interface IDiscussion {
    fun envoyerMessage()
    fun recevoirMessage()
    fun consulterHistoriqueMessages()
}