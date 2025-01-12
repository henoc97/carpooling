package tg.ulcrsandroid.carpooling.application.utils.authStrategies

class AuthContext {
    var strategy: IAuthStrategy? = null
        private set

    fun updateStrategy(newStrategy: IAuthStrategy) {
        strategy = newStrategy
    }

    fun sInscrire(email: String? = null, password: String? = null, nomComplet: String? = null) {
        strategy?.sInscrire(email, password, nomComplet)
    }

    fun seConnecter(email: String? = null, password: String? = null) {
        strategy?.seConnecter(email, password)
    }
}
