package tg.ulcrsandroid.carpooling.application.utils.authStrategies

class AuthContext {
    var strategy: IAuthStrategy? = null
        private set

    fun updateStrategy(newStrategy: IAuthStrategy) {
        strategy = newStrategy
    }

    fun sInscrire(
        email: String? = null,
        password: String? = null,
        nomComplet: String? = null,
        onSuccess: () -> Unit, // Callback pour le succès
        onError: (String) -> Unit // Callback pour l'erreur
    ) {
        strategy?.sInscrire(email, password, nomComplet, onSuccess, onError)
    }

    fun seConnecter(
        email: String? = null,
        password: String? = null,
        onSuccess: () -> Unit, // Callback pour le succès
        onError: (String) -> Unit // Callback pour l'erreur
    ) {
        strategy?.seConnecter(email, password, onSuccess, onError)
    }
}
