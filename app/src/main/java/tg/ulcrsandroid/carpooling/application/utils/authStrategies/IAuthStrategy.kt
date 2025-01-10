package tg.ulcrsandroid.carpooling.application.utils.authStrategies

interface IAuthStrategy {
    fun sInscrire(email: String? = null, password: String? = null, fullName: String? = null)
    fun seConnecter(email: String? = null, password: String? = null)
}
