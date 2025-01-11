package tg.ulcrsandroid.carpooling.application.utils.authStrategies

class AuthContext {
    var strategy: IAuthStrategy? = null
        private set

    fun updateStrategy(newStrategy: IAuthStrategy) {
        strategy = newStrategy
    }

    fun sInscrire(email: String? = null, password: String? = null, fullName: String? = null) {
        strategy?.sInscrire(email, password, fullName)
    }

    fun seConnecter(email: String? = null, password: String? = null) {
        strategy?.seConnecter(email, password)
    }
}
