package tg.ulcrsandroid.carpooling.application.utils
import tg.ulcrsandroid.carpooling.domain.models.Utilisateur

object UserManager {
    private var currentUser: Utilisateur? = null

    fun setCurrentUser(user: Utilisateur?) {
        currentUser = user
    }

    fun getCurrentUser(): Utilisateur? {
        return currentUser
    }

    fun clearCurrentUser() {
        currentUser = null
    }
}