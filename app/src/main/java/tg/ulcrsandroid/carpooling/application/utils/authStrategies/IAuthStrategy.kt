package tg.ulcrsandroid.carpooling.application.utils.authStrategies

interface IAuthStrategy {
    fun sInscrire(
        email: String? = null,
        password: String? = null,
        nomComplet: String? = null,
        onSuccess: () -> Unit, // Callback pour le succès
        onError: (String) -> Unit // Callback pour l'erreur
    )

    fun seConnecter(
        email: String? = null,
        password: String? = null,
        onSuccess: () -> Unit, // Callback pour le succès
        onError: (String) -> Unit // Callback pour l'erreur
    )
}